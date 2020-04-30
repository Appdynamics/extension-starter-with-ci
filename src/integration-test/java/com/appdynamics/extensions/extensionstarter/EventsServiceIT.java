/*
 * Copyright (c) 2019 AppDynamics,Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdynamics.extensions.extensionstarter;

/**
 * Created by Aditya Jagtiani on 12/15/17.
 */

import com.appdynamics.extensions.conf.processor.ConfigProcessor;
import com.appdynamics.extensions.eventsservice.EventsServiceDataManager;
import com.appdynamics.extensions.extensionstarter.events.ExtensionStarterEventsManager;
import com.appdynamics.extensions.http.Http4ClientBuilder;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.appdynamics.extensions.yml.YmlReader;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static com.appdynamics.extensions.eventsservice.utils.Constants.*;

public class EventsServiceIT {
    private static final Logger logger = ExtensionsLoggerFactory.getLogger(EventsServiceIT.class);

    private CloseableHttpClient httpClient, httpClientEventsServiceApiKeys;
    private HttpHost httpHost;
    private String globalAccountName, eventsApiKey;
    private ExtensionStarterEventsManager eventsManager;


    @Before
    public void setup() throws Exception {
        File configFile = new File("src/integration-test/resources/conf/config.yml");
        Map<String, ?> config = YmlReader.readFromFileAsMap(configFile);
        config = ConfigProcessor.process(config);
        Map<String, Object> eventsServiceParameters = (Map)config.get("eventsServiceParameters");
        String eventsServiceHost = (String) eventsServiceParameters.get("host");
        int eventsServicePort = (Integer) eventsServiceParameters.get("port");
        Runtime.getRuntime().exec("chmod 755 src/integration-test/resources/conf/apikeys.sh");
        ProcessBuilder pb = new ProcessBuilder("src/integration-test/resources/conf/apikeys.sh");
        Process process = pb.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        globalAccountName = reader.readLine();
        eventsApiKey=reader.readLine();
        eventsServiceParameters.put("globalAccountName", globalAccountName);
        eventsServiceParameters.put("eventsApiKey",eventsApiKey);
        boolean useSSL = (Boolean) eventsServiceParameters.get("useSSL");
        httpClient = Http4ClientBuilder.getBuilder(eventsServiceParameters).build();
        httpHost = new HttpHost(eventsServiceHost, eventsServicePort, useSSL ? "https" : "http");
        eventsManager = new ExtensionStarterEventsManager(new EventsServiceDataManager(eventsServiceParameters));
    }

    @Test
    public void testWhetherSchemaIsCreated() throws Exception {
        eventsManager.deleteSchema();
        eventsManager.createSchema();
        CloseableHttpResponse httpResponse = fetchSchemaFromEventsService();
        Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        httpResponse.close();
    }

    @Test
    public void testWhetherSchemaIsDeleted() throws Exception {
        eventsManager.deleteSchema();
        CloseableHttpResponse httpResponse = fetchSchemaFromEventsService();
        Assert.assertEquals(404, httpResponse.getStatusLine().getStatusCode());
        httpResponse.close();
    }

    @Test
    public void testWhetherSchemaIsUpdated() throws Exception {
        eventsManager.deleteSchema();
        eventsManager.createSchema();
        eventsManager.updateSchema();
        String responseBody = EntityUtils.toString(fetchSchemaFromEventsService().getEntity(), "UTF-8");
        Assert.assertTrue(responseBody.contains("appName"));
    }

    @Test
    public void testWhetherEventsArePublished() throws Exception {
        eventsManager.deleteSchema();
        eventsManager.createSchema();
        eventsManager.updateSchema();
        eventsManager.publishEvents();
        Thread.sleep(5000);
        Assert.assertTrue(!eventsManager.queryEvents().equals(""));
    }

    private CloseableHttpResponse fetchSchemaFromEventsService() throws Exception {

        HttpGet httpGet = new HttpGet(httpHost.toURI() + SCHEMA_PATH + "BTDSchema");
        httpGet.setHeader(ACCOUNT_NAME_HEADER, globalAccountName);
        httpGet.setHeader(API_KEY_HEADER, eventsApiKey);
        httpGet.setHeader(ACCEPT_HEADER, ACCEPTED_CONTENT_TYPE);
        return httpClient.execute(httpGet);
    }
}
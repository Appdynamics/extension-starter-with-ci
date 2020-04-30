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

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.http.HttpClientUtils;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.util.AssertUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.extensionstarter.util.Constants.DEFAULT_METRIC_SEPARATOR;
import static com.appdynamics.extensions.extensionstarter.util.Constants.METRICS;

/**
 * @author Satish Muddam
 */
public class HTTPMonitorTask implements AMonitorTaskRunnable {
    private static final Logger logger = ExtensionsLoggerFactory.getLogger(ExtStarterMonitorTask.class);
    private MonitorContextConfiguration configuration;
    private MetricWriteHelper metricWriteHelper;
    private Map<String, String> server;
    private String metricPrefix;
    private List<Map<String, ?>> metricList;

    public HTTPMonitorTask(MonitorContextConfiguration configuration, MetricWriteHelper metricWriteHelper,
                           Map<String, String> server) {
        this.configuration = configuration;
        this.metricWriteHelper = metricWriteHelper;
        this.server = server;
        this.metricPrefix = configuration.getMetricPrefix() + DEFAULT_METRIC_SEPARATOR + server.get("name");
        this.metricList = (List<Map<String, ?>>) configuration.getConfigYml().get(METRICS);
        AssertUtils.assertNotNull(this.metricList, "The 'metrics' section in config.yml is either null or empty");
    }

    @Override
    public void run() {
        CloseableHttpClient httpClient = configuration.getContext().getHttpClient();
        String response = HttpClientUtils.getResponseAsStr(httpClient, server.get("host"));
        List<Metric> metrics = new ArrayList<>();
        if (response != null) {
            // this creates a Metric with default properties
            Metric metric = new Metric("HTTP Status", String.valueOf(BigInteger.ONE), metricPrefix
                    + DEFAULT_METRIC_SEPARATOR + " HTTP Status");
            metrics.add(metric);
        } else {
            Metric metric = new Metric("HTTP Status", String.valueOf(BigInteger.ZERO), metricPrefix
                    + DEFAULT_METRIC_SEPARATOR + " HTTP Status");
            metrics.add(metric);
        }
        metricWriteHelper.transformAndPrintMetrics(metrics);
    }

    @Override
    public void onTaskComplete() {
        logger.info("All tasks for server {} finished", this.server.get("name"));
    }
}

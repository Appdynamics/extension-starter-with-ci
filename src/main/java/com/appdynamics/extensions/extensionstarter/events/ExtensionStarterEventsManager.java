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

package com.appdynamics.extensions.extensionstarter.events;

import com.appdynamics.extensions.eventsservice.EventsServiceDataManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class ExtensionStarterEventsManager {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionStarterEventsManager.class);
    private EventsServiceDataManager eventsServiceDataManager;

    public ExtensionStarterEventsManager(EventsServiceDataManager eventsServiceDataManager) {
        this.eventsServiceDataManager = eventsServiceDataManager;
    }

    public void createSchema() throws Exception {
        eventsServiceDataManager.createSchema("BTDSchema", FileUtils.readFileToString(new File("src/main/resources" +
                "/eventsservice/createSchema.json")));
    }

    public void updateSchema() throws Exception {
        eventsServiceDataManager.updateSchema("BTDSchema", FileUtils.readFileToString(new File("src/main/" +
                "resources/eventsservice/updateSchema.json")));
    }

    public void deleteSchema() {
        eventsServiceDataManager.deleteSchema("BTDSchema");
    }

    public void publishEvents() {
        eventsServiceDataManager.publishEvents("BTDSchema", generateEventsFromFile(new File("src/main/" +
                "resources/eventsservice/publishEvents.json")));
    }

    public String queryEvents() {
        return eventsServiceDataManager.querySchema("SELECT appName FROM BTDSCHEMA");
    }

    private List<String> generateEventsFromFile(File eventsFromFile) {
        List<String> eventsToBePublishedForSchema = Lists.newArrayList();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode arrayNode = mapper.readTree(eventsFromFile);
            for (JsonNode node : arrayNode) {
                eventsToBePublishedForSchema.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
            }
        } catch (Exception ex) {
            logger.error("Error encountered while generating events from file: {}", eventsFromFile.getAbsolutePath(),
                    ex);
        }
        return eventsToBePublishedForSchema;
    }
}

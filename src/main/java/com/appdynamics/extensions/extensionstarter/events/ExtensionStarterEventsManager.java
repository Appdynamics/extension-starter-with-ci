package com.appdynamics.extensions.extensionstarter.events;

import com.appdynamics.extensions.eventsservice.EventsServiceDataManager;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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
        eventsServiceDataManager.createSchema("BTDSchema", FileUtils.readFileToString(new File("src/main/resources/eventsservice/createSchema.json")));
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
            logger.error("Error encountered while generating events from file: {}", eventsFromFile.getAbsolutePath(), ex);
        }
        return eventsToBePublishedForSchema;
    }
}

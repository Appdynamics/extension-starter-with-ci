/*
 *   Copyright 2019. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.extensionstarter;

/**
 * Created by Aditya Jagtiani on 12/15/17.
 */

import com.appdynamics.extensions.controller.apiservices.MetricAPIService;
import com.appdynamics.extensions.util.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.appdynamics.extensions.extensionstarter.Constants.DEFAULT_METRIC_UPLOAD_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.METRIC_WITH_PROPS_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.IntegrationTestUtils.initializeMetricAPIService;

public class MetricUploadIT {

    private MetricAPIService metricAPIService;

    @Before
    public void setup() {
        metricAPIService = initializeMetricAPIService();
    }

    @Test
    public void testMetricUpload() {
        if(metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", DEFAULT_METRIC_UPLOAD_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "*", "metricValues", "*", "value");
                int cpuUtilization = (valueNode == null) ? 0 : valueNode.get(0).asInt();
                Assert.assertEquals(cpuUtilization, 20);
            }
        }
        else {
            Assert.fail("Failed to connect to the Controller API");
        }
    }

    @Test
    public void testMetricUploadWithAliasMultiplier() {
        if(metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("",METRIC_WITH_PROPS_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                valueNode = JsonUtils.getNestedObject(jsonNode, "*", "metricValues", "*", "value");
                int metricValue = (valueNode == null) ? 0 : valueNode.get(0).asInt();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Outgoing Requests\"", metricName);
                Assert.assertEquals(100, metricValue);
            }
        }
        else {
            Assert.fail("Failed to connect to the Controller API");
        }
    }
}
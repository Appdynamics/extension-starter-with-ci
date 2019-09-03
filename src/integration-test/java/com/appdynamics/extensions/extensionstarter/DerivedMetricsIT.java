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

import static com.appdynamics.extensions.extensionstarter.Constants.CLUSTER_METRIC_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.DERIVED_METRIC_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.IntegrationTestUtils.initializeMetricAPIService;

public class DerivedMetricsIT {

    private MetricAPIService metricAPIService;

    @Before
    public void setup() {
        metricAPIService = initializeMetricAPIService();
    }

    @Test
    public void testDerivedMetricUpload() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", DERIVED_METRIC_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "*", "metricValues", "*", "value");
                int totalNumOfRequests = (valueNode == null) ? 0 : valueNode.get(0).asInt();
                Assert.assertEquals(totalNumOfRequests, 120);
            }
        } else {
            Assert.fail("Failed to connect to Controller API");
        }
    }

    @Test
    public void testDerivedMetricUploadWithClusterMetrics() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", CLUSTER_METRIC_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "*", "metricValues", "*", "current");
                int totalNumOfRequests = (valueNode == null) ? 0 : valueNode.get(0).asInt();
                Assert.assertEquals(totalNumOfRequests, 20);
            }
        } else {
            Assert.fail("Failed to connect to Controller API");
        }
    }
}
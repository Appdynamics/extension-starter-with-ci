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

import com.appdynamics.extensions.controller.apiservices.MetricAPIService;
import com.appdynamics.extensions.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
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
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
import org.codehaus.jackson.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.appdynamics.extensions.extensionstarter.Constants.*;
import static com.appdynamics.extensions.extensionstarter.Constants.DEFAULT_METRIC_CHAR_REPLACEMENT_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.DEFAULT_REPLACEMENT_OVERRIDDEN_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.INVALID_CHAR_REPLACEMENT_ENDPPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.MULTIPLE_CHAR_REPLACEMENT_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.Constants.NON_ASCII_REPLACEMENT_ENDPOINT;
import static com.appdynamics.extensions.extensionstarter.IntegrationTestUtils.initializeMetricAPIService;

public class MetricCharacterReplacementIT {
    private MetricAPIService metricAPIService;

    @Before
    public void setup() {
        metricAPIService = initializeMetricAPIService();
    }

    @Test
    public void testDefaultCharacterReplacement() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", DEFAULT_METRIC_CHAR_REPLACEMENT_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Character Replacement|Pipe\"", metricName);
            } else {
                Assert.fail("Failed to connect to the Controller API");
            }
        }
    }

    @Test
    public void testDefaultCharacterReplacementWhenOverriden() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", DEFAULT_REPLACEMENT_OVERRIDDEN_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Character Replacement|Comma%\"", metricName);
            } else {
                Assert.fail("Failed to connect to the Controller API");
            }
        }
    }

    @Test
    public void testWhenMultipleReplacementsAreConfiguredForSameCharacterThenLastOneIsUsed () {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", MULTIPLE_CHAR_REPLACEMENT_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Character Replacement|#Colon\"", metricName);
            } else {
                Assert.fail("Failed to connect to the Controller API");
            }
        }
    }


    @Test
    public void testWhenReplacementForNonAsciiCharacterIsPresent() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", NON_ASCII_REPLACEMENT_ENDPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Character Replacement|Memory Used\"", metricName);
            } else {
                Assert.fail("Failed to connect to the Controller API");
            }
        }
    }

    @Test
    public void testWhenReplacementForCharacterIsInvalid() {
        if (metricAPIService != null) {
            JsonNode jsonNode = metricAPIService.getMetricData("", INVALID_CHAR_REPLACEMENT_ENDPPOINT);
            if (jsonNode != null) {
                JsonNode valueNode = JsonUtils.getNestedObject(jsonNode, "metricName");
                String metricName = (valueNode == null) ? "" : valueNode.get(0).toString();
                Assert.assertEquals("\"Custom Metrics|Extension Starter CI|Character Replacement|QuestionMark\"", metricName);
            } else {
                Assert.fail("Failed to connect to the Controller API");
            }
        }
    }
}


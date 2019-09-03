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

import static com.appdynamics.extensions.extensionstarter.Constants.*;
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


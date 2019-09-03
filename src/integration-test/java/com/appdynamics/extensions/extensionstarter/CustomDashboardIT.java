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

import com.appdynamics.extensions.controller.apiservices.CustomDashboardAPIService;
import org.codehaus.jackson.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.appdynamics.extensions.util.JsonUtils.getTextValue;

public class CustomDashboardIT {

    private CustomDashboardAPIService customDashboardAPIService;

    @Before
    public void setup() {
        customDashboardAPIService = IntegrationTestUtils.initializeCustomDashboardAPIService();
    }

    @Test
    public void testWhetherConfiguredDashboardIsUploadedToController() {
        if (customDashboardAPIService != null) {
            JsonNode allDashboardsNode = customDashboardAPIService.getAllDashboards();
            Assert.assertTrue(isDashboardPresent(allDashboardsNode));
        }
    }

    private boolean isDashboardPresent(JsonNode existingDashboards) {
        String dashboardName = "Extension Starter BTD Dashboard";
        if (existingDashboards != null) {
            for (JsonNode existingDashboard : existingDashboards) {
                if (dashboardName.equals(getTextValue(existingDashboard.get("name")))) {
                    return true;
                }
            }
        }
        return false;
    }
}
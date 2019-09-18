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
import com.appdynamics.extensions.controller.ControllerClient;
import com.appdynamics.extensions.controller.ControllerClientFactory;
import com.appdynamics.extensions.controller.ControllerInfo;
import com.appdynamics.extensions.controller.ControllerInfoFactory;
import com.appdynamics.extensions.controller.ControllerInfoValidator;
import com.appdynamics.extensions.controller.apiservices.ControllerAPIService;
import com.appdynamics.extensions.controller.apiservices.ControllerAPIServiceFactory;
import com.appdynamics.extensions.controller.apiservices.CustomDashboardAPIService;
import com.appdynamics.extensions.controller.apiservices.MetricAPIService;
import com.appdynamics.extensions.logging.ExtensionsLoggerFactory;
import com.appdynamics.extensions.yml.YmlReader;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.File;
import java.util.Map;

import static com.appdynamics.extensions.Constants.ENCRYPTION_KEY;

class IntegrationTestUtils {
    private static final Logger logger = ExtensionsLoggerFactory.getLogger(IntegrationTestUtils.class);
    // change to install dir path to initialize from controller-info.xml
    private static final File INSTALL_DIR = null;
    private static final File CONFIG_FILE = new File("src/integration-test/resources/conf/config.yml");

    static MetricAPIService initializeMetricAPIService() {
        ControllerAPIService controllerAPIService = initializeControllerAPIService();
        if (controllerAPIService != null) {
            logger.info("Attempting to setup Metric API Service");
            return controllerAPIService.getMetricAPIService();
        } else {
            logger.error("Failed to setup Metric API Service");
            return null;
        }
    }

    static CustomDashboardAPIService initializeCustomDashboardAPIService() {
        ControllerAPIService controllerAPIService = initializeControllerAPIService();
        if (controllerAPIService != null) {
            logger.info("Attempting to setup Dashboard API Service");
            return controllerAPIService.getCustomDashboardAPIService();
        } else {
            logger.error("Failed to setup Dashboard API Service");
            return null;
        }
    }


    private static ControllerAPIService initializeControllerAPIService() {
        Map<String, ?> config = YmlReader.readFromFileAsMap(CONFIG_FILE);
        config = ConfigProcessor.process(config);
        Map controllerInfoMap = (Map) config.get("controllerInfo");
        if (controllerInfoMap == null) {
            controllerInfoMap = Maps.newHashMap();
        }
        //this is for test purposes only
        controllerInfoMap.put("controllerHost","localhost");
        controllerInfoMap.put(ENCRYPTION_KEY, config.get(ENCRYPTION_KEY));
        try {
            ControllerInfo controllerInfo = ControllerInfoFactory.initialize(controllerInfoMap, INSTALL_DIR);
            logger.info("Initialized ControllerInfo");
            ControllerInfoValidator controllerInfoValidator = new ControllerInfoValidator(controllerInfo);
            if (controllerInfoValidator.isValidated()) {
                ControllerClient controllerClient = ControllerClientFactory.initialize(controllerInfo,
                        (Map<String, ?>) config.get("connection"), (Map<String, ?>) config.get("proxy"),
                        (String) config.get(ENCRYPTION_KEY));
                logger.debug("Initialized ControllerClient");
                return ControllerAPIServiceFactory.initialize(controllerInfo, controllerClient);
            }
        } catch (Exception ex) {
            logger.error("Failed to initialize the Controller API Service");
        }
        return null;
    }
}
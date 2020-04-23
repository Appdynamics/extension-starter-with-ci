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
 * Created by bhuvnesh.kumar on 12/15/17.
 */

import com.appdynamics.extensions.ABaseMonitor;
import com.appdynamics.extensions.TasksExecutionServiceProvider;
import com.appdynamics.extensions.extensionstarter.events.ExtensionStarterEventsManager;
import com.appdynamics.extensions.util.AssertUtils;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.extensionstarter.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.extensions.extensionstarter.util.Constants.MONITOR_NAME;

/**
 * This class will be the main implementation for the extension, the entry point for this class is
 * {@code doRun(TasksExecutionServiceProvider tasksExecutionServiceProvider)}
 * <p>
 * {@code ABaseMonitor} class takes care of all the boiler plate code required for "ExtensionMonitor"
 * like initializing {@code MonitorContexConfiguration}, setting the config file from monitor.xml etc.
 * It also internally calls[this call happens everytime the machine agent calls {@code ExtensionMonitor.execute()}]
 * {@code AMonitorJob.run()} -> which in turn calls
 * {@code doRun(TasksExecutionServiceProvider tasksExecutionServiceProvider)} method in this class. {@code AMonitorJob}
 * represents a single run of the extension.
 * <p>
 * {@code ExtensionMonitorTask} (named as "task") in an extension run(named as "Job").
 * Once all the tasks finish execution, the TaskExecutionServiceProvider(it is the execution service provider
 * for all the tasks in a job), will start DerivedMetricCalculation, print logs related to total metrics
 * sent to the controller in the current job.
 */
public class ExtStarterMonitor extends ABaseMonitor {
    private static final Logger logger = LoggerFactory.getLogger(ExtStarterMonitor.class);

    /**
     * Returns the default metric prefix defined in {@code Constants} to be used if metric prefix
     * is missing in config.yml
     * Required for MonitorContextConfiguration initialisation
     *
     * @return {@code String} the default metrics prefix.
     */
    @Override
    protected String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    /**
     * Returns the monitor name defined in {@code Constants}
     * Required for MonitorConfiguration initialisation
     *
     * @return {@code String} monitor's name
     */
    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    /**
     * This method can be used to initialize any additional arguments (except config-file which is
     * handled in {@code ABaseMonitor}) configured in monitor.xml
     *
     * @param args A {@code Map<String, String>} of task-arguments configured in monitor.xml
     */
    @Override
    protected void initializeMoreStuff(Map<String, String> args) {
        // Code to initialize additional arguments
    }

    /**
     * The entry point for this class.
     * NOTE: The {@code MetricWriteHelper} is initialised internally in {@code AMonitorJob}, but it is exposed through
     * {@code getMetricWriteHelper()} method in {@code TaskExecutionServiceProvider} class.
     *
     * @param tasksExecutionServiceProvider {@code TaskExecutionServiceProvider} is responsible for finishing all the
     *                                      tasks before initialising DerivedMetricsCalculator (It is basically like
     *                                                                          a service that executes the
     *                                      tasks and wait on all of them to finish and does the finish up work).
     */
    @Override
    protected void doRun(TasksExecutionServiceProvider tasksExecutionServiceProvider) {
        // reading a value from the config.yml file
        List<Map<String, String>> servers = (List<Map<String, String>>) getContextConfiguration().getConfigYml().get(
                "servers");
        AssertUtils.assertNotNull(servers, "The 'servers' section in config.yml is not initialised");
        /*
         Each task is executed in thread pool, you can have one task to fetch metrics from each artifact concurrently
         */
        for (Map<String, String> server : servers) {
            ExtStarterMonitorTask task = new ExtStarterMonitorTask(getContextConfiguration(),
                    tasksExecutionServiceProvider.getMetricWriteHelper(), server);
            tasksExecutionServiceProvider.submit(server.get("name") + " dummy", task);
            HTTPMonitorTask task1 = new HTTPMonitorTask(getContextConfiguration(),
                    tasksExecutionServiceProvider.getMetricWriteHelper(), server);
            tasksExecutionServiceProvider.submit(server.get("name"), task1);
        }
    }

    /**
     * Required by the {@code TaskExecutionServiceProvider} above to know the total number of tasks it needs to wait on.
     * Think of it as a count in the {@code CountDownLatch}
     *
     * @return Number of tasks, i.e. total number of servers to collect metrics from
     */
    @Override
    protected List<Map<String, ?>> getServers() {
        List<Map<String, ?>> servers = (List<Map<String, ?>>) getContextConfiguration().getConfigYml().get("servers");
        AssertUtils.assertNotNull(servers, "The 'servers' section in config.yml is not initialised");
        return servers;
    }

    @Override
    public void onComplete() {
        ExtensionStarterEventsManager extensionStarterEventsManager = new ExtensionStarterEventsManager
                (getContextConfiguration().getContext().getEventsServiceDataManager());
        try {
            extensionStarterEventsManager.createSchema();
            extensionStarterEventsManager.updateSchema();
            extensionStarterEventsManager.publishEvents();
        } catch (Exception ex) {
            logger.error("Error encountered while publishing events");
        }
    }

    @Override
    public MetricWriter getMetricWriter(String metricName) {
        return super.getMetricWriter(metricName);
    }
}
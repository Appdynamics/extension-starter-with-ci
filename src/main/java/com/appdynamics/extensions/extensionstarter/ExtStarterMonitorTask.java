/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.extensionstarter;

/**
 * Created by bhuvnesh.kumar on 12/15/17.
 */

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.extensionstarter.util.Constants.*;

/**
 * The ExtensionMonitorTask(namely "task") is an instance of {@link Runnable} needs to implement the interface
 * {@code AMonitorTaskRunnable} instead of {@code Runnable}. This would make the need for overriding
 * {@code onTaskComplete()} method which will be called once the {@code run()} method execution is done.
 */
public class ExtStarterMonitorTask implements AMonitorTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(ExtStarterMonitorTask.class);
    private MonitorContextConfiguration configuration;
    private MetricWriteHelper metricWriteHelper;
    private Map<String, String> server;
    private String metricPrefix;
    private List<Map<String, ?>> metricList;


    public ExtStarterMonitorTask(MonitorContextConfiguration configuration, MetricWriteHelper metricWriteHelper,
                                 Map<String, String> server) {
        this.configuration = configuration;
        this.metricWriteHelper = metricWriteHelper;
        this.server = server;
        this.metricPrefix = configuration.getMetricPrefix();
        this.metricList = (List<Map<String, ?>>) configuration.getConfigYml().get(METRICS);

        AssertUtils.assertNotNull(this.metricList, "The 'metrics' section in config.yml is either null or empty");
    }

    /**
     * This onTaskComplete() method emphasizes the need to print metrics like
     * "METRICS_COLLECTION_STATUS" or do any other task complete work.
     */
    @Override
    public void onTaskComplete() {
        /*
         Below code shows an example of how to print metrics
         */
        List<Metric> metrics = new ArrayList<>();
        // this creates a Metric with default properties
        Metric metric = new Metric("Heart Beat", String.valueOf(BigInteger.ONE), metricPrefix
                + DEFAULT_METRIC_SEPARATOR + " Heart Beat");
        metrics.add(metric);
        metricWriteHelper.transformAndPrintMetrics(metrics);
        logger.info("Completed task for Server: {}", server.get("name"));
    }

    /**
     * This method contains the main business logic of the extension.
     */
    @Override
    public void run() {
        logger.info("Created task and started working for Server: {}", server.get("name"));
        /*
         * It is in this function that you can get your metrics and process them and send them to the controller.
         * You can look at the various extensions available on the community site and build your extension based on them.
         *
         * */

        /*
        once you have collected the required metrics you can send them to the metric browser as shown in the below
        example. In this example, let's assume that you have pulled a metric called CPU Utilization, refer config.yml
        to configure what metrics you need to collect, you will create a metric object and add it to a list. The list
        hold all the metric object and using the method shown in example you can send all the metrics to the metric
        browser.
        NOTE: the underlying piece of code is designed to handle the specific way in which the 'metrics' section
        of config.yml is structured, please modify it according to your structure definition in config.yml
         */
        // get list of metrics to pull from 'metrics' section in config.yml
        // iterate through all the metrics and add them to a list
        List<Metric> metrics = new ArrayList<>();
        for (Map<String, ?> metricType : metricList) {
            for (Map.Entry<String, ?> entry : metricType.entrySet()) {
                // get details of the specific metric, in this example 'CPUUtilization' metric in config.yml
                String metricName = entry.getKey();
                logger.info("Building metric for {}", metricName);
                Map<String, ?> metricProperties = (Map<String, ?>) entry.getValue();
                buildMetric(metrics, metricName, metricProperties);
            }
        }
        buildClusterMetrics(metrics);
        generateMetricsForCharReplacement(metrics);
        metricWriteHelper.transformAndPrintMetrics(metrics);
    }

    private void buildClusterMetrics(List<Metric> metrics) {
        String baseMetricPath = metricPrefix + DEFAULT_METRIC_SEPARATOR + "Master" + DEFAULT_METRIC_SEPARATOR;
        Metric metric = new Metric("Requests", String.valueOf(10), baseMetricPath + "Node1" +
                DEFAULT_METRIC_SEPARATOR + "Requests");
        metrics.add(metric);
        metric = new Metric("Requests", String.valueOf(10), baseMetricPath + "Node2" +
                DEFAULT_METRIC_SEPARATOR + "Requests");
        metrics.add(metric);
    }

    /**
     * Creates a {@code Metric} object and add it to the {@code List<Metrics>}
     *
     * @param metrics          A {@code List<Metric>} updated by the method
     * @param metricProperties Properties of the metric type
     */
    private void buildMetric(List<Metric> metrics, String metricName, Map<String, ?> metricProperties) {
        // this example uses a hardcoded value (20),
        // use the value that you get for your metrics, you can modify the method signature to
        // pass the actual value of the metric
        // You can look at the various extensions available on the community site for further understanding
        Metric metric = new Metric(metricName, String.valueOf(20), metricPrefix + DEFAULT_METRIC_SEPARATOR + metricName,
                metricProperties);
        metrics.add(metric);
    }

    private void generateMetricsForCharReplacement(List<Metric> metrics) {
        Metric metric1 = new Metric("Pipe|", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", "Pipe|");
        Metric metric2 = new Metric("Comma,", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", "Comma,");
        Metric metric3 = new Metric(":Colon", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", ":Colon");
        Metric metric4 = new Metric("Memóry Free", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", "Memóry Free");
        Metric metric5 = new Metric("Memory Üsed", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", "Memory \u00dcsed");
        Metric metric6 = new Metric("Question?Mark", "10", new HashMap<String, Object>(),
                "Custom Metrics|Extension Starter CI|Character Replacement|", "Question?Mark");
        metrics.add(metric1);
        metrics.add(metric2);
        metrics.add(metric3);
        metrics.add(metric4);
        metrics.add(metric5);
        metrics.add(metric6);
    }
}
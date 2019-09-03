package com.appdynamics.extensions.extensionstarter;

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.http.HttpClientUtils;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.util.AssertUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.extensionstarter.util.Constants.DEFAULT_METRIC_SEPARATOR;
import static com.appdynamics.extensions.extensionstarter.util.Constants.METRICS;

/**
 * @author Satish Muddam
 */
public class HTTPMonitorTask implements AMonitorTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(ExtStarterMonitorTask.class);
    private MonitorContextConfiguration configuration;
    private MetricWriteHelper metricWriteHelper;
    private Map<String, String> server;
    private String metricPrefix;
    private List<Map<String, ?>> metricList;

    public HTTPMonitorTask (MonitorContextConfiguration configuration, MetricWriteHelper metricWriteHelper,
                            Map<String, String> server) {
        this.configuration = configuration;
        this.metricWriteHelper = metricWriteHelper;
        this.server = server;
        this.metricPrefix = configuration.getMetricPrefix() + DEFAULT_METRIC_SEPARATOR + server.get("name");
        this.metricList = (List<Map<String, ?>>) configuration.getConfigYml().get(METRICS);
        AssertUtils.assertNotNull(this.metricList, "The 'metrics' section in config.yml is either null or empty");
    }


    @Override
    public void run() {

        CloseableHttpClient httpClient = configuration.getContext().getHttpClient();

        String response = HttpClientUtils.getResponseAsStr(httpClient, server.get("host"));

        List<Metric> metrics = new ArrayList<>();
        if (response != null) {

            // this creates a Metric with default properties
            Metric metric = new Metric("HTTP Status", String.valueOf(BigInteger.ONE), metricPrefix
                    + DEFAULT_METRIC_SEPARATOR + " HTTP Status");
            metrics.add(metric);

        } else {
            Metric metric = new Metric("HTTP Status", String.valueOf(BigInteger.ZERO), metricPrefix
                    + DEFAULT_METRIC_SEPARATOR + " HTTP Status");
            metrics.add(metric);
        }
        metricWriteHelper.transformAndPrintMetrics(metrics);
    }


    @Override
    public void onTaskComplete() {
        logger.info("All tasks for server {} finished", this.server.get("name"));
    }
}

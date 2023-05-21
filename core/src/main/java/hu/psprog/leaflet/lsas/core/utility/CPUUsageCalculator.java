package hu.psprog.leaflet.lsas.core.utility;

import hu.psprog.leaflet.lsas.core.dockerapi.CPUStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;

/**
 * Utility class to calculate CPU usage of Docker containers.
 *
 * @author Peter Smith
 */
@Component
public class CPUUsageCalculator {

    private static final double DEFAULT_CPU_ACTIVITY = 0.0;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;
    private static final int PRECISION_SCALE = 2;

    /**
     * Calculates CPU usage as percentage.
     *
     * Calculation is based on the algorithm described in Docker Engine's documentation:
     *  1) Delta of the current and previous total CPU usage of the container process is calculated.
     *  2) Delta of the current and previous total CPU usage of the system is calculated.
     *  3) CPU load is calculated as the following: process usage delta divided by the system usage delta multiplied by the number of CPUs.
     *  4) The calculated value is converted to percentage value (multiplied by 100) and returned rounded to 2 fraction digits.
     *
     * @param containerRuntimeStatsModel {@link ContainerRuntimeStatsModel} containing raw statistics information
     * @return CPU usage percentage as {@link Double}
     */
    public Double calculateCPUUsagePercentage(ContainerRuntimeStatsModel containerRuntimeStatsModel) {

        CPUStatsModel currentCPUStats = containerRuntimeStatsModel.currentCPUStats();
        CPUStatsModel previousCPUStats = containerRuntimeStatsModel.previousCPUStats();

        double processCPUUsageDelta = calculateProcessCPUUsageDelta(currentCPUStats, previousCPUStats);
        double systemCPUUsageDelta = calculateSystemCPUUsageDelta(currentCPUStats, previousCPUStats);

        return isCPUActivityMeasured(processCPUUsageDelta, systemCPUUsageDelta)
                ? calculateCPULoad(currentCPUStats, processCPUUsageDelta, systemCPUUsageDelta)
                : DEFAULT_CPU_ACTIVITY;
    }

    private long calculateProcessCPUUsageDelta(CPUStatsModel currentCPUStats, CPUStatsModel previousCPUStats) {
        return currentCPUStats.cpuUsageModel().totalCPUUsage() - previousCPUStats.cpuUsageModel().totalCPUUsage();
    }

    private long calculateSystemCPUUsageDelta(CPUStatsModel currentCPUStats, CPUStatsModel previousCPUStats) {
        return currentCPUStats.systemCPUUsage() - previousCPUStats.systemCPUUsage();
    }

    private boolean isCPUActivityMeasured(double processCPUUsageDelta, double systemCPUUsageDelta) {
        return processCPUUsageDelta > DEFAULT_CPU_ACTIVITY && systemCPUUsageDelta > DEFAULT_CPU_ACTIVITY;
    }

    private double calculateCPULoad(CPUStatsModel currentCPUStats, double processCPUUsageDelta, double systemCPUUsageDelta) {

        int numberOfCPUs = currentCPUStats.onlineCPUs();
        double rawCPULoad = (processCPUUsageDelta / systemCPUUsageDelta) * numberOfCPUs * PERCENTAGE_MULTIPLIER;

        return Precision.round(rawCPULoad, PRECISION_SCALE);
    }
}

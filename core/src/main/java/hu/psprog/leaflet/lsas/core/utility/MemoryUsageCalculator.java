package hu.psprog.leaflet.lsas.core.utility;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.MemoryStatsModel;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utility class to calculate memory usage of Docker containers.
 *
 * @author Peter Smith
 */
@Component
public class MemoryUsageCalculator {

    private static final int MEGABYTES_DIVIDER = 1024 * 1024;
    private static final int PRECISION_SCALE = 2;
    private static final int INTEGER_ZERO = 0;
    private static final double PERCENTAGE_MULTIPLIER = 100.0;
    private static final double DOUBLE_ZERO = 0.0;

    /**
     * Calculates used memory in MB.
     *
     * Calculation is based on the algorithm described in Docker Engine's documentation:
     *  1) Amount of cached memory usage is subtracted from the currently used total memory of the container.
     *  2) Calculated value is divided by MEGABYTES_DIVIDER (byte -> MB) and returned.
     *
     * @param containerRuntimeStatsModel {@link ContainerRuntimeStatsModel} containing raw statistics information
     * @return currently used amount of memory in MB as {@link Integer}
     */
    public Integer extractMemoryUsageInMegabytes(ContainerRuntimeStatsModel containerRuntimeStatsModel) {

        return Optional.ofNullable(containerRuntimeStatsModel.memoryStats())
                .map(memory -> calculateMemoryUsage(memory) / MEGABYTES_DIVIDER)
                .map(Long::intValue)
                .orElse(INTEGER_ZERO);
    }

    /**
     * Calculates used memory as percentage of the total available system memory.
     *
     * Calculation is based on the algorithm described in Docker Engine's documentation:
     *  1) Amount of cached memory usage is subtracted from the currently used total memory of the container.
     *  2) Calculated value is divided by the total available memory ("limit").
     *  3) The calculated value is converted to percentage value (multiplied by 100) and returned rounded to 2 fraction digits.
     *
     * @param containerRuntimeStatsModel {@link ContainerRuntimeStatsModel} containing raw statistics information
     * @return currently used percentage of memory
     */
    public double calculateMemoryUsagePercent(ContainerRuntimeStatsModel containerRuntimeStatsModel) {

        return Optional.ofNullable(containerRuntimeStatsModel.memoryStats())
                .filter(memory -> memory.limit() > 0)
                .map(memory -> calculateMemoryUsage(memory) / ((double) memory.limit()) * PERCENTAGE_MULTIPLIER)
                .map(memoryUsagePercentage -> Precision.round(memoryUsagePercentage, PRECISION_SCALE))
                .orElse(DOUBLE_ZERO);
    }

    private long calculateMemoryUsage(MemoryStatsModel memory) {
        return memory.usage() - memory.cache();
    }
}

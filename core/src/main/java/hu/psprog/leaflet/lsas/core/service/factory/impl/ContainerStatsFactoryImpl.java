package hu.psprog.leaflet.lsas.core.service.factory.impl;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import hu.psprog.leaflet.lsas.core.service.factory.ContainerStatsFactory;
import hu.psprog.leaflet.lsas.core.utility.CPUUsageCalculator;
import hu.psprog.leaflet.lsas.core.utility.MemoryUsageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ContainerStatsFactory}.
 *
 * @author Peter Smith
 */
@Component
public class ContainerStatsFactoryImpl implements ContainerStatsFactory {

    private final CPUUsageCalculator cpuUsageCalculator;
    private final MemoryUsageCalculator memoryUsageCalculator;

    @Autowired
    public ContainerStatsFactoryImpl(CPUUsageCalculator cpuUsageCalculator, MemoryUsageCalculator memoryUsageCalculator) {
        this.cpuUsageCalculator = cpuUsageCalculator;
        this.memoryUsageCalculator = memoryUsageCalculator;
    }

    @Override
    public ContainerStats create(String containerID, ContainerRuntimeStatsModel containerRuntimeStatsModel) {

        return ContainerStats.builder()
                .id(containerID)
                .memoryUsageInMegabytes(memoryUsageCalculator.extractMemoryUsageInMegabytes(containerRuntimeStatsModel))
                .memoryUsagePercent(memoryUsageCalculator.calculateMemoryUsagePercent(containerRuntimeStatsModel))
                .cpuUsagePercent(cpuUsageCalculator.calculateCPUUsagePercentage(containerRuntimeStatsModel))
                .build();
    }
}

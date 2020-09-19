package hu.psprog.leaflet.lsas.core.service.factory.impl;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import hu.psprog.leaflet.lsas.core.utility.CPUUsageCalculator;
import hu.psprog.leaflet.lsas.core.utility.MemoryUsageCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ContainerStatsFactoryImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ContainerStatsFactoryImplTest {

    private static final String ID = "abcd1234";
    private static final double CPU_USAGE_PERCENT = 10.0;
    private static final int MEMORY_USAGE_IN_MEGABYTES = 150;
    private static final double MEMORY_USAGE_PERCENT = 12.0;
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL = ContainerRuntimeStatsModel.builder().build();
    private static final ContainerStats CONTAINER_STATS = ContainerStats.builder()
            .id(ID)
            .cpuUsagePercent(CPU_USAGE_PERCENT)
            .memoryUsageInMegabytes(MEMORY_USAGE_IN_MEGABYTES)
            .memoryUsagePercent(MEMORY_USAGE_PERCENT)
            .build();

    @Mock
    private CPUUsageCalculator cpuUsageCalculator;

    @Mock
    private MemoryUsageCalculator memoryUsageCalculator;

    @InjectMocks
    private ContainerStatsFactoryImpl containerStatsFactory;

    @Test
    public void shouldCreateCallCalculatorsToCreateContainerStats() {

        // given
        given(memoryUsageCalculator.extractMemoryUsageInMegabytes(CONTAINER_RUNTIME_STATS_MODEL)).willReturn(MEMORY_USAGE_IN_MEGABYTES);
        given(memoryUsageCalculator.calculateMemoryUsagePercent(CONTAINER_RUNTIME_STATS_MODEL)).willReturn(MEMORY_USAGE_PERCENT);
        given(cpuUsageCalculator.calculateCPUUsagePercentage(CONTAINER_RUNTIME_STATS_MODEL)).willReturn(CPU_USAGE_PERCENT);

        // when
        ContainerStats result = containerStatsFactory.create(ID, CONTAINER_RUNTIME_STATS_MODEL);

        // then
        assertThat(result, equalTo(CONTAINER_STATS));
    }
}

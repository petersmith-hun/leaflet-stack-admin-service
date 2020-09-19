package hu.psprog.leaflet.lsas.core.utility;

import hu.psprog.leaflet.lsas.core.dockerapi.CPUStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CPUUsageCalculator}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class CPUUsageCalculatorTest {

    private static final long PROCESSOR_USAGE_MULTIPLIER = 1000L;

    @InjectMocks
    private CPUUsageCalculator cpuUsageCalculator;

    @ParameterizedTest
    @MethodSource("cpuUsageCalculationParameterSource")
    public void shouldCalculateCPUUsagePercentageReturnUsage(ContainerRuntimeStatsModel runtimeStatsModel, double expectedCPUUsage) {

        // when
        Double result = cpuUsageCalculator.calculateCPUUsagePercentage(runtimeStatsModel);

        // then
        assertThat(result, equalTo(expectedCPUUsage));
    }

    private static Stream<Arguments> cpuUsageCalculationParameterSource() {

        return Stream.of(
                Arguments.arguments(cpuStats(4, 100, 1000, 90, 900), 40.0),
                Arguments.arguments(cpuStats(2, 100, 1000, 90, 900), 20.0),
                Arguments.arguments(cpuStats(4, 100, 1000, 100, 900), 0.0),
                Arguments.arguments(cpuStats(4, 100, 1000, 100, 1000), 0.0),
                Arguments.arguments(cpuStats(4, 100, 1000, 90, 1000), 0.0),
                Arguments.arguments(cpuStats(2, 500, 3000, 200, 2000), 60.0)
        );
    }

    private static ContainerRuntimeStatsModel cpuStats(int numberOfCPUs, int currentProcessTotal, int currentSystemTotal, int previousProcessTotal, int previousSystemTotal) {

        return ContainerRuntimeStatsModel.builder()
                .currentCPUStats(CPUStatsModel.builder()
                        .onlineCPUs(numberOfCPUs)
                        .cpuUsageModel(CPUStatsModel.CPUUsageModel.builder()
                                .totalCPUUsage(currentProcessTotal * PROCESSOR_USAGE_MULTIPLIER)
                                .build())
                        .systemCPUUsage(currentSystemTotal * PROCESSOR_USAGE_MULTIPLIER)
                        .build())
                .previousCPUStats(CPUStatsModel.builder()
                        .onlineCPUs(numberOfCPUs)
                        .cpuUsageModel(CPUStatsModel.CPUUsageModel.builder()
                                .totalCPUUsage(previousProcessTotal * PROCESSOR_USAGE_MULTIPLIER)
                                .build())
                        .systemCPUUsage(previousSystemTotal * PROCESSOR_USAGE_MULTIPLIER)
                        .build())
                .build();
    }
}

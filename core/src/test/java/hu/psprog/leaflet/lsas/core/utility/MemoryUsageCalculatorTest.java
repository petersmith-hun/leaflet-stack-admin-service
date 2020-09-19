package hu.psprog.leaflet.lsas.core.utility;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.MemoryStatsModel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link MemoryUsageCalculator}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class MemoryUsageCalculatorTest {

    @InjectMocks
    private MemoryUsageCalculator memoryUsageCalculator;

    @ParameterizedTest
    @MethodSource("memoryUsageExtractionParameterSource")
    public void shouldExtractMemoryUsageInMegabytesReturnUsage(MemoryStatsModel memoryStatsModel, Integer expectedMemoryUsage) {

        // given
        ContainerRuntimeStatsModel source = ContainerRuntimeStatsModel.builder()
                .memoryStats(memoryStatsModel)
                .build();

        // when
        Integer result = memoryUsageCalculator.extractMemoryUsageInMegabytes(source);

        // then
        assertThat(result, equalTo(expectedMemoryUsage));
    }

    @ParameterizedTest
    @MethodSource("memoryUsagePercentageParameterSource")
    public void shouldCalculateMemoryUsagePercentReturnUsageInPercent(MemoryStatsModel memoryStatsModel, double expectedMemoryUsagePercent) {

        // given
        ContainerRuntimeStatsModel source = ContainerRuntimeStatsModel.builder()
                .memoryStats(memoryStatsModel)
                .build();

        // when
        double result = memoryUsageCalculator.calculateMemoryUsagePercent(source);

        // then
        assertThat(result, equalTo(expectedMemoryUsagePercent));
    }

    private static Stream<Arguments> memoryUsageExtractionParameterSource() {

        return Stream.of(
                Arguments.arguments(memoryStatsModel(104857600L, 0L, 0L), 100),
                Arguments.arguments(memoryStatsModel(104857000L, 0L, 0L), 99),
                Arguments.arguments(memoryStatsModel(104857600L, 10485760L, 0L), 90),
                Arguments.arguments(null, 0)
        );
    }

    private static Stream<Arguments> memoryUsagePercentageParameterSource() {

        return Stream.of(
                Arguments.arguments(memoryStatsModel(104857600L, 0L, 3221225472L), 3.26),
                Arguments.arguments(memoryStatsModel(104857600L, 10485760L, 188743680L), 50.0),
                Arguments.arguments(memoryStatsModel(104857600L, 0L, 0L), 0.0),
                Arguments.arguments(null, 0.0)
        );
    }

    private static MemoryStatsModel memoryStatsModel(Long usage, Long cache, Long limit) {

        return MemoryStatsModel.builder()
                .usage(usage)
                .stats(Map.of("cache", cache))
                .limit(limit)
                .build();
    }
}

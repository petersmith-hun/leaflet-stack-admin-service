package hu.psprog.leaflet.lsas.core.service.impl;

import hu.psprog.leaflet.lsas.core.client.DockerContainerStatisticsClient;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.MemoryStatsModel;
import hu.psprog.leaflet.lsas.core.domain.Container;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import hu.psprog.leaflet.lsas.core.service.converter.ContainerConverter;
import hu.psprog.leaflet.lsas.core.service.converter.ContainerDetailsConverter;
import hu.psprog.leaflet.lsas.core.service.factory.ContainerStatsFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link DockerEngineServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerEngineServiceImplTest {

    private static final String CONTAINER_ID_1 = "container-1";
    private static final String CONTAINER_ID_2 = "container-2";
    private static final ContainerModel CONTAINER_MODEL_1 = ContainerModel.builder().id(CONTAINER_ID_1).build();
    private static final ContainerModel CONTAINER_MODEL_2 = ContainerModel.builder().id(CONTAINER_ID_2).build();
    private static final ContainerDetailsModel CONTAINER_DETAILS_MODEL_1 = ContainerDetailsModel.builder().id(CONTAINER_ID_1).build();
    private static final ContainerDetailsModel CONTAINER_DETAILS_MODEL_2 = ContainerDetailsModel.builder().id(CONTAINER_ID_2).build();
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL_1_1 = ContainerRuntimeStatsModel.builder()
            .memoryStats(MemoryStatsModel.builder().usage(1000L).build())
            .build();
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL_2_1 = ContainerRuntimeStatsModel.builder()
            .memoryStats(MemoryStatsModel.builder().usage(2000L).build())
            .build();
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL_1_2 = ContainerRuntimeStatsModel.builder()
            .memoryStats(MemoryStatsModel.builder().usage(3000L).build())
            .build();
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL_2_2 = ContainerRuntimeStatsModel.builder()
            .memoryStats(MemoryStatsModel.builder().usage(4000L).build())
            .build();
    private static final Container CONTAINER_1 = Container.builder().id(CONTAINER_ID_1).build();
    private static final Container CONTAINER_2 = Container.builder().id(CONTAINER_ID_2).build();
    private static final ContainerDetails CONTAINER_DETAILS_1 = ContainerDetails.builder().id(CONTAINER_ID_1).build();
    private static final ContainerDetails CONTAINER_DETAILS_2 = ContainerDetails.builder().id(CONTAINER_ID_2).build();
    private static final ContainerStats CONTAINER_STATS_1_1 = ContainerStats.builder().memoryUsagePercent(10.0).build();
    private static final ContainerStats CONTAINER_STATS_2_1 = ContainerStats.builder().memoryUsagePercent(20.0).build();
    private static final ContainerStats CONTAINER_STATS_1_2 = ContainerStats.builder().memoryUsagePercent(30.0).build();
    private static final ContainerStats CONTAINER_STATS_2_2 = ContainerStats.builder().memoryUsagePercent(40.0).build();

    @Mock
    private DockerContainerStatisticsClient dockerContainerStatisticsClient;

    @Mock
    private ContainerConverter containerConverter;

    @Mock
    private ContainerDetailsConverter containerDetailsConverter;

    @Mock
    private ContainerStatsFactory containerStatsFactory;

    @InjectMocks
    private DockerEngineServiceImpl dockerEngineService;

    @Test
    public void shouldGetExistingContainersReturnMappedListOfContainers() {

        // given
        given(dockerContainerStatisticsClient.getExistingContainers())
                .willReturn(Mono.just(Arrays.asList(CONTAINER_MODEL_1, CONTAINER_MODEL_2)));
        given(containerConverter.convert(CONTAINER_MODEL_1)).willReturn(CONTAINER_1);
        given(containerConverter.convert(CONTAINER_MODEL_2)).willReturn(CONTAINER_2);

        // when
        Mono<List<Container>> result = dockerEngineService.getExistingContainers();

        // then
        assertThat(result.block(), equalTo(Arrays.asList(CONTAINER_1, CONTAINER_2)));
    }

    @Test
    public void shouldGetContainerDetailsReturnMergedInfoOfAllContainers() {

        // given
        given(dockerContainerStatisticsClient.getContainerDetails(CONTAINER_ID_1)).willReturn(Mono.just(CONTAINER_DETAILS_MODEL_1));
        given(dockerContainerStatisticsClient.getContainerDetails(CONTAINER_ID_2)).willReturn(Mono.just(CONTAINER_DETAILS_MODEL_2));
        given(containerDetailsConverter.convert(CONTAINER_DETAILS_MODEL_1)).willReturn(CONTAINER_DETAILS_1);
        given(containerDetailsConverter.convert(CONTAINER_DETAILS_MODEL_2)).willReturn(CONTAINER_DETAILS_2);

        // when
        Flux<ContainerDetails> result = dockerEngineService.getContainerDetails(Arrays.asList(CONTAINER_ID_1, CONTAINER_ID_2));

        // then
        List<ContainerDetails> resultList = new ArrayList<>(2);
        result.doOnNext(resultList::add)
                .take(2)
                .blockLast();
        assertThat(resultList, hasItems(CONTAINER_DETAILS_1, CONTAINER_DETAILS_2));
    }

    @Test
    public void shouldGetContainerStatisticsReturnMergedInfoOfAllContainers() {

        // given
        given(dockerContainerStatisticsClient.getContainerRuntimeStats(CONTAINER_ID_1))
                .willReturn(Flux.just(CONTAINER_RUNTIME_STATS_MODEL_1_1, CONTAINER_RUNTIME_STATS_MODEL_1_2));
        given(dockerContainerStatisticsClient.getContainerRuntimeStats(CONTAINER_ID_2))
                .willReturn(Flux.just(CONTAINER_RUNTIME_STATS_MODEL_2_1, CONTAINER_RUNTIME_STATS_MODEL_2_2));
        given(containerStatsFactory.create(CONTAINER_ID_1, CONTAINER_RUNTIME_STATS_MODEL_1_1)).willReturn(CONTAINER_STATS_1_1);
        given(containerStatsFactory.create(CONTAINER_ID_1, CONTAINER_RUNTIME_STATS_MODEL_1_2)).willReturn(CONTAINER_STATS_1_2);
        given(containerStatsFactory.create(CONTAINER_ID_2, CONTAINER_RUNTIME_STATS_MODEL_2_1)).willReturn(CONTAINER_STATS_2_1);
        given(containerStatsFactory.create(CONTAINER_ID_2, CONTAINER_RUNTIME_STATS_MODEL_2_2)).willReturn(CONTAINER_STATS_2_2);

        // when
        Flux<ContainerStats> result = dockerEngineService.getContainerStatistics(Arrays.asList(CONTAINER_ID_1, CONTAINER_ID_2));

        // then
        List<ContainerStats> resultList = result.collectList().block();
        assertThat(resultList, hasSize(4));
        assertThat(resultList, hasItems(CONTAINER_STATS_1_1, CONTAINER_STATS_1_2, CONTAINER_STATS_2_1, CONTAINER_STATS_2_2));
    }
}

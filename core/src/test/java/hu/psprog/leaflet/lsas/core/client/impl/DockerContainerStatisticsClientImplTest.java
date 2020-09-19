package hu.psprog.leaflet.lsas.core.client.impl;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.MemoryStatsModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link DockerContainerStatisticsClientImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerContainerStatisticsClientImplTest {

    private static final String CONTAINER_ID = "container1234";
    private static final ParameterizedTypeReference<List<ContainerModel>> CONTAINER_MODEL_LIST_TYPEREF
            = new ParameterizedTypeReference<>() {};
    private static final List<ContainerModel> CONTAINER_MODEL_LIST
            = Collections.singletonList(ContainerModel.builder().id(CONTAINER_ID).build());
    private static final ContainerDetailsModel CONTAINER_DETAILS_MODEL = ContainerDetailsModel.builder()
            .id(CONTAINER_ID)
            .build();
    private static final ContainerRuntimeStatsModel CONTAINER_RUNTIME_STATS_MODEL = ContainerRuntimeStatsModel.builder()
            .memoryStats(MemoryStatsModel.builder()
                    .usage(12345678L)
                    .build())
            .build();

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private DockerContainerStatisticsClientImpl dockerContainerStatisticsClient;

    @Test
    public void shouldGetExistingContainersReturnListOfContainersAsMono() {

        // given
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/containers/json?all=true")).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(CONTAINER_MODEL_LIST_TYPEREF)).willReturn(Mono.just(CONTAINER_MODEL_LIST));

        // when
        Mono<List<ContainerModel>> result = dockerContainerStatisticsClient.getExistingContainers();

        // then
        assertThat(result.block(), equalTo(CONTAINER_MODEL_LIST));
    }

    @Test
    public void shouldGetContainerDetailsReturnWithResultAsMono() {

        // given
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/containers/{id}/json", CONTAINER_ID)).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(ContainerDetailsModel.class)).willReturn(Mono.just(CONTAINER_DETAILS_MODEL));

        // when
        Mono<ContainerDetailsModel> result = dockerContainerStatisticsClient.getContainerDetails(CONTAINER_ID);

        // then
        assertThat(result.block(), equalTo(CONTAINER_DETAILS_MODEL));
    }

    @Test
    public void shouldGetContainerDetailsReturnWithEmptyMonoOnError() {

        // given
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/containers/{id}/json", CONTAINER_ID)).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(ContainerDetailsModel.class)).willReturn(Mono.error(RuntimeException::new));

        // when
        Mono<ContainerDetailsModel> result = dockerContainerStatisticsClient.getContainerDetails(CONTAINER_ID);

        // then
        assertThat(result.getClass().getName(), equalTo("reactor.core.publisher.MonoOnErrorResume"));
        assertThat(result.block(), nullValue());
    }

    @Test
    public void shouldGetContainerRuntimeStatisticsReturnWithResultAsFlux() {

        // given
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/containers/{id}/stats", CONTAINER_ID)).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToFlux(ContainerRuntimeStatsModel.class)).willReturn(Flux.just(CONTAINER_RUNTIME_STATS_MODEL));

        // when
        Flux<ContainerRuntimeStatsModel> result = dockerContainerStatisticsClient.getContainerRuntimeStats(CONTAINER_ID);

        // then
        assertThat(result.blockFirst(), equalTo(CONTAINER_RUNTIME_STATS_MODEL));
    }

    @Test
    public void shouldGetContainerRuntimeStatisticsReturnWithEmptyFluxOnError() {

        // given
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/containers/{id}/stats", CONTAINER_ID)).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToFlux(ContainerRuntimeStatsModel.class)).willReturn(Flux.error(RuntimeException::new));

        // when
        Flux<ContainerRuntimeStatsModel> result = dockerContainerStatisticsClient.getContainerRuntimeStats(CONTAINER_ID);

        // then
        assertThat(result.getClass().getName(), equalTo("reactor.core.publisher.FluxOnErrorResume"));
        assertThat(result.blockFirst(), nullValue());
    }
}

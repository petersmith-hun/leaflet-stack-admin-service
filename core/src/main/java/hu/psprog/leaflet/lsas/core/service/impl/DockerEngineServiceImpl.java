package hu.psprog.leaflet.lsas.core.service.impl;

import hu.psprog.leaflet.lsas.core.client.DockerContainerStatisticsClient;
import hu.psprog.leaflet.lsas.core.domain.Container;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import hu.psprog.leaflet.lsas.core.service.DockerEngineService;
import hu.psprog.leaflet.lsas.core.service.converter.ContainerConverter;
import hu.psprog.leaflet.lsas.core.service.converter.ContainerDetailsConverter;
import hu.psprog.leaflet.lsas.core.service.factory.ContainerStatsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DockerEngineService}.
 *
 * @author Peter Smith
 */
@Service
public class DockerEngineServiceImpl implements DockerEngineService {

    private static final Duration POLL_FREQUENCY = Duration.ofSeconds(1L);

    private final DockerContainerStatisticsClient dockerContainerStatisticsClient;
    private final ContainerConverter containerConverter;
    private final ContainerDetailsConverter containerDetailsConverter;
    private final ContainerStatsFactory containerStatsFactory;

    @Autowired
    public DockerEngineServiceImpl(DockerContainerStatisticsClient dockerContainerStatisticsClient, ContainerConverter containerConverter,
                                   ContainerDetailsConverter containerDetailsConverter, ContainerStatsFactory containerStatsFactory) {
        this.dockerContainerStatisticsClient = dockerContainerStatisticsClient;
        this.containerConverter = containerConverter;
        this.containerDetailsConverter = containerDetailsConverter;
        this.containerStatsFactory = containerStatsFactory;
    }

    @Override
    public Mono<List<Container>> getExistingContainers() {

        return dockerContainerStatisticsClient.getExistingContainers()
                .map(containerModels -> containerModels.stream()
                        .map(containerConverter::convert)
                        .collect(Collectors.toList()));
    }

    @Override
    public Flux<ContainerDetails> getContainerDetails(List<String> containerIDList) {

        return containerIDList.stream()
                .map(this::getContainerDetails)
                .reduce(Flux::merge)
                .orElseGet(Flux::empty);
    }

    @Override
    public Flux<ContainerStats> getContainerStatistics(List<String> containerIDList) {

        return containerIDList.stream()
                .map(this::getContainerStatistics)
                .reduce(Flux::merge)
                .orElseGet(Flux::empty);
    }

    private Flux<ContainerDetails> getContainerDetails(String containerID) {

        return dockerContainerStatisticsClient.getContainerDetails(containerID)
                .mapNotNull(containerDetailsConverter::convert)
                .repeatWhen(longFlux -> Flux.interval(POLL_FREQUENCY));
    }

    private Flux<ContainerStats> getContainerStatistics(String containerID) {

        return dockerContainerStatisticsClient.getContainerRuntimeStats(containerID)
                .map(statistics -> containerStatsFactory.create(containerID, statistics));
    }


}

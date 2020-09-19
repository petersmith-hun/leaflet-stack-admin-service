package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.Container;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import hu.psprog.leaflet.lsas.core.service.DockerEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Mono/Flux REST controller to retrieve Docker cluster information.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping("/lsas/containers")
public class DockerEngineController {

    private static final String IDS_PARAMETER = "ids";

    private final DockerEngineService dockerEngineService;

    @Autowired
    public DockerEngineController(DockerEngineService dockerEngineService) {
        this.dockerEngineService = dockerEngineService;
    }

    /**
     * GET /containers
     * Returns a list of the existing containers.
     *
     * @return list of existing containers as {@link ContainerModel} objects, wrapped as {@link Mono}
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Container>> getRunningContainers() {

        return dockerEngineService.getExistingContainers();
    }

    /**
     * GET /lsas/containers/details?ids=...
     * Returns detailed information about the specified containers as streaming data.
     *
     * @param containerIDList list of container ID strings as provided by Docker
     * @return detailed information about the containers as {@link ContainerDetailsModel} wrapped as {@link Flux}
     */
    @GetMapping(value = "/details", produces = MediaType.TEXT_EVENT_STREAM_VALUE, params = IDS_PARAMETER)
    public Flux<ContainerDetails> getContainerDetails(@RequestParam(IDS_PARAMETER) List<String> containerIDList) {

        return dockerEngineService.getContainerDetails(containerIDList);
    }

    /**
     * GET /lsas/containers/stats?ids=...
     * Returns statistics about the specified containers as streaming data.
     *
     * @param containerIDList container ID strings as provided by Docker
     * @return statistics about the container as {@link ContainerRuntimeStatsModel} wrapped as {@link Flux}
     */
    @GetMapping(value = "/stats", produces = MediaType.TEXT_EVENT_STREAM_VALUE, params = IDS_PARAMETER)
    public Flux<ContainerStats> getContainerStats(@RequestParam(IDS_PARAMETER) List<String> containerIDList) {

        return dockerEngineService.getContainerStatistics(containerIDList);
    }
}

package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.service.DockerEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DockerEngineController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerEngineControllerTest {

    private static final List<String> CONTAINER_ID_LIST = Collections.singletonList("abcd1234");

    @Mock
    private DockerEngineService dockerEngineService;

    @InjectMocks
    private DockerEngineController dockerEngineController;

    @Test
    public void shouldGetRunningContainersCallRelevantServiceMethod() {

        // when
        dockerEngineController.getRunningContainers();

        // then
        verify(dockerEngineService).getExistingContainers();
    }

    @Test
    public void shouldGetContainerDetailsCallRelevantServiceMethod() {

        // when
        dockerEngineController.getContainerDetails(CONTAINER_ID_LIST);

        // then
        verify(dockerEngineService).getContainerDetails(CONTAINER_ID_LIST);
    }

    @Test
    public void shouldGetContainerStatsCallRelevantServiceMethod() {

        // when
        dockerEngineController.getContainerStats(CONTAINER_ID_LIST);

        // then
        verify(dockerEngineService).getContainerStatistics(CONTAINER_ID_LIST);
    }
}

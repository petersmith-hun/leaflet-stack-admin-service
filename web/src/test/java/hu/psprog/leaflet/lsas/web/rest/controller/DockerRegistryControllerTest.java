package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.domain.DockerRegistryContent;
import hu.psprog.leaflet.lsas.core.domain.DockerRepository;
import hu.psprog.leaflet.lsas.core.service.DockerRegistryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DockerRegistryController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerRegistryControllerTest {

    private static final String REGISTRY_ID = "registry-1";
    private static final String REPOSITORY_ID = "repository-1";
    private static final String GROUP_ID = "group-1";
    private static final String TAG = "1.0";
    private static final String GROUPED_REPOSITORY_ID = "group-1/repository-1";
    private static final DockerRegistryContent DOCKER_REGISTRY_CONTENT = new DockerRegistryContent(REGISTRY_ID, Collections.emptyList());
    private static final DockerRepository DOCKER_REPOSITORY = new DockerRepository(REGISTRY_ID, REPOSITORY_ID, Collections.emptyList());
    private static final Map<String, String> REGISTRY_MAP = Map.of("some-registry", "http://localhost");

    @Mock
    private DockerRegistryService dockerRegistryService;

    @InjectMocks
    private DockerRegistryController dockerRegistryController;

    @Test
    public void shouldListRegistriesCallService() {

        // given
        given(dockerRegistryService.listRegistries()).willReturn(REGISTRY_MAP);

        // when
        ResponseEntity<Map<String, String>> result = dockerRegistryController.listRegistries();

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), equalTo(REGISTRY_MAP));
    }

    @Test
    public void shouldGetRepositoriesRespondAsMono() {

        // given
        given(dockerRegistryService.getRepositories(REGISTRY_ID)).willReturn(Mono.just(DOCKER_REGISTRY_CONTENT));

        // when
        Mono<DockerRegistryContent> result = dockerRegistryController.getRepositories(REGISTRY_ID);

        // then
        assertThat(result.block(), equalTo(DOCKER_REGISTRY_CONTENT));
    }

    @Test
    public void shouldGetRepositoryTagsRespondAsMonoForNonGroupedRequest() {

        // given
        given(dockerRegistryService.getRepositoryDetails(REGISTRY_ID, REPOSITORY_ID))
                .willReturn(Mono.just(DOCKER_REPOSITORY));

        // when
        Mono<DockerRepository> result = dockerRegistryController.getRepositoryTags(REGISTRY_ID, REPOSITORY_ID);

        // then
        assertThat(result.block(), equalTo(DOCKER_REPOSITORY));
    }

    @Test
    public void shouldGetRepositoryTagsRespondAsMonoForGroupedRequest() {

        // given
        given(dockerRegistryService.getRepositoryDetails(REGISTRY_ID, GROUPED_REPOSITORY_ID))
                .willReturn(Mono.just(DOCKER_REPOSITORY));

        // when
        Mono<DockerRepository> result = dockerRegistryController.getRepositoryTags(REGISTRY_ID, GROUP_ID, REPOSITORY_ID);

        // then
        assertThat(result.block(), equalTo(DOCKER_REPOSITORY));
    }

    @Test
    public void shouldDeleteImageByTagRespondAsResponseEntityForNonGroupedRequest() {

        // when
        ResponseEntity<Void> result = dockerRegistryController.deleteImageByTag(REGISTRY_ID, REPOSITORY_ID, TAG);

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        verify(dockerRegistryService).deleteImageByTag(REGISTRY_ID, REPOSITORY_ID, TAG);
    }

    @Test
    public void shouldDeleteImageByTagRespondAsResponseEntityForGroupedRequest() {

        // when
        ResponseEntity<Void> result = dockerRegistryController.deleteImageByTag(REGISTRY_ID, GROUP_ID, REPOSITORY_ID, TAG);

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
        verify(dockerRegistryService).deleteImageByTag(REGISTRY_ID, GROUPED_REPOSITORY_ID, TAG);
    }
}

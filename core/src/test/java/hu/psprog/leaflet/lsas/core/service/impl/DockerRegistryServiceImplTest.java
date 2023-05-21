package hu.psprog.leaflet.lsas.core.service.impl;

import hu.psprog.leaflet.lsas.core.client.DockerRegistryClient;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import hu.psprog.leaflet.lsas.core.domain.DockerRegistryContent;
import hu.psprog.leaflet.lsas.core.domain.DockerRepository;
import hu.psprog.leaflet.lsas.core.domain.DockerTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DockerRegistryServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerRegistryServiceImplTest {

    private static final ServiceRegistrations.DockerIntegration DOCKER_INTEGRATION = new ServiceRegistrations.DockerIntegration();

    static {
        ServiceRegistrations.DockerRegistry leafletDockerRegistry = new ServiceRegistrations.DockerRegistry();
        leafletDockerRegistry.setHost("http://localhost:5000");
        ServiceRegistrations.DockerRegistry someOtherDockerRegistry = new ServiceRegistrations.DockerRegistry();
        someOtherDockerRegistry.setHost("http://localhost:5001");

        DOCKER_INTEGRATION.getRegistryCatalog().put("leaflet", leafletDockerRegistry);
        DOCKER_INTEGRATION.getRegistryCatalog().put("other", someOtherDockerRegistry);
    }

    @Mock
    private DockerRegistryClient dockerRegistryClient;

    @Mock
    private ServiceRegistrations serviceRegistrations;

    private DockerRegistryServiceImpl dockerRegistryService;

    @BeforeEach
    public void setup() {
        given(serviceRegistrations.getDockerIntegration()).willReturn(DOCKER_INTEGRATION);
        dockerRegistryService = new DockerRegistryServiceImpl(serviceRegistrations, dockerRegistryClient);
    }

    @Test
    public void shouldListRegistries() {

        // when
        Map<String, String> result = dockerRegistryService.listRegistries();

        // then
        assertThat(result, aMapWithSize(2));
        assertThat(result.get("leaflet"), equalTo("http://localhost:5000"));
        assertThat(result.get("other"), equalTo("http://localhost:5001"));
    }

    @Test
    public void shouldGetRepositories() {

        // given
        String registryID = "leaflet";
        List<String> repositories = Arrays.asList("leaflet", "cbfs", "lms");
        DockerRepositories dockerRepositories = DockerRepositories.builder()
                .repositories(repositories)
                .build();
        DockerRegistryContent expectedResult = new DockerRegistryContent(registryID, repositories);

        given(dockerRegistryClient.getRepositories(registryID)).willReturn(Mono.just(dockerRepositories));

        // when
        DockerRegistryContent result = dockerRegistryService.getRepositories(registryID).block();

        // then
        assertThat(result, equalTo(expectedResult));
    }

    @Test
    public void shouldGetRepositoryDetails() {

        // given
        String registryID = "leaflet";
        String repositoryID = "cbfs";
        Map<String, DockerTagManifest> tags = Map.of(
                "latest", prepareManifest("latest", 0),
                "1.0", prepareManifest("1.0", -10),
                "2.0", prepareManifest("2.0", -5),
                "3.0", prepareManifest("3.0", 0)
        );
        DockerTags dockerTags = DockerTags.builder()
                .name(repositoryID)
                .tags(List.copyOf(tags.keySet()))
                .build();

        given(dockerRegistryClient.getRepositoryTags(registryID,  repositoryID)).willReturn(Mono.just(dockerTags));
        tags.forEach((tag, manifest) ->
                given(dockerRegistryClient.getTagManifest(registryID, repositoryID, tag))
                        .willReturn(Mono.just(manifest)));

        // when
        DockerRepository result = dockerRegistryService.getRepositoryDetails(registryID, repositoryID).block();

        // then
        assertThat(result.registry(), equalTo(registryID));
        assertThat(result.name(), equalTo(repositoryID));
        assertThat(result.tags(), hasSize(4));
        assertThat(result.tags().stream().map(DockerTag::name).collect(Collectors.toList()),
                equalTo(Arrays.asList("latest", "3.0", "2.0", "1.0")));
    }

    @Test
    public void shouldDeleteImageByTag() {

        // given
        String registryID = "leaflet";
        String repositoryID = "cbfs";
        String tag = "1.0";
        String tagDigest = "1.0-digest";

        given(dockerRegistryClient.getTagDigest(registryID, repositoryID, tag)).willReturn(Mono.just(tagDigest));

        // when
        dockerRegistryService.deleteImageByTag(registryID, repositoryID, tag);

        // then
        verify(dockerRegistryClient).deleteTagByDigest(registryID, repositoryID, tagDigest);
    }

    private DockerTagManifest prepareManifest(String tag, int dayOffset) {

        ZonedDateTime baseTime = ZonedDateTime
                .of(2021, 1, 5, 22, 0, 0, 0, ZoneId.of("UTC"))
                .plusDays(dayOffset);

        String v1Compatibility = String.format("{\"created\":\"%s\"}", baseTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        DockerTagManifest.DockerTagHistory tagHistory = new DockerTagManifest.DockerTagHistory(v1Compatibility);

        return new DockerTagManifest(tag, Collections.singletonList(tagHistory));
    }
}
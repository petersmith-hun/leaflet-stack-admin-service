package hu.psprog.leaflet.lsas.core.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import hu.psprog.leaflet.lsas.core.client.DockerRegistryClient;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeType;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link DockerRegistryClientImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerRegistryClientImplTest {

    private static final ServiceRegistrations SERVICE_REGISTRATIONS = prepareConfig();
    private static final Jackson2JsonDecoder DOCKER_MANIFEST_DECODER =
            new Jackson2JsonDecoder(new ObjectMapper(), new MimeType("application", "vnd.docker.distribution.manifest.v1+prettyjws"));

    private static final String REGISTRY_ID_1 = "registry-1";
    private static final String REGISTRY_ID_2 = "registry-2";
    private static final String REPOSITORY_ID = "repository-1";
    private static final String TAG = "latest";
    private static final DockerRepositories DOCKER_REPOSITORIES = DockerRepositories.builder()
            .repositories(Arrays.asList("lcfa", "leaflet", "lms"))
            .build();
    private static final DockerTags DOCKER_TAGS = DockerTags.builder()
            .name(REPOSITORY_ID)
            .tags(Arrays.asList("1.0", "2.0", "latest"))
            .build();
    private static final DockerTagManifest.DockerTagHistory DOCKER_TAG_HISTORY = new DockerTagManifest.DockerTagHistory("tag-history");
    private static final DockerTagManifest DOCKER_TAG_MANIFEST = new DockerTagManifest(TAG, Collections.singletonList(DOCKER_TAG_HISTORY));
    private static final String TAG_DIGEST = "tag-digest";

    private static WireMockServer wireMockServerRegistry1;
    private static WireMockServer wireMockServerRegistry2;
    private static DockerRegistryClient dockerRegistryClient =
            new DockerRegistryClientImpl(SERVICE_REGISTRATIONS, DOCKER_MANIFEST_DECODER);

    @BeforeAll
    public static void setupClass() {
        wireMockServerRegistry1 = new WireMockServer(wireMockConfig().port(9999));
        wireMockServerRegistry1.start();
        wireMockServerRegistry2 = new WireMockServer(wireMockConfig().port(9998));
        wireMockServerRegistry2.start();
    }

    @AfterAll
    public static void tearDownClass() {
        wireMockServerRegistry1.stop();
        wireMockServerRegistry2.stop();
    }

    @AfterEach
    public void tearDown() {
        wireMockServerRegistry1.resetAll();
        wireMockServerRegistry2.resetAll();
    }

    @Test
    public void shouldGetRepositories() {

        // given
        wireMockServerRegistry1.givenThat(get(urlEqualTo("/v2/_catalog"))
                .willReturn(ResponseDefinitionBuilder.okForJson(DOCKER_REPOSITORIES)));

        // when
        Mono<DockerRepositories> result = dockerRegistryClient.getRepositories(REGISTRY_ID_1);

        // then
        assertThat(result.block(), equalTo(DOCKER_REPOSITORIES));
        verifyAuthorization(wireMockServerRegistry1);
    }

    @Test
    public void shouldGetRepositoriesThrowExceptionWhenUnknownClientIsRequested() {

        // when
        assertThrows(IllegalArgumentException.class,
                () -> dockerRegistryClient.getRepositories("non-existing-registry"));

        // then
        // exception expected
    }

    @Test
    public void shouldGetRepositoryTags() {

        // given
        wireMockServerRegistry1.givenThat(get(urlEqualTo("/v2/repository-1/tags/list"))
                .willReturn(ResponseDefinitionBuilder.okForJson(DOCKER_TAGS)));

        // when
        Mono<DockerTags> result = dockerRegistryClient.getRepositoryTags(REGISTRY_ID_1, REPOSITORY_ID);

        // then
        assertThat(result.block(), equalTo(DOCKER_TAGS));
        verifyAuthorization(wireMockServerRegistry1);
    }

    @Test
    public void shouldGetTagManifest() {

        // given
        wireMockServerRegistry2.givenThat(get(urlEqualTo("/v2/repository-1/manifests/latest"))
                .willReturn(ResponseDefinitionBuilder.okForJson(DOCKER_TAG_MANIFEST)));

        // when
        Mono<DockerTagManifest> result = dockerRegistryClient.getTagManifest(REGISTRY_ID_2, REPOSITORY_ID, TAG);

        // then
        assertThat(result.block(), equalTo(DOCKER_TAG_MANIFEST));
        verifyAuthorization(wireMockServerRegistry2);
    }

    @Test
    public void shouldGetTagManifestThrowExceptionWhenResponseBodyIsMalformed() {

        // given
        wireMockServerRegistry2.givenThat(get(urlEqualTo("/v2/repository-1/manifests/latest"))
                .willReturn(ResponseDefinitionBuilder.okForJson(Map.of("history", "value"))));

        // when
        assertThrows(RuntimeException.class,
                () -> dockerRegistryClient.getTagManifest(REGISTRY_ID_2, REPOSITORY_ID, TAG).block());

        // then
        // exception expected
    }

    @Test
    public void shouldGetTagDigest() {

        // given
        String tagManifestPath = "/v2/repository-1/manifests/latest";
        wireMockServerRegistry2.givenThat(get(urlEqualTo(tagManifestPath))
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Docker-Content-Digest", TAG_DIGEST)
                        .withStatus(200)));

        // when
        Mono<String> result = dockerRegistryClient.getTagDigest(REGISTRY_ID_2, REPOSITORY_ID, TAG);

        // then
        assertThat(result.block(), equalTo(TAG_DIGEST));
        wireMockServerRegistry2.verify(getRequestedFor(urlEqualTo(tagManifestPath))
                .withHeader("Accept", WireMock.equalTo("application/vnd.docker.distribution.manifest.v2+json")));
    }

    @Test
    public void shouldDeleteTagByDigest() throws InterruptedException {

        // given
        String tagDigestPath = "/v2/repository-1/manifests/tag-digest";
        wireMockServerRegistry1.givenThat(delete(urlEqualTo(tagDigestPath))
                .willReturn(ResponseDefinitionBuilder.responseDefinition().withStatus(204)));

        // when
        dockerRegistryClient.deleteTagByDigest(REGISTRY_ID_1, REPOSITORY_ID, TAG_DIGEST);

        // then
        Thread.sleep(300);
        wireMockServerRegistry1.verify(deleteRequestedFor(urlEqualTo(tagDigestPath)));
    }

    private static ServiceRegistrations prepareConfig() {

        ServiceRegistrations serviceRegistrations = new ServiceRegistrations();
        ServiceRegistrations.DockerIntegration dockerIntegration = new ServiceRegistrations.DockerIntegration();
        ServiceRegistrations.DockerRegistry dockerRegistry1 = new ServiceRegistrations.DockerRegistry();
        dockerRegistry1.setHost("http://localhost:9999");
        dockerRegistry1.setUsername("user-1");
        dockerRegistry1.setPassword("pass-1");
        ServiceRegistrations.DockerRegistry dockerRegistry2 = new ServiceRegistrations.DockerRegistry();
        dockerRegistry2.setHost("http://localhost:9998");
        dockerRegistry2.setUsername("user-1");
        dockerRegistry2.setPassword("pass-1");

        dockerIntegration.getRegistryCatalog().put(REGISTRY_ID_1, dockerRegistry1);
        dockerIntegration.getRegistryCatalog().put(REGISTRY_ID_2, dockerRegistry2);
        serviceRegistrations.setDockerIntegration(dockerIntegration);

        return serviceRegistrations;
    }

    private void verifyAuthorization(WireMockServer wireMockServer) {

        wireMockServer.verify(anyRequestedFor(anyUrl())
                .withHeader("Authorization", WireMock.equalTo("Basic dXNlci0xOnBhc3MtMQ==")));
    }
}

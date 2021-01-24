package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.domain.DockerRegistryContent;
import hu.psprog.leaflet.lsas.core.domain.DockerRepository;
import hu.psprog.leaflet.lsas.core.service.DockerRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Mono/Flux REST controller to retrieve Docker Registry information.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping("/lsas/registry")
public class DockerRegistryController {

    private final DockerRegistryService dockerRegistryService;

    @Autowired
    public DockerRegistryController(DockerRegistryService dockerRegistryService) {
        this.dockerRegistryService = dockerRegistryService;
    }

    /**
     * Returns the map of configured Docker Registry servers as a map of ID-URL pairs.
     *
     * @return the map of configured Docker Registry servers as a map of ID-URL pairs
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> listRegistries() {

        return ResponseEntity.ok(dockerRegistryService.listRegistries());
    }

    /**
     * Returns the repositories located in the given registry.
     *
     * @param registryID registry ID to be listed
     * @return available repositories as {@link DockerRegistryContent} object wrapped in {@link Mono}
     */
    @GetMapping("/{registryID}")
    public Mono<DockerRegistryContent> getRepositories(@PathVariable String registryID) {

        return dockerRegistryService.getRepositories(registryID);
    }

    /**
     * Returns the details of a given repository.
     *
     * @param registryID registry in which the repository to be queries is located
     * @param repository ID of the repository to query the details of
     * @return repository details (including available tags) as {@link DockerRepository} object wrapped in {@link Mono}
     */
    @GetMapping("/{registryID}/{repository}")
    public Mono<DockerRepository> getRepositoryTags(@PathVariable String registryID, @PathVariable String repository) {

        return dockerRegistryService.getRepositoryDetails(registryID, repository);
    }

    /**
     *
     * Returns the details of a given repository.
     *
     * @param registryID registry in which the repository to be queried is located
     * @param group group (organization) in which the repository to be queried is located
     * @param repository ID of the repository to query the details of
     * @return repository details (including available tags) as {@link DockerRepository} object wrapped in {@link Mono}
     */
    @GetMapping("/{registryID}/{group}/{repository}")
    public Mono<DockerRepository> getRepositoryTags(@PathVariable String registryID, @PathVariable String group, @PathVariable String repository) {

        return dockerRegistryService.getRepositoryDetails(registryID, String.format("%s/%s", group, repository));
    }

    /**
     * Deletes the given version (tag) of the specified repository.
     *
     * @param registryID registry in which the image is located
     * @param repository ID of the repository in which the tag is located
     * @param tag tag of the image to be deleted
     * @return empty {@link ResponseEntity} with HTTP 204 No content response
     */
    @DeleteMapping("/{registryID}/{repository}/{tag}")
    public ResponseEntity<Void> deleteImageByTag(@PathVariable String registryID, @PathVariable String repository, @PathVariable String tag) {

        dockerRegistryService.deleteImageByTag(registryID, repository, tag);

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Deletes the given version (tag) of the specified repository.
     *
     * @param registryID registry in which the image is located
     * @param group group (organization) in which the repository to be queried is located
     * @param repository ID of the repository in which the tag is located
     * @param tag tag of the image to be deleted
     * @return empty {@link ResponseEntity} with HTTP 204 No content response
     */
    @DeleteMapping("/{registryID}/{group}/{repository}/{tag}")
    public ResponseEntity<Void> deleteImageByTag(@PathVariable String registryID, @PathVariable String group, @PathVariable String repository, @PathVariable String tag) {

        dockerRegistryService.deleteImageByTag(registryID, String.format("%s/%s", group, repository), tag);

        return ResponseEntity
                .noContent()
                .build();
    }
}

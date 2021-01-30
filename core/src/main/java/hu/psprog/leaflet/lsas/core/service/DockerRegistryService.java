package hu.psprog.leaflet.lsas.core.service;

import hu.psprog.leaflet.lsas.core.domain.DockerRegistryContent;
import hu.psprog.leaflet.lsas.core.domain.DockerRepository;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service interface to handle Docker Registry operations.
 *
 * @author Peter Smith
 */
public interface DockerRegistryService {

    /**
     * Returns the map of configured Docker Registry servers as a map of ID-URL pairs.
     *
     * @return the map of configured Docker Registry servers as a map of ID-URL pairs
     */
    Map<String, String> listRegistries();

    /**
     * Returns the repositories located in the given registry.
     *
     * @param registryID registry ID to be listed
     * @return available repositories as {@link DockerRegistryContent} object wrapped in {@link Mono}
     */
    Mono<DockerRegistryContent> getRepositories(String registryID);

    /**
     * Returns the details of a given repository.
     *
     * @param registryID registry in which the repository to be queried is located
     * @param repositoryID ID of the repository to query the details of
     * @return repository details (including available tags) as {@link DockerRepository} object wrapped in {@link Mono}
     */
    Mono<DockerRepository> getRepositoryDetails(String registryID, String repositoryID);

    /**
     * Deletes the given version (tag) of the specified repository.
     *
     * @param registryID registry in which the image is located
     * @param repositoryID ID of the repository in which the tag is located
     * @param tag tag of the image to be deleted
     */
    void deleteImageByTag(String registryID, String repositoryID, String tag);
}

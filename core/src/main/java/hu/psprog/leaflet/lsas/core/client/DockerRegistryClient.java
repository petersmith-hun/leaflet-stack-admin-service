package hu.psprog.leaflet.lsas.core.client;

import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import reactor.core.publisher.Mono;

/**
 * Client interface for Docker Registry API calls.
 * Implementation should be returning Project Reactor reactive streams.
 *
 * @author Peter Smith
 */
public interface DockerRegistryClient {

    /**
     * Lists available repositories in the given registry.
     *
     * @param registryID ID of the registry to be listed
     * @return available repositories as {@link DockerRepositories} object wrapped in {@link Mono}
     */
    Mono<DockerRepositories> getRepositories(String registryID);

    /**
     * Lists available tags of the given repository in the given registry.
     *
     * @param registryID ID of the registry the repository is located in
     * @param repositoryID ID of the repository to be listed
     * @return available repository tags as {@link DockerTags} object wrapped in {@link Mono}
     */
    Mono<DockerTags> getRepositoryTags(String registryID, String repositoryID);

    /**
     * Retrieves manifest details of the given image.
     *
     * @param registryID ID of the registry the repository is located in
     * @param repositoryID ID of the repository to retrieve the manifest of
     * @param tag tag of the image in the repository to retrieve the manifest of
     * @return image manifest as {@link DockerTagManifest} object wrapped in {@link Mono}
     */
    Mono<DockerTagManifest> getTagManifest(String registryID, String repositoryID, String tag);

    /**
     * Retrieves manifest digest of the given tag of an image.
     *
     * @param registryID ID of the registry the repository is located in
     * @param repositoryID ID of the repository to retrieve the manifest of
     * @param tag tag of the image in the repository to retrieve the digest of
     * @return image digest as {@link String} wrapped in {@link Mono}
     */
    Mono<String> getTagDigest(String registryID, String repositoryID, String tag);

    /**
     * Deletes the given version (tag) of the specified repository by its digest.
     *
     * @param registryID registry in which the image is located
     * @param repositoryID ID of the repository in which the tag is located
     * @param tagDigest tag digest of the image to be deleted
     */
    void deleteTagByDigest(String registryID, String repositoryID, String tagDigest);
}

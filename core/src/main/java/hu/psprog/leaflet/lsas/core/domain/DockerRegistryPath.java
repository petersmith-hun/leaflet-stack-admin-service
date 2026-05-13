package hu.psprog.leaflet.lsas.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum defining available Docker Registry API paths.
 *
 * @author Peter Smith
 */
@Getter
@RequiredArgsConstructor
public enum DockerRegistryPath {

    REPOSITORIES("/v2/_catalog"),
    TAGS("/v2/%s/tags/list"),
    TAG_MANIFEST("/v2/%s/manifests/%s"),
    BLOB_MANIFEST("/v2/%s/blobs/%s");

    private final String uri;

}

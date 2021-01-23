package hu.psprog.leaflet.lsas.core.domain;

/**
 * Enum defining available Docker Registry API paths.
 *
 * @author Peter Smith
 */
public enum DockerRegistryPath {

    REPOSITORIES("/v2/_catalog"),
    TAGS("/v2/%s/tags/list"),
    TAG_MANIFEST("/v2/%s/manifests/%s");

    private final String uri;

    DockerRegistryPath(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}

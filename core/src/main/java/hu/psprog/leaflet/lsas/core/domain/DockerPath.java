package hu.psprog.leaflet.lsas.core.domain;

/**
 * Known and user Docker Engine API paths.
 *
 * @author Peter Smith
 */
public enum DockerPath {

    CONTAINERS("/containers/json?all=true"),
    CONTAINER_DETAILS("/containers/{id}/json"),
    CONTAINER_STATS("/containers/{id}/stats");

    private final String uri;

    DockerPath(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return uri;
    }
}

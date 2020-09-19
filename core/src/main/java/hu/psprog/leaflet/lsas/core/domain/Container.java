package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Domain class representing a Docker container's base information set.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Container {

    private final String id;
    private final String image;
    private final List<String> names;
    private final String state;
    private final long createTimestamp;
}

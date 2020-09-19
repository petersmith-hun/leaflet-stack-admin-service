package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Domain class containing information about a service's build data.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
public class BuildInfo {

    private ZonedDateTime time;
    private String version;

    @JsonCreator
    public BuildInfo(ZonedDateTime time, String version) {
        this.time = time;
        this.version = version;
    }
}

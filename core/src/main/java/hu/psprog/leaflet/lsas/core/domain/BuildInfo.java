package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.ZonedDateTime;

/**
 * Domain class containing information about a service's build data.
 *
 * @author Peter Smith
 */
public class BuildInfo {

    private ZonedDateTime time;
    private String version;

    @JsonCreator
    public BuildInfo(ZonedDateTime time, String version) {
        this.time = time;
        this.version = version;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BuildInfo buildInfo = (BuildInfo) o;

        return new EqualsBuilder()
                .append(time, buildInfo.time)
                .append(version, buildInfo.version)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(time)
                .append(version)
                .toHashCode();
    }
}

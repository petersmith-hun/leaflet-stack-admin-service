package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain class containing identification information about a single service.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
public class ServiceInfo {

    private String name;
    private String abbreviation;

    @JsonCreator
    public ServiceInfo(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }
}

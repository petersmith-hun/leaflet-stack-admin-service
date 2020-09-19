package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain class wrapping a service status.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
public class ServiceStatus {

    @JsonProperty("app")
    private ServiceInfo serviceInfo;

    @JsonProperty("build")
    private BuildInfo buildInfo;

    private boolean up = true;

    @JsonCreator
    public ServiceStatus(ServiceInfo serviceInfo, BuildInfo buildInfo) {
        this.serviceInfo = serviceInfo;
        this.buildInfo = buildInfo;
    }

    public static ServiceStatus buildDownService(String abbreviation) {

        ServiceInfo serviceInfo = new ServiceInfo(null, abbreviation);
        ServiceStatus serviceStatus = new ServiceStatus(serviceInfo, null);
        serviceStatus.up = false;

        return serviceStatus;
    }

}

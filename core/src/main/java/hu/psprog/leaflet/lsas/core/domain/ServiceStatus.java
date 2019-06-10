package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Domain class wrapping a service status.
 *
 * @author Peter Smith
 */
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

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public boolean isUp() {
        return up;
    }

    public static ServiceStatus buildDownService(String abbreviation) {

        ServiceInfo serviceInfo = new ServiceInfo(null, abbreviation);
        ServiceStatus serviceStatus = new ServiceStatus(serviceInfo, null);
        serviceStatus.up = false;

        return serviceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServiceStatus that = (ServiceStatus) o;

        return new EqualsBuilder()
                .append(up, that.up)
                .append(serviceInfo, that.serviceInfo)
                .append(buildInfo, that.buildInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(serviceInfo)
                .append(buildInfo)
                .append(up)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("serviceInfo", serviceInfo)
                .append("buildInfo", buildInfo)
                .append("up", up)
                .toString();
    }
}

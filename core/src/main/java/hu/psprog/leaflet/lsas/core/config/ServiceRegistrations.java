package hu.psprog.leaflet.lsas.core.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration model for service registrations.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "lsas")
public class ServiceRegistrations {

    private Map<String, String> registrations = new LinkedHashMap<>();

    public Map<String, String> getRegistrations() {
        return registrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServiceRegistrations that = (ServiceRegistrations) o;

        return new EqualsBuilder()
                .append(registrations, that.registrations)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(registrations)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("registrations", registrations)
                .toString();
    }
}

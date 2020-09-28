package hu.psprog.leaflet.lsas.web.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration parameters model for CORS and API Key configuration.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "lsas.security")
public class SecurityConfigParameters {

    private CORSConfigParameters cors;
    private String apiKey;

    public CORSConfigParameters getCors() {
        return cors;
    }

    public void setCors(CORSConfigParameters cors) {
        this.cors = cors;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SecurityConfigParameters that = (SecurityConfigParameters) o;

        return new EqualsBuilder()
                .append(cors, that.cors)
                .append(apiKey, that.apiKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(cors)
                .append(apiKey)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cors", cors)
                .append("apiKey", apiKey)
                .toString();
    }

    /**
     * Configuration parameter model for CORS specific settings.
     */
    public static class CORSConfigParameters {

        private boolean enabled;
        private final Map<String, String[]> allowedPaths = new HashMap<>();
        private String[] allowedOrigins;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Map<String, String[]>  getAllowedPaths() {
            return allowedPaths;
        }

        public String[] getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String[] allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            CORSConfigParameters that = (CORSConfigParameters) o;

            return new EqualsBuilder()
                    .append(enabled, that.enabled)
                    .append(allowedPaths, that.allowedPaths)
                    .append(allowedOrigins, that.allowedOrigins)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(enabled)
                    .append(allowedPaths)
                    .append(allowedOrigins)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("enabled", enabled)
                    .append("allowedPaths", allowedPaths)
                    .append("allowedOrigins", allowedOrigins)
                    .toString();
        }
    }
}

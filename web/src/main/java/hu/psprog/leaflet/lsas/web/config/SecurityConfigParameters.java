package hu.psprog.leaflet.lsas.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security configuration parameters model.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "lsas.security")
public class SecurityConfigParameters {

    private ServiceUserParameters serviceUser;

    public ServiceUserParameters getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(ServiceUserParameters serviceUser) {
        this.serviceUser = serviceUser;
    }

    public static class ServiceUserParameters {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

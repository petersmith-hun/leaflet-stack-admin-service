package hu.psprog.leaflet.lsas.core.status.impl;

import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.status.ServiceStatusAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

/**
 * {@link ServiceStatusAdapter} implementation for services providing Spring Boot Actuator based status endpoints.
 *
 * @author Peter Smith
 */
public class ActuatorBasedServiceStatusAdapter implements ServiceStatusAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorBasedServiceStatusAdapter.class);

    private Client client;
    private String serviceAbbreviation;
    private String statusURL;
    private final ServiceStatus defaultServiceStatus;

    public ActuatorBasedServiceStatusAdapter(Client client, String serviceAbbreviation, String statusURL) {
        this.client = client;
        this.serviceAbbreviation = serviceAbbreviation;
        this.statusURL = statusURL;
        this.defaultServiceStatus = ServiceStatus.buildDownService(serviceAbbreviation);
    }

    @Override
    public String getRegisteredAbbreviation() {
        return serviceAbbreviation;
    }

    @Override
    public ServiceStatus getStatus() {

        LOGGER.info("Calling service {} to request status", serviceAbbreviation);

        ServiceStatus serviceStatus = defaultServiceStatus;
        try {
            Response response = callService();
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                serviceStatus = response.readEntity(ServiceStatus.class);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to call service {} - reason: {}", serviceAbbreviation, e.getMessage());
        }

        return serviceStatus;
    }

    private Response callService() {
        return client.target(statusURL)
                .request()
                .get();
    }
}

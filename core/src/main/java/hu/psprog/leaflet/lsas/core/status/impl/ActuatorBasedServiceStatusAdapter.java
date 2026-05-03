package hu.psprog.leaflet.lsas.core.status.impl;

import hu.psprog.leaflet.bridge.client.handler.ResponseReader;
import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.status.ServiceStatusAdapter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;

import java.lang.reflect.Type;

/**
 * {@link ServiceStatusAdapter} implementation for services providing Spring Boot Actuator based status endpoints.
 *
 * @author Peter Smith
 */
public class ActuatorBasedServiceStatusAdapter implements ServiceStatusAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorBasedServiceStatusAdapter.class);

    private static final TypeReference<ServiceStatus> SERVICE_STATUS_TYPE_REFERENCE = new TypeReference<>() {
        @Override
        public Type getType() {
            return ServiceStatus.class;
        }
    };

    private final HttpClient httpClient;
    private final ResponseReader responseReader;
    private final String serviceAbbreviation;
    private final String statusURL;
    private final ServiceStatus defaultServiceStatus;

    public ActuatorBasedServiceStatusAdapter(HttpClient httpClient, ResponseReader responseReader,
                                             String serviceAbbreviation, String statusURL) {
        this.httpClient = httpClient;
        this.responseReader = responseReader;
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
            ClassicHttpRequest request = new HttpGet(statusURL);
            serviceStatus = httpClient.execute(request, response -> responseReader.read(response, SERVICE_STATUS_TYPE_REFERENCE));

        } catch (Exception exception) {
            LOGGER.error("Failed to call service {} - reason: {}", serviceAbbreviation, exception.getMessage(), exception);
        }

        return serviceStatus;
    }
}

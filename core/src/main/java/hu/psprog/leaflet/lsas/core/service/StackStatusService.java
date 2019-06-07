package hu.psprog.leaflet.lsas.core.service;

import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Service interface to gather service status information.
 *
 * @author Peter Smith
 */
public interface StackStatusService {

    /**
     * Returns the abbreviation-list of the registered services.
     *
     * @return registered abbreviations as {@link List} of {@link String} objects
     */
    List<String> getRegisteredServices();

    /**
     * Returns service status information as WebFlux-based reactive stream.
     *
     * @return reactive stream of service statuses as {@link Flux}-wrapped {@link ServiceStatus} objects
     */
    Flux<ServiceStatus> getServiceStackStatus();
}

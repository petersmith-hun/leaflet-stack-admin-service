package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.service.StackStatusService;
import hu.psprog.leaflet.lsas.web.model.RegisteredServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Flux REST controller to retrieve service information.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping("/lsas/stack-status")
public class ServiceInfoController {

    private final StackStatusService stackStatusService;

    @Autowired
    public ServiceInfoController(StackStatusService stackStatusService) {
        this.stackStatusService = stackStatusService;
    }

    /**
     * GET /lsas/stack-status/registered-services
     * Returns list of abbreviations of registered services wrapped as {@link RegisteredServices}.
     *
     * @return abbreviation list of registered services in {@link ResponseEntity}
     */
    @GetMapping("/registered-services")
    public ResponseEntity<RegisteredServices> listRegisteredServices() {
        return ResponseEntity.ok(getRegisteredServices());
    }

    /**
     * GET /lsas/stack-status/discover
     * Starts discovering the status of the registered services and returns the results in a reactive manner.
     *
     * @return status results as {@link Flux}
     */
    @GetMapping(value = "/discover", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServiceStatus> getServiceStatus() {
        return stackStatusService.getServiceStackStatus();
    }

    private RegisteredServices getRegisteredServices() {
        return new RegisteredServices(stackStatusService.getRegisteredServices());
    }
}


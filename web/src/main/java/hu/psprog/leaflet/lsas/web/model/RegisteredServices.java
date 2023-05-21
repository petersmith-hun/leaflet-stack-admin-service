package hu.psprog.leaflet.lsas.web.model;

import java.util.List;

/**
 * Domain class containing the list of registered services.
 *
 * @author Peter Smith
 */
public record RegisteredServices(
        List<String> registeredServices
) { }

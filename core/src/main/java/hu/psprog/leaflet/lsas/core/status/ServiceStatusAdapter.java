package hu.psprog.leaflet.lsas.core.status;


import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;

/**
 * Adapter interface to acquire status information from services.
 *
 * @author Peter Smith
 */
public interface ServiceStatusAdapter {

    /**
     * Returns the registered abbreviation for this current service.
     *
     * @return service abbreviation as {@link String}
     */
    String getRegisteredAbbreviation();

    /**
     * Retrieves service status info.
     *
     * @return service status info as {@link ServiceStatus}
     */
    ServiceStatus getStatus();
}

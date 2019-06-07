package hu.psprog.leaflet.lsas.core.service.factory;

import hu.psprog.leaflet.lsas.core.status.ServiceStatusAdapter;

import java.util.List;
import java.util.Map;

/**
 * Generic factory interface for creating {@link ServiceStatusAdapter} instances.
 *
 * @param <T> type of the actual {@link ServiceStatusAdapter} implementation to be created by the implementing factory
 * @author Peter Smith
 */
public interface ServiceStatusAdapterFactory<T extends ServiceStatusAdapter> {

    /**
     * Creates a single instance of {@link ServiceStatusAdapter}.
     *
     * @param abbreviation abbreviation of the service as {@link String}
     * @param url URL of the service as {@link String}
     * @return created {@link ServiceStatusAdapter} instance
     */
    T create(String abbreviation, String url);

    /**
     * Creates multiple instances of {@link ServiceStatusAdapter} objects based on the given descriptor map.
     *
     * @param adapterDescriptorMap descriptor map as abbreviation-url pairs
     * @return created {@link ServiceStatusAdapter} instances as {@link List}
     */
    List<T> createBulk(Map<String, String> adapterDescriptorMap);
}

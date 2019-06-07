package hu.psprog.leaflet.lsas.core.service.factory.impl;

import hu.psprog.leaflet.lsas.core.service.factory.ServiceStatusAdapterFactory;
import hu.psprog.leaflet.lsas.core.status.impl.ActuatorBasedServiceStatusAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link ServiceStatusAdapterFactory} implementation for {@link ActuatorBasedServiceStatusAdapter} adapters.
 *
 * @author Peter Smith
 */
@Component
public class ActuatorBasedServiceStatusAdapterFactoryImpl implements ServiceStatusAdapterFactory<ActuatorBasedServiceStatusAdapter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorBasedServiceStatusAdapterFactoryImpl.class);

    private Client client;

    @Autowired
    public ActuatorBasedServiceStatusAdapterFactoryImpl(Client client) {
        this.client = client;
    }

    @Override
    public ActuatorBasedServiceStatusAdapter create(String abbreviation, String url) {
        LOGGER.info("Initializing adapter for service {}", abbreviation);
        return new ActuatorBasedServiceStatusAdapter(client, abbreviation, url);
    }

    @Override
    public List<ActuatorBasedServiceStatusAdapter> createBulk(Map<String, String> adapterDescriptorMap) {
        return adapterDescriptorMap.entrySet().stream()
                .map(descriptor -> create(descriptor.getKey(), descriptor.getValue()))
                .collect(Collectors.toList());
    }
}

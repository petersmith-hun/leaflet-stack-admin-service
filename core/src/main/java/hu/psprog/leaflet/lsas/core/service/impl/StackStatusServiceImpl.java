package hu.psprog.leaflet.lsas.core.service.impl;

import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.service.StackStatusService;
import hu.psprog.leaflet.lsas.core.service.factory.ServiceStatusAdapterFactory;
import hu.psprog.leaflet.lsas.core.status.ServiceStatusAdapter;
import hu.psprog.leaflet.lsas.core.status.impl.ActuatorBasedServiceStatusAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link StackStatusService}.
 *
 * @author Peter Smith
 */
@Service
public class StackStatusServiceImpl implements StackStatusService {

    private final List<? extends ServiceStatusAdapter> serviceStatusAdapterList;

    @Autowired
    public StackStatusServiceImpl(ServiceRegistrations serviceRegistrations, ServiceStatusAdapterFactory<ActuatorBasedServiceStatusAdapter> serviceStatusAdapterFactory) {
        this.serviceStatusAdapterList = serviceStatusAdapterFactory.createBulk(serviceRegistrations.getRegistrations());
    }

    @Override
    public List<String> getRegisteredServices() {
        return serviceStatusAdapterList.stream()
                .map(ServiceStatusAdapter::getRegisteredAbbreviation)
                .collect(Collectors.toList());
    }

    @Override
    public Flux<ServiceStatus> getServiceStackStatus() {
        return Flux.fromStream(serviceStatusAdapterList.stream())
                .map(ServiceStatusAdapter::getStatus);
    }
}

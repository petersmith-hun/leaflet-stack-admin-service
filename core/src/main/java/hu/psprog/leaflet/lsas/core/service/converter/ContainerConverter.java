package hu.psprog.leaflet.lsas.core.service.converter;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.domain.Container;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ContainerModel} to {@link Container}.
 *
 * @author Peter Smith
 */
@Component
public class ContainerConverter implements Converter<ContainerModel, Container> {

    @Override
    public Container convert(ContainerModel containerModel) {

        return Container.builder()
                .id(containerModel.getId())
                .image(containerModel.getImage())
                .state(containerModel.getState())
                .names(containerModel.getNames())
                .createTimestamp(containerModel.getCreatedTimestamp())
                .build();
    }
}

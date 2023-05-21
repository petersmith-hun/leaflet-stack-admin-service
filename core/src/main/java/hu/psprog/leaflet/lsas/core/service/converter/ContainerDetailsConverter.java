package hu.psprog.leaflet.lsas.core.service.converter;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.DockerContainerStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link ContainerDetailsModel} to {@link ContainerDetails}.
 *
 * @author Peter Smith
 */
@Component
public class ContainerDetailsConverter implements Converter<ContainerDetailsModel, ContainerDetails> {

    @Override
    public ContainerDetails convert(ContainerDetailsModel containerDetailsModel) {

        return ContainerDetails.builder()
                .id(containerDetailsModel.id())
                .status(DockerContainerStatus.parseStatus(containerDetailsModel.status()))
                .logPath(containerDetailsModel.logPath())
                .startedAt(containerDetailsModel.startedAt())
                .build();
    }
}

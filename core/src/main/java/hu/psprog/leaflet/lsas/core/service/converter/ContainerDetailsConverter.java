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
                .id(containerDetailsModel.getId())
                .status(DockerContainerStatus.parseStatus(containerDetailsModel.getStatus()))
                .logPath(containerDetailsModel.getLogPath())
                .startedAt(containerDetailsModel.getStartedAt())
                .build();
    }
}

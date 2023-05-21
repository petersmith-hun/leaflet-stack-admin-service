package hu.psprog.leaflet.lsas.core.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Enum representation for the possible Docker container statuses.
 *
 * @author Peter Smith
 */
public enum DockerContainerStatus {

    CREATED,
    RESTARTING,
    RUNNING,
    PAUSED,
    EXITED,
    DEAD,
    UNKNOWN;

    private static final List<String> STATUS_NAMES = Arrays.stream(values())
            .map(Enum::name)
            .toList();

    /**
     * Maps a given string status representation to the corresponding enum constant.
     *
     * @param status status representation as string
     * @return corresponding status enum constant or UNKNOWN if invalid
     */
    public static DockerContainerStatus parseStatus(String status) {

        String statusValue = status.toUpperCase();

        return STATUS_NAMES.contains(statusValue)
                ? DockerContainerStatus.valueOf(statusValue)
                : UNKNOWN;
    }
}

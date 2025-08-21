package model;

import java.util.UUID;

public record MountainTask(
        UUID requestUuid,
        String name,
        EventType eventType
) {
}

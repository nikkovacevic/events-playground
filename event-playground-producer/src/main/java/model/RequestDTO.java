package model;

import java.time.LocalDate;

public record RequestDTO(
        String mountain,
        LocalDate date,
        String note,
        RequestAction requestAction
) {}

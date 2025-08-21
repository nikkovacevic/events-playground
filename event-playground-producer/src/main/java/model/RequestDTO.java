package model;

import java.time.LocalDate;
import java.util.List;

public record RequestDTO(
        List<String> mountains,
        LocalDate date,
        String note,
        RequestAction requestAction
) {}

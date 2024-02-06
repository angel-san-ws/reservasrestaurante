package com.ats.reservasrestaurante.domain.dto;

import java.time.DayOfWeek;

public record WorkingDaysDto(
        String initialDay, String finalDay
) {
}

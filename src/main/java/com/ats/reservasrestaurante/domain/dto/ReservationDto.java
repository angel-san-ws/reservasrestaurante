package com.ats.reservasrestaurante.domain.dto;

public record ReservationDto(Integer reservationNumber, Integer numberPersons, String userName,
                             String startDateTime, String endDateTime, Integer tableNumber, String status, String resCreationDateTime) {
}

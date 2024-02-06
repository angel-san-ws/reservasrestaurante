package com.ats.reservasrestaurante.application.controller;

import com.ats.reservasrestaurante.application.service.ReservationGenericService;
import com.ats.reservasrestaurante.domain.dto.CreateReservationDto;
import com.ats.reservasrestaurante.domain.dto.EditReservationDto;
import com.ats.reservasrestaurante.domain.dto.ReturnCreateReservationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {
    private final ReservationGenericService reservationGenericService;
    public ReservationController(ReservationGenericService reservationGenericService){
        this.reservationGenericService=reservationGenericService;
    }
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationDto createReservationDto){
        int ret = reservationGenericService.createReservation(createReservationDto.numberPersons(),
                createReservationDto.reservationDate(),createReservationDto.reservationStartTime());
        if(ret==0) return new ResponseEntity<>("Error creating Reservation", HttpStatus.BAD_REQUEST);
        else{
            return new ResponseEntity<>(new ReturnCreateReservationDto(String.valueOf(ret)), HttpStatus.CREATED);
        }
    }

    @PutMapping("/{reservationNumber}")
    public ResponseEntity<?> editReservationDates(@PathVariable String reservationNumber, @RequestBody EditReservationDto editReservationDto){
        reservationGenericService.editReservation(reservationNumber, editReservationDto.reservationDate(),editReservationDto.reservationStartTime());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<?> removeReservation(@PathVariable String reservationNumber){
        reservationGenericService.removeReservation(reservationNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getAllReservations() throws SecurityException{
        return new ResponseEntity<>(reservationGenericService.findAllReservations(), HttpStatus.FOUND);
    }


}

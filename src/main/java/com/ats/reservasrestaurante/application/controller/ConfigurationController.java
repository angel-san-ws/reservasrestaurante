package com.ats.reservasrestaurante.application.controller;

import com.ats.reservasrestaurante.application.exception.MessageResponse;
import com.ats.reservasrestaurante.application.service.ConfigurationGenericService;
import com.ats.reservasrestaurante.application.service.TableGenericService;
import com.ats.reservasrestaurante.domain.dto.CreateTableDto;
import com.ats.reservasrestaurante.domain.dto.TableDto;
import com.ats.reservasrestaurante.domain.dto.WorkingDaysDto;
import com.ats.reservasrestaurante.domain.dto.WorkingHoursDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigurationController {
    ConfigurationGenericService configurationGenericService;
    TableGenericService tableGenericService;

    public ConfigurationController(ConfigurationGenericService configurationGenericService,TableGenericService tableGenericService){
        this.configurationGenericService=configurationGenericService;
        this.tableGenericService=tableGenericService;
    }

    @GetMapping
    public ResponseEntity<?> getALlConfigurations(){
        return new ResponseEntity<>(configurationGenericService.findAll(), HttpStatus.FOUND);
    }

    @GetMapping("/{key}")
    public ResponseEntity<?> getConfigurationByKey(@PathVariable String key){
        try {
            return new ResponseEntity<>(configurationGenericService.findByKey(key), HttpStatus.FOUND);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/workingDays")
    public ResponseEntity<?> setRestaurantWorkingDays(@RequestBody WorkingDaysDto workingDaysDto){
        DayOfWeek initDay= DayOfWeek.valueOf(workingDaysDto.initialDay());
        DayOfWeek endDay= DayOfWeek.valueOf(workingDaysDto.finalDay());
        configurationGenericService.setRestaurantWorkingDays(initDay,endDay);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/workingHours")
    public ResponseEntity<?> setRestaurantWorkingHours(@RequestBody WorkingHoursDto workingHoursDto){
        configurationGenericService.setRestaurantWorkingHours(workingHoursDto.initialHour(),workingHoursDto.finalHour());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/table")
    public ResponseEntity<?> createTable(@RequestBody CreateTableDto createTableDto){
        System.out.println("MaxClients:"+createTableDto.maxClients());
        int tableNumber=tableGenericService.createRestaurantTable(Integer.parseInt(createTableDto.maxClients()));
        if(tableNumber>0) {
            TableDto tableDto=new TableDto(tableNumber,Integer.parseInt(createTableDto.maxClients()),true);
            return new ResponseEntity<>(tableDto, HttpStatus.CREATED);
        }
        else return new ResponseEntity<>("Error creating table", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/table")
    public ResponseEntity<?> getAllTables(){
        return new ResponseEntity<>(tableGenericService.findAllTables(), HttpStatus.FOUND);
    }

    @DeleteMapping("/table/{tableNumber}")
    public ResponseEntity<?> removeTable(@PathVariable String tableNumber){
        tableGenericService.removeRestaurantTable(Integer.parseInt(tableNumber));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/table/{tableNumber}")
    public ResponseEntity<?> editTableMaxClients(@PathVariable String tableNumber,@RequestBody CreateTableDto createTableDto) throws SecurityException{
        System.out.println("Edit table:"+tableNumber);
        tableGenericService.editRestaurantTable(Integer.parseInt(tableNumber),Integer.parseInt(createTableDto.maxClients()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

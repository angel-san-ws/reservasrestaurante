package com.ats.reservasrestaurante.application.service;

import com.ats.reservasrestaurante.domain.dto.TableDto;

import java.util.List;

public interface TableGenericService {
    public int createRestaurantTable(int maxClients); //returns tableNumber
    public void editRestaurantTable(int tableNumber, int maxClients);
    public void removeRestaurantTable(int tableNumber);

    List<TableDto> findAllTables();
}

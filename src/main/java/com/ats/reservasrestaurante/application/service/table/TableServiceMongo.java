package com.ats.reservasrestaurante.application.service.table;

import com.ats.reservasrestaurante.application.service.TableGenericService;
import com.ats.reservasrestaurante.domain.dto.TableDto;
import com.ats.reservasrestaurante.domain.entity.table.TableMongo;
import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import com.ats.reservasrestaurante.domain.repository.table.TableRepositoryMongo;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TableServiceMongo implements TableGenericService {
    private final TableRepositoryMongo tableRepositoryMongo;
    @Override
    public int createRestaurantTable(int maxClients) {
        Optional<Integer> maxTableNumber = tableRepositoryMongo.findTopByOrderByTableNumberDesc().map(TableMongo::getTableNumber);
        Integer maxTableNumberInt=null;
        if(maxTableNumber.isPresent()) {
            int newTablenumber=maxTableNumber.get().intValue()+1;
            maxTableNumberInt=Integer.valueOf(newTablenumber);
        }
        else maxTableNumberInt=1;
        TableMongo tableMongo = TableMongo.builder().maxClients(maxClients).tableNumber(maxTableNumberInt).enabled(true).build();
        tableRepositoryMongo.save(tableMongo);
        return maxTableNumberInt;
    }

    @Override
    public void editRestaurantTable(int tableNumber, int maxClients) {
        Optional<TableMongo> op=tableRepositoryMongo.findTableMongoByTableNumber(tableNumber);
        if(op.isPresent()){
            TableMongo t=op.get();
            t.setMaxClients(maxClients);
            tableRepositoryMongo.save(t);
        }
    }

    @Override
    public void removeRestaurantTable(int tableNumber) {
        Optional<TableMongo> op=tableRepositoryMongo.findTableMongoByTableNumber(tableNumber);
        if(op.isPresent()){
            TableMongo t=op.get();
            t.setEnabled(false);
            tableRepositoryMongo.save(t);
        }
    }

    @Override
    public List<TableDto> findAllTables() {
        List<TableMongo> listTablePostgres=tableRepositoryMongo.findAll();
        List<TableDto> listTablesDto=new ArrayList<>();
        for(TableMongo tableMongo:listTablePostgres){
            listTablesDto.add( new TableDto(tableMongo.getTableNumber(),tableMongo.getMaxClients(),tableMongo.getEnabled()) );
        }
        return listTablesDto;
    }

    public List<TableMongo> findAllTablesMongo(){
        return tableRepositoryMongo.findAll();
    }
}

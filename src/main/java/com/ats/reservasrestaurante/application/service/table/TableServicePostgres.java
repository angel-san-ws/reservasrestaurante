package com.ats.reservasrestaurante.application.service.table;

import com.ats.reservasrestaurante.application.service.TableGenericService;
import com.ats.reservasrestaurante.domain.dto.TableDto;
import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import com.ats.reservasrestaurante.domain.repository.table.TableRepositoryPostgres;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TableServicePostgres implements TableGenericService {
    private final TableRepositoryPostgres tableRepositoryPostgres;
    public TableServicePostgres(TableRepositoryPostgres tableRepositoryPostgres){
        this.tableRepositoryPostgres =tableRepositoryPostgres;
    }
    @Override
    public int createRestaurantTable(int maxClients) {
        Optional<Integer> maxTableNumber = tableRepositoryPostgres.findTopByOrderByTableNumberDesc().map(TablePostgres::getTableNumber);
        Integer maxTableNumberInt=null;
        if(maxTableNumber.isPresent()) {
            int newTablenumber=maxTableNumber.get().intValue()+1;
            maxTableNumberInt=Integer.valueOf(newTablenumber);
        }
        else maxTableNumberInt=1;
        TablePostgres tablePostgres = TablePostgres.builder().maxClients(maxClients).tableNumber(maxTableNumberInt).enabled(true).build();
        tableRepositoryPostgres.save(tablePostgres);
        return maxTableNumberInt;
    }

    @Override
    public void editRestaurantTable(int tableNumber, int maxClients) {
        Optional<TablePostgres> op=tableRepositoryPostgres.findTablePostgresByTableNumber(tableNumber);
        if(op.isPresent()){
            TablePostgres t=op.get();
            t.setMaxClients(maxClients);
            tableRepositoryPostgres.save(t);
        }
    }

    @Override
    public void removeRestaurantTable(int tableNumber) {
        Optional<TablePostgres> op=tableRepositoryPostgres.findTablePostgresByTableNumber(tableNumber);
        if(op.isPresent()){
            TablePostgres t=op.get();
            t.setEnabled(false);
            tableRepositoryPostgres.save(t);
        }
    }

    @Override
    public List<TableDto> findAllTables() {
        List<TablePostgres> listTablePostgres=tableRepositoryPostgres.findAll();
        List<TableDto> listTablesDto=new ArrayList<>();
        for(TablePostgres tablePostgres:listTablePostgres){
            listTablesDto.add( new TableDto(tablePostgres.getTableNumber(),tablePostgres.getMaxClients(),tablePostgres.getEnabled()) );
        }
        return listTablesDto;
    }

    public List<TablePostgres> findAllTablePostgres() {
        return tableRepositoryPostgres.findAll();
    }

}

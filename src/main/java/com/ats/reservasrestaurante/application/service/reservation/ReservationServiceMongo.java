package com.ats.reservasrestaurante.application.service.reservation;

import com.ats.reservasrestaurante.application.service.ReservationGenericService;
import com.ats.reservasrestaurante.application.service.table.TableServiceMongo;
import com.ats.reservasrestaurante.application.service.user.UserServiceMongo;
import com.ats.reservasrestaurante.domain.dto.ReservationDto;
import com.ats.reservasrestaurante.domain.entity.reservation.ReservationMongo;
import com.ats.reservasrestaurante.domain.entity.reservation.ReservationPostgres;
import com.ats.reservasrestaurante.domain.entity.table.TableMongo;
import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import com.ats.reservasrestaurante.domain.entity.user.UserMongo;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import com.ats.reservasrestaurante.domain.repository.reservation.ReservationRepositoryMongo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ReservationServiceMongo implements ReservationGenericService {
    private final UserDetailsService userDetailsService;
    private final ReservationRepositoryMongo reservationRepositoryMongo;
    private final UserServiceMongo userServiceMongo;
    private final TableServiceMongo tableServiceMongo;

    @Override
    public int createReservation(String numberPersons, String reservationDate, String reservationStartTime) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserMongo userMongo=null;
        try{
            //Get User Dto
            userMongo=userServiceMongo.findUserMongoByUsername(userDetails.getUsername());
            Optional<ReservationMongo> topReservation=reservationRepositoryMongo.findTopByOrderByReservationNumberDesc();
            //Get Next Reservation Number
            int newReservationNumber=0;
            if(!topReservation.isPresent()) newReservationNumber=1;
            else{
                newReservationNumber=topReservation.get().getReservationNumber()+1;
            }
            int newNumberPersons=Integer.parseInt(numberPersons);
            //Date and times
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime newReservationStartDateTime = LocalDateTime.parse(reservationDate+" "+reservationStartTime, formatter);
            LocalDateTime newReservationEndDateTime = newReservationStartDateTime.plusHours(1);
            LocalDateTime newCreationTime = LocalDateTime.now();
            //Find available Table
            TableMongo tableMongo=this.findAvailableTable(newNumberPersons, newReservationStartDateTime, newReservationEndDateTime);
            if(tableMongo!=null){
                ReservationMongo reservationMongo=ReservationMongo.builder()
                        .enabled(true).creationTime(newCreationTime).reservationNumber(newReservationNumber)
                        .reservationStartDateTime(newReservationStartDateTime).reservationEndDateTime(newReservationEndDateTime)
                        .numberPersons(newNumberPersons).userMongo(userMongo).tableMongo(tableMongo)
                        .build();
                reservationRepositoryMongo.save(reservationMongo);
                return newReservationNumber;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return 0;
    }

    public TableMongo findAvailableTable(int numberPersons, LocalDateTime startDateTime, LocalDateTime endDateTime){
        //Obtain list of all enabled tables
        List<TableMongo> listTables=tableServiceMongo.findAllTablesMongo();
        System.out.println("All tables:"+listTables.size());
        //Filter by enabled tables
        listTables=listTables.stream().filter(table->table.getEnabled()==true).collect(Collectors.toList());
        System.out.println("Enabled tables:"+listTables.size());
        //Filter by MaxPersons less or equals to number of persons in the reservation
        listTables=listTables.stream().filter(table->table.getMaxClients()>numberPersons).collect(Collectors.toList());
        System.out.println("Max clients:"+listTables.size());
        if(listTables.size()>0){
            //Obtain list of tables that have reservation in the date times
            List<ReservationMongo> reservationMongoList = reservationRepositoryMongo.findAllByEnabledIsTrueAndReservationStartDateTimeGreaterThanEqualAndReservationEndDateTimeLessThanEqual(startDateTime,endDateTime);
            //Go through all the table reservations and remove them from the Table List
            for(ReservationMongo reservationMongo:reservationMongoList){
                for(TableMongo tableMongo:listTables){
                    if(tableMongo.getId().compareTo(reservationMongo.getTableMongo().getId()) == 0){
                        listTables=listTables.stream().filter(table->table.getTableNumber().compareTo(reservationMongo.getTableMongo().getTableNumber())!=0).collect(Collectors.toList());
                    }
                }
            }
        }else{
            return null;
        }
        return listTables.get(0);
    }

    @Override
    public void editReservation(String reservationNumber, String reservationDate, String reservationStartTime) {
        Optional<ReservationMongo> or=reservationRepositoryMongo.findReservationMongoByReservationNumber(Integer.parseInt(reservationNumber));
        if(or.isPresent()&&or.get().getEnabled()==true){
            ReservationMongo res=or.get();
            //Date and times
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime newReservationStartDateTime = LocalDateTime.parse(reservationDate+" "+reservationStartTime, formatter);
            LocalDateTime newReservationEndDateTime = newReservationStartDateTime.plusHours(1);
            LocalDateTime newCreationTime = LocalDateTime.now();
            res.setReservationStartDateTime(newReservationStartDateTime);
            res.setReservationEndDateTime(newReservationEndDateTime);
            reservationRepositoryMongo.save(res);
        }
    }

    @Override
    public void removeReservation(String reservationNumber) {
        Optional<ReservationMongo> or=reservationRepositoryMongo.findReservationMongoByReservationNumber(Integer.parseInt(reservationNumber));
        if(or.isPresent()) {
            ReservationMongo res = or.get();
            res.setEnabled(false);
            reservationRepositoryMongo.save(res);
        }
    }

    @Override
    public List<ReservationDto> findAllReservations() {
        List<ReservationMongo> listReservationMongo = reservationRepositoryMongo.findAll();
        List<ReservationDto> listResDto=new ArrayList<>();
        DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for(ReservationMongo res:listReservationMongo){
            listResDto.add(new ReservationDto(res.getReservationNumber(), res.getNumberPersons(),res.getUserMongo().getUsername(),
                    res.getReservationStartDateTime().format(df), res.getReservationEndDateTime().format(df), res.getTableMongo().getTableNumber(),
                    (res.getEnabled()?"Enabled":"Disabled"),res.getCreationTime().format(df) ));
        }
        return listResDto;
    }
}

package com.ats.reservasrestaurante.application.service.reservation;

import com.ats.reservasrestaurante.application.service.ReservationGenericService;
import com.ats.reservasrestaurante.application.service.table.TableServicePostgres;
import com.ats.reservasrestaurante.application.service.user.UserServicePostgres;
import com.ats.reservasrestaurante.domain.dto.ReservationDto;
import com.ats.reservasrestaurante.domain.entity.reservation.ReservationPostgres;
import com.ats.reservasrestaurante.domain.entity.table.TablePostgres;
import com.ats.reservasrestaurante.domain.entity.user.UserPostgres;
import com.ats.reservasrestaurante.domain.repository.reservation.ReservationRepositoryPostgres;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServicePostgres implements ReservationGenericService {

    private final ReservationRepositoryPostgres reservationRepositoryPostgres;
    private final UserDetailsService userDetailsService;
    private final UserServicePostgres userServicePostgres;
    private final TableServicePostgres tableServicePostgres;


    public ReservationServicePostgres(ReservationRepositoryPostgres reservationRepositoryPostgres, UserDetailsService userDetailsService,
                                      UserServicePostgres userServicePostgres, TableServicePostgres tableServicePostgres){
        this.reservationRepositoryPostgres=reservationRepositoryPostgres;
        this.userDetailsService=userDetailsService;
        this.userServicePostgres=userServicePostgres;
        this.tableServicePostgres=tableServicePostgres;
    }

    @Override
    public int createReservation(String numberPersons, String reservationDate, String reservationStartTime) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserPostgres userPostgres=null;
        try{
            //Get User Dto
            userPostgres=userServicePostgres.findUserPostgressByUsername(userDetails.getUsername());
            Optional<ReservationPostgres> topReservation=reservationRepositoryPostgres.findTopByOrderByReservationNumberDesc();
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
            TablePostgres tablePostgres=this.findAvailableTable(newNumberPersons, newReservationStartDateTime, newReservationEndDateTime);
            if(tablePostgres!=null){
                ReservationPostgres reservationPostgres=ReservationPostgres.builder()
                        .enabled(true).creationTime(newCreationTime).reservationNumber(newReservationNumber)
                        .reservationStartDateTime(newReservationStartDateTime).reservationEndDateTime(newReservationEndDateTime)
                        .numberPersons(newNumberPersons).userPostgres(userPostgres).tablePostgres(tablePostgres)
                        .build();
                reservationRepositoryPostgres.save(reservationPostgres);
                return newReservationNumber;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return 0;
    }

    public TablePostgres findAvailableTable(int numberPersons, LocalDateTime startDateTime, LocalDateTime endDateTime){
        //Obtain list of all enabled tables
        List<TablePostgres> listTables=tableServicePostgres.findAllTablePostgres();
        System.out.println("All tables:"+listTables.size());
        //Filter by enabled tables
        listTables=listTables.stream().filter(table->table.getEnabled()==true).collect(Collectors.toList());
        System.out.println("Enabled tables:"+listTables.size());
        //Filter by MaxPersons less or equals to number of persons in the reservation
        listTables=listTables.stream().filter(table->table.getMaxClients()>numberPersons).collect(Collectors.toList());
        System.out.println("Max clients:"+listTables.size());
        if(listTables.size()>0){
            //Obtain list of tables that have reservation in the date times
            List<ReservationPostgres> reservationPostgresList = reservationRepositoryPostgres.findAllByEnabledIsTrueAndReservationStartDateTimeGreaterThanEqualAndReservationEndDateTimeLessThanEqual(startDateTime,endDateTime);
            //Go through all the table reservations and remove them from the Table List
            for(ReservationPostgres reservationPostgres:reservationPostgresList){
                for(TablePostgres tablePostgres:listTables){
                    if(tablePostgres.getId().compareTo(reservationPostgres.getTablePostgres().getId()) == 0){
                        listTables=listTables.stream().filter(table->table.getTableNumber().compareTo(reservationPostgres.getTablePostgres().getTableNumber())!=0).collect(Collectors.toList());
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
        Optional<ReservationPostgres> or=reservationRepositoryPostgres.findReservationPostgresByReservationNumber(Integer.parseInt(reservationNumber));
        if(or.isPresent()&&or.get().getEnabled()==true){
            ReservationPostgres res=or.get();
            //Date and times
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime newReservationStartDateTime = LocalDateTime.parse(reservationDate+" "+reservationStartTime, formatter);
            LocalDateTime newReservationEndDateTime = newReservationStartDateTime.plusHours(1);
            LocalDateTime newCreationTime = LocalDateTime.now();
            res.setReservationStartDateTime(newReservationStartDateTime);
            res.setReservationEndDateTime(newReservationEndDateTime);
            reservationRepositoryPostgres.save(res);
        }
    }

    @Override
    public void removeReservation(String reservationNumber) {
        Optional<ReservationPostgres> or=reservationRepositoryPostgres.findReservationPostgresByReservationNumber(Integer.parseInt(reservationNumber));
        if(or.isPresent()) {
            ReservationPostgres res = or.get();
            res.setEnabled(false);
            reservationRepositoryPostgres.save(res);
        }
    }

    @Override
    public List<ReservationDto> findAllReservations() {
        List<ReservationPostgres> listReservationPostgres = reservationRepositoryPostgres.findAll();
        List<ReservationDto> listResDto=new ArrayList<>();
        DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for(ReservationPostgres res:listReservationPostgres){
            listResDto.add(new ReservationDto(res.getReservationNumber(), res.getNumberPersons(),res.getUserPostgres().getUsername(),
                    res.getReservationStartDateTime().format(df), res.getReservationEndDateTime().format(df), res.getTablePostgres().getTableNumber(),
                    (res.getEnabled()?"Enabled":"Disabled"),res.getCreationTime().format(df) ));
        }
        return listResDto;
    }
}

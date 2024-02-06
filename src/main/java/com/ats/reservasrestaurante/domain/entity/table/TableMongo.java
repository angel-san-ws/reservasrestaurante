package com.ats.reservasrestaurante.domain.entity.table;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "table")
public class TableMongo {
    @Id
    private String id;
    private Integer tableNumber;
    private Integer maxClients;
    private Boolean enabled;
}

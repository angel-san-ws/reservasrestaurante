package com.ats.reservasrestaurante.domain.entity.configuration;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"configuration\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"config_key"})})
public class ConfigurationPostgres {
    @Id
    @SequenceGenerator(
            name = "config_id_sequence",
            sequenceName = "config_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "config_id_sequence"
    )
    private Integer id;
    private String configKey;
    private String configValue;
    private Boolean enabled;
}

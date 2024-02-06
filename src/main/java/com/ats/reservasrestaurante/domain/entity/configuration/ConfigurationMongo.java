package com.ats.reservasrestaurante.domain.entity.configuration;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "configuration")
public class ConfigurationMongo {
    @Id
    private String id;
    private String configKey;
    private String configValue;
    private Boolean enabled;
}

package com.ats.reservasrestaurante.application.service.configuration;

import com.ats.reservasrestaurante.application.lasting.ConfigurationConstants;
import com.ats.reservasrestaurante.application.service.ConfigurationGenericService;
import com.ats.reservasrestaurante.domain.dto.ConfigurationDto;
import com.ats.reservasrestaurante.domain.entity.configuration.ConfigurationMongo;
import com.ats.reservasrestaurante.domain.repository.configuration.ConfigurationRepositoryMongo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
@AllArgsConstructor
public class ConfigurationServiceMongo implements ConfigurationGenericService {
    private final ConfigurationRepositoryMongo configurationRepositoryMongo;
    @Override
    public void create(String key, String value) {
        ConfigurationMongo configurationMongo= ConfigurationMongo.builder()
                .configKey(key)
                .configValue(value)
                .enabled(true)
                .build();
        configurationRepositoryMongo.save(configurationMongo);
    }

    @Override
    public void update(String key, String value) {
        Optional<ConfigurationMongo> c = configurationRepositoryMongo.findByConfigKey(key);
        if(c.isPresent()){
            c.get().setConfigValue(value);
            configurationRepositoryMongo.save(c.get());
        }
    }

    @Override
    public ConfigurationDto findByKey(String key) throws Exception {
        Optional<ConfigurationMongo> c = configurationRepositoryMongo.findByConfigKey(key);
        if(c.isPresent())  return new ConfigurationDto(c.get().getConfigKey(),c.get().getConfigValue());
        else throw new Exception("Error: Key not found");
    }

    @Override
    public List<ConfigurationDto> findAll() {
        List<ConfigurationMongo> listConf=configurationRepositoryMongo.findAll();
        List<ConfigurationDto> listConfigDto = new ArrayList<>();
        for (ConfigurationMongo configurationMongo : listConf) {
            listConfigDto.add(new ConfigurationDto(configurationMongo.getConfigKey(), configurationMongo.getConfigValue()));
        }
        return listConfigDto;
    }

    @Override
    public void removeByKey(String key) {
        Optional<ConfigurationMongo> c = configurationRepositoryMongo.findByConfigKey(key);
        if(c.isPresent()){
            c.get().setEnabled(false);
            configurationRepositoryMongo.save(c.get());
        }
    }

    @Override
    public void setRestaurantWorkingDays(DayOfWeek initialWorkingDay, DayOfWeek finalWorkingDay) {
        Optional<ConfigurationMongo> oc = configurationRepositoryMongo.findByConfigKey(ConfigurationConstants.CONFIG_KEY_INITIAL_WORKING_DAY);
        ConfigurationMongo cp = null;
        if(oc.isPresent()) {
            oc.get().setConfigValue(initialWorkingDay.name());
            cp=oc.get();
        }
        else cp=ConfigurationMongo.builder().configKey(ConfigurationConstants.CONFIG_KEY_INITIAL_WORKING_DAY).configValue(initialWorkingDay.name())
                .enabled(true).build();
        configurationRepositoryMongo.save(cp);

        Optional<ConfigurationMongo> oc2 = configurationRepositoryMongo.findByConfigKey(ConfigurationConstants.CONFIG_KEY_END_WORKING_DAY);
        ConfigurationMongo cp2 = null;
        if(oc2.isPresent()) {
            oc2.get().setConfigValue(finalWorkingDay.name());
            cp2=oc2.get();
        }
        else cp2=ConfigurationMongo.builder().configKey(ConfigurationConstants.CONFIG_KEY_END_WORKING_DAY).configValue(finalWorkingDay.name())
                .enabled(true).build();
        configurationRepositoryMongo.save(cp2);
    }

    @Override
    public void setRestaurantWorkingHours(int initialHour, int finalHour) {
        Optional<ConfigurationMongo> oc = configurationRepositoryMongo.findByConfigKey(ConfigurationConstants.CONFIG_KEY_INITIAL_HOUR);
        ConfigurationMongo cp = null;
        if(oc.isPresent()) {
            oc.get().setConfigValue(Integer.toString(initialHour));
            cp=oc.get();
        }
        else cp=ConfigurationMongo.builder().configKey(ConfigurationConstants.CONFIG_KEY_INITIAL_HOUR).configValue(Integer.toString(initialHour))
                .enabled(true).build();
        configurationRepositoryMongo.save(cp);

        Optional<ConfigurationMongo> oc2 = configurationRepositoryMongo.findByConfigKey(ConfigurationConstants.CONFIG_KEY_END_HOUR);
        ConfigurationMongo cp2 = null;
        if(oc2.isPresent()) {
            oc2.get().setConfigValue(Integer.toString(finalHour));
            cp2=oc2.get();
        }
        else cp2=ConfigurationMongo.builder().configKey(ConfigurationConstants.CONFIG_KEY_END_HOUR).configValue(Integer.toString(finalHour))
                .enabled(true).build();
        configurationRepositoryMongo.save(cp2);
    }
}

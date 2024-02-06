package com.ats.reservasrestaurante.application.service.configuration;

import com.ats.reservasrestaurante.application.lasting.ConfigurationConstants;
import com.ats.reservasrestaurante.application.service.ConfigurationGenericService;
import com.ats.reservasrestaurante.domain.dto.ConfigurationDto;
import com.ats.reservasrestaurante.domain.entity.configuration.ConfigurationPostgres;
import com.ats.reservasrestaurante.domain.repository.configuration.ConfigurationRepositoryPostgres;
import com.ats.reservasrestaurante.domain.repository.table.TableRepositoryPostgres;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationServicePostgres implements ConfigurationGenericService {
    private final ConfigurationRepositoryPostgres configurationRepositoryPostgres;


    public ConfigurationServicePostgres(ConfigurationRepositoryPostgres configurationRepositoryPostgres, TableRepositoryPostgres tableRepositoryPostgres){
        this.configurationRepositoryPostgres=configurationRepositoryPostgres;
    }

    @Override
    public void create(String key, String value) {
        ConfigurationPostgres configurationPostgres = ConfigurationPostgres.builder()
                .configKey(key)
                .configValue(value)
                .enabled(true)
                .build();
        configurationRepositoryPostgres.save(configurationPostgres);
    }

    @Override
    public void update(String key, String value) {
        Optional<ConfigurationPostgres> c = configurationRepositoryPostgres.findByConfigKey(key);
        if(c.isPresent()){
            c.get().setConfigValue(value);
            configurationRepositoryPostgres.save(c.get());
        }
    }

    @Override
    public ConfigurationDto findByKey(String key) throws Exception {
        Optional<ConfigurationPostgres> c = configurationRepositoryPostgres.findByConfigKey(key);
        if(c.isPresent())  return new ConfigurationDto(c.get().getConfigKey(),c.get().getConfigValue());
        else throw new Exception("Error: Key not found");
    }

    @Override
    public List<ConfigurationDto> findAll() {
        List<ConfigurationPostgres> listConf=configurationRepositoryPostgres.findAll();
        List<ConfigurationDto> listConfigDto = new ArrayList<>();
        for (ConfigurationPostgres configurationPostgres : listConf) {
            listConfigDto.add(new ConfigurationDto(configurationPostgres.getConfigKey(), configurationPostgres.getConfigValue()));
        }
        return listConfigDto;
    }

    @Override
    public void removeByKey(String key) {
        Optional<ConfigurationPostgres> c = configurationRepositoryPostgres.findByConfigKey(key);
        if(c.isPresent()){
            c.get().setEnabled(false);
            configurationRepositoryPostgres.save(c.get());
        }
    }

    @Override
    public void setRestaurantWorkingDays(DayOfWeek initialWorkingDay, DayOfWeek finalWorkingDay) {
        Optional<ConfigurationPostgres> oc = configurationRepositoryPostgres.findByConfigKey(ConfigurationConstants.CONFIG_KEY_INITIAL_WORKING_DAY);
        ConfigurationPostgres cp = null;
        if(oc.isPresent()) {
            oc.get().setConfigValue(initialWorkingDay.name());
            cp=oc.get();
        }
        else cp=ConfigurationPostgres.builder().configKey(ConfigurationConstants.CONFIG_KEY_INITIAL_WORKING_DAY).configValue(initialWorkingDay.name())
                .enabled(true).build();
        configurationRepositoryPostgres.save(cp);

        Optional<ConfigurationPostgres> oc2 = configurationRepositoryPostgres.findByConfigKey(ConfigurationConstants.CONFIG_KEY_END_WORKING_DAY);
        ConfigurationPostgres cp2 = null;
        if(oc2.isPresent()) {
            oc2.get().setConfigValue(finalWorkingDay.name());
            cp2=oc2.get();
        }
        else cp2=ConfigurationPostgres.builder().configKey(ConfigurationConstants.CONFIG_KEY_END_WORKING_DAY).configValue(finalWorkingDay.name())
                .enabled(true).build();
        configurationRepositoryPostgres.save(cp2);
    }

    @Override
    public void setRestaurantWorkingHours(int initialHour, int finalHour) {
        Optional<ConfigurationPostgres> oc = configurationRepositoryPostgres.findByConfigKey(ConfigurationConstants.CONFIG_KEY_INITIAL_HOUR);
        ConfigurationPostgres cp = null;
        if(oc.isPresent()) {
            oc.get().setConfigValue(Integer.toString(initialHour));
            cp=oc.get();
        }
        else cp=ConfigurationPostgres.builder().configKey(ConfigurationConstants.CONFIG_KEY_INITIAL_HOUR).configValue(Integer.toString(initialHour))
                .enabled(true).build();
        configurationRepositoryPostgres.save(cp);

        Optional<ConfigurationPostgres> oc2 = configurationRepositoryPostgres.findByConfigKey(ConfigurationConstants.CONFIG_KEY_END_HOUR);
        ConfigurationPostgres cp2 = null;
        if(oc2.isPresent()) {
            oc2.get().setConfigValue(Integer.toString(finalHour));
            cp2=oc2.get();
        }
        else cp2=ConfigurationPostgres.builder().configKey(ConfigurationConstants.CONFIG_KEY_END_HOUR).configValue(Integer.toString(finalHour))
                .enabled(true).build();
        configurationRepositoryPostgres.save(cp2);
    }

}

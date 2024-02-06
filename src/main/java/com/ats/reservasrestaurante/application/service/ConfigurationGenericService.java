package com.ats.reservasrestaurante.application.service;

import com.ats.reservasrestaurante.domain.dto.ConfigurationDto;
import java.util.List;

public interface ConfigurationGenericService {
    public void create(String key, String value);
    public void update(String key, String value);
    public ConfigurationDto findByKey (String key) throws Exception;
    public List<ConfigurationDto> findAll ();
    public void removeByKey(String key);
    public void setRestaurantWorkingDays(java.time.DayOfWeek initialWorkingDay, java.time.DayOfWeek finalWorkingDay);
    public void setRestaurantWorkingHours(int initialHour, int finalHour);

}

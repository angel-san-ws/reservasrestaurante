package com.ats.reservasrestaurante.application.controller;

import com.ats.reservasrestaurante.application.lasting.ConfigurationConstants;
import com.ats.reservasrestaurante.application.service.ConfigurationGenericService;
import com.ats.reservasrestaurante.domain.dto.ConfigurationDto;
import com.ats.reservasrestaurante.domain.dto.WorkingDaysDto;
import com.ats.reservasrestaurante.domain.dto.WorkingHoursDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WithMockUser
@SpringBootTest
public class ConfigurationControllerTest {
    @Autowired
    private ConfigurationController configurationController;
    private MockMvc mockMvc;
    @MockBean
    ConfigurationGenericService configurationGenericService;

    @BeforeEach
    public void setup() {
        this.mockMvc = standaloneSetup(configurationController)
                .setControllerAdvice(new SecurityException())
                .build();
    }

    @Test
    void testFindAllConfigurations() throws Exception {
        // Given
        ConfigurationDto initialHourConfig = new ConfigurationDto(ConfigurationConstants.CONFIG_KEY_INITIAL_HOUR, "10");
        ConfigurationDto endHourConfig = new ConfigurationDto(ConfigurationConstants.CONFIG_KEY_END_HOUR, "20");

        List<ConfigurationDto> configsDto = Arrays.asList(initialHourConfig, endHourConfig);

        // When
        when(configurationGenericService.findAll()).thenReturn(configsDto);
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/config"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse();

        // Then
        verify(configurationGenericService).findAll();
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(configsDto));
    }

    @Test
    void testSetRestaurantWorkingDays() throws Exception{
        // Given
        WorkingDaysDto workingDaysDto = new WorkingDaysDto("MONDAY","FRIDAY");
        DayOfWeek initDay= DayOfWeek.valueOf(workingDaysDto.initialDay());
        DayOfWeek endDay= DayOfWeek.valueOf(workingDaysDto.finalDay());

        // When
        this.mockMvc.perform(post("/api/v1/config/workingDays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(workingDaysDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(configurationGenericService).setRestaurantWorkingDays(initDay,endDay);
    }

    @Test
    void testSetRestaurantWorkingHours() throws Exception{
        // Given
        WorkingHoursDto workingHoursDto = new WorkingHoursDto(10,20);

        // When
        this.mockMvc.perform(post("/api/v1/config/workingHours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(workingHoursDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(configurationGenericService).setRestaurantWorkingHours(10,20);
    }


}

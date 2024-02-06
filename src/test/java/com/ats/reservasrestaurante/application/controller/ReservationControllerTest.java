package com.ats.reservasrestaurante.application.controller;

import com.ats.reservasrestaurante.application.lasting.ConfigurationConstants;
import com.ats.reservasrestaurante.application.service.ReservationGenericService;
import com.ats.reservasrestaurante.domain.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WithMockUser
@SpringBootTest
public class ReservationControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private ReservationController reservationController;
    @MockBean
    private ReservationGenericService reservationGenericService;

    @BeforeEach
    public void setup() {
        this.mockMvc = standaloneSetup(reservationController)
                .setControllerAdvice(new SecurityException())
                .build();
    }

    @Test
    void testFindAllReservations() throws Exception {
        // Given
        ReservationDto res = new ReservationDto(1, 8, "asanchez",
                "05/02/2024 19:35","05/02/2024 20:35", 1, "Enabled", "05/02/2024 16:35");
        List<ReservationDto> reservationDtoList = Arrays.asList(res);

        // When
        when(reservationGenericService.findAllReservations()).thenReturn(reservationDtoList);
        MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/reservation"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse();

        // Then
        verify(reservationGenericService).findAllReservations();
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(reservationDtoList));
    }

    @Test
    void testCreateReservation() throws Exception{
        // Given
        CreateReservationDto reservationDto = new CreateReservationDto("5", "06/02/2024", "11:00");

        // When
        this.mockMvc.perform(post("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservationDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Then
        verify(reservationGenericService).createReservation(reservationDto.numberPersons(),reservationDto.reservationDate(),reservationDto.reservationStartTime());
    }

    @Test
    void testEditReservation() throws Exception{
        // Given
        EditReservationDto editReservationDto = new EditReservationDto("06/02/2024", "11:00");

        // When
        this.mockMvc.perform(put("/api/v1/reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(editReservationDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(reservationGenericService).editReservation("1",editReservationDto.reservationDate(),editReservationDto.reservationStartTime());
    }

    @Test
    void testRemoveReservation() throws Exception{
        // Given

        // When
        this.mockMvc.perform(delete("/api/v1/reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        verify(reservationGenericService).removeReservation("1");
    }


}

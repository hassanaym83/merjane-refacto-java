package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.exceptions.ResourceNotFoundException;
import com.nimbleways.springboilerplate.exceptions.UnknownProductTypeException;
import com.nimbleways.springboilerplate.services.api.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = OrderController.class)
@RequiredArgsConstructor
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    @Test
    void processOrder_returns200AndDelegates() throws Exception {
        //Arrange
        Mockito.when(orderService.processOrder(1L)).thenReturn(new ProcessOrderResponse(1L));

        //Act & Assert
        mockMvc.perform(post("/orders/{id}/processOrder", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(orderService).processOrder(1L);
    }

    @Test void orderNotFound_returns404() throws Exception {           // couvre l'advice #1
        when(orderService.processOrder(999L)).thenThrow(new ResourceNotFoundException("Order"));
        mockMvc.perform(post("/orders/{id}/processOrder", 999L))
                .andExpect(status().isNotFound());
    }

    @Test void unknownType_returns400() throws Exception {
        //Act & Assert
        when(orderService.processOrder(999L)).thenThrow(new UnknownProductTypeException());
        mockMvc.perform(post("/orders/{id}/processOrder", 999L))
                .andExpect(status().isBadRequest());
    }
    @Test void illegalArgument_returns422() throws Exception {
        //Act & Assert
        when(orderService.processOrder(999L)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/orders/{id}/processOrder", 999L))
                .andExpect(status().isUnprocessableEntity());
    }

}
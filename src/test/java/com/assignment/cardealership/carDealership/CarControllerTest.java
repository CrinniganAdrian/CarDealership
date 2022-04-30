package com.assignment.cardealership.carDealership;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.assignment.cardealership.carDealership.controllers.CarController;
import com.assignment.cardealership.carDealership.dto.Car;
import com.assignment.cardealership.carDealership.repositories.CarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(CarController.class)
public class CarControllerTest {
	 @Autowired
	    MockMvc mockMvc;
	    @Autowired
	    ObjectMapper mapper;
	    
	    @MockBean
	    CarRepository carRepository;
	    
	    Car car1 = new Car(1l, "Ford", "Fuel");
	    Car car2 = new Car(2l, "Toyota", "Hybrid");
	    Car car3 = new Car(3l, "Tesla", "Electric");
	    
	    
	    @Test
	    public void getAllCars_success() throws Exception {
	        List<Car> cars = new ArrayList<>(Arrays.asList(car1, car2, car3));
	        
	        Mockito.when(carRepository.findAll()).thenReturn(cars);
	        
	        mockMvc.perform(MockMvcRequestBuilders
	                .get("/car")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(3)))
	                .andExpect(jsonPath("$[2].carMake", is("Tesla")));
	    }
	    
	    
	    
	    @Test
	    public void createCar_success() throws Exception {
	        Car car = Car.builder()
	                .carMake("VW Golf")
	                .carType("Fuel")
	                .build();

	        Mockito.when(carRepository.save(car)).thenReturn(car);

	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(this.mapper.writeValueAsString(car));

	        mockMvc.perform(mockRequest)
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", notNullValue()))
	                .andExpect(jsonPath("$.carMake", is("VW Golf")));
	        }
	    
	    
	    
	    @Test
	    public void updateCar_success() throws Exception {
	        Car updatedCar = Car.builder()
	                .carId(1l)
	                .carMake("Ford")
	                .carType("Hybrid")
	                .build();

	        Mockito.when(carRepository.findById(car1.getCarId())).thenReturn(Optional.of(car1));
	        Mockito.when(carRepository.save(updatedCar)).thenReturn(updatedCar);

	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/car")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(this.mapper.writeValueAsString(updatedCar));

	        mockMvc.perform(mockRequest)
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", notNullValue()))
	                .andExpect(jsonPath("$.carMake", is("Ford")));
	    }
	    
	    
	    
	    @Test
	    public void deleteCarById_success() throws Exception {
	        Mockito.when(carRepository.findById(car2.getCarId())).thenReturn(Optional.of(car2));

	        mockMvc.perform(MockMvcRequestBuilders
	                .delete("/car/2")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk());
	    }


}

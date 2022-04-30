package com.assignment.cardealership.carDealership;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.assignment.cardealership.carDealership.controllers.CarController;
import com.assignment.cardealership.carDealership.controllers.PartController;
import com.assignment.cardealership.carDealership.dto.Car;
import com.assignment.cardealership.carDealership.dto.Part;
import com.assignment.cardealership.carDealership.repositories.CarRepository;
import com.assignment.cardealership.carDealership.repositories.PartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PartController.class)
public class PartControllerTest {
	 @Autowired
	    MockMvc mockMvc;
	    @Autowired
	    ObjectMapper mapper;
	    
	    @MockBean
	    PartRepository partRepository;
	    
	    Part part1 = new Part(1l, "Oil Filter", "Fuel");
	    Part part2 = new Part(2l, "Motor", "Hybrid");
	    Part part3 = new Part(3l, "Plug Socket", "Electric");
	    
	    
	    @Test
	    public void getAllParts_success() throws Exception {
	        List<Part> parts = new ArrayList<>(Arrays.asList(part1, part2, part3));
	        
	        Mockito.when(partRepository.findAll()).thenReturn(parts);
	        
	        mockMvc.perform(MockMvcRequestBuilders
	                .get("/part")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(3)))
	                .andExpect(jsonPath("$[2].partName", is("Plug Socket")));
	    }
	    
	    
	    
	    @Test
	    public void createPart_success() throws Exception {
	        Part part = Part.builder()
	                .partName("Gearbox")
	                .partType("Fuel")
	                .build();

	        Mockito.when(partRepository.save(part)).thenReturn(part);

	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/part")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(this.mapper.writeValueAsString(part));

	        mockMvc.perform(mockRequest)
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", notNullValue()))
	                .andExpect(jsonPath("$.partName", is("Gearbox")));
	        }
	    
	    
	    
	    @Test
	    public void updatePart_success() throws Exception {
	        Part updatedPart = Part.builder()
	                .partId(1l)
	                .partName("Oil Filter")
	                .partType("Hybrid")
	                .build();

	        Mockito.when(partRepository.findById(part1.getPartId())).thenReturn(Optional.of(part1));
	        Mockito.when(partRepository.save(updatedPart)).thenReturn(updatedPart);

	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/part")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(this.mapper.writeValueAsString(updatedPart));

	        mockMvc.perform(mockRequest)
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", notNullValue()))
	                .andExpect(jsonPath("$.partName", is("Oil Filter")));
	    }
	    
	    
	    
	    @Test
	    public void deletePartById_success() throws Exception {
	        Mockito.when(partRepository.findById(part2.getPartId())).thenReturn(Optional.of(part2));

	        mockMvc.perform(MockMvcRequestBuilders
	                .delete("/part/2")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk());
	    }


}

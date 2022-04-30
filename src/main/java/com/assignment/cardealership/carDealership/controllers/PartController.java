package com.assignment.cardealership.carDealership.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.cardealership.carDealership.dto.Part;
import com.assignment.cardealership.carDealership.repositories.PartRepository;

@RestController
@RequestMapping(value = "/part")
public class PartController {
    @Autowired PartRepository partRepository;
    
    @GetMapping
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    @GetMapping(value = "{partId}")
    public Part getPartById(@PathVariable(value="partId") Long partId) {
        return partRepository.findById(partId).get();
    }
    
    @PostMapping
    public Part createPart(@RequestBody @Valid Part part) {
        return partRepository.save(part);
    }
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String s) {
            super(s);
        }
    }
    
    
    @PutMapping
    public Part updatePart(@RequestBody Part part) throws NotFoundException {
        if (part == null || part.getPartId() == null) {
            throw new InvalidRequestException("Part or ID must not be null!");
        }
        Optional<Part> optionalPart = partRepository.findById(part.getPartId());
        if (!optionalPart.isPresent()) {
            throw new NotFoundException();
        }
        Part existingPart = optionalPart.get();

        existingPart.setPartName(part.getPartName());
        existingPart.setPartType(part.getPartType());
    	
        return partRepository.save(existingPart);
    }
    
    
    
    @DeleteMapping(value = "{partId}")
    public void deletePartById(@PathVariable(value = "partId") Long partId) throws NotFoundException {
        if (!partRepository.findById(partId).isPresent()) {
            throw new NotFoundException();
        }
        partRepository.deleteById(partId);
    }
}


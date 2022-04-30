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

import com.assignment.cardealership.carDealership.dto.Car;
import com.assignment.cardealership.carDealership.repositories.CarRepository;

@RestController
@RequestMapping(value = "/car")
public class CarController {
    @Autowired CarRepository carRepository;
    
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @GetMapping(value = "{carId}")
    public Car getCarById(final@PathVariable(value="carId") Long carId) {
        return carRepository.findById(carId).get();
    }
    
    @PostMapping
    public Car createCar(@RequestBody @Valid Car car) {
        return carRepository.save(car);
    }
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String s) {
            super(s);
        }
    }
    
    
    @PutMapping
    public Car updateCar(@RequestBody Car car) throws NotFoundException {
        if (car == null || car.getCarId() == null) {
            throw new InvalidRequestException("Car or ID must not be null!");
        }
        Optional<Car> optionalCar = carRepository.findById(car.getCarId());
        if (!optionalCar.isPresent()) {
            throw new NotFoundException();
        }
        Car existingCar = optionalCar.get();

        existingCar.setCarMake(car.getCarMake());
        existingCar.setCarType(car.getCarType());
    	
        return carRepository.save(existingCar);
    }
    
    
    
    @DeleteMapping(value = "{carId}")
    public void deleteCarById(@PathVariable(value = "carId") Long carId) throws NotFoundException {
        if (!carRepository.findById(carId).isPresent()) {
            throw new NotFoundException();
        }
        carRepository.deleteById(carId);
    }
}

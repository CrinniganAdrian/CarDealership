package com.assignment.cardealership.carDealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.cardealership.carDealership.dto.Part;


@Repository
public interface PartRepository extends JpaRepository<Part, Long> {}

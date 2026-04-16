package com.dos.salasDeCoworking.repository;

import com.dos.salasDeCoworking.model.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

}

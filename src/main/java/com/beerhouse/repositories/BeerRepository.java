package com.beerhouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.beerhouse.entities.Beer;

@Transactional(readOnly=true)
public interface BeerRepository extends JpaRepository<Beer, Long>{
	
	Beer findById(Long id);
	
	Beer findByName(String descricao);

}

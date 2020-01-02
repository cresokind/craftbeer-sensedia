package com.beerhouse.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.beerhouse.entities.Beer;
import com.beerhouse.repositories.BeerRepository;
import com.beerhouse.service.BeerService;

@Service
public class BeerServiceImpl implements BeerService{
	
	private static final Logger log = LoggerFactory.getLogger(BeerServiceImpl.class);

	@Autowired
	private BeerRepository beerRepository;

	public Optional<Beer> buscarPorId(Long id) {
		return Optional.ofNullable(this.beerRepository.findById(id));
	}
	
	public Optional<Beer> buscarCervejaPorNome(String name) {
		return Optional.ofNullable(this.beerRepository.findByName(name));
	}
	
	@CachePut("listarTodasCervejas")
	public Beer cadastrar(Beer beer) {
		return this.beerRepository.save(beer);
	}
	
	@CachePut("listarTodasCervejas")
	public void remover(Long id) {
		this.beerRepository.delete(id);
	}

	@Cacheable("listarTodasCervejas")
	public Page<Beer> buscarTodasCervejas(PageRequest pageRequest) {
		return this.beerRepository.findAll(pageRequest);
	}

}

package com.beerhouse.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.beerhouse.entities.Beer;

public interface BeerService {

	/**
	 * Retorna uma cerveja por ID.
	 * 
	 * @param id
	 * @return Optional<Beer>
	 */
	Optional<Beer> buscarPorId(Long id);
	
	/**
	 * Cadastrar cerveja na base de dados.
	 * 
	 * @param beer
	 * @return Beer
	 */
	Beer cadastrar(Beer beer);
	
	/**
	 * Retorna uma cerveja pelo nome.
	 * 
	 * @param name
	 * @return Optional<Beer>
	 */
	Optional<Beer> buscarCervejaPorNome(String name);
	
	/**
	 * Remove um cadastro de cerveja por id do bd.
	 * 
	 * @param id
	 */
	void remover(Long id);
	
	/**
	 * Retorna uma lista paginada de todas cervejas cadastradas.
	 * 
	 * @param pageRequest
	 * @return Page<Beer>
	 */
	Page<Beer> buscarTodasCervejas(PageRequest pageRequest);
	
}

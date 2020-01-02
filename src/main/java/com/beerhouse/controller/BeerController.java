package com.beerhouse.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beerhouse.dto.BeerDto;
import com.beerhouse.entities.Beer;
import com.beerhouse.response.Response;
import com.beerhouse.service.BeerService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/beers/v1")
public class BeerController {
	
	private static final Logger log = LoggerFactory.getLogger(BeerController.class);
	
	@Autowired
	private BeerService beerService;
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdItensPorPagina;
	
	public BeerController() {}
	
	
	/**
	 * Retorna uma cerveja por um id.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<BeerDto>>
	 */
	@ApiOperation(value = "Buscar cerveja pelo ID")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<BeerDto>> buscarCervejaPorId(@PathVariable("id") Long id) {
		log.info("Buscando beer por id: {}", id);
		
		Response<BeerDto> response = new Response<BeerDto>();
		
		Optional<Beer> beer = this.beerService.buscarPorId(id);

		if (!beer.isPresent()) {
			log.info("Nenhuma cerveja encontrada com esse id: {}", id);
			response.getErrors().add("Nenhuma cerveja encontrada com esse id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterBeerDto(beer.get()));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna todas cervejas cadastradas
	 * 
	 * @return ResponseEntity<Response<BeerDto>>
	 */
	@ApiOperation(value = "Retorna uma lista paginada de todas as cervejas cadastras")
	@GetMapping(value = "/", consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Response<Page<BeerDto>>> buscarTodasCervejas(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Buscando todas beers, página: {}", pag);
		
		Response<Page<BeerDto>> response = new Response<Page<BeerDto>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdItensPorPagina, Direction.valueOf(dir), ord);
		Page<Beer> beers = this.beerService.buscarTodasCervejas(pageRequest);
		
		if(!beers.hasContent()) {
			log.info("Nenhuma cerveja cadastrada!");
			response.getErrors().add("Nenhuma cerveja cadastrada!");
			return ResponseEntity.badRequest().body(response);
		}
		
		Page<BeerDto> beersDto = beers.map(objBeer -> this.converterBeerDto(objBeer));

		response.setData(beersDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Cadastra uma cerveja.
	 * 
	 * @param beerDto
	 * @param result
	 * @return ResponseEntity<Response<BeerDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@ApiOperation(value = "Realiza o cadastro de uma cerveja")
	@PostMapping(value = "/", consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Response<BeerDto>> cadastrarCerveja(@Valid @RequestBody BeerDto beerDto,
			BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando Cerveja: {}", beerDto.toString());
		
		Response<BeerDto> response = new Response<BeerDto>();
			
		Beer beer = this.converterDtoParaObjBeer(beerDto, result);
		
		this.validarDadosExistentes(beerDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando dados do cadastro da cerveja: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.beerService.cadastrar(beer);

		response.setData(this.converterBeerDto(beer));
		return ResponseEntity.ok(response);
	}

	
	/**
	 * Atualiza os dados de uma cerveja.
	 * 
	 * @param id
	 * @param beerDto
	 * @param result
	 * @return ResponseEntity<Response<Beer>>
	 * @throws NoSuchAlgorithmException
	 */
	@ApiOperation(value = "Altera o cadastro de uma cerveja por ID")
	@PutMapping(value = "/{id}", consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Response<BeerDto>> atualizarPorId(@PathVariable("id") Long id,
			@Valid @RequestBody BeerDto beerDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando cerveja: {}", beerDto.toString());
		
		Response<BeerDto> response = new Response<BeerDto>();

		Optional<Beer> beer = this.beerService.buscarPorId(id);
		
		if (!beer.isPresent()) {
			result.addError(new ObjectError("beer", "Nenhuma cerveja encontrada com este Id."));
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.atualizaDadosBeer(beer.get(), beerDto, result);

		this.beerService.cadastrar(beer.get());
		response.setData(this.converterBeerDto(beer.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza parcial os dados de uma cerveja.
	 * 
	 * @param id
	 * @param beerDto
	 * @param result
	 * @return ResponseEntity<Response<Beer>>
	 * @throws NoSuchAlgorithmException
	 */	
	@ApiOperation(value = "Altera o cadastro parcial de uma cerveja por ID")
	@PatchMapping(value = "/{id}", consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<Response<BeerDto>> atualizarParcialCervejaPorId(@PathVariable("id") Long id,
			@RequestBody BeerDto beerDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando cerveja: {}", beerDto.toString());
		
		Response<BeerDto> response = new Response<BeerDto>();

		Optional<Beer> beerToUpdate = this.beerService.buscarPorId(id);
		
		if (!beerToUpdate.isPresent()) {
			result.addError(new ObjectError("beer", "Nenhuma cerveja encontrada com este Id."));
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.atualizaDadosBeer(beerToUpdate.get(), beerDto, result);
		
		BeanUtils.copyProperties(beerDto, beerToUpdate, "id");

		this.beerService.cadastrar(beerToUpdate.get());
		response.setData(this.converterBeerDto(beerToUpdate.get()));

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Apaga o registro de uma beer por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Beer>>
	 */
	@ApiOperation(value = "Apaga o registro de uma beer por ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> removerBeer(@PathVariable("id") Long id) {
		
		log.info("Removendo cerveja: {}", id);
		
		Response<String> response = new Response<String>();
		Optional<Beer> beer = this.beerService.buscarPorId(id);

		if (!beer.isPresent()) {
			log.info("Erro ao remover cerveja ID: {} não encontrado.", id);
			response.getErrors().add("Não é possivel excluir o cadastro. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.beerService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Atualiza os dados da cerveja com base nos dados encontrados no DTO ( param ).
	 * 
	 * @param beer
	 * @param beerDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizaDadosBeer(Beer beer, BeerDto beerDto, BindingResult result)
			throws NoSuchAlgorithmException {
		
		beer.setName(beerDto.getName());
		beer.setCategory(beerDto.getCategory());
		beer.setIngredients(beerDto.getIngredients());
		beer.setAlcoholContent(beerDto.getAlcoholContent());
		beer.setPrice(beerDto.getPrice());
	}
	
	/**
	 * Retorna um DTO com os dados de uma cerveja.
	 * 
	 * @param beer
	 * @return BeerDto
	 */
	private BeerDto converterBeerDto(Beer beer) {
		
		BeerDto beerDto = new BeerDto();
		beerDto.setId(beer.getId());
		
		beerDto.setName(beer.getName());
		beerDto.setCategory(beer.getCategory());
		beerDto.setIngredients(beer.getIngredients());
		beerDto.setAlcoholContent(beer.getAlcoholContent());
		beerDto.setPrice(beer.getPrice());

		return beerDto;
	}
	
	/**
	 * Retorna um Obj Beer com os dados da Dto.
	 * 
	 * @param beerDto
	 * @return Beer
	 * @throws NoSuchAlgorithmException
	 */
	private Beer converterDtoParaObjBeer(BeerDto beerDto, BindingResult result) throws NoSuchAlgorithmException {
		Beer beer = new Beer();
		beer.setName(beerDto.getName());
		beer.setCategory(beerDto.getCategory());
		beer.setIngredients(beerDto.getIngredients());
		beer.setAlcoholContent(beerDto.getAlcoholContent());
		beer.setPrice(beerDto.getPrice());

		return beer;
	}
	
	/**
	 * Verifica se a cerveja ja esta cadastrada com o mesmo nome.
	 * 
	 * @param beerDto
	 * @param result
	 */
	private void validarDadosExistentes(BeerDto beerDto, BindingResult result) {
		
		this.beerService.buscarCervejaPorNome(beerDto.getName())
			.ifPresent(func -> result.addError(new ObjectError("Beer", "Cerveja ja existente: " + beerDto.getName())));
	}

}

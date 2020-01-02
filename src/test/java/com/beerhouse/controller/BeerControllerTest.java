package com.beerhouse.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.beerhouse.dto.BeerDto;
import com.beerhouse.entities.Beer;
import com.beerhouse.service.BeerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BeerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BeerService beerService;
	
	private static final String URL_BASE = "/api/beers/v1/";
	private static final Long ID_CERVEJA = 1L;
	private static final String CATEGORIA_CERVEJA = "IPA";
	private static final String NOME_CERVEJA = "Roleta Russa";
	private static final String INGREDIENTES_CERVEJA = "Roleta Russa";
	private static final Double PRECO = 35.0;
	private static final Double TEOR_ALCOOLICO = 7.8;
	
	@Test
	public void testBuscaPorId() throws Exception {
		BDDMockito.given(this.beerService.buscarPorId(Mockito.anyLong()))
				.willReturn(Optional.of(this.obterObjBeer()));

		mvc.perform(MockMvcRequestBuilders.get(URL_BASE + ID_CERVEJA)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_CERVEJA))
				.andExpect(jsonPath("$.data.category").value(CATEGORIA_CERVEJA))
				.andExpect(jsonPath("$.data.name").value(NOME_CERVEJA))
				.andExpect(jsonPath("$.data.price").value(PRECO))
				.andExpect(jsonPath("$.data.alcoholContent").value(TEOR_ALCOOLICO))
				.andExpect(jsonPath("$.data.ingredients").value(INGREDIENTES_CERVEJA))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	public void testRemoverBeer() throws Exception {
		BDDMockito.given(this.beerService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Beer()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_CERVEJA)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testAlterarBeer() throws Exception {

		BDDMockito.given(this.beerService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(this.obterObjBeer()));
		
		String response = this.mapperRequisicaoPost();

		mvc.perform(MockMvcRequestBuilders.put(URL_BASE + ID_CERVEJA)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(response))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_CERVEJA))
				.andExpect(jsonPath("$.data.category").value(CATEGORIA_CERVEJA))
				.andExpect(jsonPath("$.data.name").value(NOME_CERVEJA))
				.andExpect(jsonPath("$.data.price").value(PRECO))
				.andExpect(jsonPath("$.data.alcoholContent").value(TEOR_ALCOOLICO))
				.andExpect(jsonPath("$.data.ingredients").value(INGREDIENTES_CERVEJA))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	public void testAlterarPatchBeer() throws Exception {

		BDDMockito.given(this.beerService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(this.obterObjBeer()));
		
		String response = this.mapperRequisicaoPost();

		mvc.perform(MockMvcRequestBuilders.patch(URL_BASE + ID_CERVEJA)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(response))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_CERVEJA))
				.andExpect(jsonPath("$.data.category").value(CATEGORIA_CERVEJA))
				.andExpect(jsonPath("$.data.name").value(NOME_CERVEJA))
				.andExpect(jsonPath("$.data.price").value(PRECO))
				.andExpect(jsonPath("$.data.alcoholContent").value(TEOR_ALCOOLICO))
				.andExpect(jsonPath("$.data.ingredients").value(INGREDIENTES_CERVEJA))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	private String mapperRequisicaoPost() throws JsonProcessingException {
		BeerDto beerDto = new BeerDto();
		beerDto.setId(null);
		beerDto.setName(NOME_CERVEJA);
		beerDto.setCategory(CATEGORIA_CERVEJA);
		beerDto.setPrice(PRECO);
		beerDto.setIngredients(INGREDIENTES_CERVEJA);
		beerDto.setAlcoholContent(TEOR_ALCOOLICO);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(beerDto);
	}
	
	private Beer obterObjBeer() {
		Beer beer = new Beer();
		beer.setId(ID_CERVEJA);
		beer.setName(NOME_CERVEJA);
		beer.setCategory(CATEGORIA_CERVEJA);
		beer.setPrice(PRECO);
		beer.setIngredients(INGREDIENTES_CERVEJA);
		beer.setAlcoholContent(TEOR_ALCOOLICO);
		return beer;
	}

}

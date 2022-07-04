package com.example.algamoneyapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoneyapi.model.Categoria;
import com.example.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//aqui simples sem tratamento para lista vazia
	@GetMapping
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	//com tratamento para lista vazia
	@GetMapping("/outro")
	public ResponseEntity<?> listarCategorias(){
		List<Categoria> categoria = categoriaRepository.findAll();
		return !categoria.isEmpty() ? ResponseEntity.ok(categoria): ResponseEntity.noContent().build();
	}
	
	//busca pelo id sem tratamento para resposta vazia
	@GetMapping("/outrogato/{x}")
	public ResponseEntity<?> listarCategorias2( @PathVariable  Long x){
		Optional<Categoria> categoria = categoriaRepository.findById(x);
		return ResponseEntity.ok(categoria);
	}
	
	//busca pelo id com tratamento para resposta vazia
	@GetMapping("/outrogato2/{x}")
	public ResponseEntity<?> listarCategorias3( @PathVariable  Long x){
		Optional<Categoria> categoria = categoriaRepository.findById(x);
		return !categoria.isEmpty()?ResponseEntity.ok(categoria): ResponseEntity.noContent().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)//esta anotação é só para tratar o retorno do metodo como http cod
	public void criarCategoria(@RequestBody Categoria cat) {
		Categoria catSalva = categoriaRepository.save(cat);
	}
	
	@PostMapping("/cadastrar/")
	@ResponseStatus(HttpStatus.CREATED)//esta anotação é só para tratar o retorno do metodo como http cod
	public String criar(@RequestBody Categoria cat) {
		Categoria catSalva = categoriaRepository.save(cat);
		return catSalva.getNome();//apenas retorna o que foi salvo
	}
	
	
	
	/*passar location, no header, de onde foi criado para
	 *  ser recuperado depois
	 */
	
	
	@PostMapping("/cadastrarcat/")
	@ResponseStatus(HttpStatus.CREATED)//esta anotação é só para tratar o retorno do metodo como http cod
	public void criarCat(@RequestBody Categoria cat, HttpServletResponse resp ) {
		Categoria catSalva = categoriaRepository.save(cat);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(catSalva.getCodigo()).toUri();
		resp.setHeader("Location",uri.toASCIIString());		
		
	}
	
}

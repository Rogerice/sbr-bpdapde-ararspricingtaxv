package com.santander.bp.api.client;

import com.santander.bp.model.AppArsenalRequestDTO;
import com.santander.bp.model.AppArsenalResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@FeignClient(name = "cucumber", url = "${server.host}" + ":${server.port}" + "/api/v1/apparsenal", dismiss404 = true)
public interface CucumberClient {

	@GetMapping
	ResponseEntity<List<AppArsenalResponseDTO>> getAll();

	@DeleteMapping("/{id}")
	ResponseEntity<AppArsenalResponseDTO> delete(@PathVariable("id") long id);

	@GetMapping("/{id}")
	ResponseEntity<AppArsenalResponseDTO> getById(@PathVariable("id") long id);

	@PostMapping
	ResponseEntity<AppArsenalResponseDTO> create(@RequestBody @Valid AppArsenalRequestDTO appArsenal);

	@PutMapping("/{id}")
	ResponseEntity<AppArsenalResponseDTO> update(@PathVariable("id") long id, @RequestBody AppArsenalRequestDTO appArsenal);
}

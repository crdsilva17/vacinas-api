package br.com.municipio.vacinas.vacinas_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;
import br.com.municipio.vacinas.vacinas_api.service.LocalService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vacinas/locais")
public class LocalController {

    private final LocalService service;

    @PostMapping
    public ResponseEntity<LocalResponseDTO> cadastrarLocal(@RequestBody LocalRequestDTO request) {
        return ResponseEntity.ok(service.cadastrarLocal(request));
    }

    @PutMapping
    public ResponseEntity<LocalResponseDTO> editarLocal(@RequestParam String id, @RequestBody LocalRequestDTO request) {
        
        return ResponseEntity.ok(service.editarLocal(id, request));
        
    }

}

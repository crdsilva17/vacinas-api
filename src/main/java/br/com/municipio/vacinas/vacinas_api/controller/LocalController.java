package br.com.municipio.vacinas.vacinas_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.LocalRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.LocalResponseDTO;
import br.com.municipio.vacinas.vacinas_api.service.LocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locais")
public class LocalController {

    private final LocalService service;

    @PostMapping
    public ResponseEntity<LocalResponseDTO> cadastrarLocal(@Valid @RequestBody LocalRequestDTO request) {
        return ResponseEntity.ok(service.cadastrarLocal(request));
    }

    @GetMapping("/id")
    public ResponseEntity<LocalResponseDTO> buscarPorId(@RequestParam("id") String id) {
        return ResponseEntity.ok(service.buscarLocalPorId(id));
    }

    @GetMapping("/nome")
    public ResponseEntity<LocalResponseDTO> buscarPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(service.buscarLocalPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<LocalResponseDTO>> listarLocais() {
        return ResponseEntity.ok(service.listarLocais());
    }

    @PutMapping
    public ResponseEntity<LocalResponseDTO> editarLocal(@RequestParam String id,@Valid @RequestBody LocalRequestDTO request) {
        
        return ResponseEntity.ok(service.editarLocal(id, request));
        
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPorId(@RequestParam("id") String id) {
        service.excluirLocalPorId(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name")
    public ResponseEntity<Void> excluirPorNome(@RequestParam("name") String name) {
        service.excluirLocalPorNome(name);
        return ResponseEntity.ok().build();
    }

}

package br.com.municipio.vacinas.vacinas_api.controller;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.service.CampanhaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/campanha")
public class CampanhaController {

    private final CampanhaService service;

    @PostMapping
    public ResponseEntity<CampanhaResponseDTO> criar (@RequestBody @Valid CampanhaRequestDTO request) {
        return ResponseEntity.status((HttpStatus.CREATED)).body(service.criarCampanha(request));
    }

    @GetMapping("/{localId}")
    public ResponseEntity<List<CampanhaResponseDTO>> buscarPorLocalId(@PathVariable String localId) {
        return ResponseEntity.ok(service.buscarPorLocalId(localId));
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampanhaResponseDTO> atualizarCampanha(@PathVariable String id,
                                                                 @RequestBody CampanhaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.atualizarCampanha(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCampanha (@PathVariable String id) {
        service.excluirCampanha(id);
        return ResponseEntity.noContent().build();
    }
}

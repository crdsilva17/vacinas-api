package br.com.municipio.vacinas.vacinas_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.municipio.vacinas.vacinas_api.service.VacinaService;
import jakarta.validation.Valid;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vacinas")
public class VacinaController {

    private final VacinaService service;

    @PostMapping
    public ResponseEntity<VacinaResponseDTO> cadastrarVacina(@Valid @RequestBody VacinaRequestDTO request) {
        return ResponseEntity.ok(service.cadastrarVacina(request));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<List<VacinaResponseDTO>> buscarPorNome(@PathVariable("nome") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<VacinaResponseDTO>> buscarVacinas() {
        return ResponseEntity.ok(service.buscarVacinas());
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPorId(@RequestParam("id") String id){
        service.excluirVacinaPorId(id);
        return ResponseEntity.noContent().build();
    }


}

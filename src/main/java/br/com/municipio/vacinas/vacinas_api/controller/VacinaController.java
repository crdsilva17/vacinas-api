package br.com.municipio.vacinas.vacinas_api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;
import br.com.municipio.vacinas.vacinas_api.service.VacinaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/vacinas")
public class VacinaController {

    private final VacinaService service;

    @GetMapping
    public  List<Vacina> listar() {
        return service.buscarVacinas();

    }

    @GetMapping("/filter")
    public List<Vacina> filtrar(@RequestParam LocalDate data, @RequestParam String local) {
        return service.filtrar(data, local);

    }

    @PostMapping
    public Vacina criar(@RequestBody Vacina vaccine) {
        return service.cadastrarVacina(vaccine);
        
    }

}

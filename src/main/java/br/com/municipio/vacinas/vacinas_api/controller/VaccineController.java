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
import br.com.municipio.vacinas.vacinas_api.service.VaccineService;


@RestController
@RequestMapping("/api/v1/vaccines")
public class VaccineController {

    private final VaccineService service;

    public VaccineController(VaccineService service) {
        this.service = service;
    }

    @GetMapping
    public  List<Vacina> listar() {
        return service.listarTodas();

    }

    @GetMapping("/filter")
    public List<Vacina> filtrar(@RequestParam LocalDate data, @RequestParam String localId) {
        return service.filtrar(data, localId);

    }

    @PostMapping
    public Vacina criar(@RequestBody Vacina vaccine) {
        return service.salvar(vaccine);
        
    }

}

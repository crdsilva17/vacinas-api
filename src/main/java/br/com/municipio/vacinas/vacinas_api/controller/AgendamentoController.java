package br.com.municipio.vacinas.vacinas_api.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.municipio.vacinas.vacinas_api.dto.AgendamentoRequestDTO;
import br.com.municipio.vacinas.vacinas_api.service.AgendamentoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

        private final AgendamentoService service;

        @GetMapping("/horarios")
        public List<LocalTime> horariosDisponiveis(
                        @RequestParam String localId,

                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

                return service.horariosDisponiveis(
                                localId,
                                data);
        }

        @GetMapping("/vacina/{vacinaId}/agendado")
        public boolean verificarAgendamento(
                        Authentication authentication,
                        @PathVariable String vacinaId) {

                return service.possuiAgendamento(
                                authentication.getName(),
                                vacinaId);
        }

        @PostMapping
        public ResponseEntity<Void> agendar(
                        Authentication authentication,

                        @RequestBody AgendamentoRequestDTO dto) {

                service.agendar(
                                authentication.getName(),
                                dto);

                return ResponseEntity.ok()
                                .build();
        }

        @PutMapping("/cancelar/{vacinaId}")
        public ResponseEntity<Void> cancelar(
                        Authentication authentication,
                        @PathVariable String vacinaId) {

                service.cancelar(
                                authentication.getName(),
                                vacinaId);

                return ResponseEntity.ok().build();
        }
}

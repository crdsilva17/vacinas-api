package br.com.municipio.vacinas.vacinas_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.municipio.vacinas.vacinas_api.dto.AgendamentoRequestDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.UsuarioMapper;
import br.com.municipio.vacinas.vacinas_api.model.Agendamento;
import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.model.Vacina;
import br.com.municipio.vacinas.vacinas_api.model.enums.StatusAgendamento;
import br.com.municipio.vacinas.vacinas_api.repository.AgendamentoRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Service
@RequiredArgsConstructor
public class AgendamentoService {

        private static final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

        private final AgendamentoRepository repository;
        private final UsuarioService userRepository;
        private final VacinaRepository vacinaRepository;
        private final LocalRepository localRepository;
        private final UsuarioMapper mapper;
        private final NotificationService notificationService;

        private LocalTime[] parseHorario(
                        String horarioFuncionamento) {

                String[] partes = horarioFuncionamento.split("-");

                LocalTime abertura = LocalTime.parse(
                                partes[0].trim());

                LocalTime fechamento = LocalTime.parse(
                                partes[1].trim());

                return new LocalTime[] {
                                abertura,
                                fechamento
                };
        }

        private List<LocalTime> gerarHorarios(
                        LocalTime abertura,
                        LocalTime fechamento) {

                List<LocalTime> horarios = new ArrayList<>();

                LocalTime atual = abertura;

                while (atual.isBefore(fechamento)) {

                        horarios.add(atual);

                        atual = atual.plusMinutes(30);
                }

                return horarios;
        }

        public List<LocalTime> horariosDisponiveis(
                        String localId,
                        LocalDate data) {

                LocalVacina local = localRepository
                                .findById(localId)
                                .orElseThrow();

                LocalTime[] horario = parseHorario(
                                local.getHorarioFuncionamento());

                List<LocalTime> todos = gerarHorarios(
                                horario[0],
                                horario[1]);

                List<Agendamento> ocupados = repository.findByLocalIdAndData(
                                localId,
                                data);

                Set<LocalTime> horariosOcupados = ocupados.stream()
                                .map(
                                                Agendamento::getHorario)
                                .collect(
                                                Collectors.toSet());

                return todos.stream()
                                .filter(
                                                h -> !horariosOcupados.contains(h))
                                .toList();
        }

        public boolean possuiAgendamento(
                        String userEmail,
                        String vacinaId) {

                Usuario usuario = mapper.toUser(userRepository.getUserByEmail(userEmail));

                return repository
                                .findByUsuarioIdAndVacinaIdAndStatus(
                                                usuario.getId(),
                                                vacinaId,
                                                StatusAgendamento.AGENDADO)
                                .isPresent();
        }

        @Transactional
        public void agendar(
                        String userEmail,
                        AgendamentoRequestDTO dto) {

                Usuario usuario = mapper.toUser(userRepository.getUserByEmail(userEmail));

                Vacina vacina = vacinaRepository
                                .findById(dto.vacinaId())
                                .orElseThrow(() -> new RuntimeException("Vacina não encontrada."));

                LocalVacina local = localRepository
                                .findById(dto.localId())
                                .orElseThrow(() -> new RuntimeException("UBS não encontrada."));

                if (dto.data().isBefore(LocalDate.now())) {
                        throw new RuntimeException(
                                        "Não é permitido agendar datas passadas.");
                }

                LocalTime[] horarioFuncionamento = parseHorario(local.getHorarioFuncionamento());

                LocalTime abertura = horarioFuncionamento[0];
                LocalTime fechamento = horarioFuncionamento[1];

                if (dto.horario().isBefore(abertura)
                                || dto.horario().isAfter(fechamento)) {

                        throw new RuntimeException(
                                        "Horário fora do funcionamento da UBS.");
                }

                boolean ocupado = repository
                                .existsByLocalIdAndDataAndHorarioAndStatus(
                                                dto.localId(),
                                                dto.data(),
                                                dto.horario(),
                                                StatusAgendamento.AGENDADO);

                if (ocupado) {
                        throw new RuntimeException(
                                        "Este horário já foi reservado.");
                }

                Agendamento agendamento = new Agendamento();

                agendamento.setUsuarioId(usuario.getId());

                agendamento.setVacinaId(vacina.getId());

                agendamento.setLocalId(local.getId());

                agendamento.setData(dto.data());

                agendamento.setHorario(dto.horario());

                agendamento.setStatus(
                                StatusAgendamento.AGENDADO);

                agendamento.setCreatedAt(
                                LocalDateTime.now());

                repository.save(
                                agendamento);

                try {

                        notificationService.notifyUser(
                                        usuario.getId(),
                                        "Vacinação agendada",
                                        "Sua vacinação foi agendada para "
                                                        + dto.data()
                                                        + " às "
                                                        + dto.horario()
                                                        + " na UBS "
                                                        + local.getName());

                } catch (Exception e) {

                        log.error(
                                        "Erro ao enviar notificação de agendamento",
                                        e);
                }
        }

        public void cancelar(
                        String userEmail,
                        String vacinaId) {

                Usuario usuario = mapper.toUser(userRepository.getUserByEmail(userEmail));

                Agendamento agendamento = repository
                                .findByUsuarioIdAndVacinaIdAndStatus(
                                                usuario.getId(),
                                                vacinaId,
                                                StatusAgendamento.AGENDADO)
                                .orElseThrow(() -> new RuntimeException(
                                                "Agendamento não encontrado"));

                agendamento.setStatus(
                                StatusAgendamento.CANCELADO);

                repository.save(agendamento);

                try {
                        notificationService.notifyUser(
                                        usuario.getId(),
                                        "Agendamento cancelado",
                                        "Seu agendamento foi cancelado.");
                } catch (Exception e) {
                        log.error("Erro ao enviar notificação de cancelamento", e);
                }
        }
}

package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.CampanhaMapper;
import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import br.com.municipio.vacinas.vacinas_api.model.LocalVacina;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.repository.CampanhaRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import br.com.municipio.vacinas.vacinas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CampanhaService {
    private final CampanhaMapper mapper;
    private final LocalRepository localRepository;
    private final CampanhaRepository repository;
    private final UsuarioRepository usuarioRepository; // <-- Injetado para buscar os usuários da UBS
    private final NotificationService notificationService; // <-- Injetado para enviar os alertas FCM

    /*
     * Realiza a criação de uma nova campanha de Vacinação
     *
     */
    public CampanhaResponseDTO criarCampanha(CampanhaRequestDTO request) {
        CampanhaVacinacao campanhaVacinacao = mapper.toCampanhaVacinacao(request);
        CampanhaResponseDTO response = mapper.toDTO(repository.save(campanhaVacinacao));

        // Dispara a notificação após salvar com sucesso
        notificarUsuariosElegiveisAsync(campanhaVacinacao, "Nova Campanha de Vacinação!");

        return response;
    }

    /*
     * Retorna uma lista com todas as campanhas de vacinação cadastradas
     * por local (Posto de Vacinação).
     *
     */
    public List<CampanhaResponseDTO> buscarPorLocalId(String localId) {
        return mapper.toDTOList(repository.findByLocalIds(localId)
                .orElseThrow(() -> new RuntimeException("Nenhuma campanha encontrada para o local informado.")));
    }

    /*
     * Atualiza os dados de uma campanha de vacinação existente.
     */
    public CampanhaResponseDTO atualizarCampanha(CampanhaRequestDTO request, String id) {
        CampanhaVacinacao campanhaVacinacao = mapper.toCampanhaVacinacao(request);
        campanhaVacinacao.setId(id);
        CampanhaResponseDTO response = mapper.toDTO(repository.save(campanhaVacinacao));

        // Dispara a notificação informando a atualização
        notificarUsuariosElegiveisAsync(campanhaVacinacao, "Campanha de Vacinação Atualizada!");

        return response;
    }

    /**
     * Filtra e envia as notificações em segundo plano para não travar a requisição
     * do app Flutter.
     */
    private void notificarUsuariosElegiveisAsync(CampanhaVacinacao campanha, String tituloPrefixo) {
        new Thread(() -> {
            try {
                System.out.println("DEBUG: Iniciando busca para os locais: " + campanha.getLocalIds());

                // 1. Busca os objetos de locais (retorna a lista com o JSON que vimos no log)
                List<LocalVacina> locaisDaCampanha = localRepository.findAllById(campanha.getLocalIds());

                // 2. Extrai APENAS o nome por extenso de cada posto usando Stream
                List<String> nomesDosPostos = locaisDaCampanha.stream()
                        .map(LocalVacina::getName) // Garanta que o getter do nome na sua classe LocalVacina seja
                                                   // .getName()
                        .toList();

                System.out.println("DEBUG: Nomes dos postos extraídos para a busca: " + nomesDosPostos);

                // 3. Agora sim, busca os usuários passando a lista de nomes puros 
                List<Usuario> usuariosDoPosto = usuarioRepository.findByLocalIdIn(nomesDosPostos);
                System.out.println("DEBUG: Quantidade de usuários encontrados no posto: " + usuariosDoPosto.size());

                // Tratamento preventivo de idades
                int idadeMinima = (campanha.getAgeMin() != null && !campanha.getAgeMin().isEmpty())
                        ? Integer.parseInt(campanha.getAgeMin())
                        : 0;
                int idadeMaxima = (campanha.getAgeMax() != null && !campanha.getAgeMax().isEmpty())
                        ? Integer.parseInt(campanha.getAgeMax())
                        : 130;

                String titulo = tituloPrefixo + " " + campanha.getNome();
                String mensagem = "Uma nova vacina está disponível para a sua faixa etária no seu posto de saúde.";

                // 4. Varre os usuários encontrados
                for (Usuario usuario : usuariosDoPosto) {
                    if (usuario.getDataNscto() == null)
                        continue;

                    int idadeUsuario = Period.between(usuario.getDataNscto(), LocalDate.now()).getYears();

                    if (idadeUsuario >= idadeMinima && idadeUsuario <= idadeMaxima) {
                        notificationService.notifyUser(usuario.getId(), titulo, mensagem);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar notificações da campanha: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /*
     * Permite Deletar uma campanha de vacinação existente.
     */
    public void excluirCampanha(String id) {
        repository.deleteById(id);
    }

    public List<CampanhaResponseDTO> buscarTodos() {
        return mapper.toDTOList(repository.findAll());
    }
}

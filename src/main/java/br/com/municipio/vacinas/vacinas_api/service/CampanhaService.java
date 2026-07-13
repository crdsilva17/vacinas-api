package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.CampanhaMapper;
import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import br.com.municipio.vacinas.vacinas_api.model.Usuario;
import br.com.municipio.vacinas.vacinas_api.repository.CampanhaRepository;
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

                // LOG DE DIAGNÓSTICO: Verifique no console do Railway se os IDs aparecem aqui
                System.out.println("DEBUG: Iniciando busca para os locais: " + campanha.getLocalIds());

                // 1. Busca todos os usuários que pertencem aos locais (UBSs) da campanha
                // Se no banco localIds for uma lista, use um método 'In' no repositório.
                List<Usuario> usuariosDoPosto = usuarioRepository.findByLocalIdIn(campanha.getLocalIds());

                System.out.println("DEBUG: Quantidade de usuários encontrados no posto: " + usuariosDoPosto.size());

                // Tratamento preventivo caso as idades venham vazias ou nulas da tela do
                // Flutter
                int idadeMinima = (campanha.getAgeMin() != null && !campanha.getAgeMin().isEmpty())
                        ? Integer.parseInt(campanha.getAgeMin())
                        : 0;
                int idadeMaxima = (campanha.getAgeMax() != null && !campanha.getAgeMax().isEmpty())
                        ? Integer.parseInt(campanha.getAgeMax())
                        : 130;

                String titulo = tituloPrefixo + " " + campanha.getNome();
                String mensagem = "Uma nova vacina está disponível para a sua faixa etária no seu posto de saúde.";

                // 2. Filtra pela faixa etária calculando a idade baseada no nascimento
                for (Usuario usuario : usuariosDoPosto) {
                    if (usuario.getDataNscto() == null)
                        continue;

                    // Calcula a idade do usuário hoje
                    int idadeUsuario = Period.between(usuario.getDataNscto(), LocalDate.now()).getYears();

                    // 3. Se estiver na faixa, aciona o NotificationService para mandar o Push FCM
                    if (idadeUsuario >= idadeMinima && idadeUsuario <= idadeMaxima) {
                        notificationService.notifyUser(usuario.getId(), titulo, mensagem);
                    }
                }
            } catch (Exception e) {
                // Loga o erro para evitar que a Thread derrube a API principal
                System.err.println("Erro ao processar notificações da campanha: " + e.getMessage());
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

package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.CampanhaMapper;
import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import br.com.municipio.vacinas.vacinas_api.repository.CampanhaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CampanhaService {
    private final CampanhaMapper mapper;
    private final CampanhaRepository repository;

    /*
     * Realiza a criação de uma nova campanha de Vacinação
     *
     */
    public CampanhaResponseDTO criarCampanha(CampanhaRequestDTO request) {
        CampanhaVacinacao campanhaVacinacao = mapper.toCampanhaVacinacao(request);
        return mapper.toDTO(repository.save(campanhaVacinacao));
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
        return mapper.toDTO(repository.save(campanhaVacinacao));
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

package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.dto.CampanhaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.CampanhaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.mapper.CampanhaMapper;
import br.com.municipio.vacinas.vacinas_api.model.CampanhaVacinacao;
import br.com.municipio.vacinas.vacinas_api.repository.CampanhaRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CampanhaService {
    private CampanhaVacinacao campanhaVacinacao;
    private CampanhaMapper mapper;
    private CampanhaRepository repository;

    /*
    *   Realiza a criação de uma nova campanha de Vacinação
    *
    */
    public CampanhaResponseDTO criarCampanha (CampanhaRequestDTO request) {
        campanhaVacinacao = mapper.toCampanhaVacinacao(request);
        return mapper.toDTO(repository.save(campanhaVacinacao));
    }

    /*
     *  Retorna uma lista com todas as campanhas de vacinação cadastradas
     * por local (Posto de Vacinação).
     *
     */
    public List<CampanhaResponseDTO> buscarPorLocalId(String localId) {
        return List.of(mapper.toDTO(repository.findByLocalId(localId)));
    }

    /*
    *   Atualiza os dados de uma campanha de vacinação existente.
     */
    public CampanhaResponseDTO atualizarCampanha(CampanhaRequestDTO request, String id) {
        campanhaVacinacao = mapper.toCampanhaVacinacao(request);
        campanhaVacinacao.setId(id);
        return mapper.toDTO(repository.save(campanhaVacinacao));
    }

    /*
    *   Permite Deletar uma campanha de vacinação existente.
     */
    public void excluirCampanha(String id) {
        repository.deleteById(id);
    }
}

package br.com.municipio.vacinas.vacinas_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import lombok.RequiredArgsConstructor;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;
import br.com.municipio.vacinas.vacinas_api.mapper.VacinaMapper;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;

/*
    * A classe VacinaService foi implementada para fornecer os serviços relacionados às vacinas, como cadastro, edição, exclusão e busca de vacinas. 
    * Ela utiliza os repositórios para acessar os dados das vacinas, lotes e locais, e o mapper para converter entre as entidades e os DTOs. 
    * Os métodos da classe lidam com as regras de negócio relacionadas às vacinas, como verificar a existência de vacinas com as mesmas características, atualizar as informações dos lotes associados às vacinas e garantir a integridade dos dados ao excluir uma vacina.
*/
@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository repository;
    private final VacinaMapper mapper;

    /*
     * O método cadastrarVacina foi implementado para permitir o cadastro de uma
     * nova vacina.
     * Ele recebe os dados da vacina a ser cadastrada e verifica se já existe uma
     * vacina com as mesmas características (nome, lote, fabricante e data de
     * fabricação).
     * Se já existir, uma exceção é lançada. 
     * Em seguida, o método verifica se o lote da vacina já existe. Se não existir,
     * um novo lote é criado e salvo no repositório.
     * Por fim, a nova vacina é salva no repositório e retornada como resposta.
     */
    public VacinaResponseDTO cadastrarVacina(VacinaRequestDTO request) {

        if (repository.existsByNomeAndLoteAndFabricanteAndDataFabricacao(
                request.getNome(), request.getLote(), request.getFabricante(),
                request.getDataFabricacao())) {
            throw new RuntimeException(
                    "Já existe uma vacina com esse nome, lote e fabricante!");
        }
        try {
            return mapper.toDTO(repository.save(mapper.toEntity(request)));

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Vacina com mesmo nome e lote já existe!");
        }
    }

    /*
     * O método editarVacina foi implementado para permitir a edição de uma vacina
     * existente.
     * Ele recebe os dados atualizados da vacina e o ID da vacina a ser editada.
     * Por fim, a vacina editada é salva no repositório e retornada como resposta.
     */
    public VacinaResponseDTO editarVacina(VacinaRequestDTO request, String id) {
        Vacina vacina = mapper.toEntity(request);
        vacina.setId(id);
        return mapper.toDTO(repository.save(vacina));
    }

    /*
     * O método excluirVacinaPorId foi implementado para excluir uma vacina com base
     * no seu ID.
     * Ele verifica se a vacina existe e, em seguida, remove a vacina do
     * repositório.
     */
    public void excluirVacinaPorId(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Vacina não encontrada!");
        }
        repository.deleteById(id);
    }

    /*
     * O método buscarVacinas foi implementado para retornar uma lista com todas as
     * vacinas cadastradas.
     */
    public List<VacinaResponseDTO> buscarVacinas() {
        return mapper.toDTOList(repository.findAll());
    }

    /*
     * O método buscarPorId foi implementado para retornar uma vacina com base no
     * seu ID.
     */
    public VacinaResponseDTO buscarPorId(String id) {
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Vacina não encontrada!")));
    }

    /*
     * O método buscarPorNome foi implementado para retornar uma lista com as
     * vacinas cadastradas com um nome específico.
     */
    public List<VacinaResponseDTO> buscarPorNome(String nome) {
        return mapper.toDTOList(repository.findByNome(nome));
    }

}

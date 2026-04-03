package br.com.municipio.vacinas.vacinas_api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;

import lombok.RequiredArgsConstructor;

import br.com.municipio.vacinas.vacinas_api.repository.LocalRepository;
import br.com.municipio.vacinas.vacinas_api.repository.LoteRepository;

import br.com.municipio.vacinas.vacinas_api.model.Vacina;
import br.com.municipio.vacinas.vacinas_api.mapper.VacinaMapper;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaRequestDTO;
import br.com.municipio.vacinas.vacinas_api.dto.VacinaResponseDTO;
import br.com.municipio.vacinas.vacinas_api.repository.VacinaRepository;

import br.com.municipio.vacinas.vacinas_api.model.Lote;

/*
    * A classe VacinaService foi implementada para fornecer os serviços relacionados às vacinas, como cadastro, edição, exclusão e busca de vacinas. 
    * Ela utiliza os repositórios para acessar os dados das vacinas, lotes e locais, e o mapper para converter entre as entidades e os DTOs. 
    * Os métodos da classe lidam com as regras de negócio relacionadas às vacinas, como verificar a existência de vacinas com as mesmas características, atualizar as informações dos lotes associados às vacinas e garantir a integridade dos dados ao excluir uma vacina.
*/
@Service
@RequiredArgsConstructor
public class VacinaService {

    private final VacinaRepository repository;
    private final LocalRepository localRepository;
    private final LoteRepository loteRepository;
    private final VacinaMapper mapper;

    /*
        * O método cadastrarVacina foi implementado para permitir o cadastro de uma nova vacina. 
        * Ele recebe os dados da vacina a ser cadastrada e verifica se já existe uma vacina com as mesmas características (nome, lote, local, fabricante e data de fabricação). 
        * Se já existir, uma exceção é lançada. Caso contrário, o método verifica se o local de vacinação existe e, se não existir, lança outra exceção. 
        * Em seguida, o método verifica se o lote da vacina já existe. Se não existir, um novo lote é criado e salvo no repositório. 
        * Se o lote já existir, o método atualiza as informações do lote para incluir a nova vacina e fabricante associados a ele. 
        * Por fim, a nova vacina é salva no repositório e retornada como resposta.    
    */
    public VacinaResponseDTO cadastrarVacina(VacinaRequestDTO request) {

        if (repository.existsByNomeAndLoteAndLocalAndFabricanteAndDataFabricacao(
                request.getNome(), request.getLote(), request.getLocal(), request.getFabricante(), request.getDataFabricacao())) {
            throw new RuntimeException("Já existe uma vacina com esse nome, lote e fabricante para esse Posto de Saúde!");
        }
        try {

            if (!localRepository.existsByName(request.getLocal())) {

                throw new RuntimeException("Posto de Saúde não encontrado! Por favor, cadastre o Posto de Saúde antes de cadastrar a vacina.");
            }

            /*
                O bloco de código dentro do if foi implementado para lidar com a situação em que o lote da vacina não existe. 
                Ele cria um novo lote com as informações fornecidas na requisição, incluindo o número do lote, o fabricante e a vacina associada. 
                Em seguida, o novo lote é salvo no repositório. 
                Se o lote já existir, o método atualiza as informações do lote para incluir a nova vacina e fabricante associados a ele, se necessário, e salva as alterações no repositório.
            */
            if (!loteRepository.existsByNumeroLote(request.getLote())) {
                
                Lote lote = new Lote();
                lote.setNumeroLote(request.getLote());
                lote.setFabricante(Arrays.asList(request.getFabricante()));
                lote.setVacinasAssociadas(Arrays.asList(request.getNome()));
                loteRepository.save(lote);  
            } else {
                /*
                O bloco de código dentro do else foi implementado para lidar com a situação em que o lote da vacina já existe. 
                Ele recupera o lote existente do repositório e atualiza as informações do lote para incluir a nova vacina e fabricante associados a ele, se necessário. 
                Em seguida, as alterações no lote são salvas no repositório.
                */
                Lote lote = loteRepository.findByNumeroLote(request.getLote()).orElseThrow(null);
                List<String> vacinasAssociadas = lote.getVacinasAssociadas();
                List<String> fabricantes = lote.getFabricante();
                if (!vacinasAssociadas.contains(request.getNome())) {
                    vacinasAssociadas.add(request.getNome());
                    lote.setVacinasAssociadas(vacinasAssociadas);
                    
                }
                if (!fabricantes.contains(request.getFabricante())) {
                    fabricantes.add(request.getFabricante());
                    lote.setFabricante(fabricantes);
                }
                loteRepository.save(lote);
            }

            return mapper.toDTO(repository.save(mapper.toEntity(request)));

        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Vacina com mesmo nome e lote já existe!");
        }
    }

    /*
        * O método editarVacina foi implementado para permitir a edição de uma vacina existente. 
        * Ele recebe os dados atualizados da vacina e o ID da vacina a ser editada. 
        * O método verifica se a vacina existe e, em seguida, compara o lote atual da vacina com o novo lote fornecido. 
        * Se os lotes forem iguais, o método atualiza as informações do lote para incluir a nova vacina e fabricante associados a ele, se necessário. 
        * Se os lotes forem diferentes, o método salva o novo lote e atualiza as informações do lote antigo para remover a associação com a vacina editada, se necessário. 
        * Por fim, a vacina editada é salva no repositório e retornada como resposta.
    */
    public VacinaResponseDTO editarVacina(VacinaRequestDTO request, String id) {
        Vacina vacina = repository.findById(id).orElseThrow(
            () -> new RuntimeException("Vacina não encontrada!")
        );
        String loteAtual = vacina.getLote();
        String loteNovo = request.getLote();
        
        Lote lote = loteRepository.findByNumeroLote(loteNovo).orElseGet(
            () -> new Lote(null, loteNovo, Arrays.asList(request.getFabricante()), Arrays.asList(request.getNome()))
        );

        if (loteAtual.equals(loteNovo)){
            // Se os lotes forem iguais, o método atualiza as informações do lote para incluir a nova vacina e fabricante associados a ele, se necessário.
            if (atualizaFabricante(lote, loteAtual, request, vacina)){
                if (!lote.getFabricante().contains(request.getFabricante()))
                    lote.getFabricante().add(request.getFabricante());
            }
            if (atualizarVacina(lote, loteAtual, request, vacina)){
                if (!lote.getVacinasAssociadas().contains(request.getNome()))
                    lote.getVacinasAssociadas().add(request.getNome());
            }

            loteRepository.save(lote);

        } else {
            /*
                O bloco de código dentro do else foi implementado para lidar com a situação em que o lote da vacina está sendo alterado. 
                Ele salva o novo lote no repositório e, em seguida, atualiza as informações do lote antigo para remover a associação com a vacina editada, se necessário. 
                Por fim, ele salva as alterações no repositório.
            */
            loteRepository.save(lote);
            Lote l = loteRepository.findByNumeroLote(loteAtual).orElse(null);
            atualizaFabricante(l, loteAtual, request, vacina);
            atualizarVacina(l, loteAtual, request, vacina);
            loteRepository.save(l);
            
            if (repository.findByLote(loteAtual).isEmpty()){
                loteRepository.deleteByNumeroLote(loteAtual);
            }
        }
        vacina = mapper.toEntity(request);
        vacina.setId(id);
        return mapper.toDTO(repository.save(vacina));
    }



    /*
        * O método excluirVacinaPorId foi implementado para excluir uma vacina com base no seu ID. 
        * Ele verifica se a vacina existe e, em seguida, remove a vacina do repositório. 
        * Além disso, ele também verifica se o lote associado à vacina possui outras vacinas ou fabricantes associados. 
        * Se não houver mais vacinas ou fabricantes associados ao lote, o lote é excluído do repositório.
    */
    public void excluirVacinaPorId(String id) {
        Vacina vacina = repository.findById(id).orElseThrow(
            () -> new RuntimeException("Vacina não encontrada!")
        );

        Lote lote = loteRepository.findByNumeroLote(vacina.getLote()).orElseThrow(
            () -> new RuntimeException("Lote não encontrado!")
        );

        List<Vacina> vacinas = repository.findByLote(vacina.getLote());
        boolean flag = false;
        /* 
            O loop for foi implementado para verificar se existem outras vacinas associadas ao mesmo fabricante e lote da vacina que está sendo excluída. 
            Ele percorre a lista de vacinas e verifica se há alguma vacina com o mesmo fabricante e lote, mas com um ID diferente da vacina que está sendo excluída. 
            Se encontrar uma vacina com essas características, a variável flag é definida como true e o loop é interrompido. 
            Caso contrário, a variável flag permanece como false, indicando que não há outras vacinas associadas ao mesmo fabricante e lote.
        */
        for (Vacina v : vacinas) {
            if (!v.getId().equals(vacina.getId()) && v.getFabricante().equals(vacina.getFabricante())) {
                flag = true;
                break;
            }
        }

        if (!flag){// Se não houver outras vacinas associadas ao mesmo fabricante e lote, o fabricante é removido da lista de fabricantes do lote.
            lote.getFabricante().remove(vacina.getFabricante());
        }

        flag = false;
        /* 
            O loop for foi implementado para verificar se existem outras vacinas associadas ao mesmo lote e nome da vacina que está sendo excluída. 
            Ele percorre a lista de vacinas e verifica se há alguma vacina com o mesmo lote e nome, mas com um ID diferente da vacina que está sendo excluída. 
            Se encontrar uma vacina com essas características, a variável flag é definida como true e o loop é interrompido. 
            Caso contrário, a variável flag permanece como false, indicando que não há outras vacinas associadas ao mesmo lote e nome.
        */
        for (Vacina v : vacinas) {
            if (!v.getId().equals(vacina.getId()) && v.getNome().equals(vacina.getNome())){
                flag = true;
                break;
            }
        }

        if (!flag){// Se não houver outras vacinas associadas ao mesmo lote e nome, o nome da vacina é removido da lista de vacinas associadas do lote.
            lote.getVacinasAssociadas().remove(vacina.getNome());
        }

        if (lote.getFabricante().isEmpty() && lote.getVacinasAssociadas().isEmpty()) {
            loteRepository.deleteById(lote.getId());
        } else {
            loteRepository.save(lote);
        }

        repository.deleteById(id);
    }

    /*
        * O método buscarVacinas foi implementado para retornar uma lista com todas as vacinas cadastradas.
    */
    public List<VacinaResponseDTO> buscarVacinas() {
        return mapper.toDTOList(repository.findAll());
    }

    /*
        * O método buscarPorLocal foi implementado para retornar uma lista com as vacinas cadastradas em um local específico.
    */
    public List<VacinaResponseDTO> buscarPorLocal(String local){
        return mapper.toDTOList(repository.findByLocal(local));
    }

    /*
        * O método buscarPorLote foi implementado para retornar uma lista com as vacinas cadastradas em um lote específico.
    */
    public List<VacinaResponseDTO> buscarPorLote(String lote){
        return mapper.toDTOList(repository.findByLote(lote));
    }

    /*
        * O método buscarPorNome foi implementado para retornar uma lista com as vacinas cadastradas com um nome específico.
    */
    public List<VacinaResponseDTO> buscarPorNome(String nome){
        return mapper.toDTOList(repository.findByNome(nome));
    }

    /*
        * O método atualizaFabricante foi implementado para atualizar a lista de fabricantes associados a um lote.
    */
    private boolean atualizaFabricante(Lote lote, String loteAtual, VacinaRequestDTO request, Vacina vacina) {
        if (!lote.getFabricante().contains(request.getFabricante()) || !request.getFabricante().equals(vacina.getFabricante())){
            List<Vacina> vacinasList = repository.findAllByFabricante(vacina.getFabricante());
            boolean flag = false;
                
            for (Vacina v : vacinasList) {
                if (v.getLote().equals(loteAtual) && !v.getId().equals(vacina.getId())){
                    flag = true;
                }
            }
                
            if (!flag){
                lote.getFabricante().remove(vacina.getFabricante());
            }
            return true;
        }
        return false;
    }

    /*
        * O método atualizarVacina foi implementado para atualizar a lista de vacinas associadas a um lote.
    */
    private boolean atualizarVacina(Lote lote, String loteAtual, VacinaRequestDTO request, Vacina vacina) {
        if (!lote.getVacinasAssociadas().contains(request.getNome())|| !request.getNome().equals(vacina.getNome())){
            List<Vacina> vacinasList = repository.findByNome(vacina.getNome());
            boolean flag = false;

            for (Vacina v : vacinasList) {
                if (v.getLote().equals(loteAtual) && !v.getId().equals(vacina.getId())) {
                    flag = true;
                    break;
                }
            }

            if (!flag){
                lote.getVacinasAssociadas().remove(vacina.getNome());
            }
            return true;        
        }
        return false;
    }

}

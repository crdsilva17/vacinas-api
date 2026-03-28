package br.com.municipio.vacinas.vacinas_api.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.municipio.vacinas.vacinas_api.model.Notificacao;

public interface NotificacaoRepository extends MongoRepository<Notificacao, UUID> {

}

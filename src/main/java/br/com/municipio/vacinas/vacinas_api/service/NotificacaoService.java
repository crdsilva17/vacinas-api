package br.com.municipio.vacinas.vacinas_api.service;

import br.com.municipio.vacinas.vacinas_api.model.Notificacao;
import br.com.municipio.vacinas.vacinas_api.repository.NotificacaoRepository;

import java.util.Date;

public class NotificacaoService {

    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    public Notificacao enviarNotificacao(String mensagem) {
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(new Date());

        return repository.save(notificacao);
    }

    public void configurarLembretes() {
        // lógica de lembretes
    }

}

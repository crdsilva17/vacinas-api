package br.com.municipio.vacinas.vacinas_api.model.enums;

import lombok.Getter;

@Getter
public enum Doses {
    DUAS_DOSES("duas"),
    TRES_DOSES("tres"),
    QUATRO_DOSES("quatro"),
    VARIAS_DOSES("varias"),
    DOSE_UNICA("unica");

    private String doses;

    Doses(String doses) {
        this.doses = doses;
    }
}

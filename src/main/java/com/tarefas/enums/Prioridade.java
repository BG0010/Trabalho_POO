package com.tarefas.enums;

/**
 * Enum que representa a prioridade de uma tarefa.
 * Cada prioridade tem uma cor associada para visualização.
 */
public enum Prioridade {

    URGENTE("vermelho"),
    MEDIANO("amarelo"),
    NO_PRAZO("verde");

    // Cor associada à prioridade para exibição visual
    private final String cor;

    Prioridade(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }
}

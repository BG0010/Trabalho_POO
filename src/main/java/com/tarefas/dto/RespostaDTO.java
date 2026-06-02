package com.tarefas.dto;

import java.util.List;

/**
 * DTO genérico para padronizar todas as respostas da API.
 *
 * Exemplo de sucesso:
 * { "status": 200, "mensagem": "Tarefa encontrada", "dados": {...} }
 *
 * Exemplo de erro:
 * { "status": 404, "mensagem": "Não encontrada", "erros": [...] }
 */
public record RespostaDTO<T>(
        int status,
        String mensagem,
        T dados,
        List<String> erros
) {

    /** Cria uma resposta de sucesso com dados */
    public static <T> RespostaDTO<T> sucesso(int status, String mensagem, T dados) {
        return new RespostaDTO<>(status, mensagem, dados, null);
    }

    /** Cria uma resposta de erro com lista de erros */
    public static <T> RespostaDTO<T> erro(int status, String mensagem, List<String> erros) {
        return new RespostaDTO<>(status, mensagem, null, erros);
    }

    /** Cria uma resposta de erro simples com uma mensagem */
    public static <T> RespostaDTO<T> erro(int status, String mensagem) {
        return new RespostaDTO<>(status, mensagem, null, List.of(mensagem));
    }
}

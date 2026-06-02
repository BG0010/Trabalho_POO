package com.tarefas.dto;

import com.tarefas.entity.Tarefa;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de saída que representa os dados de uma tarefa na resposta da API.
 * Inclui o campo 'corPrioridade' para facilitar a exibição visual no frontend.
 */
public record TarefaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        Prioridade prioridade,
        String corPrioridade,  // Cor da prioridade: vermelho, amarelo ou verde
        DiaSemana diaSemana,
        LocalDate dataTarefa,
        boolean concluida,
        LocalDateTime dataCriacao
) {

    /**
     * Método estático de fábrica que converte uma entidade Tarefa em DTO.
     *
     * @param tarefa a entidade a ser convertida
     * @return o DTO preenchido com os dados da entidade
     */
    public static TarefaResponseDTO de(Tarefa tarefa) {
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getPrioridade(),
                tarefa.getPrioridade().getCor(), // pega a cor do enum
                tarefa.getDiaSemana(),
                tarefa.getDataTarefa(),
                tarefa.isConcluida(),
                tarefa.getDataCriacao()
        );
    }
}

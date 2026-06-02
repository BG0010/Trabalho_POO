package com.tarefas.dto;

import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO de entrada para criar ou atualizar uma tarefa.
 * Contém todas as validações obrigatórias.
 */
public record TarefaRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "A prioridade é obrigatória")
        Prioridade prioridade,

        @NotNull(message = "O dia da semana é obrigatório")
        DiaSemana diaSemana,

        @NotNull(message = "A data da tarefa é obrigatória")
        @FutureOrPresent(message = "A data da tarefa não pode estar no passado")
        LocalDate dataTarefa
) {}

package com.tarefas.entity;

import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma tarefa no banco de dados.
 * O @Entity indica que esta classe é mapeada para uma tabela no banco.
 */
@Entity
@Table(name = "tarefas")
@Data               // Lombok: gera getters, setters, toString, equals e hashCode
@Builder            // Lombok: permite criar objetos usando o padrão Builder
@NoArgsConstructor  // Lombok: gera construtor sem argumentos (exigido pelo JPA)
@AllArgsConstructor // Lombok: gera construtor com todos os argumentos
public class Tarefa {

    /** Identificador único gerado automaticamente pelo banco */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título da tarefa - obrigatório */
    @Column(nullable = false)
    private String titulo;

    /** Descrição detalhada da tarefa - obrigatória */
    @Column(nullable = false, length = 500)
    private String descricao;

    /** Nível de prioridade: URGENTE, MEDIANO ou NO_PRAZO */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    /** Dia da semana em que a tarefa deve ser realizada */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana diaSemana;

    /** Data em que a tarefa deve ser realizada */
    @Column(nullable = false)
    private LocalDate dataTarefa;

    /** Indica se a tarefa foi concluída */
    @Column(nullable = false)
    private boolean concluida = false;

    /** Data e hora em que a tarefa foi criada no sistema */
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Método executado automaticamente antes de salvar a tarefa pela primeira vez.
     * Define a dataCriacao com o momento atual.
     */
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.concluida = false;
    }
}

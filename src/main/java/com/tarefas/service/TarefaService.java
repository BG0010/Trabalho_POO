package com.tarefas.service;

import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.entity.Tarefa;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import com.tarefas.exception.TarefaNotFoundException;
import com.tarefas.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço que contém todas as regras de negócio das tarefas.
 * O @RequiredArgsConstructor do Lombok gera o construtor com injeção de dependência.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TarefaService {

    // Injeção de dependência por construtor (gerada pelo @RequiredArgsConstructor)
    private final TarefaRepository tarefaRepository;

    /**
     * Cria uma nova tarefa a partir do DTO de entrada.
     *
     * @param dto os dados da nova tarefa
     * @return o DTO de resposta com os dados salvos
     */
    public TarefaResponseDTO criar(TarefaRequestDTO dto) {
        Tarefa tarefa = new Tarefa();
        aplicarDadosDaRequest(tarefa, dto);

        Tarefa salva = tarefaRepository.save(tarefa);
        return TarefaResponseDTO.de(salva);
    }

    /**
     * Lista todas as tarefas cadastradas.
     *
     * @return lista de DTOs de resposta
     */
    public List<TarefaResponseDTO> listarTodas() {
        return tarefaRepository.findAll()
                .stream()
                .map(TarefaResponseDTO::de)
                .toList();
    }

    /**
     * Busca uma tarefa pelo ID.
     *
     * @param id o identificador da tarefa
     * @return o DTO da tarefa encontrada
     * @throws TarefaNotFoundException se a tarefa não existir
     */
    public TarefaResponseDTO buscarPorId(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        return TarefaResponseDTO.de(tarefa);
    }

    /**
     * Atualiza todos os dados de uma tarefa existente.
     *
     * @param id  o identificador da tarefa
     * @param dto os novos dados
     * @return o DTO da tarefa atualizada
     */
    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = buscarEntidadePorId(id);
        aplicarDadosDaRequest(tarefa, dto);

        Tarefa atualizada = tarefaRepository.save(tarefa);
        return TarefaResponseDTO.de(atualizada);
    }

    /**
     * Remove uma tarefa pelo ID.
     *
     * @param id o identificador da tarefa
     */
    public void deletar(Long id) {
        // Verifica se existe antes de deletar
        buscarEntidadePorId(id);
        tarefaRepository.deleteById(id);
    }

    /**
     * Marca uma tarefa como concluída.
     *
     * @param id o identificador da tarefa
     * @return o DTO da tarefa concluída
     */
    public TarefaResponseDTO concluir(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        tarefa.setConcluida(true);
        Tarefa salva = tarefaRepository.save(tarefa);
        return TarefaResponseDTO.de(salva);
    }

    public TarefaResponseDTO iniciar(Long id) {
        Tarefa tarefa = buscarEntidadePorId(id);
        tarefa.setConcluida(false);
        Tarefa salva = tarefaRepository.save(tarefa);
        return TarefaResponseDTO.de(salva);
    }

    /**
     * Filtra tarefas por prioridade.
     *
     * @param prioridade o nível de prioridade
     * @return lista de DTOs filtrada
     */
    public List<TarefaResponseDTO> filtrarPorPrioridade(Prioridade prioridade) {
        return tarefaRepository.findByPrioridade(prioridade)
                .stream()
                .map(TarefaResponseDTO::de)
                .toList();
    }

    /**
     * Filtra tarefas por dia da semana.
     *
     * @param diaSemana o dia da semana
     * @return lista de DTOs filtrada
     */
    public List<TarefaResponseDTO> filtrarPorDiaSemana(DiaSemana diaSemana) {
        return tarefaRepository.findByDiaSemana(diaSemana)
                .stream()
                .map(TarefaResponseDTO::de)
                .toList();
    }

    /**
     * Método auxiliar privado para buscar a entidade Tarefa pelo ID.
     * Lança TarefaNotFoundException se não encontrar.
     */
    private Tarefa buscarEntidadePorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNotFoundException(id));
    }

    /**
     * Atualiza os dados da entidade a partir do DTO de entrada.
     */
    private void aplicarDadosDaRequest(Tarefa tarefa, TarefaRequestDTO dto) {
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setDiaSemana(dto.diaSemana());
        tarefa.setDataTarefa(dto.dataTarefa());
    }
}

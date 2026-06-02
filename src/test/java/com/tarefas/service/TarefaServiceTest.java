package com.tarefas.service;

import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.entity.Tarefa;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import com.tarefas.exception.TarefaNotFoundException;
import com.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do TarefaService.
 * Usa Mockito para simular o repositório sem acessar o banco real.
 */
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Tarefa tarefaExemplo;
    private TarefaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Monta uma tarefa de exemplo para reutilizar nos testes
        tarefaExemplo = Tarefa.builder()
                .id(1L)
                .titulo("Estudar Java")
                .descricao("Revisar conceitos de Spring Boot")
                .prioridade(Prioridade.URGENTE)
                .diaSemana(DiaSemana.SEGUNDA)
                .dataTarefa(LocalDate.now().plusDays(1))
                .concluida(false)
                .dataCriacao(LocalDateTime.now())
                .build();

        requestDTO = new TarefaRequestDTO(
                "Estudar Java",
                "Revisar conceitos de Spring Boot",
                Prioridade.URGENTE,
                DiaSemana.SEGUNDA,
                LocalDate.now().plusDays(1)
        );
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void deveCriarTarefa() {
        // Configura o mock: quando salvar qualquer Tarefa, retorna a tarefa de exemplo
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaExemplo);

        TarefaResponseDTO resultado = tarefaService.criar(requestDTO);

        // Verifica se os dados estão corretos
        assertThat(resultado).isNotNull();
        assertThat(resultado.titulo()).isEqualTo("Estudar Java");
        assertThat(resultado.prioridade()).isEqualTo(Prioridade.URGENTE);
        assertThat(resultado.corPrioridade()).isEqualTo("vermelho"); // URGENTE = vermelho

        // Verifica se o save foi chamado exatamente uma vez
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve listar todas as tarefas")
    void deveListarTodasAsTarefas() {
        when(tarefaRepository.findAll()).thenReturn(List.of(tarefaExemplo));

        List<TarefaResponseDTO> resultado = tarefaService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).titulo()).isEqualTo("Estudar Java");
    }

    @Test
    @DisplayName("Deve buscar uma tarefa pelo ID com sucesso")
    void deveBuscarTarefaPorId() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExemplo));

        TarefaResponseDTO resultado = tarefaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar TarefaNotFoundException quando ID não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tarefaService.buscarPorId(99L))
                .isInstanceOf(TarefaNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa com sucesso")
    void deveAtualizarTarefa() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExemplo));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaExemplo);

        TarefaResponseDTO resultado = tarefaService.atualizar(1L, requestDTO);

        assertThat(resultado).isNotNull();
        verify(tarefaRepository).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve deletar uma tarefa com sucesso")
    void deveDeletarTarefa() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExemplo));
        doNothing().when(tarefaRepository).deleteById(1L);

        assertThatCode(() -> tarefaService.deletar(1L)).doesNotThrowAnyException();

        verify(tarefaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar ID inexistente")
    void deveLancarExcecaoAoDeletarIdInexistente() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tarefaService.deletar(99L))
                .isInstanceOf(TarefaNotFoundException.class);
    }

    @Test
    @DisplayName("Deve marcar uma tarefa como concluída")
    void deveConcluirTarefa() {
        Tarefa tarefaConcluida = Tarefa.builder()
                .id(1L).titulo("Estudar Java").descricao("Desc").prioridade(Prioridade.URGENTE)
                .diaSemana(DiaSemana.SEGUNDA).dataTarefa(LocalDate.now().plusDays(1))
                .concluida(true).dataCriacao(LocalDateTime.now()).build();

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExemplo));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaConcluida);

        TarefaResponseDTO resultado = tarefaService.concluir(1L);

        assertThat(resultado.concluida()).isTrue();
    }

    @Test
    @DisplayName("Deve filtrar tarefas por prioridade")
    void deveFiltrarPorPrioridade() {
        when(tarefaRepository.findByPrioridade(Prioridade.URGENTE)).thenReturn(List.of(tarefaExemplo));

        List<TarefaResponseDTO> resultado = tarefaService.filtrarPorPrioridade(Prioridade.URGENTE);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).prioridade()).isEqualTo(Prioridade.URGENTE);
    }

    @Test
    @DisplayName("Deve filtrar tarefas por dia da semana")
    void deveFiltrarPorDiaSemana() {
        when(tarefaRepository.findByDiaSemana(DiaSemana.SEGUNDA)).thenReturn(List.of(tarefaExemplo));

        List<TarefaResponseDTO> resultado = tarefaService.filtrarPorDiaSemana(DiaSemana.SEGUNDA);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).diaSemana()).isEqualTo(DiaSemana.SEGUNDA);
    }
}

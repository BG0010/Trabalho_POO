# ====================================================================
# Exemplos de Testes da API de Tarefas - PowerShell
# ====================================================================
# Use este arquivo para testar todos os endpoints
# Copie e cole cada seção no PowerShell

# Configure a URL base (mude para 8080 se necessário)
$baseUrl = "http://localhost:8082"

# ====================================================================
# 1. CRIAR TAREFA (POST)
# ====================================================================

Write-Host "=== 1. Criar Tarefa ===" -ForegroundColor Green
$payload = @{
    titulo = "Estudar Spring Boot"
    descricao = "Aprender sobre JPA e banco de dados"
    prioridade = "MEDIANO"
    diaSemana = "TERCA"
    dataTarefa = "2026-06-03"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$baseUrl/tarefas" `
    -Method POST `
    -Headers @{"Content-Type" = "application/json"} `
    -Body $payload

Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# Extrai o ID da tarefa criada (útil para os próximos testes)
$tarefa = ($response.Content | ConvertFrom-Json).dados
$tarefaId = $tarefa.id
Write-Host "ID da tarefa criada: $tarefaId" -ForegroundColor Yellow

# ====================================================================
# 2. LISTAR TODAS AS TAREFAS (GET)
# ====================================================================

Write-Host "`n=== 2. Listar Todas as Tarefas ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 3. BUSCAR TAREFA POR ID (GET)
# ====================================================================

Write-Host "`n=== 3. Buscar Tarefa por ID (ID: $tarefaId) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/$tarefaId" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 4. ATUALIZAR TAREFA (PUT)
# ====================================================================

Write-Host "`n=== 4. Atualizar Tarefa ===" -ForegroundColor Green
$updatePayload = @{
    titulo = "Estudar Spring Boot - IMPORTANTE"
    descricao = "Aprender sobre JPA, banco de dados e REST APIs"
    prioridade = "URGENTE"
    diaSemana = "SEGUNDA"
    dataTarefa = "2026-06-02"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/$tarefaId" `
    -Method PUT `
    -Headers @{"Content-Type" = "application/json"} `
    -Body $updatePayload

Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 5. MARCAR TAREFA COMO CONCLUÍDA (PATCH)
# ====================================================================

Write-Host "`n=== 5. Marcar Tarefa como Concluída ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/$tarefaId/concluir" -Method Patch
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 6. DESMARCAR TAREFA (Retornar para Iniciada) (PATCH)
# ====================================================================

Write-Host "`n=== 6. Marcar Tarefa como Iniciada (Desmarcar) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/$tarefaId/iniciar" -Method Patch
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 7. FILTRAR POR PRIORIDADE (GET)
# ====================================================================

Write-Host "`n=== 7. Filtrar Tarefas por Prioridade (URGENTE) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/prioridade?prioridade=URGENTE" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

Write-Host "`n=== 7b. Filtrar Tarefas por Prioridade (MEDIANO) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/prioridade?prioridade=MEDIANO" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

Write-Host "`n=== 7c. Filtrar Tarefas por Prioridade (NO_PRAZO) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/prioridade?prioridade=NO_PRAZO" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 8. FILTRAR POR DIA DA SEMANA (GET)
# ====================================================================

Write-Host "`n=== 8. Filtrar Tarefas por Dia da Semana (SEGUNDA) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas/dia-semana?diaSemana=SEGUNDA" -Method GET
Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# 9. CRIAR MÚLTIPLAS TAREFAS PARA TESTE
# ====================================================================

Write-Host "`n=== 9. Criar Múltiplas Tarefas ===" -ForegroundColor Green

$tarefas = @(
    @{
        titulo = "Fazer exercício"
        descricao = "30 minutos de corrida"
        prioridade = "MEDIANO"
        diaSemana = "SEGUNDA"
        dataTarefa = "2026-06-02"
    },
    @{
        titulo = "Reunião com cliente"
        descricao = "Apresentar proposta do projeto"
        prioridade = "URGENTE"
        diaSemana = "QUARTA"
        dataTarefa = "2026-06-04"
    },
    @{
        titulo = "Almoço com amigos"
        descricao = "No restaurante da Vila"
        prioridade = "NO_PRAZO"
        diaSemana = "DOMINGO"
        dataTarefa = "2026-06-08"
    }
)

foreach ($tarefa in $tarefas) {
    $payload = $tarefa | ConvertTo-Json
    $response = Invoke-WebRequest -Uri "$baseUrl/tarefas" `
        -Method POST `
        -Headers @{"Content-Type" = "application/json"} `
        -Body $payload
    
    $result = $response.Content | ConvertFrom-Json
    Write-Host "✓ Criada: $($result.dados.titulo) (ID: $($result.dados.id))" -ForegroundColor Cyan
}

# ====================================================================
# 10. LISTAR NOVAMENTE PARA VER TODAS
# ====================================================================

Write-Host "`n=== 10. Listar Todas as Tarefas (Final) ===" -ForegroundColor Green
$response = Invoke-WebRequest -Uri "$baseUrl/tarefas" -Method GET
$todasTarefas = ($response.Content | ConvertFrom-Json).dados
Write-Host "Total de tarefas: $($todasTarefas.Count)" -ForegroundColor Yellow
$todasTarefas | Format-Table -Property id, titulo, prioridade, diaSemana, concluida

# ====================================================================
# 11. DELETAR TAREFA (DELETE) - DESCOMENTE PARA USAR
# ====================================================================

Write-Host "`n=== 11. Deletar Tarefa (descomente se necessário) ===" -ForegroundColor Red
# $response = Invoke-WebRequest -Uri "$baseUrl/tarefas/$tarefaId" -Method DELETE
# Write-Host $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10

# ====================================================================
# RESUMO DE VALIDAÇÕES
# ====================================================================

Write-Host "`n=== RESUMO: Validações do Banco ===" -ForegroundColor Cyan
Write-Host @"
✓ Todas as tarefas estão salvas no banco H2
✓ Prioridades válidas: URGENTE (vermelho), MEDIANO (amarelo), NO_PRAZO (verde)
✓ Dias válidos: SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO
✓ Atualizar altera dados no banco
✓ Concluir/Iniciar atualiza o status no banco
✓ Deletar remove permanentemente do banco
✓ Filtrar busca no banco pela coluna especificada

📊 Acesse o H2 Console em: http://localhost:8082/h2-console
   - JDBC URL: jdbc:h2:mem:tarefasdb
   - User: sa
   - Password: (deixe em branco)
"@

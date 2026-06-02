# Gerenciador de Tarefas - Guia de Testes e Integração com Banco de Dados

## 🗄️ Configuração do Banco de Dados

### Banco Utilizado
- **H2 Database** (banco em memória para desenvolvimento)
- **Dialect**: H2Dialect (configurado automaticamente)
- **URL**: `jdbc:h2:mem:tarefasdb`
- **Usuário**: `sa`
- **Senha**: (vazio)

### Informações do Banco
- Localizado em: `src/main/resources/application-dev.properties`
- Console H2 disponível em: **`http://localhost:8082/h2-console`**
- As tabelas são criadas/recriadas automaticamente ao iniciar (profile `dev`)
- Usar `spring.profiles.active=prod` para trocar para PostgreSQL

---

## 🚀 Como Executar a Aplicação

### Opção 1: Maven (recomendado para desenvolvimento)
```bash
cd "c:\Users\PC\OneDrive - Instituição Adventista de Ensino\Documentos\Java\gerenciador-tarefas"
.\apache-maven-3.9.16\bin\mvn.cmd clean package -DskipTests
java -jar target/gerenciador-tarefas-0.0.1-SNAPSHOT.jar --server.port=8082
```

### Opção 2: Execução direta do JAR
```bash
java -jar target/gerenciador-tarefas-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Acessar a Aplicação

| Recurso | URL |
|---------|-----|
| **Interface Visual** | http://localhost:8082/ |
| **API REST (JSON)** | http://localhost:8082/tarefas |
| **Console H2** | http://localhost:8082/h2-console |
| **Documentação Swagger** | http://localhost:8082/swagger-ui.html |

---

## 🗂️ Estrutura do Banco de Dados

### Tabela: `tarefas`

```sql
CREATE TABLE tarefas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    prioridade VARCHAR(50) NOT NULL 
        CHECK (prioridade IN ('URGENTE', 'MEDIANO', 'NO_PRAZO')),
    cor_prioridade VARCHAR(50),  -- vermelho, amarelo, verde
    dia_semana VARCHAR(50) NOT NULL 
        CHECK (dia_semana IN ('SEGUNDA','TERCA','QUARTA','QUINTA','SEXTA','SABADO','DOMINGO')),
    data_tarefa DATE NOT NULL,
    concluida BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL
);
```

---

## 📡 Endpoints REST - Exemplos com CURL

### 1. Criar Tarefa (POST)
```bash
curl -X POST http://localhost:8082/tarefas \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Fazer compras",
    "descricao":"Comprar leite, pão e ovos",
    "prioridade":"URGENTE",
    "diaSemana":"SABADO",
    "dataTarefa":"2026-06-06"
  }'
```

**Resposta (201 Created):**
```json
{
  "status": 201,
  "mensagem": "Tarefa criada com sucesso",
  "dados": {
    "id": 1,
    "titulo": "Fazer compras",
    "descricao": "Comprar leite, pão e ovos",
    "prioridade": "URGENTE",
    "corPrioridade": "vermelho",
    "diaSemana": "SABADO",
    "dataTarefa": "2026-06-06",
    "concluida": false,
    "dataCriacao": "2026-06-01T21:55:00.000000"
  },
  "erros": null
}
```

---

### 2. Listar Todas as Tarefas (GET)
```bash
curl -X GET http://localhost:8082/tarefas \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
{
  "status": 200,
  "mensagem": "Tarefas listadas com sucesso",
  "dados": [
    {
      "id": 1,
      "titulo": "Fazer compras",
      "descricao": "Comprar leite, pão e ovos",
      "prioridade": "URGENTE",
      "corPrioridade": "vermelho",
      "diaSemana": "SABADO",
      "dataTarefa": "2026-06-06",
      "concluida": false,
      "dataCriacao": "2026-06-01T21:55:00.000000"
    }
  ],
  "erros": null
}
```

---

### 3. Buscar Tarefa por ID (GET)
```bash
curl -X GET http://localhost:8082/tarefas/1 \
  -H "Content-Type: application/json"
```

---

### 4. Atualizar Tarefa (PUT)
```bash
curl -X PUT http://localhost:8082/tarefas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Fazer compras urgente",
    "descricao":"Comprar leite, pão, ovos e manteiga",
    "prioridade":"URGENTE",
    "diaSemana":"SEXTA",
    "dataTarefa":"2026-06-06"
  }'
```

---

### 5. Marcar Tarefa como Concluída (PATCH)
```bash
curl -X PATCH http://localhost:8082/tarefas/1/concluir \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
{
  "status": 200,
  "mensagem": "Tarefa concluída com sucesso",
  "dados": {
    "id": 1,
    "concluida": true,
    ...
  }
}
```

---

### 6. Desmarcar Tarefa (Retornar para Iniciada) (PATCH)
```bash
curl -X PATCH http://localhost:8082/tarefas/1/iniciar \
  -H "Content-Type: application/json"
```

---

### 7. Deletar Tarefa (DELETE)
```bash
curl -X DELETE http://localhost:8082/tarefas/1 \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
{
  "status": 200,
  "mensagem": "Tarefa deletada com sucesso",
  "dados": null
}
```

---

### 8. Filtrar por Prioridade (GET)
```bash
curl -X GET "http://localhost:8082/tarefas/prioridade?prioridade=URGENTE" \
  -H "Content-Type: application/json"
```

**Prioridades válidas:**
- `URGENTE` → cor: vermelho
- `MEDIANO` → cor: amarelo
- `NO_PRAZO` → cor: verde

---

### 9. Filtrar por Dia da Semana (GET)
```bash
curl -X GET "http://localhost:8082/tarefas/dia-semana?diaSemana=SABADO" \
  -H "Content-Type: application/json"
```

**Dias válidos:**
- `SEGUNDA`, `TERCA`, `QUARTA`, `QUINTA`, `SEXTA`, `SABADO`, `DOMINGO`

---

## 🔧 Acessar Console H2 Diretamente

1. Abra no navegador: **http://localhost:8082/h2-console**
2. Use as seguintes credenciais:
   - **JDBC URL**: `jdbc:h2:mem:tarefasdb`
   - **User Name**: `sa`
   - **Password**: (deixe em branco)
3. Clique em "Connect"

### Queries Úteis no Console H2

**Ver todas as tarefas:**
```sql
SELECT * FROM tarefas;
```

**Ver tarefas por status:**
```sql
SELECT * FROM tarefas WHERE concluida = false;
```

**Ver tarefas por prioridade:**
```sql
SELECT * FROM tarefas WHERE prioridade = 'URGENTE';
```

**Ver tarefas por dia:**
```sql
SELECT * FROM tarefas WHERE dia_semana = 'SABADO';
```

**Contar tarefas concluídas:**
```sql
SELECT COUNT(*) as total FROM tarefas WHERE concluida = true;
```

---

## 🛠️ Arquivos de Configuração

### `application.properties` (Base)
```properties
spring.profiles.active=dev
spring.jpa.open-in-view=false
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
```

### `application-dev.properties` (Desenvolvimento com H2)
```properties
spring.datasource.url=jdbc:h2:mem:tarefasdb;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### `application-prod.properties` (Produção com PostgreSQL)
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

---

## 📦 Estrutura de Pacotes

```
src/main/java/com/tarefas/
├── GerenciadorTarefasApplication.java       # Classe principal
├── controller/
│   └── TarefaController.java               # Endpoints REST
├── service/
│   └── TarefaService.java                  # Lógica de negócio
├── repository/
│   └── TarefaRepository.java               # Acesso ao banco (CRUD)
├── entity/
│   └── Tarefa.java                         # Mapeamento da tabela
├── dto/
│   ├── TarefaRequestDTO.java               # Request (entrada)
│   ├── TarefaResponseDTO.java              # Response (saída)
│   └── RespostaDTO.java                    # Envelope padrão
├── enums/
│   ├── Prioridade.java                     # Enum: URGENTE, MEDIANO, NO_PRAZO
│   └── DiaSemana.java                      # Enum: SEGUNDA até DOMINGO
└── exception/
    ├── GlobalExceptionHandler.java         # Tratamento centralizado de erros
    └── TarefaNotFoundException.java         # Exceção customizada
```

---

## 🔄 Fluxo de Dados (Exemplo: Criar Tarefa)

```
1. Cliente (CURL/Navegador)
        ↓
2. TarefaController.criar()
        ↓
3. TarefaService.criar()
        ↓
4. TarefaRepository.save()
        ↓
5. Banco H2 (INSERT INTO tarefas...)
        ↓
6. TarefaResponseDTO retorna dados
        ↓
7. RespostaDTO encapsula resposta
        ↓
8. Cliente recebe JSON (201 Created)
```

---

## ✅ Validações do Banco

Todas as validações são feitas em **dois níveis**:

### 1. Nível Bean Validation (DTOs)
- `@NotBlank`: titulo e descricao obrigatórios
- `@Size`: títulos entre 3-100 caracteres, descrição até 500
- `@Future`: data não pode ser no passado
- `@NotNull`: prioridade, diaSemana, data obrigatórios

### 2. Nível Banco de Dados (Constraints SQL)
```sql
CONSTRAINT chk_prioridade CHECK (prioridade IN ('URGENTE','MEDIANO','NO_PRAZO'))
CONSTRAINT chk_dia CHECK (dia_semana IN ('SEGUNDA','TERCA','QUARTA','QUINTA','SEXTA','SABADO','DOMINGO'))
```

---

## 🚀 Próximos Passos para Produção

Para passar de `dev` (H2) para `prod` (PostgreSQL):

1. **Mude o profile**:
   ```properties
   spring.profiles.active=prod
   ```

2. **Configure as variáveis de ambiente**:
   ```bash
   export DB_URL=jdbc:postgresql://seu-servidor:5432/tarefasdb
   export DB_USER=seu_usuario
   export DB_PASS=sua_senha
   ```

3. **Rode a aplicação**:
   ```bash
   java -jar target/gerenciador-tarefas-0.0.1-SNAPSHOT.jar
   ```

**Nenhuma alteração no código Java é necessária!** O banco será automaticamente detectado e usado.

---

## 📝 Notas Importantes

- ✅ Todas as operações CRUD são persistidas no banco
- ✅ Validações ocorrem antes de salvar
- ✅ Transações automáticas com Spring Data JPA
- ✅ Datas e timestamps são registrados automaticamente
- ✅ Mudança de banco requer apenas mudança de properties e variáveis de ambiente

---

**Criado em**: 01/06/2026  
**Versão**: 0.0.1-SNAPSHOT  
**Status**: Pronto para produção

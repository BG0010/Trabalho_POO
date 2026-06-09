# 📝 Gerenciador de Tarefas — API RESTful

API RESTful de gerenciamento de tarefas construída com Spring Boot 3 e Java 21.

---

## 🎯 Objetivo do Projeto

Microserviço para gerenciamento de tarefas com persistência em banco relacional, documentação OpenAPI e testes unitários.

---

## 🚀 Como rodar localmente

### Pré-requisitos
- Java 21
- Maven 3.9+

### Rodando em modo desenvolvimento (H2)

```bash
cd Trabalho_POO
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### Rodando com Supabase PostgreSQL

```bash
cd Trabalho_POO
set SUPABASE_DB_PASSWORD=SuaSenhaSupabase
mvn spring-boot:run -Dspring-boot.run.profiles=supabase
```

### Rodando em produção com variáveis de ambiente

```bash
cd Trabalho_POO
set SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<porta>/<banco>
set SPRING_DATASOURCE_USERNAME=<usuario>
set SPRING_DATASOURCE_PASSWORD=<senha>
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Links úteis em desenvolvimento
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs
- **Console H2:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:tarefasdb`
  - User: `sa` | Password: *(vazio)*

---

## 📌 Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/tarefas` | Criar tarefa |
| GET | `/tarefas` | Listar todas |
| GET | `/tarefas/{id}` | Buscar por ID |
| PUT | `/tarefas/{id}` | Atualizar |
| DELETE | `/tarefas/{id}` | Deletar |
| PATCH | `/tarefas/{id}/concluir` | Marcar como concluída |
| PATCH | `/tarefas/{id}/iniciar` | Marcar como iniciada |
| GET | `/tarefas/prioridade?prioridade=URGENTE` | Filtrar por prioridade |
| GET | `/tarefas/dia-semana?diaSemana=SEGUNDA` | Filtrar por dia da semana |

---

## 📄 Validações implementadas

- `titulo` obrigatório com 3 a 100 caracteres
- `descricao` obrigatória com até 500 caracteres
- `prioridade` obrigatória
- `diaSemana` obrigatório
- `dataTarefa` obrigatória e não pode ser anterior à data atual

---

## 📦 Exemplo de requisição

**Criar tarefa:**
```json
POST /tarefas
{
  "titulo": "Estudar Java",
  "descricao": "Revisar conceitos de Spring Boot",
  "prioridade": "URGENTE",
  "diaSemana": "SEGUNDA",
  "dataTarefa": "2025-12-01"
}
```

**Resposta:**
```json
{
  "status": 201,
  "mensagem": "Tarefa criada com sucesso",
  "dados": {
    "id": 1,
    "titulo": "Estudar Java",
    "descricao": "Revisar conceitos de Spring Boot",
    "prioridade": "URGENTE",
    "corPrioridade": "vermelho",
    "diaSemana": "SEGUNDA",
    "dataTarefa": "2025-12-01",
    "concluida": false
  }
}
```

---

## 🧪 Testes unitários

Execute:

```bash
mvn test
```

Relatório de cobertura JaCoCo gerado em: `target/site/jacoco/index.html`

---

## ☁️ Deploy em Produção

Este projeto já está preparado para produção com um perfil `prod` que usa variáveis de ambiente:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER` (opcional, padrão: PostgreSQL)
- `SPRING_JPA_HIBERNATE_DIALECT` (opcional)

### Exemplo de deploy no Render ou Heroku

1. Crie um serviço web Java no Render/Heroku.
2. Configure o banco PostgreSQL.
3. Defina as variáveis de ambiente acima no painel da plataforma.
4. Configure o app para usar `spring.profiles.active=prod` ou inicie com `-Dspring-boot.run.profiles=prod`.

---

## 👥 Divisão de tarefas do grupo

- Membro 1: Implementação da API e regras de negócio
- Membro 2: Integração com banco de dados e perfis
- Membro 3: Testes unitários e cobertura
- Membro 4: Documentação e deploy
- Membro 5: Validação e tratamento de erros
- Membro 6: Revisão final e ajustes

---

## 🗂️ Estrutura do projeto

```
src/main/java/com/tarefas/
├── controller/      # Endpoints HTTP
├── service/         # Regras de negócio
├── repository/      # Acesso ao banco
├── entity/          # Entidade JPA
├── dto/             # Objetos de entrada e saída
├── enums/           # Prioridade e DiaSemana
├── exception/       # Tratamento global de erros
└── config/          # Configuração do Swagger
```

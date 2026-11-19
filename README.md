# workflow_es2
Entrega da atividade prática de engenharia de software 2
# Biblioteca Web

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Java 17, Spring Boot 3.2 (Web, Data JPA, Validation, Lombok), Maven |
| Frontend | React 18, React Router 6, Axios, Node.js 20 |
| Banco de dados | PostgreSQL 15 (desenvolvimento), H2 em memória (perfil `test`) |
| Testes | JUnit 5, Mockito (`@WebMvcTest`, `@MockBean`, MockMvc), `@SpringBootTest` |
| DevOps | Git/GitHub, GitHub Actions, Docker e Docker Compose |

## Modelo de dados

`AUTORES (id, nome, nacionalidade, data_nascimento, criado_em)` 1 —— N
`LIVROS (id, titulo, isbn, ano_publicacao, genero, autor_id FK, criado_em)`

## Estrutura de pastas

```
biblioteca-web/
├── backend/        # Spring Boot (model, repository, controller, testes)
├── frontend/       # React (components, services)
├── docker-compose.yml
├── .github/workflows/ci-cd.yml
└── README.md
```

## Como executar

```bash
docker-compose up -d --build
```

- Frontend: http://localhost:3000
- API: http://localhost:8080/api/autores e http://localhost:8080/api/livros
- Swagger: http://localhost:8080/swagger-ui.html

Para desenvolvimento local:

```bash
docker-compose up -d postgres
cd backend && mvn spring-boot:run
cd frontend && npm install && npm start
```

## Como rodar os testes

```bash
cd backend
mvn test                              # todos
mvn test -Dtest="*ControllerTest"     # só unitários
cd ../frontend
npm test -- --watchAll=false --passWithNoTests
```

## Endpoints

| Verbo | URL | Status |
|---|---|---|
| POST | /api/autores | 201 |
| GET | /api/autores | 200 |
| GET | /api/autores/{id} | 200 / 404 |
| PUT | /api/autores/{id} | 200 / 404 |
| DELETE | /api/autores/{id} | 200 / 404 (409 se tiver livros) |
| POST | /api/livros | 201 |
| GET | /api/livros | 200 |
| GET | /api/livros/{id} | 200 / 404 |
| PUT | /api/livros/{id} | 200 / 404 |
| DELETE | /api/livros/{id} | 200 / 404 |
| GET | /api/livros/autor/{autorId} | 200 |

## Divisão de trabalho

| Integrante | Responsabilidade | Branch |
|---|---|---|
| Izadora Galarza | CRUD de Autores (backend, testes e telas) | feature/autores |
| Miguel Pessoa | CRUD de Livros (backend, testes e telas) | feature/livros |


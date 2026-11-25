<div align="center">

# 🎓 Vkurso

### Plataforma moderna de ensino online

Crie, gerencie e acompanhe cursos em uma experiência completa para alunos, instrutores e administradores.

<br>

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-6-3178C6?style=for-the-badge&logo=typescript)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)

</div>

---

## 📚 Sobre o projeto

O **Vkurso** é uma plataforma de e-learning para criação, organização e consumo de cursos online.

A aplicação conecta alunos, instrutores e administradores em um ambiente completo, com gerenciamento de conteúdos, matrículas, progresso e vídeos.

## ✨ Funcionalidades

- 🔐 Cadastro, login e verificação de e-mail
- 👥 Perfis de aluno, instrutor e administrador
- 📖 Criação de cursos, categorias, módulos e aulas
- 📝 Matrícula em cursos
- 📊 Acompanhamento do progresso
- 🎥 Upload e reprodução de vídeos
- 🛠️ Painel administrativo
- 📈 Métricas da plataforma
- 📘 Documentação da API com Swagger

## 🚀 Pontos fortes

- Arquitetura separada entre front-end e back-end
- Autenticação segura com sessão HTTP e Redis
- Controle de acesso por função de usuário
- Armazenamento de vídeos com Cloudflare R2
- URLs temporárias para acesso seguro aos vídeos
- PostgreSQL com migrações controladas pelo Flyway
- Validação de dados e tratamento centralizado de erros
- Ambiente completo com Docker Compose

## 🧰 Tecnologias utilizadas

### Front-end

- React 19
- TypeScript
- Vite
- Tailwind CSS
- React Router
- Axios
- Headless UI
- Heroicons

### Back-end

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Session
- Redis
- PostgreSQL
- Flyway
- Cloudflare R2
- OpenAPI/Swagger
- Maven

### Infraestrutura

- Docker
- Docker Compose
- pgAdmin

## 🗂️ Estrutura do projeto

```text
Vkurso/
├── back/
│   └── vkurso/              # API Spring Boot
├── front/                   # Aplicação React
├── docker-compose.yaml      # Configuração principal
├── docker-compose.dev.yaml  # Desenvolvimento
└── docker-compose.prod.yaml # Produção
```

## ⚙️ Como executar

### Pré-requisitos

- Docker
- Docker Compose
- Git

### Configuração

```bash
git clone <url-do-repositorio>
cd Vkurso
cp .env.example .env
```

Configure as variáveis necessárias no arquivo `.env`.

### Inicialização

```bash
docker compose \
  -f docker-compose.yaml \
  -f docker-compose.dev.yaml \
  --profile local up --build
```

## 🌐 Acessos

| Serviço | Endereço |
|---|---|
| Front-end | http://localhost:5173 |
| API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui/index.html |
| MinIO | http://localhost:9001 |
| pgAdmin | http://localhost:5050 |

## 💻 Desenvolvimento local

### Front-end

```bash
cd front
npm install
npm run dev
```

### Back-end

```bash
cd back/vkurso
./mvnw spring-boot:run
```

## ✅ Validações

```bash
cd front
npm run lint
npm run build
```

```bash
cd back/vkurso
./mvnw test
```

## 🏗️ Arquitetura

```text
┌──────────────────────┐
│      Front-end       │
│   React + TypeScript │
└──────────┬───────────┘
           │ HTTP
           ▼
┌──────────────────────┐
│       Back-end       │
│   Spring Boot API    │
└──────┬───────┬───────┘
       │       │
       ▼       ▼
┌──────────┐ ┌──────────┐
│PostgreSQL│ │  Redis   │
└──────────┘ └──────────┘
           │
           ▼
      ┌─────────┐
      │  MinIO  │
      │ Vídeos  │
      └─────────┘
```

## 📌 Status do projeto

🚧 Em desenvolvimento contínuo.

## 📄 Licença

Este projeto está em desenvolvimento.

# 🐾 LifePet

Sistema web para gerenciamento de pets, consultas, vacinas, medicamentos e histórico veterinário.

## 🚀 Tecnologias

* Java 17+
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Oracle Database
* Flyway
* Swagger/OpenAPI
* Maven
* React + TypeScript

## 🔐 Autenticação

O sistema possui dois perfis:

* **TUTOR** — gerenciamento dos próprios pets e solicitação de consultas.
* **VETERINARIO** — gerenciamento de consultas, medicamentos, vacinas e informações veterinárias.

As rotas são protegidas utilizando **Spring Security + JWT**.

## ⚙️ Como executar

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

A API será executada em:

```text
http://localhost:8080
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

O frontend será executado pelo Vite.

## 📚 Swagger

A documentação da API pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

## 🐾 Principais funcionalidades

* Cadastro e gerenciamento de pets
* Agendamento de consultas
* Confirmação, realização e cancelamento de consultas
* Cadastro e gerenciamento de medicamentos
* Cadastro e acompanhamento de vacinas
* Histórico dos pets
* Autenticação com JWT
* Controle de acesso por perfil
* Validação dos dados
* Persistência com banco de dados
* Versionamento do banco utilizando Flyway

## 🔒 Segurança

A aplicação utiliza JWT para autenticação e Spring Security para controle de acesso às funcionalidades de acordo com o perfil do usuário.

## 👨‍💻 Projeto

**LifePet — Sistema de gerenciamento e cuidados para pets.
Link GitHub - https://github.com/vitor-mendess/lifepet1


## Integrantes

- Vitor Mendes da Silva — RM:565376
- Aguinel Junior — RM: 564857
- Felipe da Silva — RM: 563485
- Henrique Gonçalves — RM 562086
- Leonardo Saavedra — RM: 562229




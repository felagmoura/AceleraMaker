# 📝 Blog Pessoal - Backend

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

> Projeto desenvolvido como parte do programa **Acelera Maker** da [Montreal](https://www.montreal.com.br/), uma iniciativa para formação de desenvolvedores profissionais.

## 📌 Visão Geral

Backend Spring Boot para plataforma de blog pessoal com:

- Autenticação JWT
- Gerenciamento de postagens
- Categorização por temas
- Análise de qualidade com SonarQube

## ✨ Funcionalidades Principais

### 👤 Gerenciamento de Usuários
- **Cadastro de usuários**  
  - Nome, usuário, senha e foto
  - Validação de campos obrigatórios
- **Atualização de perfil**  
  - Modificação de informações básicas
- **Exclusão de conta**  
  - Remoção segura de usuários

### 📝 Gerenciamento de Postagens
- **Criação de posts**  
  - Vinculação automática ao autor e tema
  - Data de criação automática
- **Filtros avançados**  
  - Busca por tema (`/filtro?tema={id}`)
  - Busca por autor (`/filtro?autor={id}`)
- **Edição e exclusão**  
  - Controle de permissões por autor

## 🛠 Tecnologias Utilizadas

### Backend
| Tecnologia          | Finalidade                          | Versão   |
|---------------------|-------------------------------------|----------|
| Java                | Linguagem principal                 | 17+      |
| Spring Boot         | Framework backend                   | 3.x      |
| Spring Security     | Autenticação e autorização          | 6.x      |
| JWT                 | Tokens de acesso                    | 0.11.5   |

### Banco de Dados
| Sistema             | Modelagem                           | Driver   |
|---------------------|-------------------------------------|----------|
| MySQL               | Armazenamento persistente           | 8.0      |
| Spring Data JPA     | Mapeamento ORM                      | 3.x      |

### Qualidade
| Ferramenta          | Uso                                 |
|---------------------|-------------------------------------|
| SonarQube           | Análise estática de código          |
| JUnit 5             | Testes unitários                    |

## 🔍 Estrutura do Projeto
src/
├── main/
│ ├── java/
│ │ └── com/
│ │ └── montreal/
│ │ └── acelera/
│ │ └── blog_pessoal/
│ │ ├── config/ # Configurações e Segurança
│ │ ├── controller/ # Endpoints REST
│ │ ├── dto/ # Objetos de transferência
│ │ ├── model/ # Entidades JPA
│ │ ├── repository/ # Interfaces de dados
│ │ ├── service/ # Lógica de negócio
│ │ └── specifications/ # Especificações para filtrar
│ └── resources/
│ ├── application.properties # Configurações
│ └── sonar-project.properties # SonarQube
└── test/ # Testes automatizados


## 📚 Documentação da API

Acesse a documentação interativa:

- Swagger UI: `http://localhost:8080//swagger-ui/api-docs`

## 🚀 Configuração do Ambiente

### Pré-requisitos
- Java 17+ ([SDKMAN!](https://sdkman.io/) recomendado)
- MySQL 8.0+ ou PostgreSQL
- Maven 3.8+
- Docker (para SonarQube - *Opcional*)

### Banco de Dados
1. Configure o arquivo `application.properties`:
```properties
# Banco de Dados (MySQL exemplo)
spring.datasource.url=jdbc:mysql://localhost:3306/blog_pessoal?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=suasenha

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
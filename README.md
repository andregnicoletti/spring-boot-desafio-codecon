# 💡 Desafio Codecon: 1S vs 3J

Este projeto foi desenvolvido como solução para o desafio técnico da Codecon, disponível em:  
https://github.com/codecon-dev/desafio-1-1s-vs-3j

## 📘 Descrição

O objetivo do desafio é analisar os dados de acesso de usuários e gerar insights úteis para uma plataforma de gestão. A aplicação expõe uma API REST que processa e retorna informações como:

- Usuários com alta pontuação e ativos
- Quantidade de usuários por país
- Indicadores por equipe (total de membros, líderes, projetos concluídos, etc.)
- Volume de logins por dia com filtros configuráveis

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Maven
- JPA (Hibernate)
- H2 (banco em memória)
- Lombok

## 🧪 Como executar o projeto

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Passos para rodar:

```bash
git clone https://github.com/seu-usuario/seu-repo.git
cd seu-repo
./mvnw spring-boot:run
```

A API será iniciada em: [http://localhost:8080](http://localhost:8080)

### Endpoints principais

| Método | Rota                        | Descrição                                       |
|--------|-----------------------------|-------------------------------------------------|
| GET    | `/super-users`              | Lista os usuários com score >= 900 e ativos     |
| GET    | `/top-countries`            | Retorna os países com mais usuários             |
| GET    | `/team-insights`            | Retorna estatísticas agrupadas por equipe       |
| GET    | `/active-users-per-day?min=5` | Mostra o total de logins por dia (mínimo 5)    |

## 🧠 Organização do projeto

O projeto é organizado em camadas:

- `controller` → Camada de entrada (endpoints REST)
- `service` → Lógica de negócio e processamento
- `repository` → Acesso ao banco de dados (JPA)
- `model` → Entidades do domínio

## ✅ Diferenciais da Implementação

- Uso de **streams paralelos** para melhor desempenho em listas grandes
- Agrupamento e ordenação com **Collectors e Map.Entry**
- Código modular e de fácil leitura
- Testes manuais feitos via Postman com dados de exemplo em H2

## 📄 Licença

Este projeto foi desenvolvido para fins de estudo e demonstração técnica.
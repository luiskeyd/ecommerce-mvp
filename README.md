# E-commerce MVP — Diagrams arquiteturais (Mermaid)

Este repositório contém os diagramas arquiteturais (C4 — Nível 1 e Nível 2) do projeto E-commerce MVP no formato Mermaid.

Arquivos incluídos:

- `docs/diagrams/c4-context.mmd` — Contexto (Nível 1)
- `docs/diagrams/c4-container.mmd` — Containers (Nível 2)

---

Diagrama (Contexto) — arquivo: `docs/diagrams/c4-context.mmd`

```mermaid
C4Context
title Contexto (Nível 1) — E-commerce MVP

Person(cliente, "Cliente", "Usuário final que navega, adiciona produtos ao carrinho e finaliza compras")
Person(admin, "Administrador", "Gerencia catálogo, pedidos e configurações do sistema")
System_Ext(pagamento_externo, "Fornecedor de Pagamento (Gateway)", "Serviço de terceiros que processa transações")

System(ecommerce, "E-commerce MVP", "Sistema de comércio eletrônico composto por microserviços para checkout e pagamento")

Rel(cliente, ecommerce, "Usa para buscar produtos, gerenciar carrinho e comprar (Web / Mobile)")
Rel(admin, ecommerce, "Usa para gerenciar catálogo e pedidos")
Rel(ecommerce, pagamento_externo, "Integra para processar transações (API segura)")
```

Diagrama (Containers) — arquivo: `docs/diagrams/c4-container.mmd`

```mermaid
C4Container
title Containers (Nível 2) — E-commerce MVP

Person(cliente, "Cliente", "Usuário final")
Person(admin, "Administrador", "Operador do sistema")
System_Ext(pagamento_externo, "Gateway de Pagamento", "Serviço externo de processamento de cartões e pagamentos")

System_Boundary(ecommerce, "E-commerce MVP") {
  Container(web_app, "Frontend (Web / Mobile)", "Front-end", "Interface do cliente (páginas de catálogo, carrinho, checkout). Pode ser SPA ou app mobile")
  Container(api_gateway, "API Gateway", "Gateway / Reverse Proxy", "Roteia requisições para os microserviços, aplica auth, rate-limit e logging")
  Container(ms_checkout, "ms-checkout", "Microserviço (Java/Spring Boot)", "Gerencia carrinho, criação de pedidos, coordena fluxo de checkout")
  Container(ms_pagamento, "ms-pagamento", "Microserviço (Java/Spring Boot)", "Processa pagamentos, valida respostas do gateway e atualiza status financeiro")
  ContainerDb(db_orders, "Banco de Dados (Pedidos / Catálogo)", "RDBMS (ex: PostgreSQL/MySQL)", "Armazena pedidos, itens, clientes e inventário")
  Container(queue, "Message Broker", "RabbitMQ / Kafka (opcional)", "Fila para eventos assíncronos entre serviços (ex: eventos de pedido, confirmação de pagamento)")
  Container(ci_cd, "CI/CD / Pipeline", "Ferramenta de integração e deploy", "Automação de build, testes e deploy dos microserviços")
}

Rel(cliente, web_app, "Usa")
Rel(admin, web_app, "Usa (painel administrativo)")
Rel(web_app, api_gateway, "HTTP/HTTPS (REST API) - chamadas de frontend para backend")
Rel(api_gateway, ms_checkout, "HTTP/REST — requisições de checkout e pedidos")
Rel(api_gateway, ms_pagamento, "HTTP/REST — requisições de pagamento")
Rel(ms_checkout, db_orders, "CRUD — grava/consulta pedidos e carrinho")
Rel(ms_pagamento, db_orders, "Atualiza status de pagamento / grava transações")
Rel(ms_checkout, queue, "Publica eventos (pedido.criado, pedido.atualizado)")
Rel(ms_pagamento, queue, "Consume eventos (processar pagamento assíncrono) — opcional")
Rel(ms_pagamento, pagamento_externo, "API segura (HTTPS) — solicita autorização/captura de pagamento")
Rel(ci_cd, ms_checkout, "Build / Test / Deploy")
Rel(ci_cd, ms_pagamento, "Build / Test / Deploy")
```

# 🔐 desafio-jwt

API em **Java + Spring Boot** para **validação de tokens JWT**, criada como desafio técnico e estudo de boas práticas, arquitetura limpa e princípios SOLID.

---

## 📘 Índice

1. [Sobre o Projeto](#-sobre-o-projeto)
2. [Objetivo](#-objetivo)
3. [Tecnologias](#-tecnologias)
4. [Como Executar](#-como-executar)
5. [Testes](#-testes)
6. [Endpoints](#-endpoints)
7. [Contrato de Resposta (schema)](#-contrato-de-resposta-schema)
8. [Mensagens/Justificativas esperadas (testes)](#-mensagensjustificativas-esperadas-testes)
9. [Postman / testes manuais](#-postman--testes-manuais)
10. [Arquitetura & SOLID](#-arquitetura--solid)
11. [Melhorias Futuras](#-melhorias-futuras)
12. [Como Contribuir](#-como-contribuir)
13. [Autor](#-autor)
14. [Licença](#-licença)

---

## 🔍 Sobre o Projeto

Este repositório contém uma API simples e modular voltada à **validação de tokens JWT (JSON Web Token)**. A API recebe um JWT via POST, aplica uma série de validações nas claims esperadas e retorna um objeto com o resultado da validação e uma justificativa textual.

O projeto foi feito para demonstrar:
- Boas práticas de arquitetura e organização de código
- Injeção de dependências e separação de responsabilidades
- Aplicação de princípios SOLID
- Testes de integração cobrindo os principais cenários de validação

---

## 🎯 Objetivo

- Validar tokens JWT com regras claras (presença de claims, tipos, limites de tamanho e regras de negócio específicas, como se a seed é primo).
- Devolver um resultado uniforme com campo booleano `valid` e campo `justificativa` descrevendo o porquê da validação.
- Fornecer exemplos e suíte de testes automatizados (integração).

---

## 🛠 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web (REST)
- Auth0 Java JWT (ou biblioteca similar, conforme pom.xml)
- Maven (com Maven Wrapper incluído: `mvnw`)

---

## 🚀 Como Executar

Recomendamos usar o Maven Wrapper fornecido no projeto para garantir a versão do Maven:

1. Clone o repositório:
```bash
git clone https://github.com/majinmagros/desafio-jwt.git
```

2. Acesse o projeto:
```bash
cd desafio-jwt
```

3. Build e execução:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

A API ficará disponível em:
```
http://localhost:8080
```

> Observação: se preferir usar o Maven instalado localmente:
> ```bash
> mvn clean install
> mvn spring-boot:run
> ```

---

## 🧪 Testes

- Os testes de integração estão em: `src/test/java/com/desafio/jwt/integration/JwtControllerIT.java`
- Os tokens de exemplo usados nos testes estão centralizados em: `src/test/java/com/desafio/jwt/constants/ConstantsTest.java`

Executar apenas a suíte de integração (classe específica):
```bash
./mvnw -Dtest=JwtControllerIT test
```

Executar toda a suíte de testes:
```bash
./mvnw test
```

Cobertura (sugestão): adicionar JaCoCo ao `pom.xml` e executar:
```bash
./mvnw test jacoco:report
# relatório em target/site/jacoco/index.html
```

Observações:
- Testes de integração usam MockMvc para simular requisições HTTP contra o contexto Spring boot.
- Para tornar testes menos frágeis, mensagens literais retornadas pelo endpoint (campo `justificativa`) estão atreladas a asserts nos testes — ao alterar textos no controller é necessário atualizar os testes.

---

## 📡 Endpoints

### ✔️ Validar JWT

- Método: POST
- URL: `/jwt/validate`
- Headers: `Content-Type: application/json`

#### Body (exemplo)
```json
{
  "token": "seu_token_jwt_aqui"
}
```

#### Observações sobre códigos HTTP
- Atualmente o endpoint retorna HTTP 200 OK mesmo quando o token é considerado inválido — o resultado da validação é indicado pelo campo `valid` no corpo da resposta (boolean). Essa decisão arquitetural foi adotada para separar status HTTP (transporte) do resultado lógico da validação; documente se preferir retornar 4xx para tokens inválidos.

---

## 📦 Contrato de Resposta (schema)

Exemplo de resposta de sucesso/validação (estrutura real usada nos testes):
```json
{
  "valid": true,
  "justificativa": "Texto explicativo",
  "claims": {
    "Role": "Admin",
    "Name": "Fulano",
    "Seed": "7841"
  }
}
```

Campos:
- `valid`: boolean — se o token passou nas validações aplicadas.
- `justificativa`: string — mensagem com a justificativa do resultado (útil para análise humana e debugging).
- `claims`: objeto — claims extraídas do JWT; as chaves são sensíveis a maiúsculas/minúsculas conforme implementação atual (`Role`, `Name`, `Seed`).

---

## 🔎 Mensagens / Justificativas esperadas (cobertas pelos testes)

Os testes verificam mensagens literais no campo `justificativa`. Abaixo estão as mensagens mais relevantes (mantê-las sincronizadas entre controller e testes é importante):

- "Abrindo o JWT, as informações contidas atendem a descrição." — token válido
- "Abrindo o JWT, a Claim Name possui caracter de números." — Name contém dígitos
- "JWT invalido." — JWT malformado
- "Token vazio ou nulo." — token não fornecido
- "Abrindo o JWT, foi encontrado mais de 3 claims." — mais de 3 claims
- "Alguma claim obrigatória está ausente: Name, Role ou Seed." — claims obrigatórias ausentes
- "Claim Name excede 256 caracteres." — Name maior que 256 chars
- "Claim Role inválida. Permitidos: Admin, Member, External." — Role fora da lista
- "Seed não é um número inteiro válido." — Seed não é inteiro
- "Seed não é um número primo." — Seed não é primo

Recomendação: se o campo `justificativa` for consumido programaticamente por clientes, considere adicionar um campo `errorCode` padronizado (enum) para evitar fragilidade por mudanças textuais.

---

## 🧾 Postman / testes manuais

A Postman Collection pública para este projeto está disponível aqui:

https://go.postman.co/workspace/My-Workspace~2440c7b7-f681-4226-8844-edaaaf68788b/collection/15870896-d4a6aa22-7969-49cc-b247-1b14821a24d5?action=share&source=copy-link&creator=15870896

Como importar:
1. Abra o Postman
2. Clique em "Import"
3. Cole o link acima e importe

Exemplo `curl`:
```bash
curl -X POST http://localhost:8080/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"token":"SEU_TOKEN_AQUI"}'
```

---

## 🧩 Arquitetura & SOLID

O projeto foi estruturado seguindo os princípios:

- Single Responsibility: controllers, services, validators e DTOs têm responsabilidades separadas.
- Open/Closed: validações podem ser estendidas sem modificar implementações existentes (padrão de estratégia).
- Liskov Substitution: abstrações/interfaces permitem substituição de implementações.
- Interface Segregation: interfaces enxutas (por ex., `IJwtValidationService`).
- Dependency Inversion: controllers dependem de abstrações e recebem implementações via injeção.

---

## 🔧 Melhorias Futuras (sugestões priorizadas)

- [ ] Adicionar OpenAPI / Swagger para documentação dos endpoints.
- [ ] Adicionar JaCoCo e criar policy de cobertura no CI.
- [ ] Adicionar GitHub Actions para: build, testes, report de cobertura e análise estática.
- [ ] Transformar justificativas literais em constantes de resposta ou adicionar `errorCode` para clientes programáticos.
- [ ] Gerar tokens de teste dinamicamente nos testes (em vez de constantes "hard-coded") — assinar com chave de teste local.
- [ ] Dockerfile + docker-compose para facilitar deploy/testes locais.
- [ ] Instrumentação básica de observability (Micrometer + Actuator, logs estruturados, trace id).
- [ ] Validar e remover tokens sensíveis do histórico (se houver).

---

## 🤝 Como Contribuir

1. Faça um Fork
2. Crie sua branch:
```bash
git checkout -b minha-feature
```
3. Commit:
```bash
git commit -m "feat: descrição da mudança"
```
4. Push:
```bash
git push origin minha-feature
```
5. Abra um Pull Request

Dica: siga o padrão de commits (conventional commits) para mensagens mais claras.

---

## 👤 Autor

**William Batista Gomes**  
🔗 https://github.com/majinmagros

---

## 📄 Licença

Este projeto está sob licença MIT. Certifique-se de adicionar o arquivo `LICENSE` na raiz com o conteúdo da MIT License se ainda não estiver presente.

---

# ✅ README.md

````markdown
# 🔐 desafio-jwt

API em **Java + Spring Boot** para **validação de tokens JWT**, criada como desafio técnico e estudo de boas práticas, arquitetura limpa e princípios SOLID.

---

## 📘 Índice

1. [Sobre o Projeto](#-sobre-o-projeto)  
2. [Objetivo](#-objetivo)  
3. [Tecnologias](#-tecnologias)  
4. [Como Executar](#-como-executar)  
5. [Endpoints](#-endpoints)  
6. [Arquitetura & SOLID](#-arquitetura--solid)  
7. [Collection do Postman](#-collection-do-postman)  
8. [Melhorias Futuras](#-melhorias-futuras)  
9. [Contribuição](#-como-contribuir)  
10. [Autor](#-autor)  
11. [Licença](#-licença)

---

## 🔍 Sobre o Projeto

Este repositório contém uma API simples e modular voltada à **validação de tokens JWT (JSON Web Token)**.  

Serve como base para demonstrar:

- Boas práticas de arquitetura  
- Baixo acoplamento  
- Alto nível de coesão  
- Extensibilidade  
- Aplicação de princípios SOLID  

A API recebe um JWT, faz a validação e retorna informações relevantes como emissor e data de expiração.

---

## 🎯 Objetivo

- Validar tokens JWT com segurança e clareza.  
- Demonstrar um design limpo e bem abstraído.  
- Servir como ponto de partida para sistemas maiores que utilizem autenticação com JWT.  

---

## 🛠 Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot**  
- **Spring Web (REST)**  
- **Auth0 Java JWT**  
- **Maven**

---

## 🚀 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/majinmagros/desafio-jwt.git
````

2. Acesse o projeto:

   ```bash
   cd desafio-jwt
   ```

3. Compile e execute:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. A API ficará disponível em:

   ```
   http://localhost:8080
   ```

---

## 📡 Endpoints

### ✔️ Validar JWT

**POST** `/jwt/validate`

#### Body:

```json
{
  "token": "seu_token_jwt_aqui"
}
```

#### Resposta (exemplo):

```json
{
  "valid": true,
  "issuer": "issuer_do_token",
  "expiresAt": "2025-01-01T12:00:00"
}
```

---

## 🧩 Arquitetura & SOLID

O projeto foi estruturado seguindo os princípios:

### **S — Single Responsibility**

Cada classe tem uma única responsabilidade:
Controller, Service, DTO, Validator e Exception são bem separados.

### **O — Open/Closed**

É possível adicionar novos validadores de token sem alterar os existentes.

### **L — Liskov Substitution**

O serviço de validação implementa uma interface, permitindo substituição sem quebrar dependências.

### **I — Interface Segregation**

Interfaces enxutas e específicas (ex.: `IJwtValidationService`).

### **D — Dependency Inversion**

O controller depende de uma **abstração**, não da implementação direta.

---

## 📦 Collection do Postman

Para testar facilmente os endpoints da API, você pode usar a **collection oficial**:

👉 **Postman Collection:**
[https://web.postman.co/workspace/My-Workspace~2440c7b7-f681-4226-8844-edaaaf68788b/collection/15870896-d4a6aa22-7969-49cc-b247-1b14821a24d5?action=share&source=copy-link&creator=15870896](https://web.postman.co/workspace/My-Workspace~2440c7b7-f681-4226-8844-edaaaf68788b/collection/15870896-d4a6aa22-7969-49cc-b247-1b14821a24d5?action=share&source=copy-link&creator=15870896)

### Como importar:

1. Abra o Postman
2. Clique em **Import**
3. Cole o link acima
4. Pronto! Já pode testar a requisição `/jwt/validate`

---

## 📚 Melhorias Futuras

* [ ] Endpoint para geração de JWT
* [ ] Refresh Token
* [ ] Spring Security completo
* [ ] Testes unitários e integração
* [ ] Swagger/OpenAPI
* [ ] Dockerfile + docker-compose

---

## 🤝 Como Contribuir

1. Faça um Fork
2. Crie sua branch:

   ```bash
   git checkout -b minha-feature
   ```
3. Commit:

   ```bash
   git commit -m "Minha nova feature"
   ```
4. Push:

   ```bash
   git push origin minha-feature
   ```
5. Abra um Pull Request

---

## 👤 Autor

**William Batista Gomes**
🔗 [https://github.com/majinmagros](https://github.com/majinmagros)

---

## 📄 Licença

Este projeto está sob licença MIT.

```

---


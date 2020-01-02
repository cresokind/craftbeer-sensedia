
# Api para controle de Cervejas Artesanais

## Tecnologias utilizadas:

- Projeto em Spring Boot
- Persistencia no banco de dados usando o Spring Data.
- Banco de dados MySql embbeded e Flyway para migração do db. 
- Testes unitarios com JUnit, H2 e Mockito para evitar alterações no banco de dados.
- Teste de build usando o Travis.
- Documentacao com o Swagger.
- Coleção para teste das requisiçoes em `docs/CraftBeer.postman_collection.json`.
- Utilizado o EhCache para as requisiçoes onde lista todos os dados.

## Inicio do projeto

É necessário criar o banco de dados com as sequinte informacoes

- Banco: MySql
- Usuario: `root`
- Senha: `root`

Script para criar o banco de dados;

```bash
    CREATE DATABASE IF NOT EXISTS `craftbeer_sensedia` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
```

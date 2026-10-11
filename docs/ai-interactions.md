# Interações com o Cursor

## 1

Pedi uma API REST em Java 21 e Spring Boot, com SQLite em memória, Spring Data JPA e Hibernate, para a lista de indicados e vencedores da categoria Pior Filme.

O serviço deveria ler `C:\Users\mateu\Downloads\back-end-ai-test\Movielist.csv` na inicialização. A API deveria devolver o produtor com o maior intervalo entre dois prêmios consecutivos e o que ganhou dois prêmios mais rápido, no formato da página 2. Também pedi Richardson nível 2, somente testes de integração, banco embarcado sem instalação externa, um README e um repositório local chamado `outsera-movie-list-challenge`.

O Cursor leu o CSV e o PDF, confirmou o resultado da lista oficial (Joel Silver, 1990–1991, intervalo 1; Matthew Vaughn, 2002–2015, intervalo 13) e montou o plano da API `GET /producers/award-intervals`.

## 2

Pedi para implementar o plano.

O Cursor criou a pasta do projeto, iniciou o Git e gerou a aplicação em Spring Boot 3.5.16. O gerador público só oferecia Spring Boot 4, então o Maven Wrapper foi copiado à parte. A aplicação carrega o CSV ao subir, grava os filmes no SQLite em memória e expõe o endpoint. Os dois testes de integração passaram. O README foi escrito. O commit inicial não foi feito porque o Git da máquina não tinha nome nem e-mail configurados.

## 3

Pedi para o README explicar como usar outro dataset, substituindo o arquivo ou apontando o caminho desejado.

O Cursor atualizou o README. Dá para trocar `src/main/resources/Movielist.csv` por outro arquivo com o mesmo nome e o mesmo formato, ou subir a aplicação apontando o caminho:

```bat
mvnw.cmd spring-boot:run -- --app.csv-location=file:C:\caminho\dataset.csv
```

O CSV precisa do cabeçalho `year;title;studios;producers;winner` e do separador `;`.
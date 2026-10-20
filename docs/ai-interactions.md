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

## 4

Recebi o retorno de que o sistema fazia mais de seis loops em código para chegar ao resultado. Antes de mexer no código, pedi ao Cursor uma análise estruturada de complexidade e eficiência, com quatro entregas:

- um inventário de todos os loops e streams por classe e método, com o nível de aninhamento e a complexidade de cada um;
- a busca de iterações escondidas: chamadas ao repository dentro de loops (N+1), coleções lazy, `contains` em lista, várias streams sobre a mesma coleção e agregações feitas em memória que poderiam ir para o banco;
- a classificação de cada problema como crítico, médio ou baixo, com base no volume de dados e no número de iterações ou queries;
- refatorações com o código antes e depois e o ganho esperado.

Também defini restrições: não sacrificar legibilidade, manter métodos pequenos, preservar SOLID e a separação entre controller, service e repository, e não alterar o contrato da API.

A análise confirmou o retorno. A cada requisição, o `AwardIntervalService` fazia 8 iterações: 4 loops explícitos, sendo 2 aninhados, e 4 passadas de stream sobre a lista de intervalos. Também apontou que as regex de `splitProducers` eram recompiladas para cada filme. Não havia N+1 nem relacionamentos lazy.

Com base nisso, decidi tirar o cálculo da camada Java e levá-lo para o banco:

- os produtores passaram a ser normalizados na carga do CSV, em uma tabela `movie_producers` (`@ElementCollection` em `Movie`). A separação dos nomes saiu do service e foi para `ProducerNames`, com as regex compiladas uma única vez;
- o `MovieRepository` ganhou duas queries nativas, `findShortestIntervals` e `findLongestIntervals`, que compartilham a mesma CTE. A função de janela `LAG` emparelha cada vitória com a anterior do mesmo produtor, e a query já filtra pelo menor ou maior intervalo e devolve o resultado ordenado, mapeado direto para o record `ProducerInterval`;
- o `AwardIntervalService` ficou com um único método que só monta a resposta a partir das duas consultas.

| | Antes | Depois |
|---|---|---|
| Loops ou streams em Java por requisição | 8 (2 aninhados) | 0 |
| Queries por requisição | 1 | 2 |
| Dados trafegados | Todas as colunas dos filmes vencedores | Só os intervalos que entram na resposta |

O contrato da API não mudou. Validei com os dois testes de integração, o do dataset oficial e o de empates (vários produtores com o mesmo intervalo e o mesmo produtor aparecendo no mínimo e no máximo), e ambos passaram sem alteração.

Trade-offs que aceitei conscientemente:

- uma query a mais por requisição, em vez de juntar mínimo e máximo e separar o resultado em Java;
- o filtro `m.winner = 1` depende de como o SQLite grava booleanos;
- a separação dos nomes dos produtores continua sendo um loop, porque o CSV traz vários produtores numa única string, mas agora roda uma vez na carga dos dados e não a cada requisição.
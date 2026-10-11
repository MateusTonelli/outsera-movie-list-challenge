# outsera-movie-list-challenge

API REST da categoria Pior Filme do Golden Raspberry Awards. Na subida, a aplicação lê `src/main/resources/Movielist.csv` e grava os filmes em um SQLite em memória. Nenhuma instalação de banco é necessária.

## Pré-requisito

- JDK 21

O Maven Wrapper baixa o Maven na primeira execução.

## Rodar a aplicação

Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux ou macOS:

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Usar outro dataset

O arquivo padrão é `src/main/resources/Movielist.csv`, lido pela propriedade `app.csv-location` (`classpath:Movielist.csv`). O CSV precisa do cabeçalho `year;title;studios;producers;winner` e do separador `;`.

Para carregar outra lista, substitua esse arquivo por um CSV com o mesmo nome e o mesmo formato, ou aponte `app.csv-location` para o caminho desejado.

Windows:

```bat
mvnw.cmd spring-boot:run -- --app.csv-location=file:C:\caminho\dataset.csv
```

Linux ou macOS:

```bash
./mvnw spring-boot:run -- --app.csv-location=file:/caminho/dataset.csv
```

O prefixo `file:` indica um arquivo no disco. Com `classpath:`, o arquivo precisa estar em `src/main/resources`.

## Rodar os testes de integração

Windows:

```bat
mvnw.cmd test
```

Linux ou macOS:

```bash
./mvnw test
```

Os testes sobem o contexto Spring, carregam o CSV e chamam o endpoint. `AwardIntervalOfficialDatasetTest` usa a lista oficial. `AwardIntervalTiedDatasetTest` usa `src/test/resources/tied-movielist.csv` para empates e prêmios consecutivos.

## Endpoint

`GET /producers/award-intervals`

Devolve o menor e o maior intervalo entre dois prêmios consecutivos do mesmo produtor. Empates entram todos nas listas. Sem nenhum produtor com dois prêmios, as listas vêm vazias e o status continua `200`.

Exemplo com o CSV oficial:

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

```bat
curl http://localhost:8080/producers/award-intervals
```

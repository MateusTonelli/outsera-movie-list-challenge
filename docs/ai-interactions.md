# Registro de interações com o agente

## Pedido

Criar uma API REST em Java 21 e Spring Boot, com SQLite em memória via Spring Data JPA e Hibernate, para a categoria Pior Filme do Golden Raspberry Awards.

O serviço deve ler `Movielist.csv` na inicialização. O endpoint deve devolver o produtor com o maior intervalo entre dois prêmios consecutivos e o que ganhou dois prêmios no menor intervalo, no formato `min` e `max` com `producer`, `interval`, `previousWin` e `followingWin`.

Restrições pedidas: Richardson nível 2, somente testes de integração, banco embarcado em memória, README com instruções de execução e de teste, e repositório local `outsera-movie-list-challenge`.

## Decisões

- Spring Boot 3.5.16, porque o gerador público já só oferece a linha 4 e a versão 3.5 pedida continua no Maven Central.
- `GET /producers/award-intervals` com status `200` e `application/json`.
- SQLite compartilhado no processo: `jdbc:sqlite:file:${app.datasource-name}?mode=memory&cache=shared`, com uma conexão no pool para o banco em memória não desaparecer.
- Produtores separados por vírgula e por ` and `. O caso `, and ` é normalizado antes da divisão. Intervalos usam vitórias consecutivas do mesmo produtor; dois prêmios no mesmo ano valem intervalo `0`.
- Cada contexto de teste usa um nome próprio de banco em memória, para a lista oficial e o CSV de empates não se misturarem.
- Nenhuma skill pronta do Cursor se aplicava a este serviço. A implementação seguiu a especificação do PDF e o CSV em `Downloads\back-end-ai-test`.

## Verificação

`mvnw.cmd test` executou `AwardIntervalOfficialDatasetTest` e `AwardIntervalTiedDatasetTest` sem falhas. O resultado oficial é Joel Silver (1990–1991, intervalo 1) e Matthew Vaughn (2002–2015, intervalo 13).

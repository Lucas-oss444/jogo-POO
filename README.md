# Gambiarras Orientadas a Objetos

RPG 2D por turnos, em Java, para ensinar programação orientada a objetos a quem nunca programou em Java.

Cada turno de batalha é uma questão de POO respondida em um terminal dentro do jogo: quem acerta ataca, quem erra leva o golpe do inimigo. O jogador atravessa oito áreas, uma para cada conceito, e sua pontuação entra em um ranking local.

> **Em desenvolvimento.** Por enquanto existem as classes base do jogo e seus testes. Ainda não há uma versão jogável.

## O que o jogo ensina

1. Classes e objetos
2. Atributos e métodos
3. Construtores
4. Abstração
5. Encapsulamento
6. Herança
7. Polimorfismo
8. Interfaces

## Tecnologias

Todas gratuitas e de código aberto.

- Java 17 ou mais recente
- Swing e Java2D para a interface
- SQLite, por meio do `sqlite-jdbc`, para o ranking e os saves
- JUnit 5 para os testes
- Maven para o build

O jogo roda inteiro no computador do jogador, sem servidor nem conexão com a internet.

## Como rodar os testes

É preciso ter um JDK 17 ou mais recente (por exemplo, o [Eclipse Temurin](https://adoptium.net/)) e o [Maven](https://maven.apache.org/), com a variável `JAVA_HOME` apontando para o JDK.

```
git clone https://github.com/Lucas-oss444/jogo-POO.git
cd jogo-POO
mvn test
```

## Como gerar o `.jar`

```
mvn package
```

O arquivo é criado na pasta `target`.

## Estrutura

```
src/
  main/
    java/
      app/      ponto de entrada
      modelo/   regras do jogo, sem tela nem banco
      visao/    interface em Swing
      dao/      acesso ao banco
    resources/  textos do jogo
  test/
    java/       testes automatizados
pom.xml
```

## Andamento

- [x] Estrutura do projeto, classes base e testes
- [ ] Batalha no terminal
- [ ] Questões e correção das respostas
- [ ] Pontuação e regras
- [ ] Janela do jogo
- [ ] Terminal dentro do jogo
- [ ] Banco de dados e ranking
- [ ] Saves
- [ ] Primeira fase completa

## Autor

Lucas ([@Lucas-oss444](https://github.com/Lucas-oss444)), projeto de faculdade.

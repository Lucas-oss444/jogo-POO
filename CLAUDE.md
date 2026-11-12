# Gambiarras Orientadas a Objetos

RPG 2D por turnos, em Java, para ensinar programação orientada a objetos a alunos que nunca programaram em Java. Cada turno de batalha é uma questão de POO respondida em um terminal dentro do jogo: acertou, o herói ataca; errou, o inimigo ataca.

O design completo (roteiro, fases, pontuação, itens, quest secreta) está em `DESIGN.md`. Leia esse arquivo antes de trabalhar em qualquer mecânica ou texto do jogo.

## Como trabalhar comigo

- Eu (Lucas) quero escrever o código e aprender no processo. Explique o conceito e o que precisa ser feito, deixe que eu implemente e depois revise o que escrevi, apontando erros e melhorias. Não entregue a implementação pronta a menos que eu peça.
- Quando eu pedir código, ele deve ser enxuto: sem comentários decorativos, linhas separadoras ou extras opcionais.
- Responda em português.
- Em `DESIGN.md`, o que está em "Decidido" não muda sem eu pedir. O que está em "Proposto" são sugestões ainda não confirmadas: pergunte antes de implementar.

## Stack

- Java 17+, sem framework de jogo.
- Interface: Swing + Java2D (`JFrame`, `JPanel`, `paintComponent`).
- Banco: SQLite via JDBC (driver `sqlite-jdbc`), arquivo local. O jogo não usa rede: sem API, servidor ou banco remoto.
- Testes: JUnit 5.
- Build: Maven, na estrutura padrão (`src/main/java`, `src/test/java`, `src/main/resources`). `mvn test` roda os testes; `mvn package` gera o `.jar`. O Maven precisa de `JAVA_HOME` apontando para um JDK 17+.
- Áudio: `javax.sound.sampled` (`Clip`, arquivos WAV).
- Custo zero: só entram ferramentas, bibliotecas, assets e serviços gratuitos. Antes de adicionar qualquer dependência ou asset, conferir a licença. O JDK é o Eclipse Temurin; não usar o Java da Oracle.

## Estrutura

```
src/
  main/
    java/
      app/      Main
      modelo/   regras do jogo, sem tela nem banco
      visao/    Swing (só o package-info por enquanto)
      dao/      acesso ao banco (só o package-info por enquanto)
    resources/  textos, questões, imagens e sons
  test/
    java/       testes JUnit, espelhando os pacotes de main
    resources/
pom.xml
CLAUDE.md
DESIGN.md
```

Regras de arquitetura:

- `modelo` não imprime nada nem conhece Swing ou JDBC. Métodos devolvem valores; quem exibe é `app` ou `visao`.
- Só as classes de `dao` falam com o banco, sempre com `PreparedStatement` e `try-with-resources`. Nunca concatenar entrada do jogador em SQL.
- Atributos privados, sem setters gerados por padrão. O estado muda por métodos com regra, como `receberDano`.
- Questões, falas, menus e mensagens ficam em arquivos por idioma ou no banco, nunca escritos direto no código. O jogo terá português e inglês.
- Todo aviso mostrado ao jogador tem tom humorístico ou imersivo e diz o que fazer em seguida. Nenhuma mensagem técnica na tela.
- O jogo roda em janela e em tela cheia, e não pune nem pausa quando perde o foco: pesquisar fora é permitido. A única restrição é colar no campo de resposta texto que não veio do jogo.
- O sistema de arquivos do terminal do jogo é simulado. Nenhum comando do jogador pode criar, ler ou apagar arquivos reais, nem executar comandos do sistema operacional.
- A pasta com as respostas das questões não pode ser citada no README nem em nenhum texto voltado ao jogador, e as respostas não devem aparecer em logs ou mensagens de erro.

## Padrão de qualidade

É um projeto de faculdade, mas deve ter acabamento profissional. Na prática:

- Git desde o início, com commits pequenos e mensagens claras. Nada de código comentado ou arquivos de teste manual no repositório.
- Código legível: nomes consistentes em português, métodos curtos, sem duplicação, sem números soltos (usar constantes).
- Nenhum aviso do compilador ignorado e nenhuma exceção engolida em `catch` vazio.
- README com descrição, capturas de tela, como rodar, como testar e créditos.
- Créditos e licença de todo asset de terceiros (arte, som, fonte) em um arquivo próprio.
- Integração contínua rodando os testes a cada push, quando o repositório estiver em um serviço que ofereça isso.
- Versões numeradas, cada uma com o `.jar` e o pacote do `jpackage`.
- Uma funcionalidade só entra quando a anterior está completa e polida. Na dúvida entre adicionar e terminar, terminar.

## Testes e robustez

Os testes automatizados são responsabilidade sua (Claude Code): escreva e mantenha os testes mesmo quando o código do jogo for escrito por mim. Nenhuma etapa está concluída enquanto os testes dela não passarem.

Testes (JUnit 5):

- `modelo`: dano, vida nunca negativa, `estaVivo`, ataques de cada classe, cálculo de pontuação, regras de morte e de dificuldade, `Corretor` (cada etapa da correção).
- `dao`: contra um SQLite em memória (`jdbc:sqlite::memory:`), nunca contra o banco real do jogador. Cobrir gravação e leitura de jogador, partida, ranking e saves.
- Saves: salvar e carregar devolve o mesmo estado; limite de 3 saves; save corrompido ou incompleto não derruba o jogo.
- Para cada bug corrigido, um teste que falhava antes da correção.

Toda entrada do jogador (apelido, respostas, comandos do terminal) é não confiável. Testar e tratar, sem travar nem lançar exceção até a tela:

- vazio, só espaços, texto muito longo, quebras de linha, acentos, emojis e caracteres de controle;
- tentativas de SQL injection (`'; DROP TABLE jogador; --`, `' OR '1'='1`), que devem ser gravadas ou recusadas como texto comum;
- comandos inexistentes no terminal do jogo;
- colagem de texto grande no campo de resposta.
- apelidos obscenos, inclusive disfarçados com números, símbolos, acentos e letras repetidas, que devem ser recusados, e nomes legítimos parecidos, que devem ser aceitos.

Banco de dados:

- Só `PreparedStatement`, com `try-with-resources`. Nenhuma query montada por concatenação.
- `PRAGMA foreign_keys = ON` em toda conexão (no SQLite vem desligado).
- Restrições no esquema: `NOT NULL`, `UNIQUE`, `CHECK` (pontuação não negativa, espaço de save entre 1 e 3).
- Gravações com mais de um comando dentro de transação.
- Arquivo do banco ausente é recriado ao iniciar; arquivo corrompido gera aviso ao jogador e um banco novo, preservando o antigo com outro nome.
- Limite de tamanho no apelido, validado antes de chegar ao DAO.

Falhas em geral:

- O jogador nunca vê stack trace. Erros inesperados vão para um arquivo de log e a tela mostra uma mensagem simples.
- Logs e mensagens de erro não contêm respostas das questões.
- Recurso ausente (imagem, som, arquivo de questões ou de textos) não derruba o jogo: usar substituto e registrar no log.
- Alterações de interface Swing acontecem na thread de eventos (`SwingUtilities.invokeLater`).

## Ordem de desenvolvimento

Entrega 1 é a fase 1 completa, do menu ao ranking, em 9 partes. Uma parte só começa quando a anterior está pronta e testada.

| # | Parte | O que entra | Pronta quando |
|---|---|---|---|
| 0 | Fundação | Maven, git, `Personagem`, `Heroi`, `Guerreiro`, `Mago`, `Inimigo` | Compila, testes passam, primeiro commit feito |
| 1 | Batalha no terminal | `Batalha` no `modelo` e `Main` exibindo com textos do arquivo | Um duelo roda até alguém morrer, sem o `modelo` imprimir nada |
| 2 | Questões e correção | `Questao`, `Corretor`, leitura das questões de arquivo; acerto faz o herói atacar, erro faz o inimigo atacar | Uma batalha inteira é decidida por respostas digitadas |
| 3 | Pontuação e regras | Pontos por tentativa, contagem de mortes, modo médio | A partida termina com uma pontuação calculada |
| 4 | Janela | `JFrame`, resolução interna ampliada, laço de jogo, tela de batalha desenhada, tela cheia | A batalha da parte 3 aparece desenhada na janela |
| 5 | Terminal do jogo | Explorador, editor, terminal, comandos, autocomplete, atalhos, bloqueio de colagem | O jogador responde as questões pela interface estilo editor |
| 6 | Banco e ranking | Conexão SQLite, esquema, DAOs, validação de apelido, tela de ranking | A pontuação é gravada e aparece no ranking |
| 7 | Saves | 3 espaços, salvamento manual e automático, carregar pelo menu | Fechar e reabrir o jogo continua de onde parou |
| 8 | Fase 1 de ponta a ponta | Menu, abertura e tutorial, tela de carregamento, guia, boss da Vila da Forja, polimento | Alguém que nunca programou joga do menu ao ranking sem ajuda |

Depois: demais fases e o Isaac; extras (Giroto, modos fácil e difícil, itens, trilha); inglês e `jpackage`.

Rotina de cada parte:

1. Listar o que a parte usa de "Proposto" no `DESIGN.md` e esperar minha confirmação.
2. Explicar o conceito e o que precisa ser escrito; eu implemento, ou peço para você fazer.
3. Revisar e escrever os testes.
4. Fechar com testes passando, "Estado atual" atualizado e um commit.

## Estado atual

Parte 0 concluída e publicada em `https://github.com/Lucas-oss444/jogo-POO` (repositório público; `DESIGN.md` fica fora dele pelo `.gitignore`). Parte 1 (batalha no terminal) escrita. Em `modelo`:

- `Personagem` (abstrata): `nome`, `vida`, `ataque`, getters, `estaVivo()`, `receberDano(int)` e `atacar(Personagem)`. Os dois últimos devolvem o dano realmente sofrido; a vida nunca fica negativa.
- `Heroi` (abstrata, estende `Personagem`): concentra `VIDA_INICIAL`, `DANO_FRACO` e `DANO_FORTE`, iguais para toda classe de herói. `atacar` é o ataque fraco e `atacarForte` o forte. Os valores (100, 10, 25) são provisórios.
- `Guerreiro`, `Mago`: estendem `Heroi` e só definem as chaves de texto dos nomes dos ataques (`ataque.guerreiro.fraco` etc.). Os textos estão em `src/main/resources/textos_pt.properties`.
- `Inimigo`: estende `Personagem`, recebe nome, vida e ataque no construtor.
- `TipoAtaque`: enum `FRACO`/`FORTE`.
- `Batalha`: `registrarAcerto(tipo)` (herói ataca), `registrarErro(tipo)` (inimigo ataca; no forte o dano dobra), `terminou`, `heroiVenceu`, `heroiPerdeu`, `reiniciar`. Turno em batalha encerrada lança `IllegalStateException`.
- `app.Main`: duelo no terminal contra um inimigo de treino. O acerto ainda é simulado por uma pergunta "s/n", que a parte 2 troca pela questão.

Testes: `PersonagemTest`, `HeroiTest`, `BatalhaTest` e `MainTest`, 31 testes passando com `mvn test`.

Parte 1 escrita e testada, aguardando minha revisão e o commit.

Atualize esta seção quando uma etapa for concluída.

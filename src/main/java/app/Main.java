package app;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import modelo.Batalha;
import modelo.Guerreiro;
import modelo.Heroi;
import modelo.Inimigo;
import modelo.TipoAtaque;

public class Main {

    private static final int VIDA_DO_INIMIGO = 60;
    private static final int ATAQUE_DO_INIMIGO = 15;

    private static final String OPCAO_FRACO = "1";
    private static final String OPCAO_FORTE = "2";
    private static final String OPCAO_SIM = "s";
    private static final String OPCAO_NAO = "n";

    private static final ResourceBundle TEXTOS =
            ResourceBundle.getBundle("textos", Locale.forLanguageTag("pt"));

    public static void main(String[] args) {
        executar(new Scanner(System.in), System.out);
    }

    static void executar(Scanner entrada, PrintStream saida) {
        Heroi heroi = new Guerreiro(texto("heroi.nome"));
        Inimigo inimigo = new Inimigo(texto("inimigo.nome"), VIDA_DO_INIMIGO, ATAQUE_DO_INIMIGO);
        Batalha batalha = new Batalha(heroi, inimigo);

        saida.println(texto("batalha.inicio", inimigo.getNome()));
        while (!batalha.heroiVenceu()) {
            saida.println(texto("batalha.vidas",
                    heroi.getNome(), heroi.getVida(), inimigo.getNome(), inimigo.getVida()));

            String ataque = lerOpcao(entrada, saida,
                    texto("batalha.escolha", texto(heroi.chaveAtaqueFraco()), texto(heroi.chaveAtaqueForte())),
                    List.of(OPCAO_FRACO, OPCAO_FORTE));
            if (ataque == null) {
                saida.println(texto("batalha.saida"));
                return;
            }
            TipoAtaque tipo = ataque.equals(OPCAO_FORTE) ? TipoAtaque.FORTE : TipoAtaque.FRACO;

            // Provisório: na parte 2 o acerto passa a vir da resposta de uma questão.
            String acertou = lerOpcao(entrada, saida, texto("batalha.simulacao"), List.of(OPCAO_SIM, OPCAO_NAO));
            if (acertou == null) {
                saida.println(texto("batalha.saida"));
                return;
            }

            if (acertou.equals(OPCAO_SIM)) {
                int dano = batalha.registrarAcerto(tipo);
                saida.println(texto("batalha.acerto", heroi.getNome(), texto(heroi.chaveAtaque(tipo)), dano));
            } else {
                int dano = batalha.registrarErro(tipo);
                saida.println(texto("batalha.erro", inimigo.getNome(), dano));
            }

            if (batalha.heroiPerdeu()) {
                saida.println(texto("batalha.derrota"));
                batalha.reiniciar();
            }
        }
        saida.println(texto("batalha.vitoria", inimigo.getNome()));
    }

    /** Repete a pergunta até receber uma das opções. Devolve null se a entrada acabar. */
    private static String lerOpcao(Scanner entrada, PrintStream saida, String pergunta, List<String> opcoes) {
        saida.println(pergunta);
        while (entrada.hasNextLine()) {
            String resposta = entrada.nextLine().trim().toLowerCase(Locale.ROOT);
            if (opcoes.contains(resposta)) {
                return resposta;
            }
            saida.println(texto("batalha.opcao.invalida"));
            saida.println(pergunta);
        }
        return null;
    }

    static String texto(String chave, Object... valores) {
        return MessageFormat.format(TEXTOS.getString(chave), valores);
    }
}

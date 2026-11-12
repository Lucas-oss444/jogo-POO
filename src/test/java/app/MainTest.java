package app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class MainTest {

    private static final String ACERTO_FORTE = "2\ns\n";
    private static final String ERRO_FRACO = "1\nn\n";
    private static final String NOME_DO_INIMIGO = Main.texto("inimigo.nome");

    private static String jogar(String entrada) {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        Main.executar(new Scanner(entrada), new PrintStream(saida, true, StandardCharsets.UTF_8));
        return saida.toString(StandardCharsets.UTF_8);
    }

    private static String vitoria() {
        return Main.texto("batalha.vitoria", NOME_DO_INIMIGO);
    }

    @Test
    void acertosSeguidosLevamAVitoria() {
        String saida = jogar(ACERTO_FORTE.repeat(3));

        assertTrue(saida.contains(vitoria()));
        assertFalse(saida.contains(Main.texto("batalha.derrota")));
    }

    @Test
    void morrerRecomecaABatalhaEAindaDaParaVencer() {
        String saida = jogar(ERRO_FRACO.repeat(7) + ACERTO_FORTE.repeat(3));

        assertTrue(saida.contains(Main.texto("batalha.derrota")));
        assertTrue(saida.contains(vitoria()));
    }

    @Test
    void entradaInvalidaPedeDeNovoSemQuebrar() {
        String lixo = "banana\n\n   \n3\n'; DROP TABLE jogador; --\n" + "x".repeat(10_000) + "\n\u0000😀\n";

        String saida = jogar(lixo + ACERTO_FORTE.repeat(3));

        assertTrue(saida.contains(Main.texto("batalha.opcao.invalida")));
        assertTrue(saida.contains(vitoria()));
    }

    @Test
    void opcaoAceitaMaiusculaEEspacos() {
        String saida = jogar(" 2 \n S \n".repeat(3));

        assertTrue(saida.contains(vitoria()));
    }

    @Test
    void entradaQueAcabaNoMeioEncerraSemErro() {
        assertTrue(jogar("").contains(Main.texto("batalha.saida")));
        assertTrue(jogar("1\n").contains(Main.texto("batalha.saida")));
    }
}

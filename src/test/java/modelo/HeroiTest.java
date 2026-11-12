package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;

class HeroiTest {

    private static final int VIDA_DO_ALVO = 1000;

    private static List<Heroi> herois() {
        return List.of(new Guerreiro("Ana"), new Mago("Bia"));
    }

    private static Inimigo alvo() {
        return new Inimigo("Alvo", VIDA_DO_ALVO, 1);
    }

    @Test
    void todaClasseComecaComAMesmaVida() {
        for (Heroi heroi : herois()) {
            assertEquals(Heroi.VIDA_INICIAL, heroi.getVida(), heroi.getClass().getSimpleName());
        }
    }

    @Test
    void ataqueFracoCausaOMesmoDanoEmTodaClasse() {
        for (Heroi heroi : herois()) {
            Inimigo alvo = alvo();

            assertEquals(Heroi.DANO_FRACO, heroi.atacar(alvo), heroi.getClass().getSimpleName());
            assertEquals(VIDA_DO_ALVO - Heroi.DANO_FRACO, alvo.getVida());
        }
    }

    @Test
    void ataqueForteCausaOMesmoDanoEmTodaClasse() {
        for (Heroi heroi : herois()) {
            Inimigo alvo = alvo();

            assertEquals(Heroi.DANO_FORTE, heroi.atacarForte(alvo), heroi.getClass().getSimpleName());
            assertEquals(VIDA_DO_ALVO - Heroi.DANO_FORTE, alvo.getVida());
        }
    }

    @Test
    void ataqueForteCausaMaisDanoQueOFraco() {
        assertTrue(Heroi.DANO_FORTE > Heroi.DANO_FRACO);
    }

    @Test
    void cadaClasseTemSeusPropriosNomesDeAtaque() {
        Heroi guerreiro = new Guerreiro("Ana");
        Heroi mago = new Mago("Bia");

        assertNotEquals(guerreiro.chaveAtaqueFraco(), guerreiro.chaveAtaqueForte());
        assertNotEquals(mago.chaveAtaqueFraco(), mago.chaveAtaqueForte());
        assertNotEquals(guerreiro.chaveAtaqueFraco(), mago.chaveAtaqueFraco());
        assertNotEquals(guerreiro.chaveAtaqueForte(), mago.chaveAtaqueForte());
    }

    @Test
    void todoNomeDeAtaqueExisteNoArquivoDeTextos() {
        ResourceBundle textos = ResourceBundle.getBundle("textos", Locale.forLanguageTag("pt"));

        for (Heroi heroi : herois()) {
            assertTrue(textos.containsKey(heroi.chaveAtaqueFraco()), heroi.chaveAtaqueFraco());
            assertTrue(textos.containsKey(heroi.chaveAtaqueForte()), heroi.chaveAtaqueForte());
        }
    }

    @Test
    void inimigoUsaAVidaEOAtaqueRecebidos() {
        Inimigo inimigo = new Inimigo("Bug", 40, 7);
        Heroi heroi = new Guerreiro("Ana");

        assertEquals(40, inimigo.getVida());
        assertEquals(7, inimigo.atacar(heroi));
        assertEquals(Heroi.VIDA_INICIAL - 7, heroi.getVida());
    }
}

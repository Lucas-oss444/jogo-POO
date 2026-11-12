package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PersonagemTest {

    private static final int VIDA = 100;
    private static final int ATAQUE = 10;

    private static class Boneco extends Personagem {
        Boneco() {
            super("Boneco", VIDA, ATAQUE);
        }
    }

    @Test
    void construtorGuardaNomeVidaEAtaque() {
        Personagem boneco = new Boneco();

        assertEquals("Boneco", boneco.getNome());
        assertEquals(VIDA, boneco.getVida());
        assertEquals(ATAQUE, boneco.getAtaque());
    }

    @Test
    void receberDanoReduzAVidaEDevolveOQueFoiPerdido() {
        Personagem boneco = new Boneco();

        int perdido = boneco.receberDano(30);

        assertEquals(30, perdido);
        assertEquals(VIDA - 30, boneco.getVida());
    }

    @Test
    void vidaNuncaFicaNegativa() {
        Personagem boneco = new Boneco();

        int perdido = boneco.receberDano(VIDA + 50);

        assertEquals(VIDA, perdido);
        assertEquals(0, boneco.getVida());
    }

    @Test
    void danoZeroNaoAlteraAVida() {
        Personagem boneco = new Boneco();

        assertEquals(0, boneco.receberDano(0));
        assertEquals(VIDA, boneco.getVida());
    }

    @Test
    void danoNegativoNaoCura() {
        Personagem boneco = new Boneco();

        assertEquals(0, boneco.receberDano(-20));
        assertEquals(VIDA, boneco.getVida());
    }

    @Test
    void estaVivoEnquantoTemVida() {
        Personagem boneco = new Boneco();

        boneco.receberDano(VIDA - 1);

        assertTrue(boneco.estaVivo());
    }

    @Test
    void morreQuandoAVidaChegaAZero() {
        Personagem boneco = new Boneco();

        boneco.receberDano(VIDA);

        assertFalse(boneco.estaVivo());
    }

    @Test
    void atacarCausaDanoNoAlvoENaoNoAtacante() {
        Personagem atacante = new Boneco();
        Personagem alvo = new Boneco();

        int dano = atacante.atacar(alvo);

        assertEquals(ATAQUE, dano);
        assertEquals(VIDA - ATAQUE, alvo.getVida());
        assertEquals(VIDA, atacante.getVida());
    }

    @Test
    void atacarAlvoQuaseMortoDevolveSoAVidaQueRestava() {
        Personagem atacante = new Boneco();
        Personagem alvo = new Boneco();
        alvo.receberDano(VIDA - 3);

        assertEquals(3, atacante.atacar(alvo));
        assertFalse(alvo.estaVivo());
    }
}

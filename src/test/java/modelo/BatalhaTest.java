package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BatalhaTest {

    private static final int VIDA_DO_INIMIGO = 60;
    private static final int ATAQUE_DO_INIMIGO = 15;

    private Heroi heroi;
    private Inimigo inimigo;
    private Batalha batalha;

    @BeforeEach
    void preparar() {
        heroi = new Guerreiro("Ana");
        inimigo = new Inimigo("Bug", VIDA_DO_INIMIGO, ATAQUE_DO_INIMIGO);
        batalha = new Batalha(heroi, inimigo);
    }

    @Test
    void batalhaComecaEmAndamento() {
        assertFalse(batalha.terminou());
        assertFalse(batalha.heroiVenceu());
        assertFalse(batalha.heroiPerdeu());
    }

    @Test
    void acertoComAtaqueFracoFereSoOInimigo() {
        assertEquals(Heroi.DANO_FRACO, batalha.registrarAcerto(TipoAtaque.FRACO));
        assertEquals(VIDA_DO_INIMIGO - Heroi.DANO_FRACO, inimigo.getVida());
        assertEquals(Heroi.VIDA_INICIAL, heroi.getVida());
    }

    @Test
    void acertoComAtaqueForteCausaMaisDano() {
        assertEquals(Heroi.DANO_FORTE, batalha.registrarAcerto(TipoAtaque.FORTE));
        assertEquals(VIDA_DO_INIMIGO - Heroi.DANO_FORTE, inimigo.getVida());
    }

    @Test
    void erroNoAtaqueFracoFereSoOHeroi() {
        assertEquals(ATAQUE_DO_INIMIGO, batalha.registrarErro(TipoAtaque.FRACO));
        assertEquals(Heroi.VIDA_INICIAL - ATAQUE_DO_INIMIGO, heroi.getVida());
        assertEquals(VIDA_DO_INIMIGO, inimigo.getVida());
    }

    @Test
    void erroNoAtaqueForteCustaMaisCaro() {
        int esperado = ATAQUE_DO_INIMIGO * Batalha.MULTIPLICADOR_ERRO_FORTE;

        assertEquals(esperado, batalha.registrarErro(TipoAtaque.FORTE));
        assertEquals(Heroi.VIDA_INICIAL - esperado, heroi.getVida());
    }

    @Test
    void heroiVenceQuandoOInimigoMorre() {
        derrotarInimigo();

        assertTrue(batalha.terminou());
        assertTrue(batalha.heroiVenceu());
        assertFalse(batalha.heroiPerdeu());
    }

    @Test
    void heroiPerdeQuandoMorre() {
        derrotarHeroi();

        assertTrue(batalha.terminou());
        assertTrue(batalha.heroiPerdeu());
        assertFalse(batalha.heroiVenceu());
    }

    @Test
    void batalhaEncerradaRecusaNovosTurnos() {
        derrotarInimigo();

        assertThrows(IllegalStateException.class, () -> batalha.registrarAcerto(TipoAtaque.FRACO));
        assertThrows(IllegalStateException.class, () -> batalha.registrarErro(TipoAtaque.FRACO));
        assertEquals(Heroi.VIDA_INICIAL, heroi.getVida());
    }

    @Test
    void reiniciarDevolveAVidaTodaAosDois() {
        batalha.registrarAcerto(TipoAtaque.FORTE);
        derrotarHeroi();

        batalha.reiniciar();

        assertEquals(Heroi.VIDA_INICIAL, heroi.getVida());
        assertEquals(VIDA_DO_INIMIGO, inimigo.getVida());
        assertFalse(batalha.terminou());
    }

    private void derrotarInimigo() {
        while (!batalha.heroiVenceu()) {
            batalha.registrarAcerto(TipoAtaque.FORTE);
        }
    }

    private void derrotarHeroi() {
        while (!batalha.heroiPerdeu()) {
            batalha.registrarErro(TipoAtaque.FRACO);
        }
    }
}

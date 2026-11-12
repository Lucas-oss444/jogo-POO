package modelo;

/** Executa os turnos de uma luta. Quem decide se o jogador acertou é quem chama. */
public class Batalha {

    public static final int MULTIPLICADOR_ERRO_FORTE = 2;

    private final Heroi heroi;
    private final Inimigo inimigo;

    public Batalha(Heroi heroi, Inimigo inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
    }

    public Heroi getHeroi() {
        return heroi;
    }

    public Inimigo getInimigo() {
        return inimigo;
    }

    public boolean terminou() {
        return heroiVenceu() || heroiPerdeu();
    }

    public boolean heroiVenceu() {
        return !inimigo.estaVivo();
    }

    public boolean heroiPerdeu() {
        return !heroi.estaVivo();
    }

    /** O jogador acertou: o herói ataca. Devolve o dano causado no inimigo. */
    public int registrarAcerto(TipoAtaque tipo) {
        exigirEmAndamento();
        return heroi.atacar(inimigo, tipo);
    }

    /** O jogador errou: o inimigo ataca. Devolve o dano causado no herói. */
    public int registrarErro(TipoAtaque tipo) {
        exigirEmAndamento();
        int multiplicador = tipo == TipoAtaque.FORTE ? MULTIPLICADOR_ERRO_FORTE : 1;
        return heroi.receberDano(inimigo.getAtaque() * multiplicador);
    }

    public void reiniciar() {
        heroi.restaurarVida();
        inimigo.restaurarVida();
    }

    private void exigirEmAndamento() {
        if (terminou()) {
            throw new IllegalStateException("A batalha já terminou; chame reiniciar() antes de um novo turno.");
        }
    }
}

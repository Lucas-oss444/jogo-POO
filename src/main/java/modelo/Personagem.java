package modelo;

public abstract class Personagem {

    private final String nome;
    private final int vidaMaxima;
    private final int ataque;
    private int vida;

    protected Personagem(String nome, int vida, int ataque) {
        this.nome = nome;
        this.vidaMaxima = vida;
        this.vida = vida;
        this.ataque = ataque;
    }

    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    /** Devolve o dano que o alvo realmente sofreu. */
    public int atacar(Personagem alvo) {
        return alvo.receberDano(ataque);
    }

    /** Devolve a vida perdida. Dano não positivo é ignorado, para um ataque nunca virar cura. */
    public int receberDano(int dano) {
        if (dano <= 0) {
            return 0;
        }
        int perdido = Math.min(dano, vida);
        vida -= perdido;
        return perdido;
    }

    public void restaurarVida() {
        vida = vidaMaxima;
    }
}

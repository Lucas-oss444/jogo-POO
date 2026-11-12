package modelo;

/** Toda classe de herói tem a mesma vida e o mesmo dano; só mudam os nomes dos ataques. */
public abstract class Heroi extends Personagem {

    public static final int VIDA_INICIAL = 100;
    public static final int DANO_FRACO = 10;
    public static final int DANO_FORTE = 25;

    protected Heroi(String nome) {
        super(nome, VIDA_INICIAL, DANO_FRACO);
    }

    public int atacarForte(Personagem alvo) {
        return alvo.receberDano(DANO_FORTE);
    }

    public int atacar(Personagem alvo, TipoAtaque tipo) {
        return tipo == TipoAtaque.FORTE ? atacarForte(alvo) : atacar(alvo);
    }

    /** Chave do nome do ataque nos arquivos de texto, não o texto em si. */
    public abstract String chaveAtaqueFraco();

    public abstract String chaveAtaqueForte();

    public String chaveAtaque(TipoAtaque tipo) {
        return tipo == TipoAtaque.FORTE ? chaveAtaqueForte() : chaveAtaqueFraco();
    }
}

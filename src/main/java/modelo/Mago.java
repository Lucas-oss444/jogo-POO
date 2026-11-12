package modelo;

public class Mago extends Heroi {

    public Mago(String nome) {
        super(nome);
    }

    @Override
    public String chaveAtaqueFraco() {
        return "ataque.mago.fraco";
    }

    @Override
    public String chaveAtaqueForte() {
        return "ataque.mago.forte";
    }
}

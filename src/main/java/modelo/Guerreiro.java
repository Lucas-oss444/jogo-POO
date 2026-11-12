package modelo;

public class Guerreiro extends Heroi {

    public Guerreiro(String nome) {
        super(nome);
    }

    @Override
    public String chaveAtaqueFraco() {
        return "ataque.guerreiro.fraco";
    }

    @Override
    public String chaveAtaqueForte() {
        return "ataque.guerreiro.forte";
    }
}

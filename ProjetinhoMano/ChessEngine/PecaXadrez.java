package ProjetinhoMano.ChessEngine;

import ProjetinhoMano.Layout.*;
public abstract class PecaXadrez extends Peca {

    private Cor cor;
    private int contagemMoviento;

    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getContagemMoviento() {
        return contagemMoviento;
    }

    public PosicaoXadrez getPosicaoDaPeca() {
        return PosicaoXadrez.fromPosicao(posicao);
    }

    protected boolean temPecaOponente(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().piece(posicao);
        return p != null && p.getCor() != cor;
    }

    protected void aumentarContagem() {
        contagemMoviento++;
    }

    protected void reduzirContagem() {
        contagemMoviento--;
    }
}
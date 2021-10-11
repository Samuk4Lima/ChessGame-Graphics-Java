package ProjetinhoMano.ChessEngine.Pecas;

import ProjetinhoMano.Layout.Tabuleiro;
import ProjetinhoMano.Layout.Posicao;
import ProjetinhoMano.ChessEngine.PecaXadrez;
import ProjetinhoMano.ChessEngine.Cor;

public class Bispo extends PecaXadrez {

    public Bispo(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao pos = new Posicao(0, 0);

        // diagonal superior esquerda
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setPosicao(pos.getLinha() - 1, pos.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal inferior esquerda
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setPosicao(pos.getLinha() + 1, pos.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal inferior direita
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setPosicao(pos.getLinha() + 1, pos.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // digaonal superior esquerda
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setPosicao(pos.getLinha() - 1, pos.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        return mat;
    }

    @Override
    public String toString() {
        return "B";
    }
}
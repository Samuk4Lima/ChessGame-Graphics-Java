package ProjetinhoMano.ChessEngine.Pecas;

import ProjetinhoMano.Layout.*;
import ProjetinhoMano.ChessEngine.*;

public class Torre extends PecaXadrez {

    public Torre(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao pos = new Posicao(0, 0);

        // p/ cima
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setLinha(pos.getLinha() - 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ esquerda
        pos.setPosicao(posicao.getLinha(), posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setColuna(pos.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ baixo
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setLinha(pos.getLinha() + 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ direita
        pos.setPosicao(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            pos.setColuna(pos.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        return mat;
    }

    @Override
    public String toString() {
        return "T";
    }
}
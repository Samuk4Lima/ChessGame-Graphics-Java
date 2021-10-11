package ProjetinhoMano.ChessEngine.Pecas;

import ProjetinhoMano.Layout.*;
import ProjetinhoMano.ChessEngine.*;

public class Rei extends PecaXadrez {

    private Partida partida;

    public Rei(Tabuleiro tabuleiro, Cor cor, Partida partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().piece(posicao);
        return p == null || p.getCor() != getCor();
    }

    private boolean testRookCastling(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) getTabuleiro().piece(posicao);
        return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContagemMoviento() == 0;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao pos = new Posicao(0, 0);

        // /p cima
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna());
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ esquerda
        pos.setPosicao(posicao.getLinha(), posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ baixo
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna());
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // p/ direita
        pos.setPosicao(posicao.getLinha(), posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal superior a esquerda
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal inferior a esquerda
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal inferior a direita
        pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // diagonal superior a direita
        pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        if (getContagemMoviento() == 0 && !partida.getCheck()) {
            // roque por torre
            Posicao kingRoque = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (testRookCastling(kingRoque)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().piece(p1) == null && getTabuleiro().piece(p2) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }
            // roque rainha-torre
            Posicao queenRoque = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (testRookCastling(queenRoque)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().piece(p1) == null && getTabuleiro().piece(p2) == null && getTabuleiro().piece(p3) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }

        return mat;
    }

    @Override
    public String toString() {
        return "K";
    }
}
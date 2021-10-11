package ProjetinhoMano.ChessEngine.Pecas;

import ProjetinhoMano.Layout.*;
import ProjetinhoMano.ChessEngine.*;

public class Peao extends PecaXadrez {

    private Partida partida;

    public Peao(Tabuleiro tabuleiro, Cor cor, Partida partida) {
        super(tabuleiro, cor);
        this.partida = partida;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao pos = new Posicao(0, 0);

        // BRANCO
        if (getCor() == Cor.BRANCO) {
            // eh primeiro movimento
            if (getContagemMoviento() == 0) {
                Posicao p = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
                pos.setPosicao(posicao.getLinha() - 2, posicao.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos) && getTabuleiro().posicaoExiste(p)
                        && !getTabuleiro().temPeca(p)) {
                    mat[pos.getLinha()][pos.getColuna()] = true;
                }
            }
            // cima
            pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            // diagonal esquerda
            pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            // digaonal direita
            pos.setPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }

            // movimento de checagem de en passant
            if (partida.getEnPassant() != null) {
                PecaXadrez enPassantVulnerable = partida.getEnPassant();
                Posicao p = new Posicao(0, 0);

                // checagem de peça a esquerda
                pos.setPosicao(posicao.getLinha(), posicao.getColuna() - 1);
                p.setPosicao(pos.getLinha() - 1, pos.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)
                        && getTabuleiro().piece(pos) == enPassantVulnerable && !getTabuleiro().temPeca(p)
                        && getTabuleiro().posicaoExiste(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }

                // checagem de peça a direita
                pos.setPosicao(posicao.getLinha(), posicao.getColuna() + 1);
                p.setPosicao(pos.getLinha() - 1, pos.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)
                        && getTabuleiro().piece(pos) == enPassantVulnerable && !getTabuleiro().temPeca(p)
                        && getTabuleiro().posicaoExiste(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }
        }

        // PRETO
        else {
            // eh primeiro movimento
            if (getContagemMoviento() == 0) {
                Posicao p = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
                pos.setPosicao(posicao.getLinha() + 2, posicao.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos) && getTabuleiro().posicaoExiste(p)
                        && !getTabuleiro().temPeca(p)) {
                    mat[pos.getLinha()][pos.getColuna()] = true;
                }
            }
            // descendo
            pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(pos) && !getTabuleiro().temPeca(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            // diagonal esquerda descendo
            pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            // diagonal direita descendo
            pos.setPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }

            // checagem de movimento enpassant
            if (partida.getEnPassant() != null) {
                PecaXadrez enPassantVulnerable = partida.getEnPassant();
                Posicao p = new Posicao(0, 0);

                // checagem de peça a esquerda
                pos.setPosicao(posicao.getLinha(), posicao.getColuna() - 1);
                p.setPosicao(pos.getLinha() + 1, pos.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)
                        && getTabuleiro().piece(pos) == enPassantVulnerable && !getTabuleiro().temPeca(p)
                        && getTabuleiro().posicaoExiste(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }

                // checagem de peça a direita
                pos.setPosicao(posicao.getLinha(), posicao.getColuna() + 1);
                p.setPosicao(pos.getLinha() + 1, pos.getColuna());
                if (getTabuleiro().posicaoExiste(pos) && temPecaOponente(pos)
                        && getTabuleiro().piece(pos) == enPassantVulnerable && !getTabuleiro().temPeca(p)
                        && getTabuleiro().posicaoExiste(p)) {
                    mat[p.getLinha()][p.getColuna()] = true;
                }
            }
        }

        return mat;

    }

    @Override
    public String toString() {
        return "P";
    }
}
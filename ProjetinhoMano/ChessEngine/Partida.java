package ProjetinhoMano.ChessEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ProjetinhoMano.Layout.*;
import ProjetinhoMano.ChessEngine.Pecas.*;

import javax.swing.*;

public class Partida {

    private Tabuleiro tabuleiro;
    private int turno;
    private Cor vez;
    private boolean check;
    private boolean checkMate;
    private PecaXadrez enPassant;
    private PecaXadrez promovido;

    private List<Peca> noTabuleiro = new ArrayList<>();
    private List<Peca> capturadas = new ArrayList<>();

    public int getTurno() {
        return turno;
    }

    public Cor getVez() {
        return vez;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public PecaXadrez getEnPassant() {
        return enPassant;
    }

    public PecaXadrez getPromovido() {
        return promovido;
    }

    public Partida() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        vez = Cor.BRANCO;
        PosicaoInicial();
    }

    public PecaXadrez[][] getPecas() {
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(PosicaoXadrez pOrigem) {
        Posicao posicao = pOrigem.toPosicao();
        validateOrigem(posicao);
        return tabuleiro.piece(posicao).movimentosPossiveis();
    }

    public PecaXadrez movendoPeca(PosicaoXadrez pOrigem, PosicaoXadrez pDestino) {
        Posicao origem = pOrigem.toPosicao();
        Posicao destino = pDestino.toPosicao();
        validateOrigem(origem);
        validateDestino(origem, destino);
        Peca pecaCapturada = mover(origem, destino);

        if (testCheck(vez)) {
            desmover(origem, destino, pecaCapturada);
            throw new XadrezException("Não é permitido se pôr em cheque");
        }

        PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.piece(destino);

        // promoção
        promovido = null;
        if (pecaMovida instanceof Peao) {
            if ((pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0)
                    || (pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7)) {
                promovido = pecaMovida;
                String mensagemPromocao = "Seu peão pode ser promovido, por favor escolha uma das opções abaixo:";
                String tituloPromocao = "Promoção de Peão";
                String[] options = {"Rainha", "Bispo", "Torre", "Cavalo"};
                String tipo = "";
                while (tipo == "") {
                    int promocao = JOptionPane.showOptionDialog(null, mensagemPromocao, tituloPromocao, JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, options,"");
                    switch (promocao) {
                        case 0:
                            tipo = "Q";
                            promovido = subPecaPromovida(tipo);
                            break;
                        case 1:
                            tipo = "B";
                            promovido = subPecaPromovida(tipo);
                            break;
                        case 2:
                            tipo = "T";
                            promovido = subPecaPromovida(tipo);
                            break;
                        case 3:
                            tipo = "C";
                            promovido = subPecaPromovida(tipo);
                            break;
                    }
                }
            }
        }


        check = testCheck(oponente(vez));

        if (testCheckMate(oponente(vez))) {
            checkMate = true;
        } else {
            atualizarTurno();
        }

        // en passant
        if (pecaMovida instanceof Peao && Math.abs(origem.getLinha() - destino.getLinha()) == 2) {
            enPassant = pecaMovida;
        } else {
            enPassant = null;
        }

        return (PecaXadrez) pecaCapturada;
    }

    public PecaXadrez subPecaPromovida(String tipo) {
        if (promovido == null) {
            throw new IllegalStateException("Não há peças para ser promovidas");
        }
        if (!tipo.equals("Q") && !tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T")) {
            return promovido;
        }

        Posicao pos = promovido.getPosicaoDaPeca().toPosicao();
        Peca p = tabuleiro.retirarPeca(pos);
        noTabuleiro.remove(p);

        PecaXadrez newPeca = newPeca(tipo, promovido.getCor());
        tabuleiro.colocarPeca(newPeca, pos);
        noTabuleiro.add(newPeca);

        return newPeca;
    }

    private PecaXadrez newPeca(String tipo, Cor cor) {
        if (tipo.equals("Q"))
            return new Rainha(tabuleiro, cor);
        else if (tipo.equals("B"))
            return new Bispo(tabuleiro, cor);
        else if (tipo.equals("C"))
            return new Cavalo(tabuleiro, cor);
        else
            return new Torre(tabuleiro, cor);
    }

    private Peca mover(Posicao origem, Posicao destino) {
        PecaXadrez p = (PecaXadrez) tabuleiro.retirarPeca(origem);
        p.aumentarContagem();
        Peca pecaCapturada = tabuleiro.retirarPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecaCapturada != null) {
            noTabuleiro.remove(pecaCapturada);
            capturadas.add(pecaCapturada);
        }

        // roque lado do rei
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            mover(origemT, destinoT);

        }

        // roque rainha-torre
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            mover(origemT, destinoT);

        }

        // en passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posPeao;
                if (p.getCor() == Cor.BRANCO) {
                    posPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.retirarPeca(posPeao);
                capturadas.add(pecaCapturada);
                noTabuleiro.remove(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    private void desmover(Posicao origem, Posicao destino, Peca pecaCapturada) {
        PecaXadrez p = (PecaXadrez) tabuleiro.retirarPeca(destino);
        p.reduzirContagem();
        tabuleiro.colocarPeca(p, origem);

        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            noTabuleiro.add(pecaCapturada);
            capturadas.remove(pecaCapturada);
        }

        // roque por torre
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            desmover(origemT, destinoT, null);

        }

        // roque rainha-torre
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            desmover(origemT, destinoT, null);

        }

        // en passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassant) {
                PecaXadrez peao = (PecaXadrez) tabuleiro.retirarPeca(destino);
                Posicao posPeao;
                if (p.getCor() == Cor.BRANCO) {
                    posPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, posPeao);
            }
        }
    }

    private void validateOrigem(Posicao posicao) {
        PecaXadrez p = (PecaXadrez) tabuleiro.piece(posicao);
        if (!tabuleiro.temPeca(posicao)) {
            throw new XadrezException("Não há peça na posição de origem");
        }
        if (!tabuleiro.piece(posicao).haMovimento()) {
            throw new XadrezException("Sem movimentos possiveis para esta peça");
        }
        if (p.getCor() != vez) {
            throw new XadrezException("Essa peça não é sua");
        }
    }

    private void validateDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.piece(origem).movimentoPossivel(destino)) {
            throw new XadrezException("Este movimento não é possível");
        }
    }

    public void atualizarTurno() {
        turno++;
        vez = (turno % 2 == 0) ? Cor.PRETO : Cor.BRANCO;
    }

    private Cor oponente(Cor cor) {
        return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private PecaXadrez rei(Cor cor) {
        List<Peca> list = noTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("Não há rei " + cor + " no tabuleiro");
    }

    private boolean testCheck(Cor cor) {
        Posicao posRei = rei(cor).getPosicaoDaPeca().toPosicao();
        List<Peca> opon = noTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
                .collect(Collectors.toList());
        for (Peca p : opon) {
            if (p.movimentoPossivel(posRei)) {
                return true;
            }

        }
        return false;
    }

    private boolean testCheckMate(Cor cor) {
        if (!testCheck(cor)) {
            return false;
        }
        List<Peca> allies = noTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
                .collect(Collectors.toList());
        for (Peca p : allies) {
            boolean[][] mat = p.movimentosPossiveis();
            for (int i = 0; i < tabuleiro.getLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = ((PecaXadrez) p).getPosicaoDaPeca().toPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca cp = mover(origem, destino);
                        boolean check = testCheck(cor);
                        desmover(origem, destino, cp);
                        if (!check) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez piece) {
        tabuleiro.colocarPeca(piece, new PosicaoXadrez(coluna, linha).toPosicao());
        noTabuleiro.add(piece);
    }

    private void PosicaoInicial() {
        colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
    }
}
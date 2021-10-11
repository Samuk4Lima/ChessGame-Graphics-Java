package ProjetinhoMano.GUI;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ProjetinhoMano.ChessEngine.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class Graphics {

    public static PosicaoXadrez lerPosicao(String str, int turno) {
        Partida partida;
        try {
            char coluna = str.charAt(0);
            int linha = Integer.parseInt(str.substring(1));
            return new PosicaoXadrez(coluna, linha);
        } catch (XadrezException e) {
            throw new XadrezException("Erro ao inserir posição, válido de a1 a h8");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Erro ao inserir posição, válido de a1 a h8");
        }
    }

    //impressao do tabuleiro para uma string (com seleção de casas possiveis)
    public static String telaDoJogoAppend(Partida partida, List<PecaXadrez> pecasCapturadas, boolean[][] movimentosPossiveis) {
        String str = "";
        str += printTabuleiro(partida.getPecas(), movimentosPossiveis);
        str += "\n" + pecasCapturadas(pecasCapturadas);
        str += "\nTurno:" + partida.getTurno();
        return str;
    }

    //impressao do tabuleiro para uma string (normal e sem casas selecionadas com *)
    public static String telaDoJogoAppend(Partida partida, List<PecaXadrez> pecasCapturadas) {
        String str = "";
        str += printTabuleiro(partida.getPecas());
        str += "\n" + pecasCapturadas(pecasCapturadas);
        str += "\nTurno:" + partida.getTurno();
        return str;
    }

    //funcao colorir jtextPane ou jtext area que nao funcionou
    public static JTextPane Colorindotela(JTextPane telaDoJogo, Partida partida, List<PecaXadrez> pecasCapturadas,PecaXadrez[][] pecas) {
        int auxi = 0;
        int auxj = 0;

        String telaDoJogostr = telaDoJogoAppend(partida, pecasCapturadas);
        String str[] = telaDoJogostr.split("\n");

        Highlighter.HighlightPainter preto = new DefaultHighlighter.DefaultHighlightPainter(new Color(0, 0, 0));
        Highlighter.HighlightPainter branco = new DefaultHighlighter.DefaultHighlightPainter(Color.WHITE);
        Highlighter.HighlightPainter podeSerCapturado = new DefaultHighlighter.DefaultHighlightPainter(new Color(51, 173, 255));

        try {
            for (int i = 0; i < str.length; i++) {
                for (int j = 0; j < str[i].length(); j++) {
                    auxi = i; //linha
                    if (j == 3 || j == 4 || j == 5) {
                        auxj = 1;
                    }
                    if (j == 6 || j == 7 || j == 8) {
                        auxj = 2;
                    }
                    if (j == 9 || j == 10 || j == 11) {
                        auxj = 3;
                    }
                    if (j == 12 || j == 13 || j == 14) {
                        auxj = 4;
                    }
                    if (j == 15 || j == 16 || j == 17) {
                        auxj = 5;
                    }
                    if (j == 18 || j == 19 || j == 20) {
                        auxj = 6;
                    }
                    if (j == 21 || j == 22 || j == 23) {
                        auxj = 7;
                    }
                    if (j == 24 || j == 25 || j == 26) {
                        auxj = 8;
                    }

                    if (str[i].length() == 26) {
                        switch (j) {
                            case 1, 2:

                            case 3, 4, 5:
                                if (pecas[auxi][auxj] == null) {
                                } else {
                                    if (pecas[auxi][auxj].getCor() == Cor.BRANCO) {
                                        telaDoJogo.getHighlighter().addHighlight(3, 5, branco);
                                        telaDoJogo.getHighlighter().addHighlight(3,5,branco);
                                    } else {
                                        telaDoJogo.getHighlighter().addHighlight(3, 5, preto);
                                    }
                                }
                            case 6, 7, 8:

                            case 9, 10, 11:

                            case 12, 13, 14:

                            case 15, 16, 17:

                            case 18, 19, 20:

                            case 21, 22, 23:

                            case 24, 25, 26:

                        }
                    }

                    str[i] += "\n";
                }
            }
        } catch (BadLocationException e) {
            String mensagem = "Algo deu errado no código de cor do tabuleiro";
            String titulo = "BadLocationException";
            JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);;
        }
        return telaDoJogo;
    }

    //tabuleiro para posicoes tradicionais
    public static String printTabuleiro(PecaXadrez[][] pecas) {
        String str = "";
        for (int i = 0; i < pecas.length; i++) {
            str += (8 - i) + " ";
            for (int j = 0; j < pecas[i].length; j++) {
                str += printPeca(pecas[i][j], false);
            }
            str += "\n";
        }
        str += "   a  b  c  d  e  f  g  h ";
        return str;
    }

    //tabuleiro para imprimir posicao colorida
    public static String printTabuleiro(PecaXadrez[][] pecas, boolean[][] movimentosPossiveis) {
        String str = "";
        for (int i = 0; i < pecas.length; i++) {
            str += (8 - i) + " ";
            for (int j = 0; j < pecas[i].length; j++) {
                str += printPeca(pecas[i][j], movimentosPossiveis[i][j]);
            }
            str += "\n";
        }
        str += "   a  b  c  d  e  f  g  h ";
        return str;
    }

    private static String printPeca(PecaXadrez peca, boolean background) {
        String str = "";
        if (background && peca == null) {
            str += " * ";
        } else if (peca == null) {
            str += " - ";
        } else if (background) {
            if (peca.getCor() == Cor.BRANCO) {
                str += "*" + peca + "*";

            } else {
                str += "*" + peca.toString().toLowerCase() + "*";
            }
        } else if (peca.getCor() == Cor.BRANCO) {
            str += "[" + peca + "]";

        } else {
            str += "|" + peca.toString().toLowerCase() + "|";
        }
        str += "";
        return str;
    }

    private static String pecasCapturadas(List<PecaXadrez> capturada) {
        String str = "";
        List<PecaXadrez> brancas = capturada.stream().filter(x -> x.getCor() == Cor.BRANCO)
                .collect(Collectors.toList());
        List<PecaXadrez> pretas = capturada.stream().filter(x -> x.getCor() == Cor.PRETO)
                .collect(Collectors.toList());
        str += "\nPeças capturadas:\nBrancas: ";
        str +=Arrays.toString(brancas.toArray());

        str += "\nPretas: ";
        String control=Arrays.toString(pretas.toArray());
        str += control.toLowerCase();

        return str;
    }
}
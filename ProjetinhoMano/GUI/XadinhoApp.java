package ProjetinhoMano.GUI;

import ProjetinhoMano.ChessEngine.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class XadinhoApp extends JFrame {

    private JTextField entradaDeJogadas;
    private JTextArea telaDoJogo;
    private JButton Jogar;
    private JButton desistirButton;
    private JLabel Jogador0Label;
    private JLabel Jogador1Label;
    private JLabel VezDoJogador;
    private JLabel interfaceLabelJogada;
    private javax.swing.JPanel JPanel;
    private JRadioButton Destino;
    private JRadioButton Origem;
    private JLabel LabelJogada;
    private JLabel InstrucoesLabel;
    private Partida partida;
    private PecaXadrez capturedPiece;
    private PosicaoXadrez origem;
    private PosicaoXadrez destino;
    private List<PecaXadrez> pecasCapturadas;

    public XadinhoApp(String title, Partida partida, String jogador0Nome, String jogador1Nome, List<PecaXadrez> pecasCapturadas) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(JPanel);
        this.pack();
        this.partida = partida;
        this.pecasCapturadas = pecasCapturadas;

        //colocando radio botões no mesmo grupo
        ButtonGroup grupoBotoes = new ButtonGroup();
        grupoBotoes.add(Origem);
        grupoBotoes.add(Destino);

        //configurando texto dos jogador0label e jogador1label
        Jogador0Label.setText(jogador0Nome + " (Brancas) (Minusculas)");
        Jogador1Label.setText(jogador1Nome + " (Pretas) (Maiusculas)");

        //mostrar vez do jogador de acordo com o nome inserido ao invés de partida.getVez()
        String jogadordavez;
        if (partida.getVez() == Cor.BRANCO) {
            jogadordavez = jogador0Nome+" (Brancas)";
        } else{
            jogadordavez = jogador1Nome+" (Pretas)";
        }
        VezDoJogador.setText("Vez do Jogador: " + jogadordavez);

        //imprime a tela pela primeira vez
        telaDoJogo.setText(Graphics.telaDoJogoAppend(partida, pecasCapturadas));

        Jogar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jogada(partida, pecasCapturadas, jogador0Nome, jogador1Nome, VezDoJogador);
            }
        });

        entradaDeJogadas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER) {
                    Jogada(partida, pecasCapturadas, jogador0Nome, jogador1Nome, VezDoJogador);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        desistirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XadinhoApp.this.dispose();
                // crie uma janela onde o nonCurrentPlayer seja o vencedor
                if (partida.getVez() == Cor.BRANCO) {
                    String mensagem = jogador1Nome + " venceu! (Pretas)";
                    String titulo = "Jogador vencedor";
                    JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String mensagem = jogador0Nome + " venceu! (Brancas)";
                    String titulo = "Jogador vencedor";
                    JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
                }
                System.exit(0);
            }
        });
    }

    public static String[] nomesDosJogadores() {
        String pretas;
        String brancas;
        String[] jogadores = new String[2];
        String titulo = "Pretas";
        String titulo1 = "Brancas";
        String titulo2 = "Jogador por lado";
        StringBuilder mensagem = new StringBuilder();
        pretas = JOptionPane.showInputDialog(null, "Quem controlará as pretas? ", titulo, JOptionPane.INFORMATION_MESSAGE);
        brancas = JOptionPane.showInputDialog(null, "Quem controlará as brancas? ", titulo1, JOptionPane.INFORMATION_MESSAGE);
        mensagem.append("").append(pretas).append(" jogará com as pretas.\n").append(brancas).append(" jogará com as brancas.");
        JOptionPane.showMessageDialog(null, mensagem, titulo2, JOptionPane.INFORMATION_MESSAGE);
        jogadores[1] = pretas;
        jogadores[0] = brancas;
        return jogadores;
    }

    public void Jogada(Partida partida, List<PecaXadrez> pecasCapturadas, String jogador0Nome,
                       String jogador1Nome, JLabel VezDoJogador) throws GUIJogadaException {
        try {
            String jogada = entradaDeJogadas.getText();

            if (entradaDeJogadas.getText().isEmpty()) {
                String msg = "A caixa de jogada está vazia, preencha com a jogada desejada e tente novamente";
                throw new GUIJogadaException(msg);
            }

            //mostrar vez do jogador de acordo com o nome inserido ao invés de partida.getVez()
            String jogadordavez;
            if (partida.getVez() == Cor.BRANCO) {
                jogadordavez = jogador0Nome+" (Brancas)";
            } else{
                jogadordavez = jogador1Nome+" (Pretas)";
            }
            VezDoJogador.setText("Vez do Jogador: " + jogadordavez);

            if (Origem.isSelected()) {
                origem = Graphics.lerPosicao(jogada, partida.getTurno());

                boolean[][] movimentosPossiveis = partida.movimentosPossiveis(origem);

                //imprime tabuleiro com movimentos possiveis
                telaDoJogo.setText(Graphics.telaDoJogoAppend(partida, pecasCapturadas, movimentosPossiveis));

                Destino.setSelected(true);
                entradaDeJogadas.setText("");
            } else if (Destino.isSelected()) {
                destino = Graphics.lerPosicao(jogada, partida.getTurno());

                capturedPiece = partida.movendoPeca(origem, destino);

                if (capturedPiece != null) {
                    pecasCapturadas.add(capturedPiece);
                }

                //reimprime com as jogadas aparentes
                telaDoJogo.setText(Graphics.telaDoJogoAppend(partida, pecasCapturadas));

                Origem.setSelected(true);
                entradaDeJogadas.setText("");

                //mostrar vez do jogador de acordo com o nome inserido ao invés de partida.getVez()
                if (partida.getVez() == Cor.BRANCO) {
                    jogadordavez = jogador0Nome+" (Brancas)";
                } else{
                    jogadordavez = jogador1Nome+" (Pretas)";
                }
                VezDoJogador.setText("Vez do Jogador: " + jogadordavez);

                //checagem xeque-mate (acontece toda vez que há alguma alteração no construtor ou qualquer de suas variáveis
                if (!partida.getCheckMate()) {
                    if (partida.getCheck()) {
                        String titleXeque = "Xeque!";
                        String msgXeque = "";
                        if (partida.getVez() == Cor.BRANCO)
                            msgXeque = jogador1Nome + ", seu rei está em xeque";
                        else
                            msgXeque = jogador0Nome + ", seu rei está em xeque";
                        JOptionPane.showMessageDialog(null, msgXeque, titleXeque, JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    this.dispose();
                    String titleXequeMate = "Xeque-Mate!";
                    String msgXequeMate = "";
                    if (partida.getVez() == Cor.BRANCO)
                        msgXequeMate = "O vencedor é: " + jogador0Nome;
                    else
                        msgXequeMate = "O vencedor é: " + jogador1Nome;
                    JOptionPane.showMessageDialog(null, msgXequeMate, titleXequeMate, JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }

        } catch (XadrezException error) {
            String mensagemErro1 = error.getMessage();
            String tituloErro1 = "XadrezException";
            JOptionPane.showMessageDialog(null, mensagemErro1, tituloErro1, JOptionPane.INFORMATION_MESSAGE);
            entradaDeJogadas.setText("");
        } catch (InputMismatchException error) {
            String mensagemErro2 = error.getMessage();
            String tituloErro2 = "InputMismatchException";
            JOptionPane.showMessageDialog(null, mensagemErro2, tituloErro2, JOptionPane.INFORMATION_MESSAGE);
            entradaDeJogadas.setText("");
        } catch (NumberFormatException error) {
            String mensagemErro3 = error.getMessage();
            String tituloErro3 = "NumberFormatException";
            JOptionPane.showMessageDialog(null, mensagemErro3, tituloErro3, JOptionPane.INFORMATION_MESSAGE);
            entradaDeJogadas.setText("");
        } catch (GUIJogadaException error) {
            String mensagemErro4 = error.getMessage();
            String tituloErro4 = "JogadaException";
            JOptionPane.showMessageDialog(null, mensagemErro4, tituloErro4, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {

        String[] jogadores = nomesDosJogadores();
        Partida partida = new Partida();
        List<PecaXadrez> pecasCapturadas = new ArrayList<>();

        XadinhoApp XadinhoApp = new XadinhoApp("Xadrez UPE - Samuel Lima Maranhão e Matheus Souza de Oliveira",
                partida, jogadores[0], jogadores[1], pecasCapturadas);
        XadinhoApp.setVisible(true);

        XadinhoApp.telaDoJogo.setBackground(new Color(128, 128, 128));
        XadinhoApp.telaDoJogo.setForeground(Color.WHITE);

        XadinhoApp.JPanel.setToolTipText(null); //linha necessaria pois o tooltip ficava aparecendo texto
        XadinhoApp.Origem.setSelected(true);
    }

}

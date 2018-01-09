import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
/**
 * A classe ConfiguraTeclado cria uma janela para gerar uma matriz de int com o c�digo
 * ascii das teclas primidas.
 * <p>
 * Essas matriz corresponde as teclas de um jogador de tenis.
 *
 * No caso de estar conclu�do o processo de recolha de informac�o,
 * notifica a inst�ncia tenis da alterac�o das teclas.
 * e torna a janela invis�vel.
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class ConfiguraTeclado implements KeyListener {
    public final static char newline = '\n';
    
    private JTextArea text_area;
    private JFrame janela;
    private int tecla_actual;
    private int[] teclas;
    
    private Tenis tenis;
    /**
     * Constructor da classe.
     */
    public ConfiguraTeclado(){
        janela = new JFrame("ConfiguraTeclado");
        janela.getContentPane().setLayout(new FlowLayout());
        
        text_area = new JTextArea(5,50);
        text_area.setEditable(false);
        text_area.addKeyListener(this);
        
        janela.getContentPane().add(text_area);
        janela.setResizable(false);
        janela.pack();
        janela.setVisible(true);
    }
    /**
     * Procedimento que reinicia as defini��es da classe.
     * <p>
     * cria uma nova matriz de int.
     */
    public void reset(){
        tecla_actual = 0;
        teclas = new int[4];
        text_area.setText("");
        println("Digite as teclas correspondente a cima, baixo, esquerda e direita,respectivamente");
    }
    /**
     * Procedimento que regista a inst�ncia da classe Tenis criada
     * <p>
     *  Quando acabar a recolha da informa��o sobre as teclas
     * esta classe chama o alteraTeclas da classe tenis
     *
     * @param tenis inst�ncia da classe tenis
     */
    public void registaTenis(Tenis tenis){
        this.tenis = tenis;
    }
    /**
     * Procedimento que escreve uma frase na JTextArea
     * @param mensagem mensagem a escrever
     */
    public void print(String mensagem){
        text_area.append(mensagem);
    }
    /**
     * Procedimento similar ao print mas muda de linha no final da frase
     * @param mensagem mensagem a escrever
     */
    public void println(String mensagem){
        text_area.append(mensagem + newline);
    }
    /**
     * M�todo herdado por implementa��o de KeyListener
     * � chamado cada vez que � pressionada uma tecla.
     * <p>
     * Chama o procedimento processaEventos();
     * @param e evento
     */
    public void keyPressed(KeyEvent e) {
        processaEventos(e);
    }
    /**
     * M�todo herdado por implementa��o de KeyListener
     * <p>
     * M�todo sem implementa��o.
     * @param e evento
     */
    public void keyReleased(KeyEvent e) {
    }
    /**
     * M�todo herdado por implmeenta��o de KeyListener
     * <p>
     * M�todo sem implementa��o.
     * @param e evento
     */
    public void keyTyped(KeyEvent e) {
    }
    /**
     * Procedimento respons�vel pela actualiza��o da teclas, matriz de int
     * que guarda os c�digos ascii das teclas primidas.
     *
     * <p>
     * No caso de estar conclu�do o processo, notifica a instancia teclas da alteracao.
     * e define torna a janela invis�vel.
     *
     * @param e evento ocorrido
     */
    private void processaEventos(KeyEvent e){
        if(e.isActionKey())
            println("ActionKey primida");
        else
            println("Tecla primida: " + e.getKeyChar());
        teclas[tecla_actual] = e.getKeyCode();
        
        ++tecla_actual;
        if(tecla_actual == 4) {
            tenis.alteraTeclas(teclas);
            setVisible(false);
        }
    }
    /**
     * Modificador do estado visivel da JFrame
     * @param visivel novo estado
     */
    public void setVisible(boolean visivel){
        janela.setVisible(visivel);
    }
}
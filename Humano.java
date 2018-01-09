import java.awt.*;
import java.awt.event.*;
/**
 * Classe que representa um jogador Humano de tenis
 *  Implementa keylistener
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Humano extends Jogador implements KeyListener {
    private boolean teclas_primidas[] = { false, false, false, false };
    /* cima , baixo, esquerda, direita */
    private int teclas[] = { 38 , 40, 37, 39 };
    
    /**
     * Constructor da classe.
     * <p>
     * @param campo tabuleiro de jogo
     * @param id_do_jogador identificação do jogador (int de 1,2,4,5)
     * @param nome nome do jogador
     * @param posicao posicao do Jogador
     */
    public Humano(Tabuleiro campo,int id_do_jogador,String nome,Posicao posicao){
        super(campo,id_do_jogador,nome,posicao);
    }
    /**
     * Constructor da classe.
     * <p>
     * @param id_do_jogador identificação do jogador (int de 1 a 4)
     * @param nome nome do jogador
     */
    public Humano(int id_do_jogador,String nome){
        super(id_do_jogador,nome);
    }
    /** Inspector para teclas
     * @return teclas
     */
    public int[] teclas(){
        return teclas;
    }
    /**
     * Modificador para teclas
     * @param teclas nova combinacao e teclas para a instancia
     */
    public void alteraTeclas(int[] teclas){
        this.teclas = teclas;
    }
    /**
     * Método run da thread
     * Vida do jogador, a sua vida resume-se à verificação
     * de teclas primidas, e no caso de existir uam tecla assinalada
     * como primida movimenta-se.
     */
    public void run(){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Dimensao dimensao_do_campo = campo.dimensaoDoCampo();
        Dimensao dimensao_do_jogador = campo.dimensaoDoJogador();
        int limite_esquerdo;
        int limite_direito;
        
        switch(id_do_jogador){
            case Tabuleiro.jogador_da_esquerda:
            case Tabuleiro.jogador_da_esquerda_pares: {
                limite_esquerdo = 0;
                limite_direito = (campo.dimensaoDoCampo().largura() / 2);
            }
            break;
            default: {
                limite_esquerdo = (campo.dimensaoDoCampo().largura() / 2);
                limite_direito = campo.dimensaoDoCampo().largura();
                break;
            }
        }
        while (viva) {
            if(!em_pausa){
                campo.iniciaAcesso(id_do_jogador);
                
                /* cima e baixo */
                if(teclas_primidas[0] && posicao.valorDeY() > 0)
                    posicao.alteraY(posicao.valorDeY() - 1);
                else
                    if(teclas_primidas[1] && ((posicao.valorDeY() + dimensao_do_jogador.altura()) < dimensao_do_campo.altura()) )
                        posicao.alteraY(posicao.valorDeY() + 1);
                
                /* esquerda e direita */
                if(teclas_primidas[2] && posicao.valorDeX() > limite_esquerdo)
                    posicao.alteraX(posicao.valorDeX() - 1);
                else
                    if(teclas_primidas[3] && ((posicao.valorDeX() + dimensao_do_jogador.largura()) < limite_direito) )
                        posicao.alteraX(posicao.valorDeX() + 1);
                
                campo.actualizaJanela();
                campo.terminaAcesso();
                
                try {
                    Thread.sleep(8);
                } catch (InterruptedException ex) {}
            }
            else{
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex) {}
            }
        }
        campo = null;
    }
    /**
     * Método herdado por implementação de KeyListener
     * <p>
     * Método que assinala uma tecla como primida.
     * @param evento evento
     */
    public void keyPressed(KeyEvent evento) {
        for(int iterador = 0; iterador != 4; iterador++)
            if(teclas[iterador] == evento.getKeyCode())
                teclas_primidas[iterador] = true;
    }
    /**
     * Método herdado por implementação de KeyListener
     * <p>
     * Método que assinala uma tecla como não primida.
     * @param evento evento
     */
    public void keyReleased(KeyEvent evento) {
        for(int iterador = 0; iterador != 4; iterador++)
            if(teclas[iterador] == evento.getKeyCode())
                teclas_primidas[iterador] = false;
    }
    /**
     * Método herdado por implementação de KeyListener
     * <p>
     * Método sem implementação.
     * @param e evento
     */
    public void keyTyped(KeyEvent e) {
        // System.out.println("caracter primido: " + e.getKeyChar());
    }
    public String tipoDeJogador() {
        return "Humano";
    }
}

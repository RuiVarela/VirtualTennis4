import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
/**
 * Classe que representa um Campo de tenis
 * Esta classe constitui o objecto partilhado do jogo
 * <p>
 * Exemplo de acesso ao campo:
 * <p>
 * campo.iniciaAcesso(Tabuleiro.bola);
 * <p>
 * ...
 * <p>
 * Posicao posicao_da_esqerda = campo.posicaoDoJogador(Tabuleiro.jogador_da_esquerda);
 * <p>
 * compara��es e movimento...
 * <p>
 * ...
 * <p>
 * campo.terminaAcesso();
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Tabuleiro extends JPanel {
    public static final int jogador_da_esquerda = 1;
    public static final int jogador_da_direita = 2;
    public static final int bola = 3;
    public static final int jogador_da_esquerda_pares = 5;
    public static final int jogador_da_direita_pares = 4;
    public static final int ninguem = 0;
    
    private Dimensao dimensao_do_campo;
    private Dimensao dimensao_da_bola;
    private Dimensao dimensao_do_jogador;
    
    /* indica quem est� a usar o objecto*/
    private int utilizador_do_objecto = ninguem;
    
    private Posicao posicao_jogador_esquerda;
    private Posicao posicao_jogador_direita;
    private Posicao posicao_jogador_esquerda_pares;
    private Posicao posicao_jogador_direita_pares;
    private Posicao posicao_bola;
    
    /* imagens */
    private Image imagem_bola;
    private Image imagem_jogador_dir;
    private Image imagem_jogador_esq;
    private Image imagem_campo;
    
    /* Double buffering */
    private Image imagem_double_buffering;
    private Graphics graphics_double_buffering;
    
    /**
     * Constructor da classe
     * @param posicao_da_bola posi��o da bola
     * @param posicao_jogador_esquerda posi��o do jogador da esquerda
     * @param posicao_jogador_direita posi��o do jogador da direita
     * @param posicao_jogador_direita_pares posi��o do segundo jogador da direita
     * @param posicao_jogador_esquerda_pares posi��o do segundo jogador da esquerda
     */
    public Tabuleiro(Posicao posicao_da_bola,Posicao posicao_jogador_esquerda,
    Posicao posicao_jogador_direita,Posicao posicao_jogador_direita_pares,Posicao posicao_jogador_esquerda_pares){
        // super("Virtual Tennis 2004");
        
        this.posicao_bola = posicao_da_bola;
        this.posicao_jogador_direita = posicao_jogador_direita;
        this.posicao_jogador_esquerda = posicao_jogador_esquerda;
        this.posicao_jogador_direita_pares = posicao_jogador_direita_pares;
        this.posicao_jogador_esquerda_pares = posicao_jogador_esquerda_pares;
        
        imagem_bola = Toolkit.getDefaultToolkit().getImage("Imagens/bola.gif");
        imagem_campo = Toolkit.getDefaultToolkit().getImage("Imagens/campo.gif");
        imagem_jogador_dir = Toolkit.getDefaultToolkit().getImage("Imagens/jogadordireito.gif");
        imagem_jogador_esq = Toolkit.getDefaultToolkit().getImage("Imagens/jogadoresquerdo.gif");
        
        // MediaTracker � o sistem que se encarrega de carregar as imagens devidamente
        MediaTracker media_tracker = new MediaTracker(this);
        
        // Regista a imagem no MediaTracker
        media_tracker.addImage(imagem_campo,0);
        media_tracker.addImage(imagem_bola,1);
        media_tracker.addImage(imagem_jogador_dir,2);
        media_tracker.addImage(imagem_jogador_esq,3);
        
        // Espera at� as imagens ter acabado de carregar
        try{ media_tracker.waitForAll(); }
        catch(Exception e){
            System.out.println("Erro Ao carregar imagens");
            System.exit(0);
        }
        
        dimensao_do_campo = new Dimensao(imagem_campo.getWidth(this)
        ,imagem_campo.getHeight(this));
        
        dimensao_do_jogador = new Dimensao(imagem_jogador_dir.getWidth(this)
        ,imagem_jogador_dir.getHeight(this));
        
        dimensao_da_bola = new Dimensao(imagem_bola.getWidth(this)
        ,imagem_bola.getHeight(this));
    }
    /**
     * Inspector para dimensao do campo.
     * Corresponde � dimens�o da imagem do campo
     * @return Dimens�o do campo
     */
    public Dimensao dimensaoDoCampo(){
        return dimensao_do_campo;
    }
    /**
     * Inspector para dimensao do campo.
     * Corresponde a dimens�o da imagem da bola
     * @return Dimens�o da bola
     */
    public Dimensao dimensaoDaBola(){
        return dimensao_da_bola;
    }
    /**
     * Inspector para dimensao do jogador.
     * Corresponde � dimens�o da imagem do jogador.
     * @return Dimens�o do jogador
     */
    public Dimensao dimensaoDoJogador(){
        return dimensao_do_jogador;
    }
    /**
     * Altera as posi��es dos jogadores para as posi��es iniciais
     **/
    public void poeJogadoresNaPosicaoInicial() {
        
        if (posicao_jogador_esquerda != null) {
            posicao_jogador_esquerda.alteraX(50);
            posicao_jogador_esquerda.alteraY(80);
        }
        
        if (posicao_jogador_direita != null) {
            posicao_jogador_direita.alteraX(500);
            posicao_jogador_direita.alteraY(210);
        }
        
        if (posicao_jogador_esquerda_pares != null) {
            posicao_jogador_esquerda_pares.alteraX(200);
            posicao_jogador_esquerda_pares.alteraY(210);
        }
        if (posicao_jogador_direita_pares != null) {
            posicao_jogador_direita_pares.alteraX(360);
            posicao_jogador_direita_pares.alteraY(80);
        }
    }
    /**
     * Sobrecarga ao m�todo update da classe JPanel,
     * este m�todo � chama pelo repaint, a sobrecarga a este metodo tem como objectivo,
     * n�o apagar o ecra a cada vez que este � pintado, cada vez que � chamado o repaint
     * @param graphics superficie a pintar
     */
    public void update(Graphics graphics){
    }
    /**
     * Sobrecarga ao metodo getWidth da classe JPanel
     * @return largura do campo
     */
    public int getWidth(){
        return dimensao_do_campo.largura();
    }
    /**
     * Sobrecarga ao metodo getHeight da classe JPanel
     * @return altura do campo
     */
    public int getHeight(){
        return dimensao_do_campo.altura();
    }
    /**
     * sobrecarga ao metodo paint da classe JPanel
     * assim � possivel desenhar directamente no Panel
     * @param graphics superficie a pintar
     */
    public void paint(Graphics graphics) {
        
        /*  inicializar os objectos relativos a double buffering*/
        if(imagem_double_buffering == null){
            imagem_double_buffering = createImage(dimensao_do_campo.largura(),dimensao_do_campo.altura());
            graphics_double_buffering = imagem_double_buffering.getGraphics();
        }
        
        /* desenha o fundo */
        graphics_double_buffering.drawImage(imagem_campo, 0, 0, this);
        
        /* desenha a bola */
        graphics_double_buffering.drawImage(imagem_bola,posicao_bola.valorDeX(),posicao_bola.valorDeY(), this);
        
        /* desenha os jogadores */
        graphics_double_buffering.drawImage(imagem_jogador_esq,posicao_jogador_esquerda.valorDeX(),posicao_jogador_esquerda.valorDeY(), this);
        graphics_double_buffering.drawImage(imagem_jogador_dir,posicao_jogador_direita.valorDeX(),posicao_jogador_direita.valorDeY(), this);
        
        if (posicao_jogador_esquerda_pares != null)
            graphics_double_buffering.drawImage(imagem_jogador_esq,posicao_jogador_esquerda_pares.valorDeX(),posicao_jogador_esquerda_pares.valorDeY(), this);
        if (posicao_jogador_direita_pares != null)
            graphics_double_buffering.drawImage(imagem_jogador_dir,posicao_jogador_direita_pares.valorDeX(),posicao_jogador_direita_pares.valorDeY(), this);
        
        /* desenha a imagem resultante no panel */
        graphics.drawImage(imagem_double_buffering, 0, 0, this);
    }
    
    /**
     * Procedimento chave na sincroniza��o,
     * funciona como porta de acesso ao campo
     * este m�todo tem de ser acedido antes de aceder a alguma zona critica.
     * @param id_do_utilizador identificacao do utilizador
     *
     */
    public synchronized void iniciaAcesso(int id_do_utilizador){
        /* reter as threads, no caso do objecto estar ocupado */
        while (utilizador_do_objecto != ninguem) {
            try {
                //System.out.println("ID retido: " + id_do_utilizador);
                wait();
            } catch (InterruptedException excepcao) { }
        }
        utilizador_do_objecto = id_do_utilizador;
        //System.out.println("ID iniciou: " + id_do_utilizador);
    }
    /**
     * Inspector para a posicao de um jogador
     * @param id_do_jogador identifica��o do jogador
     * @return posicao do jogador corresponente a id_do_jogador
     */
    public Posicao posicaoDoJogador(int id_do_jogador){
        Posicao posicao = null;
        
        switch(id_do_jogador){
            case 1: {
                posicao = posicao_jogador_esquerda;
                break;
            }
            case 2: {
                posicao = posicao_jogador_direita;
                break;
            }
            case 4: {
                posicao = posicao_jogador_direita_pares;
                break;
            }
            case 5: {
                posicao = posicao_jogador_esquerda_pares;
                break;
            }
        }
        return posicao;
    }
    /**
     * Inspector para a posicao da bola
     * @return posicao da bola
     */
    public Posicao posicaoDaBola(){
        return posicao_bola;
    }
    /**
     * M�todo para re-pintar o Painel
     */
    public void actualizaJanela(){
        repaint();
    }
    /**
     * M�todo que deve chamado no fim de acesso ao objecto partilhado
     */
    public synchronized void terminaAcesso(){
        // System.out.println("ID saiu: " + utilizador_do_objecto);
        utilizador_do_objecto = ninguem;
        
        /*
         * avisa as threads adormecidas que o objecto foi liberado,
         * de seguida as threads concorrem pelo objecto,
         * a primeira a ter posse do objecto ganha o lock do objecto(ver threads no tut sun),
         * as outras voltam a adormecer
         * http://java.sun.com/docs/books/tutorial/essential/threads/monitors.html
         *
         */
        
        notifyAll(); // podemos usar tb o notify();
    }
}
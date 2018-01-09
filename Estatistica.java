import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import sun.audio.*;
import java.io.*;


/**
 * Classe Estatistica, contabiliza os pontos de cada jogador
 * assim como o numero máximo de tacadas num ponto
 * dá informações do nome dos jogadores e das equipas
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Estatistica extends JPanel {
    
    private Image digitos[];
    private Image buffer;
    private Graphics g_buffer;
    private String equipa_esquerda, equipa_direita;
    private String jogador_esquerda1,jogador_esquerda2,jogador_direita1,jogador_direita2;
    private int pontos_direita,pontos_esquerda, max_tacadas;
    private Tenis tenis;
    
    /**
     * Constructor da classe.
     * <p>
     * @param tenis menu principal do jogo
     * @param equipa_esquerda nome da equipa da esquerda
     * @param equipa_direita nome da equipa da direita
     * @param jogador_esquerda1 nome do jogador um na esquerda
     * @param jogador_esquerda2 nome do jogador dois da esquerda (null quando nao existe)
     * @param jogador_direita1 nome do jogador um da direita
     * @param jogador_direita2 nome do jogador dois da direita (null quando nao existe)
     */
    public Estatistica(Tenis tenis,String equipa_esquerda, String equipa_direita, String jogador_esquerda1,
    String jogador_esquerda2, String jogador_direita1, String jogador_direita2){
        this.tenis = tenis;
        this.equipa_esquerda = equipa_esquerda;
        this.equipa_direita = equipa_direita;
        this.jogador_esquerda1 = jogador_esquerda1;
        this.jogador_esquerda2 = jogador_esquerda2;
        this.jogador_direita1 = jogador_direita1;
        this.jogador_direita2 = jogador_direita2;
        
        digitos = new Image[10];
        
        //carregar as imagens dos digitos
        for(int i = 0; i < 10;i++)
            digitos[i] = carregaImagem("Imagens/digitos/" + i + ".gif");
    }
    
    /** Faz reset aos pontos da estatistica
     *  poe os pontos das duas equipas a zero
     */
    public void reset(){
        pontos_direita = 0;
        pontos_esquerda = 0;
    }
    
    /** Carrega uma imagem para a memória
     * @param img imagem a carregar
     */
    public Image carregaImagem(String img) {
        Image imagem = Toolkit.getDefaultToolkit().getImage(img);
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(imagem,0);
        
        try{
            tracker.waitForID(0);
        }
        catch(InterruptedException e){}
        return imagem;
    }
    
    /** Desenha um digito (0 a 9) no buffer
     * @param digit numero do digito a desenhar
     * @param x posicao no eixo dos XX onde o digito vai ser desenhado
     */
    public void desenhaDigito(int digit, int x) {
        g_buffer.drawImage(digitos[digit],x,32,this);
    }
    
    /** contabiliza mais uma ponto para a equipa que o marcou
     * se o numero de tacadas desse ponto for maior que o numero
     * de tacadas maximo feito anteriormente o valor da tacadas é actualizado
     * e verifica se o jogo terminou
     * @param equipa equipa que marcou o ponto (1 ou 2)
     * @param tacadas numero de  tacadas feitas no ponto
     */
    public void contabilizaPontoETacadasEVerificaFimDeJogo(int equipa,int tacadas){
        if (equipa == Tabuleiro.jogador_da_esquerda)
            ++pontos_esquerda;
        else
            ++pontos_direita;
        
        if (tacadas > max_tacadas)
            this.max_tacadas = tacadas;
        
        if ((pontos_esquerda == 9) || (pontos_direita == 9))
            tenis.terminouJogo(equipa);
        repaint();
    }
    
    
    /** devolve o total de pontos de uma equipa
     * @param equipa equipa a qual queremos saber os pontos
     * @return numero de pontos da equipa
     */
    public int pontos(int equipa){
        int pontos;
        if(equipa == Tabuleiro.jogador_da_direita)
            pontos = pontos_direita;
        else
            pontos = pontos_esquerda;
        return pontos;
    }
    
    /** Devolve a largura do Painel da estatística
     * @return largura do painel da estatística
     */
    public int getWidth(){
        return 592;
    }
    
    /** Devolve a altura do Painel da estatística
     * @return altura do painel da estatística
     */
    public int getHeight(){
        return 170;
    }
    
    /** Pinta a estatística no ecra
     * inclui o nome das equipas, nome dos jogadores
     * pontos de cada equipa e número máximo de tacadas dadas num ponto
     */
    public void paint(Graphics g) {
        
        if(buffer == null){
            buffer = createImage(getWidth(),getHeight());
            g_buffer = buffer.getGraphics();
        }
        
        //Desenha quadrado com fundo
        g_buffer.setColor(new Color(105,0,0));
        g_buffer.fillRect(0,0,getWidth(),getHeight());
        
        //Desenha contorno do quadrado
        g_buffer.setColor(Color.WHITE);
        g_buffer.drawRect(10,20,getWidth() - 30, 65);
        
        Font equipas = new Font("Helvetica", Font.BOLD,35);
        Font jogadores = new Font("Helvetica", Font.ITALIC, 15);
        g_buffer.setFont(equipas);
        g_buffer.setColor(Color.BLUE);
        g_buffer.drawString(equipa_esquerda,18,65);
        
        g_buffer.setFont(jogadores);
        g_buffer.drawString("Jogador 1: " + jogador_esquerda1,30,105);
        if (jogador_esquerda2!=null)
            g_buffer.drawString("Jogador 2: " + jogador_esquerda2,30,125);
        
        g_buffer.setFont(equipas);
        g_buffer.setColor(Color.RED);
        g_buffer.drawString(equipa_direita, 380,65);
        
        g_buffer.setFont(jogadores);
        g_buffer.drawString("Jogador 1: " + jogador_direita1,370,105);
        if (jogador_direita2!=null)
            g_buffer.drawString("Jogador 2: " + jogador_direita2,370,125);
        
        g_buffer.setFont(equipas);
        g_buffer.setColor(Color.white);
        g_buffer.drawString("Vs",(getWidth() / 2) - 30,65);
        
        g_buffer.setFont(jogadores);
        g_buffer.drawString("Número máximo de jogadas num ponto:  " + max_tacadas,20,155);
        
        desenhaDigito(pontos_esquerda,(getWidth() / 2) - 70);
        desenhaDigito(pontos_direita,(getWidth() / 2 + 20));
        
        //copia o buffer para o encra
        g.drawImage(buffer,0,0, this);
    }
}
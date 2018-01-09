import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;


/**
 * Classe que representa o contador de tempo do jogo
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Relogio extends JPanel {
    private Adormece adormece_minutos;
    private Adormece adormece_dots;
    private Image digitos[], publico;
    private Image buffer;
    private Graphics g_buffer;
    private boolean flash;
    private int sec,min;
    private int x;
    private boolean em_pausa = false;
    private Tenis tenis;
    /**
     * Constructor da classe
     * constroi um relogio.
     * @param tenis instancia da classe tenis
     */
    public Relogio(Tenis tenis){
        sec = 0;
        min = 2;
        x=-255;
        flash = true;
        digitos = new Image[12];
        this.tenis = tenis;
        em_pausa = false;
        //carregar as imagens dos digitos
        for(int i = 0; i < 10; ++i)
            digitos[i] = carregaImagem("Imagens/digitos/"+i+".gif");
        
        digitos[10] = carregaImagem("Imagens/digitos/dot_p.gif");
        digitos[11] = carregaImagem("Imagens/digitos/dot_a.gif");
        publico = carregaImagem("Imagens/publico.gif");
    }
    
    /**
     * muda o estado pausa do relogio
     * se estiver em pausa o relogio está parado
     * caso contrário o relogio está a descontar o tempo de jogo
     * @param pausa estado do relogio (true ou false)
     */
    public void mudaEstadoPausa(boolean pausa){
        em_pausa = pausa;
    }
    /**
     * Inicia o relogios
     * cria as threads adormece
     */
    public void inicia(){
        adormece_dots = new Adormece(this,"relogio",300);
        adormece_dots.start();
        adormece_minutos = new Adormece(this,"relogio2",1150);
        adormece_minutos.start();
    }
    /**
     * Faz reset ao tempo do contador
     * poe o relogio de novo nos 2 minutos (tempo do jogo)
     */
    public void reset(){
        sec = 0;
        min = 2;
        x=-255;
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
    
    /** Desenha um digito no buffer
     * @param digit numero do digito a desenhar
     * @param x posição no eixo dos XX do digito a desenhar
     */
    public void desenhaDigito(int digit, int x) {
        g_buffer.drawImage(digitos[digit],x,25,this);
    }
    
    /** Actualiza o estado do relogio
     * @param numero numero da thread adormece (1 thread dos dots, 2 thread dos digitos)
     */
    public synchronized void actualizaRelogio(int numero) {
        if(!em_pausa){
            if ((min == 0) && (sec == 0)){
                em_pausa = true;
                tenis.terminouTempo();
            }
            if (numero == 2) {
                --sec;
                if (sec == -1){
                    if(min != 0)
                        --min;
                    sec = 59;
                }
            }
            else {
                repaint();
            }
        }
    }
    /** Pinta no ecra o contador descrescente do jogo
     * @param g buffer onde vai ser pintado
     */
    public void paint(Graphics g) {
        
        
        flash = !flash;
        
        String secStr;
        if(sec < 10)
            secStr="0"+sec;
        else
            secStr=""+sec;
        
        if(buffer == null){
            buffer = createImage(getWidth(),getHeight());
            g_buffer = buffer.getGraphics();
        }
        //Desenha publico
        g_buffer.drawImage(publico,0,0,this);
        
        x = x+5;
        
        Font texto_rotativo = new Font("Helvetica", Font.BOLD, 12);
        g_buffer.setFont(texto_rotativo);
        g_buffer.setColor(Color.BLACK);
        g_buffer.drawString("Virtua Tennis 4 - by Houdini & Ryven & Nuno...",x,80);
        if (x >= 580)
            x = -255;
        
        //desenha minutos e segundos
        int posicao_do_relogio_X = 240;
        desenhaDigito(min,posicao_do_relogio_X);
        desenhaDigito(Integer.parseInt(secStr.substring(0,1)),posicao_do_relogio_X + 37);
        desenhaDigito(Integer.parseInt(secStr.substring(1,2)),posicao_do_relogio_X + 67);
        
        //os dois pontinhos.. (dots)
        if(flash)
            g_buffer.drawImage(digitos[11],posicao_do_relogio_X+30,25,this);
        else
            g_buffer.drawImage(digitos[10],posicao_do_relogio_X+30,25,this);
        
        
        //copia o buffer para o encra
        g.drawImage(buffer,0,0, this);
    }
    
    /** Sobrecarga ao metodo finalize para matar as threads
     */
    public void destroi(){
        adormece_minutos.mata();
        adormece_dots.mata();
        adormece_dots = null;
        adormece_minutos = null;
        tenis = null;
    }
}
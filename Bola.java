import java.awt.*;
import java.awt.event.*;
import java.util.*;
import sun.audio.*;
import java.io.*;


/**
 * Classe bola
 * representa uma bola , com uma posicao no eixo do X
 * e uma posicao no eixo Y, que anda num tabuleiro com
 * uma velocidade constante e quando bate num jogador
 * ou nos limites do tabuleiro muda de direcção.
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Bola extends Thread {
    
    private int id_da_bola;
    private int velocidade_da_bola_X;
    private int velocidade_da_bola_Y;
    private int numero_de_tacadas;
    private Posicao posicao_da_bola;
    private Tabuleiro campo;
    private Dimensao dimensao_do_campo;
    private Dimensao dimensao_da_bola;
    private Dimensao jogadores;
    private int bola_com_largura;
    private int bola_sem_largura;
    private int bola_com_altura;
    private int bola_sem_altura;
    private boolean viva = true;
    private Estatistica estatistica;
    private boolean em_pausa = false;
    
    /**
     * Constructor da classe.
     * <p>
     * @param campo tabuleiro de jogo
     * @param id_da_bola identificação da bola (3)
     * @param nome nome da bola
     * @param posicao posicao da bola no campo
     */
    public Bola(Tabuleiro campo,int id_da_bola,String nome,Posicao posicao) {
        super(nome);
        this.id_da_bola = id_da_bola;
        this.posicao_da_bola = posicao;
        this.campo = campo;
        dimensao_do_campo = campo.dimensaoDoCampo();
        dimensao_da_bola = campo.dimensaoDaBola();
    }
    
    /**
     * Constructor da classe.
     * <p>
     * @param id_da_bola identificação da bola (3)
     * @param nome nome da bola
     */
    public Bola(int id_da_bola,String nome) {
        super(nome);
        this.id_da_bola = id_da_bola;
    }
    
    /**
     * "mata" a bola, a bola só anda se estiver "viva"
     * faz a bola parar.
     */
    public void mata(){
        viva = false;
    }
    
    /**
     * muda o estado pausa da bola
     * se em_pause for true, a bola está parada
     * caso contrário a bola está a andar
     * @param pausa estado da bola (true ou false)
     */
    public void mudaEstadoPausa(boolean pausa){
        em_pausa = pausa;
    }
    
    
    /**
     * carrega um ficheiro de som(.wav .au ,etc) e toca esse som
     * @param som ficheiro de som a carregar
     **/
    public static void som(String som){
        //ponto.au bate.wav fimdejogo.wav
        try {
            InputStream in = new FileInputStream(som);
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);
        } catch(IOException excepcao) {}
    }
    
    /**
     * Regista na bola a estatistica para que
     * a bola possa dar informações sobre os pontos
     * @param estatistica estatistica do jogo
     */
    public void registaEstatistica(Estatistica estatistica){
        this.estatistica = estatistica;
        
    }
    
    /** Devolve a posicao da bola no tabuleiro
     * @return posicao da bola no tabuleiro
     */
    public Posicao posicaoDaBola(){
        return posicao_da_bola;
    }
    
    /** Diz á bola qual a sua Posicao
     * @param posicao nova posicao da bola
     */
    public void registaPosicao(Posicao posicao) {
        posicao_da_bola = posicao;
    }
    
    /**
     * Regista na bola o tabuleiro a qual pertence esta bola
     * @param campo tabuleiro a qual pertence a bola
     */
    public void registaCampo(Tabuleiro campo){
        this.campo = campo;
    }
    
    /**
     * Devolve a velocidade da bola no eixo dos XX
     * @return velocidade da bola no eixo dos XX
     */
    public int velocidadeDaBolaX() {
        return velocidade_da_bola_X;
    }
    
    /** Gera numeros aleatorios inteiros entre minimo e maximo sem contar com zero
     * @param minimo numero minimo do valor a gerar
     * @param maximo numero maximo do valor a gerar
     * @return numero aleatório gerado com intervalo [minimo,maximo] excepto 0
     */
    public static int rand(int minimo, int maximo) {
        
        int i;
        do {
            Random rn = new Random();
            int n = maximo - minimo + 1;
            i = rn.nextInt() % n;
            if (i < 0)
                i = -i;
        } while (minimo + i == 0);
        
        return minimo + i;
    }
     
    /** Altera a posicao no eixo dos YY quando colido com o jogador, e indica se bateu no jogador
     * @param posicao_jogador Posicao do jogador
     * @param e_da_direita indica se o jogador é o jogador da direita
     * @return devolve true se a bola colidiu com o jogador ou false caso não tenha colidido.
     */
    public boolean alteraPosicaoYQuandoBateNoJogador(Posicao posicao_jogador, boolean e_da_direita) {
        int jogador_com_largura = posicao_jogador.valorDeX() + jogadores.largura();
        int jogador_sem_largura = posicao_jogador.valorDeX();
        int jogador_com_altura = posicao_jogador.valorDeY() + jogadores.altura();
        int jogador_sem_altura = posicao_jogador.valorDeY();
        
        boolean bola_em_direccao_correcta;
        if( (e_da_direita && (velocidade_da_bola_X == 1)) || (!e_da_direita && (velocidade_da_bola_X == -1)) )
            bola_em_direccao_correcta = true;
        else
            bola_em_direccao_correcta = false;
        
        boolean frente_ao_jogador;
        if(e_da_direita)
            frente_ao_jogador = (bola_com_largura >= jogador_sem_largura) && (bola_com_largura <= jogador_com_largura);
        else
            frente_ao_jogador = (bola_sem_largura <= jogador_com_largura) && (bola_sem_largura >= jogador_sem_largura);
        
        boolean na_linha_do_jogador = (bola_com_altura >= jogador_sem_altura) && (bola_sem_altura <= jogador_com_altura);
        
        
        if (frente_ao_jogador && na_linha_do_jogador && bola_em_direccao_correcta) {
            velocidade_da_bola_X *= -1;
            ++numero_de_tacadas;
            if ((bola_sem_altura + (dimensao_da_bola.altura() / 2)) > (jogador_com_altura-20))
                velocidade_da_bola_Y=1;
            
            else if ((bola_sem_altura + (dimensao_da_bola.altura() / 2)) < (jogador_sem_altura+20))
                velocidade_da_bola_Y = -1;
            else if  ( (bola_sem_altura + (dimensao_da_bola.altura() /2) ) > (jogador_sem_altura+28) &&
            (bola_sem_altura + (dimensao_da_bola.altura()/2)) < (jogador_com_altura-28) ) {
                // nao permite que a bola ande com Y=0 para sempre (util no CPU vs CPU)
                if (velocidade_da_bola_Y == 0)
                    velocidade_da_bola_Y = rand(-1,1);
                else
                    velocidade_da_bola_Y = 0;
            }
            else 
                if (velocidade_da_bola_Y == 0)
                    velocidade_da_bola_Y = rand(-1,1);
            
            
        }
        
        return (frente_ao_jogador && na_linha_do_jogador && bola_em_direccao_correcta);
    }
    
    /** Reinicia as definicoes da bola
     */
    public void reset(){
        
        numero_de_tacadas = 0;
        
        // decide se a bola vai para a direita ou para a esquerda no inicio
        velocidade_da_bola_X = rand(-1,1);
        
        // decide se a bola vai para cima ou para baixo no inicio
        velocidade_da_bola_Y = rand(-1,1);
        
        posicao_da_bola.alteraY(rand(0,dimensao_do_campo.altura()-20));
        posicao_da_bola.alteraX(dimensao_do_campo.largura()/2);
        
        campo.poeJogadoresNaPosicaoInicial();
        campo.actualizaJanela();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {}
    }
    
    /**
     * Método run da thread
     * Vida da bola, poe a bola a andar com velocidade constante
     * quando colide com algum jogador ou com os limties do tabuleiro
     * altera a sua trajectoria
     */
    public void run() {
        
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        dimensao_do_campo = campo.dimensaoDoCampo();
        dimensao_da_bola = campo.dimensaoDaBola();
        jogadores = campo.dimensaoDoJogador();
        
        reset();
        
        while (viva) {
            if(!em_pausa){
                campo.iniciaAcesso(id_da_bola);
                
                Posicao jogador_esquerda = campo.posicaoDoJogador(Tabuleiro.jogador_da_esquerda);
                Posicao jogador_direita = campo.posicaoDoJogador(Tabuleiro.jogador_da_direita);
                Posicao jogador_esquerda_pares = campo.posicaoDoJogador(Tabuleiro.jogador_da_esquerda_pares);
                Posicao jogador_direita_pares = campo.posicaoDoJogador(Tabuleiro.jogador_da_direita_pares);
                
                bola_com_largura = posicao_da_bola.valorDeX() + dimensao_da_bola.largura();
                bola_sem_largura = posicao_da_bola.valorDeX();
                bola_com_altura = posicao_da_bola.valorDeY() + dimensao_da_bola.altura();
                bola_sem_altura = posicao_da_bola.valorDeY();
                
                /* voltar para tras quando bate de lado */
                if ((bola_com_largura >= dimensao_do_campo.largura()) || (bola_sem_largura <= 0)) {
                    som("Sons/ponto.au");
                    if (bola_sem_largura <=0)
                        estatistica.contabilizaPontoETacadasEVerificaFimDeJogo(Tabuleiro.jogador_da_direita,numero_de_tacadas);
                    else
                        estatistica.contabilizaPontoETacadasEVerificaFimDeJogo(Tabuleiro.jogador_da_esquerda,numero_de_tacadas);
                    reset();
                }
                
                /* voltar para tras quando bate em cima ou em baixo do tabuleiro */
                if ((bola_com_altura >= dimensao_do_campo.altura()) || (bola_sem_altura <= 0))
                    velocidade_da_bola_Y *= -1;
                
                alteraPosicaoYQuandoBateNoJogador(jogador_esquerda,false);
                
                if (jogador_esquerda_pares != null)
                    alteraPosicaoYQuandoBateNoJogador(jogador_esquerda_pares,false);
                
                alteraPosicaoYQuandoBateNoJogador(jogador_direita,true);
                
                if (jogador_direita_pares != null)
                    alteraPosicaoYQuandoBateNoJogador(jogador_direita_pares,true);
                
                posicao_da_bola.alteraX(posicao_da_bola.valorDeX() + velocidade_da_bola_X);
                posicao_da_bola.alteraY(posicao_da_bola.valorDeY() + velocidade_da_bola_Y);
                
                campo.actualizaJanela();
                campo.terminaAcesso();
                
                try {
                    Thread.sleep(4);
                } catch (InterruptedException ex) {}
            }
            else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            }
        }
        estatistica = null;
        campo = null;
    }
}
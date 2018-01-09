import java.awt.*;


/**
 * A Classe CPU é uma classe derivada da classe jogador
 * e representa o jogador comanda-do pelo computador
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */

public class Cpu extends Jogador {
    
    private Bola bola;
    private long dificuldade = 4;
    
    /**
     * Constructor da classe Cpu
     * controi um jogador comandado pelo computador
     * <p>
     * @param bola bola do tabuleiro
     * @param id_do_jogador identificação do jogador (int de 1,2,4,5)
     * @param nome nome do jogador
     * @param posicao posicao do Jogador
     */
    public Cpu(Bola bola, Tabuleiro campo, int id_do_jogador,String nome,Posicao posicao){
        super(campo,id_do_jogador,nome,posicao);
        this.bola = bola;
    }
    
    /**
     * Constructor da classe Cpu
     * controi um jogador comandado pelo computador
     * <p>
     * @param id_do_jogador identificação do jogador (int de 1,2,4,5)
     * @param nome nome do jogador
     */
    public Cpu(int id_do_jogador,String nome){
        super(id_do_jogador,nome);
    }
    
    
    /**
     * Regista a bola no CPU
     * @param bola bola do jogo
     */
    public void registaBola(Bola bola){
        this.bola = bola;
    }
    
    /**
     * Devolve o tipo de jogador (Cpu)
     * @return cpu tipo de jogador
     */
     public String tipoDeJogador() {
        return "Cpu";
    }
    
     
    public void modificaDificuldade(long dificuldade) {
        this.dificuldade = dificuldade;
    }
    
    
    /**
     * Método run da thread (chamado pelo .start() )
     * o cpu segue a direção do eixo Y da bola quando a bola
     * esta a vir na sua direcção, quando a bola está a ir em
     * direcção contrária o cpu anda para a frente
     * e para trás no eixo X
     */
    /* método run da thread */
    public void run(){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Dimensao dimensao_do_jogador = campo.dimensaoDoJogador();
        Dimensao dimensao_do_campo = campo.dimensaoDoCampo();
        Dimensao dimensao_do_tabuleiro = campo.dimensaoDoCampo();
        
        int meio_do_jogador = dimensao_do_jogador.altura()/2;
        int meio_do_tabuleiro = dimensao_do_campo.altura()/2;
        int meio_do_tabuleiro_Y = dimensao_do_campo.largura()/2;
        
        boolean avanca = true;
        
        while(viva) {
            if(!em_pausa){
                campo.iniciaAcesso(id_do_jogador);
                Posicao posicao_da_bola = campo.posicaoDaBola();
                
                //se a bola vier na direcção do CPU...
                if (bola.velocidadeDaBolaX() == 1 && posicao.valorDeX() > meio_do_tabuleiro_Y
                || bola.velocidadeDaBolaX() == -1 && posicao.valorDeX() < meio_do_tabuleiro_Y)
                    if (((posicao.valorDeY() + meio_do_jogador) >= posicao_da_bola.valorDeY()) && (posicao.valorDeY() > 0))
                        posicao.alteraY(posicao.valorDeY() -1);
                    else {
                        if (posicao.valorDeY() + dimensao_do_jogador.altura() < dimensao_do_tabuleiro.altura())
                            posicao.alteraY(posicao.valorDeY() + 1);
                    }
                //depois de bater a bola o CPU anda para a frente e para trás
                else
                    if (avanca) {
                        posicao.alteraX(posicao.valorDeX()+1);
                        if (posicao.valorDeX()==meio_do_tabuleiro_Y-150)
                            avanca = false;
                        if (posicao.valorDeX() == dimensao_do_campo.largura()-20)
                            avanca = false;
                    }
                    else {
                        posicao.alteraX(posicao.valorDeX()-1);
                        if (posicao.valorDeX()==10)
                            avanca = true;
                        if (posicao.valorDeX() == meio_do_tabuleiro_Y+150 )
                            avanca = true;
                    }
                
                campo.actualizaJanela();
                campo.terminaAcesso();
                
                try {
                    Thread.sleep(dificuldade); //4 já é dificil, 5 accesivel, 6 faci
                }catch (InterruptedException ex) {}
            }
            else{
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex) {}
            }
        }
        campo = null;
        bola = null;
    }
}


import java.awt.*;
/**
 * Classe abstracta que representa um jogador de tenis
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public abstract class Jogador extends Thread {
    protected String nome;
    protected int id_do_jogador;
    protected Posicao posicao;
    protected Tabuleiro campo;
    protected boolean viva = true;
    protected boolean em_pausa = false;
    /**
     * Constructor da classe.
     * <p>
     * @param campo tabuleiro de jogo
     * @param id_do_jogador identificação do jogador (int de 1,2,4,5)
     * @param nome nome do jogador
     * @param posicao posicao do Jogador
     */
    public Jogador(Tabuleiro campo,int id_do_jogador,String nome,Posicao posicao) {
        super(nome);
        this.nome = nome;
        this.id_do_jogador = id_do_jogador;
        this.posicao = posicao;
        this.campo = campo;
    }
    /**
     * Constructor da classe.
     * <p>
     * @param id_do_jogador identificação do jogador (int de 1 a 4)
     * @param nome nome do jogador
     */
    public Jogador(int id_do_jogador,String nome) {
        this.nome = nome;
        this.id_do_jogador = id_do_jogador;
    }
    /**
     * Inspector para o nome do jogador
     * @return nome do jogador
     */
    public String nome(){
        return nome;
    }
    /**
     * Inspector para o id_do_jogador
     * @return id do jogador
     */
    public int idDoJogador(){
        return id_do_jogador;
    }
    /**
     * Inspector para a posição do jogador
     * @return posicao do jogador
     */
    public Posicao posicao(){
        return posicao;
    }
    /**
     * Método para guardar a referencia da posição do jogador
     * @param posicao posicao do jogador
     */
    public void registaPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    /**
     * Método para guardar a referência do campo
     * @param campo Tabuleiro de jogo
     */
    public void registaCampo(Tabuleiro campo){
        this.campo = campo;
    }
    /**
     * procedmento para matar a thread
     */
    public void mata(){
        viva = false;
    }
    /**
     * Modificador de pausa, método usapo para por a thread em pausa, e tirara pausa.
     * @param pausa proximo estado da thread
     */
    public void mudaEstadoPausa(boolean pausa){
        em_pausa = pausa;
    }
    /**
     * Indica o tipo de Jogador
     * @return tipo de jogador
     */
    public abstract String tipoDeJogador();
}

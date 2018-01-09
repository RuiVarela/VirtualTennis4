/**
 * A classe posicao é a representação de uma posicao, definida por
 * duas coordenadas, atraves de inteiros.
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Posicao {
    private int x;
    private int y;
    /**
     * Constructor da classe.
     * <p>
     * Inicializa as coordenadas a 0.
     */
    public Posicao() {
        y = 0;
        x = 0;
    }
    /**
     * Constructor da classe segundo coordenadas
     * <p>
     * @param x Coordenada x
     * @param y Coodenada y
     */
    public Posicao(int x,int y) {
        this.y = y;
        this.x = x;
    }
    /**
     * Inspector para y
     * @return valor da coordenada y
     */
    public int valorDeY(){
        return y;
    }
    /**
     * Inspector para x
     * @return valor da coordenada x
     */
    public int valorDeX(){
        return x;
    }
    /**
     * Modificador para y
     * @param y nova coordenada y
     */
    public void alteraY(int y){
        this.y = y;
    }
    /**
     * Modificador para x
     * @param x nova coordenada x
     */
    public void alteraX(int x){
        this.x = x;
    }
    /**
     * Representacao da instância
     * @return String que caracteriza a instância
     */
    public String toString(){
        return("x: " + valorDeX() + " y: " + valorDeY());
    }
}
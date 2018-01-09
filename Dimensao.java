/**
 * A classe Dimensao � a representa��o de uma Dimens�o, definida por
 * altura e largura, atraves de inteiros.
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Dimensao {
    private int largura;
    private int altura;
    /**
     * Constructor da classe.
     * <p>
     * Inicializa a altura e largura a 0.
     */
    public Dimensao() {
        altura = 0;
        largura = 0;
    }
    /**
     * Constructor da classe segundo a altura e largura
     * <p>
     * @param altura altura da dimens�o
     * @param largura largura da dimens�o
     */
    public Dimensao(int largura,int altura) {
        this.altura = altura;
        this.largura = largura;
    }
    /**
     * Inspector para altura
     * <p>
     * @return valor da altura
     */
    public int altura(){
        return altura;
    }
    /**
     * Inspector para largura
     * <p>
     * @return valor da largura
     */
    public int largura(){
        return largura;
    }
    /**
     * Modificador para altura
     * @param altura nova altura
     */
    public void alteraAltura(int altura){
        this.altura = altura;
    }
    /**
     * Modificador para largura
     * @param largura nova largura
     */
    public void alteraLargura(int largura){
        this.largura = largura;
    }
    /**
     * Representacao da inst�ncia
     * @return String que caracteriza a inst�ncia
     */
    public String toString(){
        return("largura: " + largura() + " altura: " + altura());
    }
}

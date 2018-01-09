import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

/**
 * A classe Ajuda cria uma janela para a qual é carregado um ficheiro de texto
 * por forma a prestar ajuda aos utilizadores.
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Ajuda {
    public final static char newline = '\n';
    private JTextArea text_area;
    private JFrame janela;
    private JScrollPane scroll_pane;
    /**
     * Constructor da classe.
     * <p>
     * Chama o contructor Ajuda(x,y), com o valor 0 para ambas as coordenadas.
     */
    public Ajuda() {
        this(0,0);
    }
    /**
     * Constructor da classe.
     * <p>
     * Cria a janela na posicao x,y do ecran.
     * @param x Coordenada x
     * @param y Coodenada y
     */
    public Ajuda(int x,int y){
        janela = new JFrame("Ajuda");
        janela.getContentPane().setLayout(new FlowLayout());
        
        text_area = new JTextArea(10,45);
        text_area.setEditable(false);
        
        scroll_pane = new JScrollPane(text_area);
        scroll_pane.setPreferredSize(new Dimension(675, 220));
        scroll_pane.setWheelScrollingEnabled(true);
        
        janela.getContentPane().add(scroll_pane);
        janela.setLocation(x,y);
        janela.setResizable(false);
        janela.pack();
        janela.setVisible(true);
    }
    /**
     * Procedimento que carrega um ficheiro de texto, e exibe-o na JTextArea
     * da JFrame.
     * @param nome_do_ficheiro ficheiro a carregar
     */
    public void carregaFicheiro(String nome_do_ficheiro) {
        BufferedReader leitor = null;
        try{
            leitor = new BufferedReader(new FileReader(new File(nome_do_ficheiro)));
            String linha;
            while ((linha = leitor.readLine()) != null){
                println(linha);
            }
        }
        catch (FileNotFoundException ex) {
            println("Erro: O Ficheiro não existe");
        }
        catch (IOException ex){
            println("Erro de Leitura... ");
        }
        finally{
            /* Tratar de limpar o canal */
            try {
                leitor.close();
            }
            catch (IOException ex) {}
        }
    }
    /**
     * Modificador do estado visivel da JFrame
     * @param visivel novo estado
     */
    public void setVisible(boolean visivel){
        janela.setVisible(visivel);
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
     * procedimento que apagar a JTextArea
     */
    public void clear(){
        text_area.setText("");
    }
}


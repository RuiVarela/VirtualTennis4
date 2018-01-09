import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * Classe Adormece
 * Faz um sleep dado e actualiza o relogio de sleep em sleep
 *
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Adormece extends Thread {
    
    Relogio relogio;
    int sleep_time;
    boolean viva = true;
    
    /** Contructor da classe
     * @param relogio relogio a actualizar 
     * @param nome nome da thread adormece
     * @param sleep_time tempo do sleep (de adormecimento)
     */
    Adormece(Relogio relogio,String nome,int sleep_time) {
        super(nome);
        this.relogio = relogio;
        this.sleep_time = sleep_time;
    }
    
    /** "Mata" a thread 
     * faz parar a actualização do relogio enquanto estiver a false
     */
    void mata(){
        viva = false;
    }
    
    /** Metodo run da thread (chamado pelo .start() )
     * actualiza o relogio de acordo com o tempo de sleep 
     */
    public void run() {
        
        while(viva) {
            //halt the thread for 50 ms here
            try {
                sleep(sleep_time);
                if (sleep_time==1150)
                    relogio.actualizaRelogio(2);
                else
                    relogio.actualizaRelogio(1);
            }catch (Exception e) { } 
        }
        relogio = null;
    }
}



import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A classe Tenis representa o jogo de de ténis
 * é a main classe do projecto.
 * A classe tenis deriva de uma JFrame e
 * fisicamente corresponde ao menu principal
 * @author      Ryven
 * @author      Houdini
 * @author      Nuno
 * @version     %I%, %G%
 */
public class Tenis extends JFrame{
    private JFrame janela_de_jogo;
    
    /* componentes */
    private JButton bt_teclas;
    private JComboBox cb_tipo_de_jogador;
    private JComboBox cb_tipo_de_jogo;
    private JComboBox cb_equipa;
    private JComboBox cb_jogador_n;
    private JLabel lb_dificuldade;
    private JLabel lb_numero;
    private JLabel lb_modo;
    private JSlider sl_cpu;
    private JTextField tf_nome;
    private JTextField tf_nome_da_equipa;
    
    private String nome_da_equipa_esquerda;
    private String nome_da_equipa_direita;
    
    private Bola bola = new Bola(Tabuleiro.bola,"Bola");
    private Jogador jogador_direita = null;
    private Jogador jogador_esquerda = null;
    private Jogador jogador_direita_pares = null;
    private Jogador jogador_esquerda_pares = null;
    
    private int[] teclas;
    private boolean teclas_foram_modificadas = false;
    private ConfiguraTeclado configurador_de_teclas;
    
    private boolean pausa = false;
    private boolean terminou_jogo = false;
    private Relogio relogio = null;
    private Tabuleiro tabuleiro;
    private Estatistica estatistica;
    
    /* indica os jogadores que sao humanos */
    private boolean[] humanos = new boolean[5];
    
    /**
     * Constructor da classe
     */
    public Tenis(){
        super("Virtual Tennis 4 - Menu");
        
        /* Mata o processo ao fechar a janela principal */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iniciaComponentes();
        pack();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
        setVisible(true);
    }
    /**
     * Modificador de teclas
     * @param teclas nova combinação de teclas
     */
    public void alteraTeclas(int[] teclas){
        this.teclas = teclas;
        teclas_foram_modificadas = true;
    }
    /**
     * Procedimntos que cria um objecto ajuda
     * e carrega o ficheiro readme.txt
     */
    private void ajuda() {
        Ajuda ajuda = new Ajuda();
        ajuda.carregaFicheiro("readme.txt");
    }
    /**
     * Método chamado pelo ActionListener da ComboBox cb_tipo_de_jogo
     * regista as alterações relativas
     */
    private void comboBoxSeleccaoModo(ActionEvent evento) {
        String tipo_escolhido = (String) ((JComboBox)evento.getSource()).getSelectedItem();
        if(tipo_escolhido.equals("Singular")) {
            lb_numero.setVisible(false);
            cb_jogador_n.setSelectedIndex(0);
            cb_jogador_n.setVisible(false);
            if(cb_equipa.getSelectedItem().equals("Esquerda")) {
                jogador_esquerda_pares = null;
                humanos[4] = false;
            }
            else{
                jogador_direita_pares = null;
                humanos[3] = false;
            }
        }
        else {
            lb_numero.setVisible(true);
            cb_jogador_n.setVisible(true);
        }
    }
    /**
     * Método chamado pela ComboBox cb_tipo_de_jogador
     */
    private void comboBoxSeleccaoTipoDeJogador(ActionEvent evento) {
        String tipo_escolhido = (String) ((JComboBox)evento.getSource()).getSelectedItem();
        if(tipo_escolhido.equals("Cpu")) {
            cb_tipo_de_jogo.setSelectedIndex(0);
            cb_tipo_de_jogo.setVisible(false);
            lb_modo.setVisible(false);
            lb_dificuldade.setVisible(true);
            sl_cpu.setVisible(true);
            bt_teclas.setVisible(false);
        }
        else {
            cb_tipo_de_jogo.setVisible(true);
            lb_modo.setVisible(true);
            lb_dificuldade.setVisible(false);
            sl_cpu.setVisible(false);
            bt_teclas.setVisible(true);
        }
    }
    /**
     * Activa o configurador de teclas
     */
    private void configuraTeclas(){
        if (configurador_de_teclas == null) {
            configurador_de_teclas = new ConfiguraTeclado();
            configurador_de_teclas.registaTenis(this);
        }
        configurador_de_teclas.setVisible(true);
        configurador_de_teclas.reset();
        teclas_foram_modificadas = true;
    }
    /**
     * Procedimento chamado pelo buttonlistener do bt_valida_jogador
     */
    private void validaJogador(){
        int id_do_jogador;
        if(cb_equipa.getSelectedItem().equals("Esquerda")) {
            if(cb_jogador_n.getSelectedItem().equals("1"))
                id_do_jogador = Tabuleiro.jogador_da_esquerda;
            else
                id_do_jogador = Tabuleiro.jogador_da_esquerda_pares;
            nome_da_equipa_esquerda = tf_nome_da_equipa.getText();
        }
        else {
            if(cb_jogador_n.getSelectedItem().equals("1"))
                id_do_jogador = Tabuleiro.jogador_da_direita;
            else
                id_do_jogador = Tabuleiro.jogador_da_direita_pares;
            nome_da_equipa_direita = tf_nome_da_equipa.getText();
        }
        Jogador jogador;
        boolean e_humano = false;
        if(cb_tipo_de_jogador.getSelectedItem().equals("Cpu")) {
            jogador = new Cpu(id_do_jogador,tf_nome.getText());
            sl_cpu.getValue();
            ((Cpu)jogador).registaBola(bola);
            ((Cpu)jogador).modificaDificuldade((long) sl_cpu.getValue());
            humanos[id_do_jogador - 1] = false;
        }
        else {
            jogador = new Humano(id_do_jogador,tf_nome.getText());
            humanos[id_do_jogador - 1] = true;
            if(teclas_foram_modificadas)
                ((Humano)jogador).alteraTeclas(teclas);
        }
        switch(id_do_jogador){
            case Tabuleiro.jogador_da_esquerda:
                jogador_esquerda = jogador;
                break;
            case Tabuleiro.jogador_da_esquerda_pares:
                jogador_esquerda_pares = jogador;
                break;
            case Tabuleiro.jogador_da_direita:
                jogador_direita = jogador;
                break;
            case Tabuleiro.jogador_da_direita_pares:
                jogador_direita_pares = jogador;
                break;
        }
        teclas_foram_modificadas = false;
    }
    /**
     * procedimento chamado pelo ActionListener do bt_pausa
     */
    private void pausa(){
        if(!terminou_jogo){
            pausa = !pausa;
            bola.mudaEstadoPausa(pausa);
            relogio.mudaEstadoPausa(pausa);
            jogador_esquerda.mudaEstadoPausa(pausa);
            jogador_direita.mudaEstadoPausa(pausa);
            if(jogador_esquerda_pares != null)
                jogador_esquerda_pares.mudaEstadoPausa(pausa);
            if(jogador_direita_pares != null)
                jogador_direita_pares.mudaEstadoPausa(pausa);
        }
    }
    /**
     * procedimento chamado pelo butão regresso ao menu
     * e clikcar no fechar janela
     */
    private void destroiJanela(){
        /* desligar e matar */
        janela_de_jogo.dispose();
        bola.mata();
        jogador_esquerda.mata();
        jogador_direita.mata();
        if(jogador_esquerda_pares != null)
            jogador_esquerda_pares.mata();
        if(jogador_direita_pares != null)
            jogador_direita_pares.mata();
        
        /* reiniciar */
        bola = new Bola(Tabuleiro.bola,"Bola");
        humanos = new boolean[5];
        jogador_direita = null;
        jogador_esquerda = null;
        jogador_direita_pares = null;
        jogador_esquerda_pares = null;
        estatistica = null;
        tabuleiro = null;
        relogio.destroi();
        relogio = null;
        setVisible(true);
        System.gc();
    }
    /**
     * procedimento chamado pelo butão e recomecar
     */
    private void recomecar(){
        relogio.reset();
        relogio.repaint();
        bola.reset();
        estatistica.reset();
        estatistica.repaint();
        bola.mudaEstadoPausa(false);
        relogio.mudaEstadoPausa(false);
        jogador_esquerda.mudaEstadoPausa(false);
        jogador_direita.mudaEstadoPausa(false);
        if(jogador_esquerda_pares != null)
            jogador_esquerda_pares.mudaEstadoPausa(false);
        if(jogador_direita_pares != null)
            jogador_direita_pares.mudaEstadoPausa(false);
        terminou_jogo = false;
        pausa = false;
    }
    /**
     * Procedimento que notifica a instância que acabou o tempo
     */
    public void terminouTempo(){
        int pontos_direita = estatistica.pontos(Tabuleiro.jogador_da_direita);
        int pontos_esquerda = estatistica.pontos(Tabuleiro.jogador_da_esquerda);
        if(pontos_direita == pontos_esquerda)
            terminouJogo(Tabuleiro.ninguem);
        else if(pontos_direita > pontos_esquerda)
            terminouJogo(Tabuleiro.jogador_da_direita);
        else
            terminouJogo(Tabuleiro.jogador_da_esquerda);
    }
    /**
     * Procedimento que notifica a instância que acabou o jogo
     * @param vencedor id da equipa vencedora (1 - esquerda, 2 - direita)
     */
    public void terminouJogo(int vencedor){
        Bola.som("Sons/fimdejogo.wav");
        pausa();
        terminou_jogo = true;
        if(vencedor == Tabuleiro.ninguem)
            JOptionPane.showMessageDialog(janela_de_jogo,"Empate!");
        else{
            String equipa = "Ganhou a Equipa da ";
            if(vencedor == Tabuleiro.jogador_da_direita)
                equipa = equipa.concat("Direita : " + nome_da_equipa_direita);
            else
                equipa = equipa.concat("Esquerda : " + nome_da_equipa_direita);
            JOptionPane.showMessageDialog(janela_de_jogo,equipa);
        }
    }
    /**
     * procedimento chamado pelo ActionListener do bt_start
     * inicializa um novo jogo
     */
    private void joga(){
        
        if((jogador_esquerda != null) && (jogador_direita != null)) {
            Posicao pos_bola = new Posicao(1,1);
            Posicao pos_jogador_esquerda = new Posicao(50,80);
            Posicao pos_jogador_direita = new Posicao(500,210);
            
            Posicao pos_jogador_esquerda_pares = ((jogador_esquerda_pares == null) ? null : new Posicao(200,210));
            Posicao pos_jogador_direita_pares = ((jogador_direita_pares == null) ? null : new Posicao(360,80));
            
            tabuleiro = new Tabuleiro(pos_bola,pos_jogador_esquerda,pos_jogador_direita
            ,pos_jogador_direita_pares,pos_jogador_esquerda_pares);
            
            estatistica = new Estatistica(this,nome_da_equipa_esquerda,nome_da_equipa_direita,
            jogador_esquerda.nome(),
            ((jogador_esquerda_pares == null) ? null : jogador_esquerda_pares.nome())
            ,jogador_direita.nome(),
            ((jogador_direita_pares == null) ? null : jogador_direita_pares.nome())
            );
            
            bola.registaEstatistica(estatistica);
            
            JButton bt_regressar = new JButton("Regressar");
            bt_regressar.setBackground(new Color(105,0,0));
            bt_regressar.setFocusable(false);
            bt_regressar.setForeground(new Color(255,255,255));
            bt_regressar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    destroiJanela();
                }
            } );
            JButton bt_recomecar = new JButton("Recomecar");
            bt_recomecar.setFocusable(false);
            bt_recomecar.setBackground(new Color(105,0,0));
            bt_recomecar.setForeground(new Color(255,255,255));
            bt_recomecar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    recomecar();
                }
            } );
            JButton bt_pausa = new JButton("Pausa");
            bt_pausa.setFocusable(false);
            bt_pausa.setBackground(new Color(105,0,0));
            bt_pausa.setForeground(new Color(255,255,255));
            bt_pausa.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    pausa();
                }
            } );
            JPanel painel_dos_butoes = new JPanel();
            painel_dos_butoes.setLayout(new FlowLayout());
            
            painel_dos_butoes.add(bt_regressar);
            painel_dos_butoes.add(bt_pausa);
            painel_dos_butoes.add(bt_recomecar);
            
            /* Cria A janela de jogo */
            janela_de_jogo = new JFrame("Virtual Tennis 4");
            
            relogio = new Relogio(this);
            
            relogio.setBounds(0,0,tabuleiro.getWidth(),109);
            tabuleiro.setBounds(0,relogio.getHeight(),tabuleiro.getWidth(),tabuleiro.getHeight());
            estatistica.setBounds(0,tabuleiro.getHeight() + relogio.getHeight(),estatistica.getWidth(),estatistica.getHeight());
            
            painel_dos_butoes.setBounds(0,tabuleiro.getHeight() + relogio.getHeight() + estatistica.getHeight(),tabuleiro.getWidth(),50);
            painel_dos_butoes.setBackground(new Color(105,0,0));
            
            janela_de_jogo.getContentPane().setLayout(null);
            janela_de_jogo.getContentPane().add(relogio);
            janela_de_jogo.getContentPane().add(tabuleiro);
            janela_de_jogo.getContentPane().add(estatistica);
            janela_de_jogo.getContentPane().add(painel_dos_butoes);
            
            if(humanos[0])
                janela_de_jogo.addKeyListener((Humano)jogador_esquerda);
            if(humanos[1])
                janela_de_jogo.addKeyListener((Humano)jogador_direita);
            if(humanos[3])
                janela_de_jogo.addKeyListener((Humano)jogador_direita_pares);
            if(humanos[4])
                janela_de_jogo.addKeyListener((Humano)jogador_esquerda_pares);
            
            int altura = painel_dos_butoes.getHeight() + relogio.getHeight() + estatistica.getHeight() + tabuleiro.getHeight() + 20;
            int largura = tabuleiro.getWidth() + 10;
            
            janela_de_jogo.setBounds( (Toolkit.getDefaultToolkit().getScreenSize().width - largura) / 2
            ,0, largura,altura);
            
            janela_de_jogo.setResizable(false);
            setVisible(false);
            janela_de_jogo.setVisible(true);
            
            /* quando se clika no icon de fechar a janala de jogo */
            janela_de_jogo.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    destroiJanela();
                }
            });
            
            bola.registaCampo(tabuleiro);
            bola.registaPosicao(pos_bola);
            
            jogador_esquerda.registaCampo(tabuleiro);
            jogador_esquerda.registaPosicao(pos_jogador_esquerda);
            
            jogador_direita.registaCampo(tabuleiro);
            jogador_direita.registaPosicao(pos_jogador_direita);
            
            if(jogador_esquerda_pares != null) {
                jogador_esquerda_pares.registaCampo(tabuleiro);
                jogador_esquerda_pares.registaPosicao(pos_jogador_esquerda_pares);
                jogador_esquerda_pares.start();
            }
            
            if(jogador_direita_pares != null) {
                jogador_direita_pares.registaCampo(tabuleiro);
                jogador_direita_pares.registaPosicao(pos_jogador_direita_pares);
                jogador_direita_pares.start();
            }
            pausa = false;
            relogio.inicia();
            bola.start();
            jogador_esquerda.start();
            jogador_direita.start();
            
            tabuleiro.actualizaJanela();
        }
    }
    /**
     * Inicia os componetes Swing
     */
    private void iniciaComponentes(){
        /* Painel da esquerda */
        JPanel painel_da_esquerda = new JPanel();
        JLabel lb_equipa = new JLabel("Equipa");
        
        String[] lista_de_tipos_equipa = {"Esquerda" , "Direita" };
        
        cb_equipa = new JComboBox(lista_de_tipos_equipa);
        
        JLabel lb_nome_da_equipa = new JLabel("Nome da equipa");
        tf_nome_da_equipa = new JTextField(10);
        lb_modo = new JLabel("Modo");
        
        String[] lista_de_tipos_tipos_de_jogo = {"Singular" , "Pares" };
        cb_tipo_de_jogo = new JComboBox(lista_de_tipos_tipos_de_jogo);
        
        cb_tipo_de_jogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                comboBoxSeleccaoModo(evt);
            }
        } );
        
        JLabel lb_jogador = new JLabel("Jogador");
        
        String[] lista_de_tipos_de_jogador = {"Humano", "Cpu" };
        cb_tipo_de_jogador = new JComboBox(lista_de_tipos_de_jogador);
        
        cb_tipo_de_jogador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                comboBoxSeleccaoTipoDeJogador(evt);
            }
        } );
        
        /* Painel do meio */
        JPanel painel_do_meio = new JPanel();
        lb_numero = new JLabel("Numero");
        
        String[] lista_de_tipos_jogador_n = {"1", "2"};
        
        cb_jogador_n = new JComboBox(lista_de_tipos_jogador_n);
        JLabel lb_nome = new JLabel("Nome");
        tf_nome = new JTextField(10);
        lb_dificuldade = new JLabel("Dificuldade");
        sl_cpu = new JSlider(JSlider.HORIZONTAL,4, 6, 5);
        
        /* por info no slider*/
        Hashtable informacoes_do_slider = new Hashtable();
        informacoes_do_slider.put(new Integer(4), new JLabel("+") );
        informacoes_do_slider.put(new Integer(5), new JLabel("Speed") );
        informacoes_do_slider.put(new Integer(6), new JLabel("-") );
        sl_cpu.setLabelTable(informacoes_do_slider);
        sl_cpu.setPaintLabels(true);
        
        bt_teclas = new JButton("Definir Teclas");
        
        bt_teclas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                configuraTeclas();
            }
        } );
        
        JButton bt_valida_jogador = new JButton("Valida Jogador");
        
        bt_valida_jogador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                validaJogador();
            }
        } );
        
        JButton bt_ajuda = new JButton("Ajuda");
        
        bt_ajuda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ajuda();
            }
        } );
        
        /* Painel da Direita */
        JPanel painel_da_direita = new JPanel();
        JButton bt_start = new JButton("Start");
        
        bt_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if((jogador_esquerda != null) && (jogador_direita != null)) {
                    String jogador_esquerda2 = (jogador_esquerda_pares != null) ? "\n   Jogador 2: "+jogador_esquerda_pares.nome() : "";
                    String jogador_direita2 = (jogador_direita_pares != null) ? "\n   Jogador2: "+jogador_direita_pares.nome() : "";
                    
                    Object[] options = {"Começar jogo",
                    "Configurar de novo"};
                    int opcao_escolhida = JOptionPane.showOptionDialog(janela_de_jogo,
                    "CONSTITUIÇÃO DAS EQUIPAS:\n"
                    + "Equipa da esquerda - " + jogador_esquerda.tipoDeJogador()+ ": "+nome_da_equipa_esquerda
                    + "\n   Jogador 1: " + jogador_esquerda.nome()
                    + jogador_esquerda2
                    + "\n\nEquipa da direita - " +jogador_direita.tipoDeJogador()+ ": "+ nome_da_equipa_direita
                    + "\n   Jogador 1: " + jogador_direita.nome()
                    + jogador_direita2,
                    "Virtua Tennis 4 - By: Punisher / Ryven / Magalinio - IGE 2003 ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, // nao usa outro icon
                    options,  //matriz das opcoes
                    options[0]); //opção default
                    if (opcao_escolhida == 0)
                        joga();
                }
                else{
                    String jogador_nao_inicializado = new String();
                    if(jogador_esquerda == null)
                        jogador_nao_inicializado = jogador_nao_inicializado.concat("Esquerda");
                    if((jogador_esquerda == null) && (jogador_direita == null))
                        jogador_nao_inicializado = jogador_nao_inicializado.concat(" e ");
                    if(jogador_direita == null)
                        jogador_nao_inicializado = jogador_nao_inicializado.concat(new String("Direita"));
                    JOptionPane.showMessageDialog(janela_de_jogo,
                    "Jogadores não validados: " + jogador_nao_inicializado + "\nTem de validar todos os jogadores que pretendem entrar em jogo!");
                }
            }
        } );
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        setResizable(false);
        
        tf_nome_da_equipa.setText("Coders");
        painel_da_esquerda.setLayout(new GridLayout(4, 2, 2, 2));
        painel_da_esquerda.add(lb_equipa);
        painel_da_esquerda.add(cb_equipa);
        painel_da_esquerda.add(lb_nome_da_equipa);
        painel_da_esquerda.add(tf_nome_da_equipa);
        painel_da_esquerda.add(lb_modo);
        painel_da_esquerda.add(cb_tipo_de_jogo);
        painel_da_esquerda.add(lb_jogador);
        painel_da_esquerda.add(cb_tipo_de_jogador);
        
        getContentPane().add(painel_da_esquerda);
        
        painel_do_meio.setLayout(new GridLayout(4, 2, 2, 2));
        
        tf_nome.setText("Namespace");
        painel_do_meio.add(lb_numero);
        painel_do_meio.add(cb_jogador_n);
        painel_do_meio.add(lb_nome);
        painel_do_meio.add(tf_nome);
        painel_do_meio.add(lb_dificuldade);
        sl_cpu.setPreferredSize(new Dimension(100, 16));
        painel_do_meio.add(sl_cpu);
        painel_do_meio.add(bt_teclas);
        painel_do_meio.add(bt_valida_jogador);
        
        getContentPane().add(painel_do_meio);
        
        painel_da_direita.setLayout(new GridLayout(2, 1));
        painel_da_direita.add(bt_ajuda);
        painel_da_direita.add(bt_start);
        
        getContentPane().add(painel_da_direita);
        
        lb_dificuldade.setVisible(false);
        sl_cpu.setVisible(false);
        lb_numero.setVisible(false);
        cb_jogador_n.setVisible(false);
    }
    /**
     * Ponto de entrada no programa
     * @param argumentos algumentos +arrados ao programa
     */
    public static void main(String[] argumentos){
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        Tenis jogo = new Tenis();
    }
}

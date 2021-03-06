/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import br.ufrn.dca.controle.QuanserClient;
import br.ufrn.dca.controle.QuanserClientException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author PedroKlisley
 */

//Trava e Gráfico

//Ver erros de gráfico
//Colocar Erro de Regime
//Filtro Derivativo

//Ver anti windup PID
//Ver como fica com integração condicional

//Melhorar Simulação
//Melhorar Gráfico

public class ControleTanquesProj1 implements Runnable {
    //Ajeitar Periodo
    
    /**
     * @param args the command line arguments
     */
    
    static FrameInicial frame;
    static Controle controle;// = new Controle();
    static volatile boolean atualizar;// = false;    
    
    public void run() {
        //System.out.println("Hello from a thread!");
        int PERIODOENVIO = 100;                     //Período de leitura e escrita em ms
        long tStart, tGeralStart;                   //Mostradores de tempo
        boolean conectado = false;
        tGeralStart = System.nanoTime(); 
        
        while(true)                                 //Rotina Thread Principal  
        {
            if (conectado)
            {
                //System.out.println("Teste Conexao");
                tStart = System.nanoTime();

                configurarSimulacao(controle, tGeralStart);

                //rotinaDeControle(controle, tGeralStart, frame);

                atualizar = true;
                while(atualizar);
                frame.SetControle(controle);
                int a = PERIODOENVIO - (int)(System.nanoTime() - tStart)/1000000;
                try {
                    //System.out.println("Tempo de Rotina : " + a);
                    Thread.sleep(retornaPositivo(a));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ControleTanquesProj1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                conectado = frame.isConectado();
                System.out.println("");
            }
        }
    }
    
    static void configurarSimulacao(Controle controle, long tGeralStart)
    {
        /*****LEITURA DO SINAL*****/
        //controle.setTanqueSelecionado(2);
        controle.getSinaisEntrada()[1].setPV(controle.getSinalSaida().getPV_T2());
        controle.getSinaisEntrada()[0].setPV(controle.getSinalSaida().getPV_T1());
        //controle.getSinaisEntrada()[0].setPV(10);
        //controle.getSinaisEntrada()[1].setPV(5);
        
        /*****CÁLCULO DO SINAL*****/
        controle.calcularSinal((System.nanoTime() - tGeralStart)/1000000000.0);
    }
    
    static void rotinaDeControle(Controle controle, long tGeralStart, FrameInicial frame) //throws InterruptedException
    {
        /*****LEITURA DO SINAL*****/
        try 
        {
            for(int i = 0; i < controle.getCanaisEntrada().size(); i++)
            {
                controle.getSinaisEntrada()[(int)controle.getCanaisEntrada().get(i)].setPV(6.25*frame.getQuanserClient().read((int)controle.getCanaisEntrada().get(i)));
            }
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }

        /*****CÁLCULO DO SINAL*****/
        controle.calcularSinal((System.nanoTime() - tGeralStart)/1000000000.0);

        /*****ESCRITA DO SINAL*****/
        try 
        {
            //quanserClient.write(controle.getCanalSaida(),controle.getSinalSaida().getTensaoSaida());
            frame.getQuanserClient().write(controle.getCanalSaida(),controle.getSinalSaida().getTensaoSaida());
            System.out.println("Escreveu!");
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException 
    {
        
        /*int PERIODOENVIO = 100;                     //Período de leitura e escrita em ms
        Controle controle = new Controle();
        long tStart, tGeralStart;                   //Mostradores de tempo
        tGeralStart = System.nanoTime();            //Tempo total
        
        boolean conectado = false;*/
        
        controle = new Controle();
        atualizar = false;    
        //JFrame.setDefaultLookAndFeelDecorated(true); 
               
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        frame = new FrameInicial();
        frame.SetControle(controle);
        
        frame.setVisible(true);
        RefineryUtilities.centerFrameOnScreen(frame);

        (new Thread(new ControleTanquesProj1())).start();
        
        while(true)
        {
            if(atualizar)
            {
                frame.AtualizarDados();
                atualizar = false;
            }
        }
    }
    
    static int retornaPositivo(int numero)
    { //Previne travamento da Thread em caso da thread ultrapassar 100ms
        if (numero < 0)
        {
            return 0;
        }
        return numero;
    }
}

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
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author PedroKlisley
 */

//Ver erros de gráfico
//Colocar Erro de Regime
//Filtro Derivativo

//Ver anti windup PID
//Ver como fica com integração condicional

//Melhorar Simulação
//Melhorar Gráfico

public class ControleTanquesProj1 {
    //Ajeitar Periodo
    
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws InterruptedException 
    {
        
        int PERIODOENVIO = 100;                     //Período de leitura e escrita em ms
        Controle controle = new Controle();
        long tStart, tGeralStart;                   //Mostradores de tempo
        tGeralStart = System.nanoTime();            //Tempo total
        //QuanserClient quanserClient = null;
        boolean conectado = false;
        
        FrameInicial frame = new FrameInicial();
        frame.SetControle(controle);
        frame.setVisible(true);
        RefineryUtilities.centerFrameOnScreen(frame);

        while(true)                                 //Rotina Thread Principal  
        {
            if (conectado)
            {
                //System.out.println("Teste Conexao");
                tStart = System.nanoTime();

                double nivel = 0.0;

                /*****LEITURA DO SINAL*****/
                /***************USAR NA PLANTA**************/
                try 
                {
                    for(int i = 0; i < controle.getCanaisEntrada().size(); i++)
                    {
                        controle.getSinaisEntrada()[(int)controle.getCanaisEntrada().get(i)].setPV(6.25*frame.getQuanserClient().read((int)controle.getCanaisEntrada().get(i)));
                    }
                    //System.out.println("Leu!");
                } 
                catch (QuanserClientException ex) {
                    ex.printStackTrace();
                }
                //*/
                
                /***************SIMULAÇÃO**************
                controle.setTanqueSelecionado(2);
                controle.getSinaisEntrada()[1].setPV(controle.getSinalSaida().getPV());
                //*/

                /*****CÁLCULO DO SINAL*****/
                controle.calcularSinal((System.nanoTime() - tGeralStart)/1000000000.0);
                

                /*****ESCRITA DO SINAL*****/
                /***************USAR NA PLANTA**************
                try 
                {
                    //quanserClient.write(controle.getCanalSaida(),controle.getSinalSaida().getTensaoSaida());
                    frame.getQuanserClient().write(controle.getCanalSaida(),controle.getSinalSaida().getTensaoSaida());
                    System.out.println("Escreveu!");
                } 
                catch (QuanserClientException ex) {
                    ex.printStackTrace();
                }
                //*/

                frame.AtualizarDados();
                frame.SetControle(controle);
                int a = PERIODOENVIO - (int)(System.nanoTime() - tStart)/1000000;
                Thread.sleep(retornaPositivo(a));
            }
            else
            {
                conectado = frame.isConectado();
                System.out.println("");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import br.ufrn.dca.controle.QuanserClient;
import br.ufrn.dca.controle.QuanserClientException;
import java.util.ArrayList;

/**
 *
 * @author PedroKlisley
 */
public class Controle {
    private SinalEntrada[] sinaisEntrada;
    private SinalSaida sinalSaida;
    private ArrayList canaisEntrada;
    private int canalSaida;
    private QuanserClient quanserClient;
    private int duracao;
    private Tempo tempoClasse;
    private int canalTanque1;
    private int canalTanque2;
    private int tanqueSelecionado;
    
    public Controle()
    {
        sinalSaida = new SinalSaida();
        sinaisEntrada = new SinalEntrada[8];
        for(int i = 0; i < sinaisEntrada.length; i++)
        {
            sinaisEntrada[i] = new SinalEntrada();
        }
        canaisEntrada = new ArrayList();
        
        canaisEntrada.add(0);
        canaisEntrada.add(1);
                
        canalSaida = 0;
        tempoClasse = new Tempo();
        canalTanque1 = 0;
        canalTanque2 = 1;
        /*try 
        {
            quanserClient = new QuanserClient("10.13.99.69", 20081);
            System.out.println("Conectou!");
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }*/
    }
    
    void lerSinal()
    {
        //getTanque().setNivel(6.25);
        
        //getSinais()[i].setNivelTanque(6.25);
        try 
        {
            for(int i = 0; i < canaisEntrada.size(); i++)
            {
                sinaisEntrada[(int)canaisEntrada.get(i)].setPV(6.25*quanserClient.read(0));
            }
            System.out.println("Leu!");
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }
        
        
        
    }
    
    void calcularSinal(double tempo){
        sinalSaida.setPV_T1(sinaisEntrada[canalTanque1].getPV());
        sinalSaida.setPV_T2(sinaisEntrada[canalTanque2].getPV());
        sinalSaida.calcularAmplitudeAtual(tempo, tempoClasse);
    }
    
    void enviarSinal()
    {
        try 
        {
            quanserClient.write(canalSaida,sinalSaida.getTensaoSaida());
            System.out.println("Escreveu!");
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the sinaisEntrada
     */
    public SinalEntrada[] getSinaisEntrada() {
        return sinaisEntrada;
    }

    /**
     * @param sinaisEntrada the sinaisEntrada to set
     */
    public void setSinaisEntrada(SinalEntrada[] sinaisEntrada) {
        this.sinaisEntrada = sinaisEntrada;
    }
    
    /**
     * @return the sinalSaida
     */
    public SinalSaida getSinalSaida() {
        return sinalSaida;
    }

    /**
     * @param sinalSaida the sinalSaida to set
     */
    public void setSinalSaida(SinalSaida sinalSaida) {
        this.sinalSaida = sinalSaida;
    }

    /**
     * @return the canalSaida
     */
    public int getCanalSaida() {
        return canalSaida;
    }

    /**
     * @param canalSaida the canalSaida to set
     */
    public void setCanalSaida(int canalSaida) {
        this.canalSaida = canalSaida;
    }

    /**
     * @return the canaisEntrada
     */
    public ArrayList getCanaisEntrada() {
        return canaisEntrada;
    }

    /**
     * @param canaisEntrada the canaisEntrada to set
     */
    public void setCanaisEntrada(ArrayList canaisEntrada) {
        this.canaisEntrada = canaisEntrada;
    }

    /**
     * @return the tempoClasse
     */
    public Tempo getTempoClasse() {
        return tempoClasse;
    }

    /**
     * @param tempoClasse the tempoClasse to set
     */
    public void setTempoClasse(Tempo tempoClasse) {
        this.tempoClasse = tempoClasse;
    }

    /**
     * @return the TanqueSelecionado
     */
    public int getTanqueSelecionado() {
        return tanqueSelecionado;
    }

    /**
     * @param TanqueSelecionado the TanqueSelecionado to set
     */
    public void setTanqueSelecionado(int tanqueSelecionado) {
        this.tanqueSelecionado = tanqueSelecionado;
    }

    /**
     * @param canalTanque1 the canalTanque1 to set
     */
    public void setCanalTanque1(int canalTanque1) {
        this.canalTanque1 = canalTanque1;
    }

    /**
     * @param canalTanque2 the canalTanque2 to set
     */
    public void setCanalTanque2(int canalTanque2) {
        this.canalTanque2 = canalTanque2;
    }

    
}

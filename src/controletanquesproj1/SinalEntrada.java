/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

/**
 *
 * @author PedroKlisley
 */
public class SinalEntrada {
    private double PV;
    
    public SinalEntrada()
    {
        PV = 0.0;
    }

    /**
     * @return the PV
     */
    public double getPV() {
        return PV;
    }

    /**
     * @param PV the PV to set
     */
    public void setPV(double PV) {
        this.PV = PV;
    }
}

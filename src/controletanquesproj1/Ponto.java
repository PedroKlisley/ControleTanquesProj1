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
public class Ponto {
    private double x;
    private double y;
    
    public Ponto()
    {
        x = 0.0;
        y = 0.0;
    }
    
    public Ponto(double novoX, double novoY)
    {
        x = novoX;
        y = novoY;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
}

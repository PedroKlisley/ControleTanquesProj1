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
public class Tempo {
    private double tempoInicial;
    private double duracao;
    private double duracaoCorrente;
    private boolean iniciarAleatorio;
    private double amplitudeAleatorio;

    public Tempo()
    {
        tempoInicial = 0.0;
        duracao = 0.0;
        duracaoCorrente = 0.0;
        iniciarAleatorio = true;
    }
    /**
     * @return the tempoInicial
     */
    public double getTempoInicial() {
        return tempoInicial;
    }

    /**
     * @param tempoInicial the tempoInicial to set
     */
    public void setTempoInicial(double tempoInicial) {
        this.tempoInicial = tempoInicial;
    }

    /**
     * @return the duracao
     */
    public double getDuracao() {
        return duracao;
    }

    /**
     * @param duracao the duracao to set
     */
    public void setDuracao(double duracao) {
        this.duracao = duracao;
    }

    /**
     * @return the duracaoCorrente
     */
    public double getDuracaoCorrente() {
        return duracaoCorrente;
    }

    /**
     * @param duracaoCorrente the duracaoCorrente to set
     */
    public void setDuracaoCorrente(double duracaoCorrente) {
        this.duracaoCorrente = duracaoCorrente;
    }

    /**
     * @return the iniciarAleatorio
     */
    public boolean isIniciarAleatorio() {
        return iniciarAleatorio;
    }

    /**
     * @param iniciarAleatorio the iniciarAleatorio to set
     */
    public void setIniciarAleatorio(boolean iniciarAleatorio) {
        this.iniciarAleatorio = iniciarAleatorio;
    }

    /**
     * @return the amplitudeAleatorio
     */
    public double getAmplitudeAleatorio() {
        return amplitudeAleatorio;
    }

    /**
     * @param amplitudeAleatorio the amplitudeAleatorio to set
     */
    public void setAmplitudeAleatorio(double amplitudeAleatorio) {
        this.amplitudeAleatorio = amplitudeAleatorio;
    }
}

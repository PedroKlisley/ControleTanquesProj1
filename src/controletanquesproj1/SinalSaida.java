/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import br.ufrn.dca.controle.QuanserClientException;
import java.util.Random;

/**
 *
 * @author PedroKlisley
 */
public class SinalSaida {
    
    /*****Entradas Sinal Genérico*****/
    private double amplitudeMax;
    private double periodo;
    private double offset;
    private boolean malhaFechada;
    private int tipo;
    
    /*****Entradas Sinal Aleatório*****/
    private double amplitudeMin;
    private double duracaoMax;
    private double duracaoMin;
    
    /*****Entradas Malha Fechada*****/
    private int tipoControle;
    private double PV;
    private double nivelTanqueTrava;
    private double setPoint;
    
    /*****Saídas Malha Genérica*****/
    private double amplitude;
    private double tensaoSaida;
    
    /*****Entradas Controle*****/
    private double erro;  
    private double erroAnterior;
    private double kp;
    private double kd;
    private double ki;
    private double td;
    private double ti;
    private boolean segundaOrdem;
    
    /*****Saídas Controle*****/    
    private double sinalDeControle;
    private double acaoP;
    private double acaoI;
    private double acaoD;
    
    
    /*****Entradas Filtro*****/
    private boolean antiWindup;
    private double tauAW;
    private boolean integracaoCondicional;
    private double gama;//Constante Filtro Derivativo
    private boolean filtroDerivativo;
    
    
    
    /*****Variáveis de Estado Controle*****/
    private double somaErro;
    private double difSaturacao;
    private double PVAnterior;
    private double tempoAnterior; //Usado para calcular passo
    
    /*****Entradas Segunda Ordem*****/
    private int tipoOvershoot; //0 - Absoluto; 1 - Percentual
    private int tipoTempoSubida; //0: 0-100%; 1: 5 - 95%; 2: 10 - 90%
    private int tipoTempoAcomodacao; //0: 2%; 1: 5%; 2: 7%; 3: 10%
    private double PVD20;
    
    /*****Saídas Segunda Ordem*****/
    private double tempoSubida;
    private double overshoot;
    private double tempoPico;
    private double tempoAcomodacao;
    private boolean calculouTempoSubida;
    private boolean calculouOvershoot;
    private boolean pareceAcomodado;
    
    /*****Variáveis de Estado Segunda Ordem*****/
    private double tempoInicioSinalNovo;
    private double tempoInicioSubida;
    private boolean calculouInicioTempoSubida;
    private double mudancaSP;
    private boolean mudouSinal;
    private double SPAnterior;
    private double SPAtual;
    private double derivadaPV;
    private double overshootSat;
    
    public SinalSaida()
    {
        
        PV = 0.0;
        amplitude = 0.0;
        amplitudeMax = 0.0;
        amplitudeMin = 0.0;
        periodo = 0.0;
        
        duracaoMax = 1.0;
        duracaoMin = 0.0;
        tipo = 0;
        offset = 0.0;
        
        malhaFechada = false;
        tensaoSaida = 0.0;
        setPoint = 0.0;
        tipoControle = 0;
        erro = 0.0;
        erroAnterior = 0.0;
        somaErro = 0.0;
        tempoAnterior = 0.0;
        PVAnterior = 0.0;
        kp = 2.0;
        kd = 0.005;
        ki = 0.5;
        td = 0.0;
        ti = 0.0;
        antiWindup = false;
        tauAW = 1.0;
        integracaoCondicional = false;
        difSaturacao = 0.0;
        sinalDeControle = 0.0;
        acaoP = 0.0;
        acaoI = 0.0;
        acaoD = 0.0;
        SPAnterior = 0.0;
        SPAtual = 0.0;
        tempoSubida = 0.0;
        overshoot = 0.0;
        tempoPico = 0.0;
        tempoAcomodacao = 0.0;
    }

    void calcularAmplitudeAtual(double tempoEmS, Tempo tempoClasse)
    {
        //double diferencaDeTempo = tempoEmS - tempoAnterior;
        double diferencaDeTempo = 0.1 ;                 //Tempo de amostra fixa
        
        tempoAnterior = tempoEmS;
        switch(tipo)
        {
            case 0:
            //Degrau
            {
                amplitude = amplitudeMax;
                break;
            }
            case 1:
            //Senoidal
            {
                amplitude = amplitudeMax*Math.sin(2*Math.PI*tempoEmS/periodo);
                break;
            }
            case 2:
            //Quadrada
            {
                if(tempoEmS%periodo > periodo/2)
                {
                    amplitude = amplitudeMax;
                }
                else
                {
                    amplitude = -amplitudeMax;
                }
                break;
            }
            case 3:
            //Dente de serra
            {
                amplitude = 2*amplitudeMax*(tempoEmS%periodo)/periodo - amplitudeMax;
                break;
            }
            case 4:
            //Aleatório
            {
                if(tempoClasse.isIniciarAleatorio())
                {
                    tempoClasse.setIniciarAleatorio(false);
                    Random r = new Random();
                    tempoClasse.setDuracao(duracaoMin + (duracaoMax - duracaoMin) * r.nextDouble());
                    tempoClasse.setAmplitudeAleatorio(amplitudeMin + (amplitudeMax - amplitudeMin) * r.nextDouble());
                    tempoClasse.setDuracaoCorrente(0);
                    tempoClasse.setTempoInicial(System.nanoTime()/1000000000.0);
                }
                else{
                    tempoClasse.setDuracaoCorrente(System.nanoTime()/1000000000 - tempoClasse.getTempoInicial());
                    if (tempoClasse.getDuracaoCorrente() > tempoClasse.getDuracao())
                    {
                        tempoClasse.setIniciarAleatorio(true);
                    }
                }
                amplitude = tempoClasse.getAmplitudeAleatorio();
                break;
            }
        }

        amplitude += offset;    

        if( isMalhaFechada())
        {   
            setSetPoint(amplitude); 
            if(setPoint != SPAtual)
            {
                mudouSinal = true;
            }
            erroAnterior = getErro();
            erro = setPoint - PV;
        
            if(getTipoControle() == 0) //P
            {
                somaErro = 0.0;
                acaoP = getErro()*getKp();
                amplitude = getAcaoP();
            }
            else if(getTipoControle() == 1) //PD kd
            {
                somaErro = 0.0;
                acaoP = getErro()*getKp();
                acaoD = getKd()*(getErro()-erroAnterior)/diferencaDeTempo;
                if(filtroDerivativo)
                {
                    acaoD = acaoD/(1 + gama*acaoD);
                }
                amplitude = getAcaoP() + getAcaoI();
            }
            else if(getTipoControle() == 2) //PI ki
            {
                acaoP = getErro()*getKp();
                if(isAntiWindup() && sinalDeControle != tensaoSaida)
                {
                    difSaturacao = (sinalDeControle - tensaoSaida)/getTauAW();   
                    acaoI = somaErro + (getKi()*getErro() - kp*difSaturacao)*diferencaDeTempo ;
                    amplitude = getAcaoP() + getAcaoI();
                    somaErro += (ki*getErro() - kp*difSaturacao)*diferencaDeTempo;
                }
                else 
                {
                    if(!isIntegracaoCondicional() || sinalDeControle == tensaoSaida)
                    {   
                        acaoI = somaErro + getKi()*getErro()*diferencaDeTempo;
                        somaErro += ki*getErro()*diferencaDeTempo;
                    }
                    amplitude = getAcaoP() + getAcaoI();
                }
            }
            else if(getTipoControle() == 3) //PID kd e ki
            {
                acaoP = getErro()*getKp();
                acaoD = getKd()*(getErro()-erroAnterior)/diferencaDeTempo;
                if(filtroDerivativo)
                {
                    //acaoD = acaoD/(1 + gama*(acaoD/kp));
                    acaoD = acaoD/(1 + gama*(acaoD/(kp*erro)));
                }
                if(isAntiWindup() && sinalDeControle != tensaoSaida)
                {
                    difSaturacao = (sinalDeControle - tensaoSaida)/getTauAW();   
                    acaoI = somaErro + (getKi()*getErro() - kp*difSaturacao)*diferencaDeTempo ;
                    amplitude = getAcaoP() + getAcaoI() + getAcaoD();
                    somaErro += (ki*getErro() - kp*difSaturacao)*diferencaDeTempo;
                }
                else 
                {
                    if(!isIntegracaoCondicional() || sinalDeControle == tensaoSaida)
                    {   
                        acaoI = somaErro + getKi()*getErro()*diferencaDeTempo;
                        somaErro += ki*getErro()*diferencaDeTempo;
                    }
                    amplitude = getAcaoP() + getAcaoI() + getAcaoD();
                }
            }
            else if(getTipoControle() == 4) //PI-D kd e ki
            {
                acaoP = getErro()*getKp();
                acaoD = getKd()*(PV-PVAnterior)/diferencaDeTempo;
                if(filtroDerivativo)
                {
                    acaoD = acaoD/(1 + gama*acaoD);
                }
                if(isAntiWindup() && sinalDeControle != tensaoSaida)
                {
                    difSaturacao = (sinalDeControle - tensaoSaida)/getTauAW();
                    acaoI = somaErro + ( getKi()*getErro() - kp*difSaturacao )*diferencaDeTempo ;
                    amplitude = getAcaoP() + getAcaoI() + getAcaoD();
                    somaErro += (ki*getErro() - kp*difSaturacao)*diferencaDeTempo;
                }
                else 
                {   
                    if(!isIntegracaoCondicional() || sinalDeControle == tensaoSaida)
                    {   
                        acaoI = somaErro + getKi()*getErro()*diferencaDeTempo;
                        somaErro += ki*getErro()*diferencaDeTempo;
                    }
                    amplitude = getAcaoP() + getAcaoI() + getAcaoD();
                }
            }
            
            if(segundaOrdem) //Segunda Ordem
            {                   
                if(mudouSinal)
                {
                    tempoSubida = 0.0;
                    overshoot = PV;
                    overshootSat = PV;
                    tempoPico = 0.0;
                    tempoAcomodacao = 0.0;
                    tempoInicioSinalNovo = tempoEmS;
                    SPAnterior = SPAtual;
                    SPAtual = setPoint;
                    calculouInicioTempoSubida = false;
                    calculouTempoSubida = false;
                    calculouOvershoot = false;
                    mudancaSP = SPAtual - SPAnterior;
                    
                    mudouSinal = false;
                }
                else //Verificar atraso de transporte
                {
                    derivadaPV = (PV-PVAnterior)/diferencaDeTempo;
                    if(mudancaSP <= 0.0)
                    {
                        CalcularTempoDescida(tempoEmS);
                        
                        //Calculo Overshoot
                        if(!isCalculouOvershoot())
                        {
                            overshootMin(tempoEmS);
                            if(PV <= setPoint && derivadaPV >= 0.6)
                            {
                                calculouOvershoot = true;
                            }
                        }
                    }
                    else //SPAtual > SPAnterior
                    {
                        CalcularTempoSubida(tempoEmS);
                        if(!isCalculouOvershoot())
                        {
                            overshootMax(tempoEmS);
                            if(PV >= setPoint && derivadaPV <= -0.6)
                            {
                                calculouOvershoot = true;
                            }
                        }
                        //Calculo Overshoot
                    }
                    switch(tipoTempoAcomodacao)
                    {
                        case 0:
                        {
                            if(isPareceAcomodado())
                            {
                                if((PV > 1.02*setPoint) || (PV < 0.98*setPoint))
                                {   
                                    pareceAcomodado = false;
                                }
                            }
                            else
                            {
                                if((PV >= 0.98*setPoint) && (PV <= 1.02*setPoint))
                                {   
                                    pareceAcomodado = true;
                                    tempoAcomodacao = tempoEmS - tempoInicioSinalNovo;
                                }
                            }
                        }
                        case 1:
                        {
                            if(isPareceAcomodado())
                            {
                                if((PV > 1.05*setPoint) || (PV < 0.95*setPoint))
                                {   
                                    pareceAcomodado = false;
                                }
                            }
                            else
                            {
                                if((PV >= 0.95*setPoint) && (PV <= 1.05*setPoint))
                                {   
                                    pareceAcomodado = true;
                                    tempoAcomodacao = tempoEmS - tempoInicioSinalNovo;
                                }
                            }
                        }
                        case 2:
                        {
                            if(isPareceAcomodado())
                            {
                                if((PV > 1.07*setPoint) || (PV < 0.93*setPoint))
                                {   
                                    pareceAcomodado = false;
                                }
                            }
                            else
                            {
                                if((PV >= 0.93*setPoint) && (PV <= 1.07*setPoint))
                                {   
                                    pareceAcomodado = true;
                                    tempoAcomodacao = tempoEmS - tempoInicioSinalNovo;
                                }
                            }
                        }
                        case 3:
                        {
                            if(isPareceAcomodado())
                            {//Ver getDerivada
                                if((PV > 1.1*setPoint) || (PV < 0.9*setPoint))
                                {   
                                    pareceAcomodado = false;
                                }
                            }
                            else
                            {
                                if((PV >= 0.9*setPoint) && (PV <= 1.1*setPoint))
                                {   
                                    pareceAcomodado = true;
                                    tempoAcomodacao = tempoEmS - tempoInicioSinalNovo;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Diferenca de Tempo: " + diferencaDeTempo);
        }
        else
        {
            setSetPoint(0.0);
        }
        
        PVAnterior = PV;
        tensaoSaida = amplitude;
        sinalDeControle = amplitude;
        
        if(amplitude > 4)
        {
                tensaoSaida = 4;
        }
        else if(amplitude < -4)
        {
                tensaoSaida = -4;
        }
        if( nivelTanqueTrava > 28)
        {
            if( amplitude > 3.15)
            {
                //tensaoSaida = 2.82;
                tensaoSaida = 2.9;
            }
            if( nivelTanqueTrava > 29)
            {
                tensaoSaida = 0;
            }
        }
        else if(nivelTanqueTrava < 4 && getTensaoSaida() < 0)
        {
              tensaoSaida = 0;
        }
        /*****************SIMULAÇÃO**********/
            //PV += (0.2*tensaoSaida - PV*0.01);
            nivelTanqueTrava += (0.2*tensaoSaida - nivelTanqueTrava*0.01);
            PV += (0.3*nivelTanqueTrava - PV*0.01);
        //*/
    }
    
    void overshootMin(double tempoEmS)
    {            
        if(PV < overshootSat)
        {
            overshootSat = PV;
            tempoPico = tempoEmS - tempoInicioSinalNovo;
         
            switch(getTipoOvershoot())
            {
                case 0:
                {
                    overshoot = (setPoint-PV)*100/setPoint;
                    break;
                }
                case 1:
                {
                    overshoot = (setPoint-PV);
                    break;
                }
            }
        } 
    }
    
    void overshootMax(double tempoEmS)
    {
        if(PV > overshootSat)
        {
            overshootSat = PV;
            tempoPico = tempoEmS - tempoInicioSinalNovo;
            switch(getTipoOvershoot())
            {
                case 0:
                {
                    overshoot = (PV-setPoint)*100/setPoint;
                    break;
                }
                case 1:
                {
                    overshoot = (PV-setPoint);
                    break;
                }
            }
            
        } 
        
        
    }
            
    void CalcularTempoDescida(double tempoEmS)
    {
        //Calculo Tempo Subida
        switch(tipoTempoSubida)
        {
            case 0:
            {
                if(!isCalculouTempoSubida() && PV <= SPAtual)
                {
                    tempoSubida = tempoEmS - tempoInicioSinalNovo;
                    calculouTempoSubida = true;
                }
                break;
            }
            case 1:
            {
                if(!calculouInicioTempoSubida && PV <= SPAnterior-0.05*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV <= SPAnterior-0.95*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
            case 2:
            {
                if(!calculouInicioTempoSubida && PV <= SPAnterior-0.1*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV <= SPAnterior-0.9*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
        }
    }
     
    void CalcularTempoSubida(double tempoEmS)
    {
        //Calculo Tempo Subida
        switch(tipoTempoSubida)
        {
            case 0:
            {
                if(!isCalculouTempoSubida() && PV >= SPAtual)
                {
                    tempoSubida = tempoEmS - tempoInicioSinalNovo;
                    calculouTempoSubida = true;
                }
                break;
            }
            case 1:
            {
                if(!calculouInicioTempoSubida && PV >= SPAnterior+0.05*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV >= SPAnterior+0.95*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
            case 2:
            {
                if(!calculouInicioTempoSubida && PV >= SPAnterior+0.1*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV >= SPAnterior+0.9*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
        }
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

    
    
    /**
     * @return the amplitude
     */
    public double getAmplitude() {
        return amplitude;
    }

    
    /**
     * @param amplitude the amplitude to set
     */
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    /**
     * @return the amplitudeMax
     */
    public double getAmplitudeMax() {
        return amplitudeMax;
    }

    /**
     * @param amplitudeMax the amplitudeMax to set
     */
    public void setAmplitudeMax(double amplitudeMax) {
        this.amplitudeMax = amplitudeMax;
    }

    /**
     * @return the amplitudeMin
     */
    public double getAmplitudeMin() {
        return amplitudeMin;
    }

    /**
     * @param amplitudeMin the amplitudeMin to set
     */
    public void setAmplitudeMin(double amplitudeMin) {
        this.amplitudeMin = amplitudeMin;
    }

    /**
     * @return the duracaoMax
     */
    public double getDuracaoMax() {
        return duracaoMax;
    }

    /**
     * @param duracaoMax the duracaoMax to set
     */
    public void setDuracaoMax(double duracaoMax) {
        this.duracaoMax = duracaoMax;
    }

    /**
     * @return the duracaoMin
     */
    public double getDuracaoMin() {
        return duracaoMin;
    }

    /**
     * @param duracaoMin the duracaoMin to set
     */
    public void setDuracaoMin(double duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    /**
     * @return the periodo
     */
    public double getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(double periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the offset
     */
    public double getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }


    /**
     * @return the malhaFechada
     */
    public boolean isMalhaFechada() {
        return malhaFechada;
    }

    /**
     * @param malhaFechada the malhaFechada to set
     */
    public void setMalhaFechada(boolean malhaFechada) {
        this.malhaFechada = malhaFechada;
    }

    /**
     * @return the tensaoSaida
     */
    public double getTensaoSaida() {
        return tensaoSaida;
    }

    /**
     * @return the setPoint
     */
    public double getSetPoint() {
        return setPoint;
    }

    /**
     * @param setPoint the setPoint to set
     */
    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    /**
     * @return the kp
     */
    public double getKp() {
        return kp;
    }

    /**
     * @param kp the kp to set
     */
    public void setKp(double kp) {
        this.kp = kp;
    }

    /**
     * @return the kd
     */
    public double getKd() {
        return kd;
    }

    /**
     * @param kd the kd to set
     */
    public void setKd(double kd) {
        this.kd = kd;
    }

    /**
     * @return the ki
     */
    public double getKi() {
        return ki;
    }

    /**
     * @param ki the ki to set
     */
    public void setKi(double ki) {
        this.ki = ki;
    }

    /**
     * @return the td
     */
    public double getTd() {
        return td;
    }

    /**
     * @param td the td to set
     */
    public void setTd(double td) {
        this.td = td;
    }

    /**
     * @return the ti
     */
    public double getTi() {
        return ti;
    }

    /**
     * @param ti the ti to set
     */
    public void setTi(double ti) {
        this.ti = ti;
    }

    /**
     * @return the erro
     */
    public double getErro() {
        return erro;
    }

    /**
     * @return the antiWindup
     */
    public boolean isAntiWindup() {
        return antiWindup;
    }

    /**
     * @param antiWindup the antiWindup to set
     */
    public void setAntiWindup(boolean antiWindup) {
        this.antiWindup = antiWindup;
    }

    /**
     * @return the tauAW
     */
    public double getTauAW() {
        return tauAW;
    }

    /**
     * @param tauAW the tauAW to set
     */
    public void setTauAW(double tauAW) {
        this.tauAW = tauAW;
    }

    /**
     * @return the integracaoCondicional
     */
    public boolean isIntegracaoCondicional() {
        return integracaoCondicional;
    }

    /**
     * @param integracaoCondicional the integracaoCondicional to set
     */
    public void setIntegracaoCondicional(boolean integracaoCondicional) {
        this.integracaoCondicional = integracaoCondicional;
    }

    /**
     * @return the tipoControle
     */
    public int getTipoControle() {
        return tipoControle;
    }

    /**
     * @param tipoControle the tipoControle to set
     */
    public void setTipoControle(int tipoControle) {
        this.tipoControle = tipoControle;
    }

    /**
     * @return the acaoP
     */
    public double getAcaoP() {
        return acaoP;
    }

    /**
     * @return the acaoI
     */
    public double getAcaoI() {
        return acaoI;
    }

    /**
     * @return the acaoD
     */
    public double getAcaoD() {
        return acaoD;
    }

    /**
     * @param segundaOrdem the segundaOrdem to set
     */
    public void setSegundaOrdem(boolean segundaOrdem) {
        this.segundaOrdem = segundaOrdem;
    }

    /**
     * @param nivelTanqueTrava the nivelTanqueTrava to set
     */
    public void setNivelTanqueTrava(double nivelTanqueTrava) {
        this.nivelTanqueTrava = nivelTanqueTrava;
    }

    /**
     * @return the tempoSubida
     */
    public double getTempoSubida() {
        return tempoSubida;
    }

    /**
     * @return the overshoot
     */
    public double getOvershoot() {
        return overshoot;
    }

    /**
     * @return the tempoPico
     */
    public double getTempoPico() {
        return tempoPico;
    }

    /**
     * @return the tempoAcomodacao
     */
    public double getTempoAcomodacao() {
        return tempoAcomodacao;
    }

    /**
     * @return the calculouTempoSubida
     */
    public boolean isCalculouTempoSubida() {
        return calculouTempoSubida;
    }

    /**
     * @return the calculouOvershoot
     */
    public boolean isCalculouOvershoot() {
        return calculouOvershoot;
    }

    /**
     * @return the pareceAcomodado
     */
    public boolean isPareceAcomodado() {
        return pareceAcomodado;
    }

    /**
     * @return the derivadaPV
     */
    public double getDerivadaPV() {
        return derivadaPV;
    }

    /**
     * @param tipoOvershoot the tipoOvershoot to set
     */
    public void setTipoOvershoot(int tipoOvershoot) {
        this.tipoOvershoot = tipoOvershoot;
    }

    /**
     * @param tipoTempoSubida the tipoTempoSubida to set
     */
    public void setTipoTempoSubida(int tipoTempoSubida) {
        this.tipoTempoSubida = tipoTempoSubida;
    }

    /**
     * @param tipoTempoAcomodacao the tipoTempoAcomodacao to set
     */
    public void setTipoTempoAcomodacao(int tipoTempoAcomodacao) {
        this.tipoTempoAcomodacao = tipoTempoAcomodacao;
    }

    /**
     * @param gama the gama to set
     */
    public void setGama(double gama) {
        this.gama = gama;
    }

    /**
     * @param filtroDerivativo the filtroDerivativo to set
     */
    public void setFiltroDerivativo(boolean filtroDerivativo) {
        this.filtroDerivativo = filtroDerivativo;
    }

    /**
     * @return the tipoOvershoot
     */
    public int getTipoOvershoot() {
        return tipoOvershoot;
    }

}

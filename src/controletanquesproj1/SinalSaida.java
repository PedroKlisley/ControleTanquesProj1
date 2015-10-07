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
    private int tipoOnda;
    
    /*****Entradas Sinal Aleatório*****/
    private double amplitudeMin;
    private double duracaoMax;
    private double duracaoMin;
    
    /*****Entradas Malha Fechada*****/
    private int tipoControle;
    private double PV_T2;
    private double PV_T1;
    private double setPoint_T1;
    private double setPoint_T2;
    
    /*****Saídas Malha Genérica*****/
    private double amplitudeSinal;
    private double tensaoSaida;
    
    /*****Entradas Controle*****/
    private double kp_T2;
    private double kd_T2;
    private double ki_T2;
    private double td_T2;
    private double ti_T2;
    private boolean segundaOrdem;
    private int controlador_T2;
    
    /*****Entradas Controle_T1*****/
    private double kp_T1;
    private double kd_T1;
    private double ki_T1;
    private double td_T1;
    private double ti_T1;
    private int controlador_T1;
    
    /*****Saídas Controle*****/    
    private double sinalDeControle_T2;
    private double acaoP_T2;
    private double acaoI_T2;
    private double acaoD_T2;
    
    /*****Saídas Controle_T1*****/    
    private double sinalDeControle_T1;
    private double acaoP_T1;
    private double acaoI_T1;
    private double acaoD_T1;
    
    
    /*****Entradas Filtro*****/
    private boolean antiWindup_T2;
    private double tauAW_T2;
    private boolean integracaoCondicional_T2;
    private double gama_T2;//Constante Filtro Derivativo
    private boolean filtroDerivativo_T2;
    
    /*****Entradas Filtro_T1*****/
    private boolean antiWindup_T1;
    private double tauAW_T1;
    private boolean integracaoCondicional_T1;
    private double gama_T1;//Constante Filtro Derivativo
    private boolean filtroDerivativo_T1;
    
    /*****Variáveis de Estado Controle*****/
    private double erro_T2;  
    private double erroAnterior_T2;
    private double somaErro_T2;
    private double difSaturacao_T2;
    private double PVAnterior_T2;
    private double tempoAnterior; //Usado para calcular passo
    
    /*****Variáveis de Estado Controle_T1*****/
    private double erro_T1;  
    private double erroAnterior_T1;
    private double somaErro_T1;
    private double difSaturacao_T1;
    private double PVAnterior_T1;

    /*****Entradas Segunda Ordem*****/
    private int tipoOvershoot; //0 - Absoluto; 1 - Percentual
    private int tipoTempoSubida; //0: 0-100%; 1: 5 - 95%; 2: 10 - 90%
    private int tipoTempoAcomodacao; //0: 2%; 1: 5%; 2: 7%; 3: 10%
    
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
    private double SPAnterior_T2;
    private double SPAtual_T2;
    private double derivadaPV;
    private double overshootSat;
    private double derivadaOvershoot;
    
    public SinalSaida()
    {
        /*****Entradas Sinal Genérico*****/
        amplitudeMax = 0.0;
        periodo = 0.0;
        offset = 0.0;
        malhaFechada = false;
        tipoOnda = 0;

        /*****Entradas Sinal Aleatório*****/
        amplitudeMin = 0.0;
        duracaoMax = 0.0;
        duracaoMin = 0.0;

        /*****Entradas Malha Fechada*****/
        tipoControle = 0;
        PV_T2 = 0.0;
        PV_T1 = 0.0;
        setPoint_T1 = 0.0;
        setPoint_T2 = 0.0;
        

        /*****Saídas Malha Genérica*****/
        amplitudeSinal = 0.0;
        tensaoSaida = 0.0;

        /*****Entradas Controle*****/
        kp_T2 = 0.0;
        kd_T2 = 0.0;
        ki_T2 = 0.0;
        td_T2 = 0.0;
        ti_T2 = 0.0;
        segundaOrdem = false;
        controlador_T2 = 0;

        /*****Entradas Controle_T1*****/
        kp_T1 = 0.0;
        kd_T1 = 0.0;
        ki_T1 = 0.0;
        td_T1 = 0.0;
        ti_T1 = 0.0;
        controlador_T1 = 0;

        /*****Saídas Controle*****/    
        sinalDeControle_T2 = 0.0;
        acaoP_T2 = 0.0;
        acaoI_T2 = 0.0;
        acaoD_T2 = 0.0;

        /*****Saídas Controle_T1*****/    
        sinalDeControle_T1 = 0.0;
        acaoP_T1 = 0.0;
        acaoI_T1 = 0.0;
        acaoD_T1 = 0.0;


        /*****Entradas Filtro*****/
        antiWindup_T2 = false;
        tauAW_T2 = 0.0;
        integracaoCondicional_T2 = false;
        gama_T2 = 0.0;//Constante Filtro Derivativo
        filtroDerivativo_T2 = false;

        /*****Entradas Filtro_T1*****/
        antiWindup_T1 = false;
        tauAW_T1 = 0.0;
        integracaoCondicional_T1 = false;
        gama_T1 = 0.0;//Constante Filtro Derivativo
        filtroDerivativo_T1 = false;

        /*****Variáveis de Estado Controle*****/
        erro_T2 = 0.0;  
        erroAnterior_T2 = 0.0;
        somaErro_T2 = 0.0;
        difSaturacao_T2 = 0.0;
        PVAnterior_T2 = 0.0;
        tempoAnterior = 0.0; //Usado para calcular passo

        /*****Variáveis de Estado Controle_T1*****/
        erro_T1 = 0.0;  
        erroAnterior_T1 = 0.0;
        somaErro_T1 = 0.0;
        difSaturacao_T1 = 0.0;
        PVAnterior_T1 = 0.0;

        /*****Entradas Segunda Ordem*****/
        tipoOvershoot = 1; //0 - Absoluto; 1 - Percentual
        tipoTempoSubida = 0; //0: 0-100%; 1: 5 - 95%; 2: 10 - 90%
        tipoTempoAcomodacao = 1; //0: 2%; 1: 5%; 2: 7%; 3: 10%

        /*****Saídas Segunda Ordem*****/
        tempoSubida = 0.0;
        overshoot = 0.0;
        tempoPico = 0.0;
        tempoAcomodacao = 0.0;
        calculouTempoSubida = false;
        calculouOvershoot = false;
        pareceAcomodado = false;

        /*****Variáveis de Estado Segunda Ordem*****/
        tempoInicioSinalNovo = 0.0;
        tempoInicioSubida = 0.0;
        calculouInicioTempoSubida = false;
        mudancaSP = 0.0;
        mudouSinal = false;
        SPAnterior_T2 = 0.0;
        SPAtual_T2 = 0.0;
        derivadaPV = 0.0;
        overshootSat = 0.0;
        derivadaOvershoot = 0.3;
        //derivadaOvershoot = 0.6;
    }

    void calculoMalhaAberta(double tempoEmS, Tempo tempoClasse)
    {
        tempoAnterior = tempoEmS;
        switch(tipoOnda)
        {
            case 0:
            //Degrau
            {
                amplitudeSinal = amplitudeMax;
                break;
            }
            case 1:
            //Senoidal
            {
                amplitudeSinal = amplitudeMax*Math.sin(2*Math.PI*tempoEmS/periodo);
                break;
            }
            case 2:
            //Quadrada
            {
                if(tempoEmS%periodo > periodo/2)
                {
                    amplitudeSinal = amplitudeMax;
                }
                else
                {
                    amplitudeSinal = -amplitudeMax;
                }
                break;
            }
            case 3:
            //Dente de serra
            {
                amplitudeSinal = 2*amplitudeMax*(tempoEmS%periodo)/periodo - amplitudeMax;
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
                amplitudeSinal = tempoClasse.getAmplitudeAleatorio();
                break;
            }
        }

        amplitudeSinal += offset;    
    }
    
    void calculoControladorTanque1(double diferencaDeTempo)
    { 
        erro_T1 = getSetPoint_T1() - PV_T1;

        if(controlador_T1 == 0) //P
        {
            somaErro_T1 = 0.0;
            acaoP_T1 = erro_T1*kp_T1;
            sinalDeControle_T1 = acaoP_T1;
        }
        else if(controlador_T1 == 1) //PD kd
        {
            somaErro_T1 = 0.0;
            acaoP_T1 = erro_T1*kp_T1;
            acaoD_T1 = kd_T1*(erro_T1-erroAnterior_T1)/diferencaDeTempo;
            if(filtroDerivativo_T1)
            {
                acaoD_T1 = acaoD_T1/(1 + gama_T1*acaoD_T1);
            }
            sinalDeControle_T1 = acaoP_T1 + acaoI_T1;
        }
        else if(controlador_T1 == 2) //PI ki
        {
            acaoP_T1 = erro_T1*kp_T1;
            if(antiWindup_T1 && sinalDeControle_T1 != tensaoSaida)
            {
                difSaturacao_T1 = (sinalDeControle_T1 - tensaoSaida)/tauAW_T1;   
                acaoI_T1 = somaErro_T1 + (ki_T1*erro_T1 - kp_T1*difSaturacao_T1)*diferencaDeTempo ;
                sinalDeControle_T1 = acaoP_T1 + acaoI_T1;
                somaErro_T1 += (ki_T1*erro_T1 - kp_T1*difSaturacao_T1)*diferencaDeTempo;
            }
            else 
            {
                if(integracaoCondicional_T1 || sinalDeControle_T1 == getTensaoSaida())
                {   
                    acaoI_T1 = somaErro_T1 + ki_T1*erro_T1*diferencaDeTempo;
                    somaErro_T1 += ki_T1*erro_T1*diferencaDeTempo;
                }
                sinalDeControle_T1 = acaoP_T1 + acaoI_T1;
            }
        }
        else if(controlador_T1 == 3) //PID kd e ki
        {
            acaoP_T1 = erro_T1*kp_T1;
            acaoD_T1 = kd_T1*(erro_T1-erroAnterior_T1)/diferencaDeTempo;
            if(filtroDerivativo_T1)
            {
                //acaoD = acaoD/(1 + gama*(acaoD/kp));
                acaoD_T1 = acaoD_T1/(1 + gama_T1*(acaoD_T1/(kp_T1*erro_T1)));
            }
            if(antiWindup_T1 && sinalDeControle_T1 != tensaoSaida)
            {
                difSaturacao_T1 = (sinalDeControle_T1 - getTensaoSaida())/tauAW_T1;   
                acaoI_T1 = somaErro_T1 + (ki_T1*erro_T1 - kp_T1*difSaturacao_T1)*diferencaDeTempo ;
                 sinalDeControle_T1 = acaoP_T1 + acaoI_T1 + acaoD_T1;
                somaErro_T1 += (ki_T1*erro_T1 - kp_T1*difSaturacao_T1)*diferencaDeTempo;
            }
            else 
            {
                if(integracaoCondicional_T1 || sinalDeControle_T1 == tensaoSaida)
                {   
                    acaoI_T1 = somaErro_T1 + ki_T1*erro_T1*diferencaDeTempo;
                    somaErro_T1 += ki_T1*erro_T1*diferencaDeTempo;
                }
                sinalDeControle_T1 = acaoP_T1 + acaoI_T1 + acaoD_T1;
            }
        }
        else if(controlador_T1 == 4) //PI-D kd e ki
        {
            acaoP_T1 = erro_T1*kp_T1;
            acaoD_T1 = kd_T1*(PV_T1-PVAnterior_T1)/diferencaDeTempo;
            if(filtroDerivativo_T1)
            {
                acaoD_T1 = acaoD_T1/(1 + gama_T1*acaoD_T1);
            }
            if(antiWindup_T1 && sinalDeControle_T1 != tensaoSaida)
            {
                difSaturacao_T1 = (sinalDeControle_T1 - tensaoSaida)/tauAW_T1;
                acaoI_T1 = somaErro_T1 + ( ki_T1*erro_T1 - kp_T1*difSaturacao_T1 )*diferencaDeTempo ;
                 sinalDeControle_T1 = acaoP_T1 + acaoI_T1 + acaoD_T1;
                somaErro_T1 += (ki_T1*erro_T1 - kp_T1*difSaturacao_T1)*diferencaDeTempo;
            }
            else 
            {   
                if(!integracaoCondicional_T1 || sinalDeControle_T1 == tensaoSaida)
                {   
                    acaoI_T1 = somaErro_T1 + ki_T1*erro_T1*diferencaDeTempo;
                    somaErro_T1 += ki_T1*erro_T1*diferencaDeTempo;
                }
                 sinalDeControle_T1 = acaoP_T1 + acaoI_T1 + acaoD_T1;
            }
        }
        
        PVAnterior_T1 = PV_T1;
        erroAnterior_T1 = erro_T1;
        
    }
    
    
    void calculoControladorTanque2(double diferencaDeTempo)
    {
        if(setPoint_T2 != SPAtual_T2)
        {
            mudouSinal = true;
        }
        
        erro_T2 = setPoint_T2 - PV_T2;

        if(controlador_T2 == 0) //P
        {
            somaErro_T2 = 0.0;
            acaoP_T2 = erro_T2*kp_T2;
            sinalDeControle_T2 = acaoP_T2;
        }
        else if(controlador_T2 == 1) //PD kd
        {
            somaErro_T2 = 0.0;
            acaoP_T2 = erro_T2*kp_T2;
            acaoD_T2 = kd_T2*(erro_T2-erroAnterior_T2)/diferencaDeTempo;
            if(filtroDerivativo_T2)
            {
                acaoD_T2 = acaoD_T2/(1 + gama_T2*acaoD_T2);
            }
            sinalDeControle_T2 = acaoP_T2 + acaoI_T2;
        }
        else if(controlador_T2 == 2) //PI ki
        {
            acaoP_T2 = erro_T2*kp_T2;
            if(antiWindup_T2 && sinalDeControle_T2 != tensaoSaida && tipoControle == 0)
            {
                difSaturacao_T2 = (sinalDeControle_T2 - tensaoSaida)/tauAW_T2;   
                acaoI_T2 = somaErro_T2 + (ki_T2*erro_T2 - kp_T2*difSaturacao_T2)*diferencaDeTempo ;
                sinalDeControle_T2 = acaoP_T2 + acaoI_T2;
                somaErro_T2 += (ki_T2*erro_T2 - kp_T2*difSaturacao_T2)*diferencaDeTempo;
            }
            else 
            {
                if((integracaoCondicional_T2 || sinalDeControle_T2 == getTensaoSaida()) && tipoControle == 0)
                {   
                    acaoI_T2 = somaErro_T2 + ki_T2*erro_T2*diferencaDeTempo;
                    somaErro_T2 += ki_T2*erro_T2*diferencaDeTempo;
                }
                sinalDeControle_T2 = acaoP_T2 + acaoI_T2;
            }
        }
        else if(controlador_T2 == 3) //PID kd e ki
        {
            acaoP_T2 = erro_T2*kp_T2;
            acaoD_T2 = kd_T2*(erro_T2-erroAnterior_T2)/diferencaDeTempo;
            if(filtroDerivativo_T2)
            {
                //acaoD = acaoD/(1 + gama*(acaoD/kp));
                acaoD_T2 = acaoD_T2/(1 + gama_T2*(acaoD_T2/(kp_T2*erro_T2)));
            }
            if(antiWindup_T2 && sinalDeControle_T2 != tensaoSaida && tipoControle == 0)
            {
                difSaturacao_T2 = (sinalDeControle_T2 - getTensaoSaida())/tauAW_T2;   
                acaoI_T2 = somaErro_T2 + (ki_T2*erro_T2 - kp_T2*difSaturacao_T2)*diferencaDeTempo ;
                 sinalDeControle_T2 = acaoP_T2 + acaoI_T2 + acaoD_T2;
                somaErro_T2 += (ki_T2*erro_T2 - kp_T2*difSaturacao_T2)*diferencaDeTempo;
            }
            else 
            {
                if((integracaoCondicional_T2 || sinalDeControle_T2 == tensaoSaida) && tipoControle == 0);
                {   
                    acaoI_T2 = somaErro_T2 + ki_T2*erro_T2*diferencaDeTempo;
                    somaErro_T2 += ki_T2*erro_T2*diferencaDeTempo;
                }
                sinalDeControle_T2 = acaoP_T2 + acaoI_T2 + acaoD_T2;
            }
        }
        else if(controlador_T2 == 4) //PI-D kd e ki
        {
            acaoP_T2 = erro_T2*kp_T2;
            acaoD_T2 = kd_T2*(PV_T2-PVAnterior_T2)/diferencaDeTempo;
            if(filtroDerivativo_T2)
            {
                acaoD_T2 = acaoD_T2/(1 + gama_T2*acaoD_T2);
            }
            if(antiWindup_T2 && sinalDeControle_T2 != tensaoSaida && tipoControle == 0)
            {
                difSaturacao_T2 = (sinalDeControle_T2 - tensaoSaida)/tauAW_T2;
                acaoI_T2 = somaErro_T2 + ( ki_T2*erro_T2 - kp_T2*difSaturacao_T2 )*diferencaDeTempo ;
                 sinalDeControle_T2 = acaoP_T2 + acaoI_T2 + acaoD_T2;
                somaErro_T2 += (ki_T2*erro_T2 - kp_T2*difSaturacao_T2)*diferencaDeTempo;
            }
            else 
            {   
                if((!integracaoCondicional_T2 || sinalDeControle_T2 == tensaoSaida) && tipoControle == 0)
                {   
                    acaoI_T2 = somaErro_T2 + ki_T2*erro_T2*diferencaDeTempo;
                    somaErro_T2 += ki_T2*erro_T2*diferencaDeTempo;
                }
                 sinalDeControle_T2 = acaoP_T2 + acaoI_T2 + acaoD_T2;
            }
        }
        
        PVAnterior_T2 = PV_T2;
        erroAnterior_T2 = erro_T2;
        
    }
    
    void calcularIndicadoresDeSegundaOrdem(double tempoEmS, double diferencaDeTempo)
    {
        if(mudouSinal)
        {
            tempoSubida = 0.0;
            overshoot = PV_T2;
            overshootSat = PV_T2;
            tempoPico = 0.0;
            tempoAcomodacao = 0.0;
            tempoInicioSinalNovo = tempoEmS;
            SPAnterior_T2 = SPAtual_T2;
            SPAtual_T2 = setPoint_T2;
            calculouInicioTempoSubida = false;
            calculouTempoSubida = false;
            calculouOvershoot = false;
            mudancaSP = SPAtual_T2 - SPAnterior_T2;

            mudouSinal = false;
        }
        else //Verificar atraso de transporte
        {
            derivadaPV = (PV_T2-PVAnterior_T2)/diferencaDeTempo;
            if(mudancaSP <= 0.0)
            {
                CalcularTempoDescida(tempoEmS);

                //Calculo Overshoot
                if(!isCalculouOvershoot())
                {
                    overshootMin(tempoEmS);
                    if(PV_T2 <= setPoint_T2 && derivadaPV >= derivadaOvershoot)
                    {
                        calculouOvershoot = true;
                    }
                }
            }
            else //SPAtual_T2 > SPAnterior_T2
            {
                CalcularTempoSubida(tempoEmS);
                if(!isCalculouOvershoot())
                {
                    overshootMax(tempoEmS);
                    if(PV_T2 >= setPoint_T2 && derivadaPV <= -derivadaOvershoot)
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
                        if((PV_T2 > 1.02*setPoint_T2) || (PV_T2 < 0.98*setPoint_T2))
                        {   
                            pareceAcomodado = false;
                        }
                    }
                    else
                    {
                        if((PV_T2 >= 0.98*setPoint_T2) && (PV_T2 <= 1.02*setPoint_T2))
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
                        if((PV_T2 > 1.05*setPoint_T2) || (PV_T2 < 0.95*setPoint_T2))
                        {   
                            pareceAcomodado = false;
                        }
                    }
                    else
                    {
                        if((PV_T2 >= 0.95*setPoint_T2) && (PV_T2 <= 1.05*setPoint_T2))
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
                        if((PV_T2 > 1.07*setPoint_T2) || (PV_T2 < 0.93*setPoint_T2))
                        {   
                            pareceAcomodado = false;
                        }
                    }
                    else
                    {
                        if((PV_T2 >= 0.93*setPoint_T2) && (PV_T2 <= 1.07*setPoint_T2))
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
                        if((PV_T2 > 1.1*setPoint_T2) || (PV_T2 < 0.9*setPoint_T2))
                        {   
                            pareceAcomodado = false;
                        }
                    }
                    else
                    {
                        if((PV_T2 >= 0.9*setPoint_T2) && (PV_T2 <= 1.1*setPoint_T2))
                        {   
                            pareceAcomodado = true;
                            tempoAcomodacao = tempoEmS - tempoInicioSinalNovo;
                        }
                    }
                }
            }
        }
    }
    
    void setarTravas(double sinalTrava)
    {
        if(sinalTrava > 4)
        {
            tensaoSaida = 4;
        }
        else if(sinalTrava < -4)
        {
            tensaoSaida = -4;
        }
        else
        {
            tensaoSaida = sinalTrava;
        }
        if( PV_T1 > 28)
        {
            if(sinalTrava > 3.15)
            {
                //tensaoSaida = 2.82;
                tensaoSaida = 2.9;
            }
            if( PV_T1 > 29)
            {
                tensaoSaida = 0;
            }
        }
        else if(PV_T1 < 4 && tensaoSaida < 0)
        {
              tensaoSaida = 0;
        }
    }
    
    void configurarSimulacao()
    {
        //PV_T2 += (0.2*tensaoSaida - PV_T2*0.01);
        PV_T1 += (0.2*getTensaoSaida() - PV_T1*0.01);
        PV_T2 += (0.3*PV_T1 - PV_T2*0.01);
    }
    
    
    void calcularAmplitudeAtual(double tempoEmS, Tempo tempoClasse)
    {
        //double diferencaDeTempo = tempoEmS - tempoAnterior;
        double diferencaDeTempo = 0.1 ;                 //Tempo de amostra fixa
        
        calculoMalhaAberta(tempoEmS, tempoClasse);

        if(isMalhaFechada())
        {
            if(!segundaOrdem)
            {//Controle Simples do Tanque 1
                setSetPoint_T1(amplitudeSinal);
                calculoControladorTanque1(diferencaDeTempo);
                setarTravas(sinalDeControle_T1);
            }
            else
            {
                switch(tipoControle)
                {   
                    case 0:
                    {//Controle Simples do Tanque 2
                        setPoint_T2 = amplitudeSinal;
                        calculoControladorTanque2(diferencaDeTempo);
                        setarTravas(sinalDeControle_T2);
                        calcularIndicadoresDeSegundaOrdem(tempoEmS, diferencaDeTempo);
                        break;
                    }

                    case 1:
                    {//Controle em cascata do Tanque 2
                        setPoint_T2 = amplitudeSinal;
                        calculoControladorTanque2(diferencaDeTempo);
                        setSetPoint_T1(sinalDeControle_T2);
                        calculoControladorTanque1(diferencaDeTempo);
                        setarTravas(sinalDeControle_T1);
                        calcularIndicadoresDeSegundaOrdem(tempoEmS, diferencaDeTempo);
                        break;
                    }
                }
            }
        }
        else
        {
            //setSetPoint(0.0);
        }
                
        configurarSimulacao();
        System.out.println("A thread do controlador eh: " + Thread.currentThread());
    }
    
    void overshootMin(double tempoEmS)
    {            
        if(PV_T2 < overshootSat)
        {
            overshootSat = PV_T2;
            tempoPico = tempoEmS - tempoInicioSinalNovo;
         
            switch(tipoOvershoot)
            {
                case 0:
                {
                    overshoot = (setPoint_T2-PV_T2)*100/setPoint_T2;
                    break;
                }
                case 1:
                {
                    overshoot = (setPoint_T2-PV_T2);
                    break;
                }
            }
        } 
    }
    
    void overshootMax(double tempoEmS)
    {
        if(PV_T2 > overshootSat)
        {
            overshootSat = PV_T2;
            tempoPico = tempoEmS - tempoInicioSinalNovo;
            switch(tipoOvershoot)
            {
                case 0:
                {
                    overshoot = (PV_T2-setPoint_T2)*100/setPoint_T2;
                    break;
                }
                case 1:
                {
                    overshoot = (PV_T2-setPoint_T2);
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
                if(!isCalculouTempoSubida() && PV_T2 <= SPAtual_T2)
                {
                    tempoSubida = tempoEmS - tempoInicioSinalNovo;
                    calculouTempoSubida = true;
                }
                break;
            }
            case 1:
            {
                if(!calculouInicioTempoSubida && PV_T2 <= SPAnterior_T2-0.05*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV_T2 <= SPAnterior_T2-0.95*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
            case 2:
            {
                if(!calculouInicioTempoSubida && PV_T2 <= SPAnterior_T2-0.1*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV_T2 <= SPAnterior_T2-0.9*mudancaSP)
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
                if(!isCalculouTempoSubida() && PV_T2 >= SPAtual_T2)
                {
                    tempoSubida = tempoEmS - tempoInicioSinalNovo;
                    calculouTempoSubida = true;
                }
                break;
            }
            case 1:
            {
                if(!calculouInicioTempoSubida && PV_T2 >= SPAnterior_T2+0.05*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV_T2 >= SPAnterior_T2+0.95*mudancaSP)
                    {
                        tempoSubida = tempoEmS - tempoInicioSubida;
                        calculouTempoSubida = true;
                    }
                }
                break;
            }
            case 2:
            {
                if(!calculouInicioTempoSubida && PV_T2 >= SPAnterior_T2+0.1*mudancaSP)
                {
                    tempoInicioSubida = tempoEmS;
                    calculouInicioTempoSubida = true;
                }
                else
                {
                    if(!isCalculouTempoSubida() && PV_T2 >= SPAnterior_T2+0.9*mudancaSP)
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
     * @param sinalTravaMax the amplitudeMax to set
     */
    public void setAmplitudeMax(double amplitudeMax) {
        this.amplitudeMax = amplitudeMax;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(double periodo) {
        this.periodo = periodo;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * @param malhaFechada the malhaFechada to set
     */
    public void setMalhaFechada(boolean malhaFechada) {
        this.malhaFechada = malhaFechada;
    }

    /**
     * @param tipoOnda the tipoOnda to set
     */
    public void setTipoOnda(int tipoOnda) {
        this.tipoOnda = tipoOnda;
    }

    /**
     * @param amplitudeMin the amplitudeMin to set
     */
    public void setAmplitudeMin(double amplitudeMin) {
        this.amplitudeMin = amplitudeMin;
    }

    /**
     * @param duracaoMax the duracaoMax to set
     */
    public void setDuracaoMax(double duracaoMax) {
        this.duracaoMax = duracaoMax;
    }

    /**
     * @param duracaoMin the duracaoMin to set
     */
    public void setDuracaoMin(double duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    /**
     * @param tipoControle the tipoControle to set
     */
    public void setTipoControle(int tipoControle) {
        this.tipoControle = tipoControle;
    }

    /**
     * @return the PV_T2
     */
    public double getPV_T2() {
        return PV_T2;
    }

    /**
     * @param PV_T2 the PV_T2 to set
     */
    public void setPV_T2(double PV_T2) {
        this.PV_T2 = PV_T2;
    }

    /**
     * @return the PV_T1
     */
    public double getPV_T1() {
        return PV_T1;
    }

    /**
     * @param PV_T1 the PV_T1 to set
     */
    public void setPV_T1(double PV_T1) {
        this.PV_T1 = PV_T1;
    }

    /**
     * @return the setPoint_T2
     */
    public double getSetPoint_T2() {
        return setPoint_T2;
    }

    /**
     * @param setPoint_T2 the setPoint_T2 to set
     */
    public void setSetPoint_T2(double setPoint_T2) {
        this.setPoint_T2 = setPoint_T2;
    }

    /**
     * @return the amplitudeSinal
     */
    public double getAmplitudeSinal() {
        return amplitudeSinal;
    }

    /**
     * @return the tensaoSaida
     */
    public double getTensaoSaida() {
        return tensaoSaida;
    }

    /**
     * @return the kp_T2
     */
    public double getKp_T2() {
        return kp_T2;
    }

    /**
     * @param kp_T2 the kp_T2 to set
     */
    public void setKp_T2(double kp_T2) {
        this.kp_T2 = kp_T2;
    }

    /**
     * @param kd_T2 the kd_T2 to set
     */
    public void setKd_T2(double kd_T2) {
        this.kd_T2 = kd_T2;
    }

    /**
     * @param ki_T2 the ki_T2 to set
     */
    public void setKi_T2(double ki_T2) {
        this.ki_T2 = ki_T2;
    }

    /**
     * @param td_T2 the td_T2 to set
     */
    public void setTd_T2(double td_T2) {
        this.td_T2 = td_T2;
    }

    /**
     * @param ti_T2 the ti_T2 to set
     */
    public void setTi_T2(double ti_T2) {
        this.ti_T2 = ti_T2;
    }

    /**
     * @param segundaOrdem the segundaOrdem to set
     */
    public void setSegundaOrdem(boolean segundaOrdem) {
        this.segundaOrdem = segundaOrdem;
    }

    /**
     * @param controlador_T2 the controlador_T2 to set
     */
    public void setControlador_T2(int controlador_T2) {
        this.controlador_T2 = controlador_T2;
    }

    /**
     * @param kp_T1 the kp_T1 to set
     */
    public void setKp_T1(double kp_T1) {
        this.kp_T1 = kp_T1;
    }

    /**
     * @param kd_T1 the kd_T1 to set
     */
    public void setKd_T1(double kd_T1) {
        this.kd_T1 = kd_T1;
    }

    /**
     * @param ki_T1 the ki_T1 to set
     */
    public void setKi_T1(double ki_T1) {
        this.ki_T1 = ki_T1;
    }

    /**
     * @param td_T1 the td_T1 to set
     */
    public void setTd_T1(double td_T1) {
        this.td_T1 = td_T1;
    }

    /**
     * @param ti_T1 the ti_T1 to set
     */
    public void setTi_T1(double ti_T1) {
        this.ti_T1 = ti_T1;
    }

    /**
     * @param controlador_T1 the controlador_T1 to set
     */
    public void setControlador_T1(int controlador_T1) {
        this.controlador_T1 = controlador_T1;
    }

    /**
     * @return the sinalDeControle_T2
     */
    public double getSinalDeControle_T2() {
        return sinalDeControle_T2;
    }

    /**
     * @return the acaoP_T2
     */
    public double getAcaoP_T2() {
        return acaoP_T2;
    }

    /**
     * @return the acaoI_T2
     */
    public double getAcaoI_T2() {
        return acaoI_T2;
    }

    /**
     * @return the acaoD_T2
     */
    public double getAcaoD_T2() {
        return acaoD_T2;
    }

    /**
     * @return the sinalDeControle_T1
     */
    public double getSinalDeControle_T1() {
        return sinalDeControle_T1;
    }

    /**
     * @return the acaoP_T1
     */
    public double getAcaoP_T1() {
        return acaoP_T1;
    }

    /**
     * @return the acaoI_T1
     */
    public double getAcaoI_T1() {
        return acaoI_T1;
    }

    /**
     * @return the acaoD_T1
     */
    public double getAcaoD_T1() {
        return acaoD_T1;
    }

    /**
     * @param antiWindup_T2 the antiWindup_T2 to set
     */
    public void setAntiWindup_T2(boolean antiWindup_T2) {
        this.antiWindup_T2 = antiWindup_T2;
    }

    /**
     * @param tauAW_T2 the tauAW_T2 to set
     */
    public void setTauAW_T2(double tauAW_T2) {
        this.tauAW_T2 = tauAW_T2;
    }

    /**
     * @param integracaoCondicional_T2 the integracaoCondicional_T2 to set
     */
    public void setIntegracaoCondicional_T2(boolean integracaoCondicional_T2) {
        this.integracaoCondicional_T2 = integracaoCondicional_T2;
    }

    /**
     * @param gama_T2 the gama_T2 to set
     */
    public void setGama_T2(double gama_T2) {
        this.gama_T2 = gama_T2;
    }

    /**
     * @param filtroDerivativo_T2 the filtroDerivativo_T2 to set
     */
    public void setFiltroDerivativo_T2(boolean filtroDerivativo_T2) {
        this.filtroDerivativo_T2 = filtroDerivativo_T2;
    }

    /**
     * @param antiWindup_T1 the antiWindup_T1 to set
     */
    public void setAntiWindup_T1(boolean antiWindup_T1) {
        this.antiWindup_T1 = antiWindup_T1;
    }

    /**
     * @param tauAW_T1 the tauAW_T1 to set
     */
    public void setTauAW_T1(double tauAW_T1) {
        this.tauAW_T1 = tauAW_T1;
    }

    /**
     * @param integracaoCondicional_T1 the integracaoCondicional_T1 to set
     */
    public void setIntegracaoCondicional_T1(boolean integracaoCondicional_T1) {
        this.integracaoCondicional_T1 = integracaoCondicional_T1;
    }

    /**
     * @param gama_T1 the gama_T1 to set
     */
    public void setGama_T1(double gama_T1) {
        this.gama_T1 = gama_T1;
    }

    /**
     * @param filtroDerivativo_T1 the filtroDerivativo_T1 to set
     */
    public void setFiltroDerivativo_T1(boolean filtroDerivativo_T1) {
        this.filtroDerivativo_T1 = filtroDerivativo_T1;
    }

    /**
     * @return the erro_T1
     */
    public double getErro_T1() {
        return erro_T1;
    }

    /**
     * @return the erro_T2
     */
    public double getErro_T2() {
        return erro_T2;
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
     * @return the setPoint_T1
     */
    public double getSetPoint_T1() {
        return setPoint_T1;
    }

    /**
     * @param setPoint_T1 the setPoint_T1 to set
     */
    public void setSetPoint_T1(double setPoint_T1) {
        this.setPoint_T1 = setPoint_T1;
    }

    /**
     * @return the malhaFechada
     */
    public boolean isMalhaFechada() {
        return malhaFechada;
    }



}

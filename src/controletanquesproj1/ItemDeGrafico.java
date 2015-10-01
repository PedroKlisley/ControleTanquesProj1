/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import java.util.ArrayList;

/**
 *
 * @author PedroKlisley
 */
public class ItemDeGrafico {
    private String nome;
    private ArrayList pontos; //Vetor de ArrayList de Pontos

    public ItemDeGrafico(int qtdPontos)
    {
        nome = "";
        pontos = new ArrayList();
        
        for (int j = 0; j < qtdPontos; j++)
        {
            pontos.add(new Ponto((double) j, 0.0));
        }
    }
    
    public ItemDeGrafico(String _nome, int qtdPontos)
    {
        nome = _nome;
        pontos = new ArrayList();

        for (int j = 0; j < qtdPontos; j++)
        {
            pontos.add(new Ponto((double) j, 0.0));
        }
    }
    
    public ItemDeGrafico(String _nome, Ponto _ponto){
        nome = _nome;
        pontos = new ArrayList();
        pontos.add(_ponto);
    }
    
    public ItemDeGrafico(String _nome, ArrayList _pontos){
        nome = _nome;
        pontos = _pontos;
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the pontos
     */
    public ArrayList getPontos() {
        return pontos;
    }

    /**
     * @param pontos the pontos to set
     */
    public void setPontos(ArrayList pontos) {
        this.pontos = pontos;
    }
    
    public void removePrimeiroPonto()
    {
        pontos.remove(0);
    }
    
    public Ponto getUltimoPonto()
    {
        return (Ponto) pontos.get(pontos.size()-1);
    }
}

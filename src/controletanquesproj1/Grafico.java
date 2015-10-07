/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import java.awt.Color;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author PedroKlisley
 */
public class Grafico {

    
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.     */
    
    XYSeries serie; 
    private XYSeriesCollection[] datasets;
    private XYLineAndShapeRenderer[] renderer = new XYLineAndShapeRenderer[2];
    
    
    
    public void createDatasets(ArrayList[] vetorDeItensDeGrafico) {
        
        setDatasets(new XYSeriesCollection[2]);
        
        for (int i = 0; i < getDatasets().length; i++)
        {
            getDatasets()[i] = new XYSeriesCollection();
            
            for (int j = 0; j < vetorDeItensDeGrafico[i].size(); j++)
            {
                ItemDeGrafico itemDeGrafico = (ItemDeGrafico) vetorDeItensDeGrafico[i].get(j);
                serie = new XYSeries(itemDeGrafico.getNome());

                for(int k = 0; k < itemDeGrafico.getPontos().size(); k++)
                {
                    serie.add((double) ((Ponto)itemDeGrafico.getPontos().get(k)).getX(), (double) ((Ponto)itemDeGrafico.getPontos().get(k)).getY() );   
                }
                getDatasets()[i].addSeries(serie);
            }
            
        }    
        getRenderer()[0] = new XYLineAndShapeRenderer();
        
        //return getDatasets();
    }
    
    //public void updateSeries(ArrayList vetorDeItensDeGrafico[k])
    public void updateSeries(ArrayList[] vetorDeItensDeGrafico)
    {
        for (int k = 0; k < vetorDeItensDeGrafico.length; k++)
        {
            for (int i = 0; i < vetorDeItensDeGrafico[k].size(); i++)
            {
                boolean estahNoGrafico = false;
                ItemDeGrafico itemDeGrafico = ((ItemDeGrafico) vetorDeItensDeGrafico[k].get(i));
                for (int j = 0; j < getDatasets()[k].getSeries().size(); j++)
                {
                    if ( itemDeGrafico.getNome().equals(((XYSeries) getDatasets()[k].getSeries().get(j)).getKey() ) )
                    {
                        estahNoGrafico = true;
                    }
                }
                if(!estahNoGrafico)
                {
                    serie = new XYSeries(itemDeGrafico.getNome());
                    for(int j = 0; j < getDatasets()[k].getSeries(0).getItemCount()-1; j++)
                    {
                        serie.add(getDatasets()[k].getSeries(0).getDataItem(j).getXValue(), 0.0);   
                    }
                    serie.add(getDatasets()[k].getSeries(0).getMaxX(), itemDeGrafico.getUltimoPonto().getY());   
                    getDatasets()[k].addSeries(serie);
                }
            }
        
            for (int i = 0; i < getDatasets()[k].getSeries().size(); i++)
            {
                boolean estahNosItens = false;
                serie = getDatasets()[k].getSeries(i);
                for (int j = 0; j < vetorDeItensDeGrafico[k].size(); j++)
                {
                    if ( serie.getKey().equals( ((ItemDeGrafico) vetorDeItensDeGrafico[k].get(j)).getNome() ))
                    {
                        estahNosItens = true;
                    }
                }
                if(!estahNosItens)
                {
                    getDatasets()[k].removeSeries(i);
                }
            }
        }
        System.out.println("lfkdjhdskhaskjfhkjhsdfkjlshfkjshdkj A thread de atualizar série eh: " + Thread.currentThread());
    }
    
    public void updatePoint(ArrayList[] vetorDeItensDeGrafico)
    {
        for (int k = 0; k < vetorDeItensDeGrafico.length; k++)
        {    
            for (int i = 0; i < vetorDeItensDeGrafico[k].size(); i++)
            {
                ItemDeGrafico itemDeGrafico = (ItemDeGrafico)vetorDeItensDeGrafico[k].get(i);
                try
                {
                    serie = getDatasets()[k].getSeries(itemDeGrafico.getNome());//Sempre dá erro
                }
                catch(UnknownKeyException ex) 
                {
                    ex.printStackTrace();
                }
                serie.remove(0);
                serie.addOrUpdate(serie.getMaxX()+1, itemDeGrafico.getUltimoPonto().getY());
            }
        }
    }
    
    /**
     * Creates a chart.
     * 
     * @param _datasets
     * @param datasets
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    public JFreeChart createChart() {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart("",      // chart title
            "Amostra",                      // x axis label
            "Amplitude (V)",                      // y axis label
            getDatasets()[0],                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        plot.getRangeAxis(0).setRange(-30,30);
        
        final NumberAxis axis2 = new NumberAxis("Altura (cm)");
        axis2.setAutoRange(true);
        axis2.setAutoRangeIncludesZero(false);
        
        //axis2.setRange(-4.9, 34.9);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, getDatasets()[1]);
        plot.mapDatasetToRangeAxis(1, 1);
        
        /*
        getRenderer().setSeriesLinesVisible(0, true);
        getRenderer().setSeriesShapesVisible(0, false);
        getRenderer().setSeriesShapesVisible(1, false);
        getRenderer().setSeriesShapesVisible(2, false);
        getRenderer().setSeriesLinesVisible(3, true);
        getRenderer().setSeriesShapesVisible(3, false);
        */
        
        renderer[0].setBaseShapesVisible(false);
        renderer[0].setAutoPopulateSeriesPaint(true);
        
        plot.setRenderer(renderer[0]);
        
        renderer[1] = new XYLineAndShapeRenderer();
        renderer[1].setBaseLinesVisible(true);
        renderer[1].setBaseShapesVisible(true);
        
        plot.setRenderer(1, renderer[1]);


        // change the auto tick unit selection to integer units only...
        //final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

    /**
     * @return the renderer
     */
    

    /**
     * @return the datasets
     */
    public XYSeriesCollection[] getDatasets() {
        return datasets;
    }

    /**
     * @param datasets the datasets to set
     */
    public void setDatasets(XYSeriesCollection[] datasets) {
        this.datasets = datasets;
    }

    /**
     * @return the renderer
     */
    public XYLineAndShapeRenderer[] getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(XYLineAndShapeRenderer[] renderer) {
        this.renderer = renderer;
    }

    


    
    
}

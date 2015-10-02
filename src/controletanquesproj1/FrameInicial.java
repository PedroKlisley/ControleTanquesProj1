/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controletanquesproj1;

import br.ufrn.dca.controle.QuanserClient;
import br.ufrn.dca.controle.QuanserClientException;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author PedroKlisley
 */
public class FrameInicial extends javax.swing.JFrame {

     private Controle controle;
    final Grafico demo;
    
    final int sizePlot = 600; // qtd de Amostras no gráfico
    
    ArrayList itensDeGraficoCm = new ArrayList(); //Itens para gráfico em cm
    ArrayList itensDeGraficoV = new ArrayList(); //Itens para gráfico em V
    ArrayList[] vetorDeItensDeGrafico;
    private boolean conectado = false;
    private QuanserClient quanserClient = null;
    
    /**
     * Creates new form FrameInicial
     */
    public FrameInicial() {
        initComponents();
        
        jLabel_4v.setEnabled(false);
        jSpinner_4v.setEnabled(false);
        
        jLabel_SetPoint.setEnabled(true);
        jSpinner_0a30.setEnabled(true);        

        jLabel_Am_mi .setEnabled(false);
        jSpinner_Am_mi.setEnabled(false);

        jLabel_Du_ma .setEnabled(false);
        jSpinner_Du_ma.setEnabled(false);

        jLabel_Du_mi .setEnabled(false);
        jSpinner_Du_mi.setEnabled(false);
        
        jLabel_Offset.setEnabled(false);
        jSpinner_Offset.setEnabled(false);      
        
        jLabel_Periodo.setEnabled(false);
        jSpinner_Periodo.setEnabled(false);        
        
        jCBPlotP0.setVisible(false);
        jCBPlotP1.setVisible(false);
        
        jCBPlotErro.setVisible(false);
        jCBPlotAcaoP.setVisible(false);
        jCBPlotAcaoI.setVisible(false);
        jCBPlotAcaoD.setVisible(false);
        
        jPanelGrafico.setLayout(new java.awt.BorderLayout());
        demo  = new Grafico();
        
        itensDeGraficoV.add( new ItemDeGrafico("MV", sizePlot));
        itensDeGraficoV.add( new ItemDeGrafico("MV Saturada", sizePlot));
        itensDeGraficoCm.add( new ItemDeGrafico("Set Point", sizePlot));
        
        vetorDeItensDeGrafico = new ArrayList[2];
        vetorDeItensDeGrafico[0] = itensDeGraficoV;
        vetorDeItensDeGrafico[1] = itensDeGraficoCm;
        
        demo.createDatasets(vetorDeItensDeGrafico); //Inconsistencia
        //dataset = demo.createDataset(itensDeGrafico);
        JFreeChart chart = demo.createChart();
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 660));
        setPanel(chartPanel);
        
        for(int i = 0; i < 8; i++)
        {
            jCBoxCanalSaida.addItem(i);
        }
        
        jCBoxTanque1.addItem(0);
        jCBoxTanque1.addItem(1);
        
        jCBoxTanque2.addItem(0);
        jCBoxTanque2.addItem(1);
        jCBoxTanque2.setSelectedItem(1);
        
        jCBoxTanqueSelected.addItem(1);
        jCBoxTanqueSelected.addItem(2);
        jCBoxTanqueSelected.addItem(3);
        jCBoxTanqueSelected.setSelectedItem(3);
        //System.out.println("Thread Grafico = " + Thread.currentThread().getId());
    }

    public void setPanel(ChartPanel component)
    {
        jPanelGrafico.add(component);
        jPanelGrafico.validate();
    }
    
    public void SetControle(Controle _controle)
    {
        controle = _controle;
    }
    
    public void AtualizarDados()
    {
        int xMax = (int) demo.getDatasets()[0].getSeries(0).getMaxX();
        ArrayList pontosDeGraficoV = new ArrayList();
        ArrayList pontosDeGraficoCm = new ArrayList();
        
        pontosDeGraficoV.add( new ItemDeGrafico("MV", new Ponto( 0.0, controle.getSinalSaida().getAmplitude() ) ) );
        pontosDeGraficoV.add( new ItemDeGrafico("MV Saturada", new Ponto(0.0, controle.getSinalSaida().getTensaoSaida() ) ) );
        pontosDeGraficoCm.add( new ItemDeGrafico("Set Point", new Ponto(0.0, controle.getSinalSaida().getSetPoint() ) ) );
        
        for(int i = 2; i < itensDeGraficoV.size() ; i++)
        {
            ItemDeGrafico itemDeGrafico = (ItemDeGrafico) itensDeGraficoV.get(i);
            
            if(itemDeGrafico.getNome().equals("Acao P"))
            {
                pontosDeGraficoV.add( new ItemDeGrafico("Acao P", new Ponto(0.0, controle.getSinalSaida().getAcaoP() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("Acao I"))
            {
                pontosDeGraficoV.add( new ItemDeGrafico("Acao I", new Ponto(0.0, controle.getSinalSaida().getAcaoI() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("Acao D"))
            {
                pontosDeGraficoV.add( new ItemDeGrafico("Acao D", new Ponto(0.0, controle.getSinalSaida().getAcaoD() ) ) );
            }
        }
        
        for(int i = 1; i < itensDeGraficoCm.size() ; i++)
        {
            ItemDeGrafico itemDeGrafico = (ItemDeGrafico) itensDeGraficoCm.get(i);
            
            if(itemDeGrafico.getNome().equals("Erro"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("Erro", new Ponto(0.0, controle.getSinalSaida().getErro()) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 0"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 0", new Ponto(0.0, controle.getSinaisEntrada()[0].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 1"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 1", new Ponto(0.0, controle.getSinaisEntrada()[1].getPV() ) ) );
                //controle.getSinalSaida().setPVD20((double) demo.getDatasets()[1].getSeries("PV 1").getY(xMax - 20));
            }
            
            if(itemDeGrafico.getNome().equals("PV 2"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 2", new Ponto(0.0, controle.getSinaisEntrada()[2].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 3"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 3", new Ponto(0.0, controle.getSinaisEntrada()[3].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 4"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 4", new Ponto(0.0, controle.getSinaisEntrada()[4].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 5"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 5", new Ponto(0.0, controle.getSinaisEntrada()[5].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 6"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 6", new Ponto(0.0, controle.getSinaisEntrada()[6].getPV() ) ) );
            }
            
            if(itemDeGrafico.getNome().equals("PV 7"))
            {
                pontosDeGraficoCm.add( new ItemDeGrafico("PV 7", new Ponto(0.0, controle.getSinaisEntrada()[7].getPV() ) ) );
            }
        }
        //pontosDeGrafico.add( new ItemDeGrafico("PV 0", new Ponto(0.0, controle.getSinalSaida().getNivelTanque() ) ) );
        
        ArrayList[] vetorDePontosDeGrafico = new ArrayList[2];
        vetorDePontosDeGrafico[0] = pontosDeGraficoV;
        vetorDePontosDeGrafico[1] = pontosDeGraficoCm;
        demo.updatePoint(vetorDePontosDeGrafico);    
        
        
        if(controle.getSinalSaida().isCalculouOvershoot())
        {
            jLabelOvershoot.setText("" + controle.getSinalSaida().getOvershoot());
            jLabelTPico.setText("" + controle.getSinalSaida().getTempoPico());
        }
        else
        {
            jLabelOvershoot.setText("");
            jLabelTPico.setText("");
        }
        
        if(controle.getSinalSaida().isCalculouTempoSubida())
        {
            jLabelTSubida.setText("" + controle.getSinalSaida().getTempoSubida());
        }
        else
        {
            jLabelTSubida.setText("");
        }
            
        if(controle.getSinalSaida().isPareceAcomodado())
        {
            jLabelTAcomodacao.setText("" + controle.getSinalSaida().getTempoAcomodacao());
        }
        else
        {
            jLabelTAcomodacao.setText("");
        }
        
        //jLabelTPico.setText("" + controle.getSinalSaida().getDerivadaPV());

        //System.out.println("Maximo X: " + xMax);
    }
    
    private void AdicionaItemDeGrafico(String nome, int index)
    {
        boolean estahNosItens = false;
        
        for(int i = 0; i < vetorDeItensDeGrafico[index].size(); i++)
        {
            ItemDeGrafico itemDeGrafico = (ItemDeGrafico) vetorDeItensDeGrafico[index].get(i);
            if(itemDeGrafico.getNome().equals(nome))
            {
                estahNosItens = true;
            }
        }
        if(!estahNosItens)
        {
            vetorDeItensDeGrafico[index].add(new ItemDeGrafico(nome, sizePlot));
        }
    }
    
    private void RemoveItemDeGrafico(String nome, int index)
    {
        int flag = -1;
        for(int i = 0; i < vetorDeItensDeGrafico[index].size(); i++)
        {
            ItemDeGrafico itemDeGrafico = (ItemDeGrafico) vetorDeItensDeGrafico[index].get(i);
            if(itemDeGrafico.getNome().equals(nome))
            {
                flag = i;
            }
        }
        if(flag != -1)
        {
            vetorDeItensDeGrafico[index].remove(flag);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupMalha = new javax.swing.ButtonGroup();
        buttonGroupSinal = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTxtIP = new javax.swing.JTextField();
        jTxtPorta = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jCB_Pt0 = new javax.swing.JCheckBox();
        jCB_Pt1 = new javax.swing.JCheckBox();
        jCB_Pt2 = new javax.swing.JCheckBox();
        jCB_Pt3 = new javax.swing.JCheckBox();
        jCB_Pt4 = new javax.swing.JCheckBox();
        jCB_Pt5 = new javax.swing.JCheckBox();
        jCB_Pt6 = new javax.swing.JCheckBox();
        jCB_Pt7 = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jCBoxCanalSaida = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jCBoxTanque1 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jCBoxTanque2 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jRadioButtonMA = new javax.swing.JRadioButton();
        jRadioButtonMF = new javax.swing.JRadioButton();
        jLabelControleTanque = new javax.swing.JLabel();
        jCBoxTanqueSelected = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jRBDegrau = new javax.swing.JRadioButton();
        jRBSenoidal = new javax.swing.JRadioButton();
        jRBQuadrado = new javax.swing.JRadioButton();
        jRBDenteDeSerra = new javax.swing.JRadioButton();
        jRBAleatorio = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel_4v = new javax.swing.JLabel();
        jLabel_Am_mi = new javax.swing.JLabel();
        jSpinner_Am_mi = new javax.swing.JSpinner();
        jLabel_Du_ma = new javax.swing.JLabel();
        jSpinner_Du_ma = new javax.swing.JSpinner();
        jLabel_Du_mi = new javax.swing.JLabel();
        jSpinner_Du_mi = new javax.swing.JSpinner();
        jSpinner_4v = new javax.swing.JSpinner();
        jLabel_Offset = new javax.swing.JLabel();
        jSpinner_Offset = new javax.swing.JSpinner();
        jLabel_Periodo = new javax.swing.JLabel();
        jLabel_SetPoint = new javax.swing.JLabel();
        jSpinner_0a30 = new javax.swing.JSpinner();
        jSpinner_Periodo = new javax.swing.JSpinner();
        jPanelControle = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabelKp = new javax.swing.JLabel();
        jSpinnerKp = new javax.swing.JSpinner();
        jLabelKd = new javax.swing.JLabel();
        jSpinnerKd = new javax.swing.JSpinner();
        jLabelTau_d = new javax.swing.JLabel();
        jSpinnerTd = new javax.swing.JSpinner();
        jLabelKi = new javax.swing.JLabel();
        jSpinnerKi = new javax.swing.JSpinner();
        jLabelTau_i = new javax.swing.JLabel();
        jSpinnerTi = new javax.swing.JSpinner();
        jLabel_Taw = new javax.swing.JLabel();
        jSpinner_Taw = new javax.swing.JSpinner();
        jSpinnerGama = new javax.swing.JSpinner();
        jLabelGama = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jCBOvershoot = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jCBTempoSubida = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jCBTempoAcomodacao = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jCBoxTipoControle = new javax.swing.JComboBox();
        jPanel18 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jCBIntCondicional = new javax.swing.JCheckBox();
        jCBAntiWindup = new javax.swing.JCheckBox();
        jCBFiltroDerivativo = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jSpinnerKp_MI = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jSpinnerKi_MI = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jSpinnerKd_MI = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        jSpinnerTaw_MI = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jSpinnerTi_MI = new javax.swing.JSpinner();
        jSpinnerTd_MI = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        jSpinnerGama_MI = new javax.swing.JSpinner();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jCBAntiWindup_MI = new javax.swing.JCheckBox();
        jCBIntCondicional_MI = new javax.swing.JCheckBox();
        jCBFiltroDerivativo_MI = new javax.swing.JCheckBox();
        jPanel21 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jCBoxTipoControle_MI = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jCBPlotP0 = new javax.swing.JCheckBox();
        jCBPlotP1 = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jCBPlotSP = new javax.swing.JCheckBox();
        jCBPlotMV = new javax.swing.JCheckBox();
        jCBPlotMVSat = new javax.swing.JCheckBox();
        jCBPlotErro = new javax.swing.JCheckBox();
        jCBPlotAcaoP = new javax.swing.JCheckBox();
        jCBPlotAcaoI = new javax.swing.JCheckBox();
        jCBPlotAcaoD = new javax.swing.JCheckBox();
        jPanel2Ordem = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabelOvershoot = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelTSubida = new javax.swing.JLabel();
        jLabelTPico = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabelTAcomodacao = new javax.swing.JLabel();
        jLabelOvershootUnit = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanelGrafico = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton_Enviar = new javax.swing.JButton();
        jButton_Parar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane.setToolTipText("");
        jTabbedPane.setName(""); // NOI18N

        jLabel1.setText("IP: ");

        jLabel2.setText("Porta: ");

        jTxtIP.setText("10.13.99.69");

        jTxtPorta.setText("20081");

        jButton1.setText("Conectar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Canais de Entrada");

        jCB_Pt0.setSelected(true);
        jCB_Pt0.setText("Porta 0");
        jCB_Pt0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt0ActionPerformed(evt);
            }
        });

        jCB_Pt1.setSelected(true);
        jCB_Pt1.setText("Porta 1");
        jCB_Pt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt1ActionPerformed(evt);
            }
        });

        jCB_Pt2.setText("Porta 2");
        jCB_Pt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt2ActionPerformed(evt);
            }
        });

        jCB_Pt3.setText("Porta 3");
        jCB_Pt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt3ActionPerformed(evt);
            }
        });

        jCB_Pt4.setText("Porta 4");
        jCB_Pt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt4ActionPerformed(evt);
            }
        });

        jCB_Pt5.setText("Porta 5");
        jCB_Pt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt5ActionPerformed(evt);
            }
        });

        jCB_Pt6.setText("Porta 6");
        jCB_Pt6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt6ActionPerformed(evt);
            }
        });

        jCB_Pt7.setText("Porta 7");
        jCB_Pt7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_Pt7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jCB_Pt2)
                        .addComponent(jCB_Pt1)
                        .addComponent(jCB_Pt0))
                    .addComponent(jCB_Pt4)
                    .addComponent(jCB_Pt5)
                    .addComponent(jCB_Pt6)
                    .addComponent(jCB_Pt7)
                    .addComponent(jCB_Pt3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCB_Pt0)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt1)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt2)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt3)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt4)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt5)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt6)
                .addGap(18, 18, 18)
                .addComponent(jCB_Pt7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Canal de Saída");

        jLabel9.setText("Tanque 1");

        jCBoxTanque1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBoxTanque1ItemStateChanged(evt);
            }
        });

        jLabel10.setText("Tanque 2");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jCBoxCanalSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jCBoxTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jCBoxTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBoxCanalSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBoxTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBoxTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTxtPorta)
                    .addComponent(jTxtIP, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addGap(65, 65, 65))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTxtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTxtPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Conexao", jPanel1);

        jLabel5.setText("Escolha o Tipo de Malha: ");

        buttonGroupMalha.add(jRadioButtonMA);
        jRadioButtonMA.setText("Malha Aberta");
        jRadioButtonMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMAActionPerformed(evt);
            }
        });

        buttonGroupMalha.add(jRadioButtonMF);
        jRadioButtonMF.setSelected(true);
        jRadioButtonMF.setText("Malha Fechada");
        jRadioButtonMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMFActionPerformed(evt);
            }
        });

        jLabelControleTanque.setText("Controlar Tanque ");

        jCBoxTanqueSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBoxTanqueSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonMF)
                    .addComponent(jRadioButtonMA)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelControleTanque)
                    .addComponent(jCBoxTanqueSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonMA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonMF))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelControleTanque, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBoxTanqueSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel6.setText("Tipo de Sinal: ");

        buttonGroupSinal.add(jRBDegrau);
        jRBDegrau.setSelected(true);
        jRBDegrau.setText("Degrau");
        jRBDegrau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBDegrauActionPerformed(evt);
            }
        });

        buttonGroupSinal.add(jRBSenoidal);
        jRBSenoidal.setText("Senoildal");
        jRBSenoidal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBSenoidalActionPerformed(evt);
            }
        });

        buttonGroupSinal.add(jRBQuadrado);
        jRBQuadrado.setText("Quadrado");
        jRBQuadrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBQuadradoActionPerformed(evt);
            }
        });

        buttonGroupSinal.add(jRBDenteDeSerra);
        jRBDenteDeSerra.setText("Dente De Serra");
        jRBDenteDeSerra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBDenteDeSerraActionPerformed(evt);
            }
        });

        buttonGroupSinal.add(jRBAleatorio);
        jRBAleatorio.setText("Aleatorio");
        jRBAleatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBAleatorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBSenoidal)
                    .addComponent(jRBAleatorio)
                    .addComponent(jRBDenteDeSerra)
                    .addComponent(jRBQuadrado)
                    .addComponent(jRBDegrau)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBDegrau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBSenoidal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBQuadrado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBDenteDeSerra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBAleatorio))
        );

        jLabel_4v.setText("Amplitude Máxima:");

        jLabel_Am_mi.setText("Amplitude Mínima:");

        jSpinner_Am_mi.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel_Du_ma.setText("Duração Máxima:");

        jSpinner_Du_ma.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel_Du_mi.setText("Duração Mínima:");

        jSpinner_Du_mi.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jSpinner_4v.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel_Offset.setText("Offset:");

        jSpinner_Offset.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel_Periodo.setText("Período:");

        jLabel_SetPoint.setText("Amplitude Set Point:");

        jSpinner_0a30.setModel(new javax.swing.SpinnerNumberModel(15.0d, 0.0d, 30.0d, 1.0d));

        jSpinner_Periodo.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_Du_ma)
                            .addComponent(jLabel_Du_mi)
                            .addComponent(jLabel_Am_mi)
                            .addComponent(jLabel_4v))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinner_4v, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                            .addComponent(jSpinner_Du_mi, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSpinner_Am_mi)
                            .addComponent(jSpinner_Du_ma)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_SetPoint)
                            .addComponent(jLabel_Offset)
                            .addComponent(jLabel_Periodo))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinner_0a30, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSpinner_Offset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner_Periodo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_4v)
                    .addComponent(jSpinner_4v, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_SetPoint)
                    .addComponent(jSpinner_0a30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Am_mi)
                    .addComponent(jSpinner_Am_mi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Du_ma)
                    .addComponent(jSpinner_Du_ma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Du_mi)
                    .addComponent(jSpinner_Du_mi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Offset)
                    .addComponent(jSpinner_Offset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Periodo)
                    .addComponent(jSpinner_Periodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 115, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("Sinal", jPanel3);

        jLabelKp.setText("Kp");

        jSpinnerKp.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(2.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        jSpinnerKp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerKpValueChanged(evt);
            }
        });

        jLabelKd.setText("Kd");

        jSpinnerKd.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.005d), Double.valueOf(0.001d), null, Double.valueOf(1.0d)));
        jSpinnerKd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerKdValueChanged(evt);
            }
        });

        jLabelTau_d.setText("tau_d");

        jSpinnerTd.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.01d), Double.valueOf(0.001d), null, Double.valueOf(1.0d)));
        jSpinnerTd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerTdValueChanged(evt);
            }
        });

        jLabelKi.setText("Ki");

        jSpinnerKi.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.05d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        jSpinnerKi.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerKiValueChanged(evt);
            }
        });

        jLabelTau_i.setText("tau_i");

        jSpinnerTi.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(4.0d), Double.valueOf(0.001d), null, Double.valueOf(1.0d)));
        jSpinnerTi.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerTiValueChanged(evt);
            }
        });

        jLabel_Taw.setText("Tau_aw");

        jSpinner_Taw.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0E-4d), Double.valueOf(1.0E-4d), null, Double.valueOf(1.0d)));
        jSpinner_Taw.setEnabled(false);

        jSpinnerGama.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.1d), null, null, Double.valueOf(1.0d)));
        jSpinnerGama.setEnabled(false);

        jLabelGama.setText("Gama");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelKp)
                                    .addComponent(jLabelKi))
                                .addGap(6, 6, 6))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabelKd, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerKd)
                            .addComponent(jSpinnerKi)
                            .addComponent(jSpinnerKp))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_Taw)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner_Taw, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelTau_i, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelTau_d, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelGama, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinnerGama)
                            .addComponent(jSpinnerTi, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jSpinnerTd))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelKp)
                    .addComponent(jSpinnerKp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Taw)
                    .addComponent(jSpinner_Taw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelKi)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerKi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelTau_i)
                        .addComponent(jSpinnerTi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelKd)
                    .addComponent(jSpinnerKd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTau_d)
                    .addComponent(jSpinnerTd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerGama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelGama)))
        );

        jPanel11.setToolTipText("");

        jLabel14.setText("Overshoot");

        jCBOvershoot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Percentual", "Absoluto" }));

        jLabel17.setText("Tempo de Subida");

        jCBTempoSubida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0% - 100%", "5% - 95%", "10% - 90%" }));

        jLabel19.setText("Tempo de Acomodação");

        jCBTempoAcomodacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2%", "5%", "7%", "10%" }));
        jCBTempoAcomodacao.setSelectedIndex(1);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBTempoAcomodacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBOvershoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCBTempoSubida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jCBTempoSubida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBOvershoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jCBTempoAcomodacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jLabel8.setText("Tipo De Controle:");

        jCBoxTipoControle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P", "PD", "PI", "PID", "PI-D" }));
        jCBoxTipoControle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBoxTipoControleActionPerformed(evt);
            }
        });

        jLabel7.setText("Filtros:");

        jCBIntCondicional.setText("Integração Cond.");
        jCBIntCondicional.setEnabled(false);
        jCBIntCondicional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIntCondicionalActionPerformed(evt);
            }
        });

        jCBAntiWindup.setText("Anti Windup");
        jCBAntiWindup.setEnabled(false);
        jCBAntiWindup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAntiWindupActionPerformed(evt);
            }
        });

        jCBFiltroDerivativo.setText("Derivativo");
        jCBFiltroDerivativo.setEnabled(false);
        jCBFiltroDerivativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBFiltroDerivativoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCBIntCondicional))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jCBAntiWindup)
                            .addComponent(jCBFiltroDerivativo))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBIntCondicional)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBAntiWindup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBFiltroDerivativo)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jCBoxTipoControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCBoxTipoControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelControleLayout = new javax.swing.GroupLayout(jPanelControle);
        jPanelControle.setLayout(jPanelControleLayout);
        jPanelControleLayout.setHorizontalGroup(
            jPanelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelControleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelControleLayout.setVerticalGroup(
            jPanelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane.addTab("Controle", jPanelControle);

        jLabel15.setText("Kp");

        jSpinnerKp_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel20.setText("Ki");

        jSpinnerKi_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel24.setText("Kd");

        jSpinnerKd_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel25.setText("tau_aw");

        jSpinnerTaw_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        jSpinnerTaw_MI.setEnabled(false);

        jLabel26.setText("tau_i");

        jLabel27.setText("tau_d");

        jSpinnerTi_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jSpinnerTd_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        jLabel28.setText("Gama");

        jSpinnerGama_MI.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        jSpinnerGama_MI.setEnabled(false);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinnerKp_MI, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(jSpinnerKi_MI)))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerKd_MI)))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSpinnerTaw_MI, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(jSpinnerTi_MI)
                    .addComponent(jSpinnerTd_MI)
                    .addComponent(jSpinnerGama_MI, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jSpinnerKp_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jSpinnerTaw_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jSpinnerKi_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jSpinnerTi_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jSpinnerKd_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jSpinnerTd_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jSpinnerGama_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel29.setText("Filtros");

        jCBAntiWindup_MI.setText("Anti Windup");
        jCBAntiWindup_MI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAntiWindup_MIActionPerformed(evt);
            }
        });

        jCBIntCondicional_MI.setText("Integração Cond.");
        jCBIntCondicional_MI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBIntCondicional_MIActionPerformed(evt);
            }
        });

        jCBFiltroDerivativo_MI.setText("Derivativo");
        jCBFiltroDerivativo_MI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBFiltroDerivativo_MIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBFiltroDerivativo_MI)
                    .addComponent(jLabel29)
                    .addComponent(jCBAntiWindup_MI)
                    .addComponent(jCBIntCondicional_MI))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addGap(18, 18, 18)
                .addComponent(jCBAntiWindup_MI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBIntCondicional_MI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBFiltroDerivativo_MI)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel30.setText("Tipo de Controle");

        jCBoxTipoControle_MI.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P", "PD", "PI", "PID", "PI-D" }));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jCBoxTipoControle_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBoxTipoControle_MI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 199, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("MalhaInterna", jPanel9);

        jLabel16.setText("Escolha quais curvas plotar no gráfico:");

        jCBPlotP0.setText("PV 0");
        jCBPlotP0.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotP0StateChanged(evt);
            }
        });

        jCBPlotP1.setText("PV 1");
        jCBPlotP1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotP1StateChanged(evt);
            }
        });
        jCBPlotP1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBPlotP1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBPlotP1)
                    .addComponent(jCBPlotP0))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBPlotP0)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotP1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jCBPlotSP.setSelected(true);
        jCBPlotSP.setText("Set Point");
        jCBPlotSP.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotSPStateChanged(evt);
            }
        });

        jCBPlotMV.setSelected(true);
        jCBPlotMV.setText("MV");
        jCBPlotMV.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotMVStateChanged(evt);
            }
        });

        jCBPlotMVSat.setSelected(true);
        jCBPlotMVSat.setText("MV Saturada");
        jCBPlotMVSat.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotMVSatStateChanged(evt);
            }
        });

        jCBPlotErro.setText("Erro");
        jCBPlotErro.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotErroStateChanged(evt);
            }
        });

        jCBPlotAcaoP.setText("Ação P");
        jCBPlotAcaoP.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotAcaoPStateChanged(evt);
            }
        });

        jCBPlotAcaoI.setText("Ação I");
        jCBPlotAcaoI.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotAcaoIStateChanged(evt);
            }
        });

        jCBPlotAcaoD.setText("Ação D");
        jCBPlotAcaoD.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCBPlotAcaoDStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBPlotSP)
                    .addComponent(jCBPlotMV)
                    .addComponent(jCBPlotMVSat)
                    .addComponent(jCBPlotErro)
                    .addComponent(jCBPlotAcaoP)
                    .addComponent(jCBPlotAcaoI)
                    .addComponent(jCBPlotAcaoD))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBPlotSP)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotMV)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotMVSat)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotErro)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotAcaoP)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotAcaoI)
                .addGap(18, 18, 18)
                .addComponent(jCBPlotAcaoD)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jLabel11.setText("Overshoot: ");

        jLabel12.setText("Tempo de Subida: ");

        jLabel13.setText("Tempo de pico: ");

        jLabel18.setText("Tempo de acomodação");

        jLabelOvershootUnit.setText("%");

        jLabel21.setText("s");

        jLabel22.setText("s");

        jLabel23.setText("s");

        javax.swing.GroupLayout jPanel2OrdemLayout = new javax.swing.GroupLayout(jPanel2Ordem);
        jPanel2Ordem.setLayout(jPanel2OrdemLayout);
        jPanel2OrdemLayout.setHorizontalGroup(
            jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2OrdemLayout.createSequentialGroup()
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOvershoot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTPico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTSubida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTAcomodacao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOvershootUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2OrdemLayout.setVerticalGroup(
            jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2OrdemLayout.createSequentialGroup()
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2OrdemLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabelTSubida, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2OrdemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOvershoot, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabelOvershootUnit))
                .addGap(18, 18, 18)
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelTPico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2OrdemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabelTAcomodacao, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2Ordem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2Ordem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane.addTab("Gráfico", jPanel8);

        javax.swing.GroupLayout jPanelGraficoLayout = new javax.swing.GroupLayout(jPanelGrafico);
        jPanelGrafico.setLayout(jPanelGraficoLayout);
        jPanelGraficoLayout.setHorizontalGroup(
            jPanelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 865, Short.MAX_VALUE)
        );
        jPanelGraficoLayout.setVerticalGroup(
            jPanelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 657, Short.MAX_VALUE)
        );

        jButton_Enviar.setText("Enviar");
        jButton_Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EnviarActionPerformed(evt);
            }
        });

        jButton_Parar.setText("Parar");
        jButton_Parar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PararActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_Enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton_Parar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Parar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane))
                .addGap(7, 7, 7)
                .addComponent(jPanelGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jTabbedPane.getAccessibleContext().setAccessibleName("MalhaInterna");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_PararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PararActionPerformed
        controle.getSinalSaida().setMalhaFechada(false);
        controle.getSinalSaida().setAmplitudeMax(0.0);
        controle.getSinalSaida().setOffset(0.0);
        
        //System.out.println("Thread Grafico = " + Thread.currentThread().getId());
    }//GEN-LAST:event_jButton_PararActionPerformed

    private void jButton_EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EnviarActionPerformed
        //System.out.println("Thread Grafico = " + Thread.currentThread().getId());
        if(isConectado())       
        {
            if (jRadioButtonMF.isSelected())
            {
                controle.getSinalSaida().setMalhaFechada(true);
                controle.getSinalSaida().setAmplitudeMax((double)jSpinner_0a30.getValue());
            }
            else if(!jRBAleatorio.isSelected())
            {
                controle.getSinalSaida().setMalhaFechada(false);
                controle.getSinalSaida().setAmplitudeMax((double)jSpinner_4v.getValue());
            }

            if(jRBDegrau.isSelected())
            {
                controle.getSinalSaida().setTipo(0);
                
                /*jLabelOvershoot.setVisible(true);
                jLabelTPico.setVisible(true);
                jLabelTSubida.setVisible(true);
                jLabelTAcomodacao.setVisible(true);*/
                jPanel2Ordem.setEnabled(true);
            }
            else if(jRBSenoidal.isSelected())
            {
                controle.getSinalSaida().setTipo(1);
                
                /*jLabelOvershoot.setVisible(false);
                jLabelTPico.setVisible(false);
                jLabelTSubida.setVisible(false);
                jLabelTAcomodacao.setVisible(false);*/
                
                jPanel2Ordem.setEnabled(false);
            }
            else if(jRBQuadrado.isSelected())
            {
                controle.getSinalSaida().setTipo(2);
                
                /*jLabelOvershoot.setVisible(true);
                jLabelTPico.setVisible(true);
                jLabelTSubida.setVisible(true);
                jLabelTAcomodacao.setVisible(true);*/
                
                jPanel2Ordem.setEnabled(true);
            }
            else if(jRBDenteDeSerra.isSelected())
            {
                controle.getSinalSaida().setTipo(3);
                
                /*jLabelOvershoot.setVisible(false);
                jLabelTPico.setVisible(false);
                jLabelTSubida.setVisible(false);
                jLabelTAcomodacao.setVisible(false);*/
                jPanel2Ordem.setEnabled(false);
            }
            else if(jRBAleatorio.isSelected())
            {
                controle.getSinalSaida().setTipo(4);
                controle.getSinalSaida().setAmplitudeMin((double)jSpinner_Am_mi.getValue());
                controle.getSinalSaida().setDuracaoMax((double)jSpinner_Du_ma.getValue());
                controle.getSinalSaida().setDuracaoMin((double)jSpinner_Du_mi.getValue());
                
                /*jLabelOvershoot.setVisible(true);
                jLabelTPico.setVisible(true);
                jLabelTSubida.setVisible(true);
                jLabelTAcomodacao.setVisible(true);*/
                
                jPanel2Ordem.setEnabled(false);
            }

            //controle.setTanqueSelecionado((int)jCBoxTanqueSelected.getSelectedItem());
            controle.getSinalSaida().setTipoSistemaControle((int)jCBoxTanqueSelected.getSelectedIndex());
            controle.setCanalTanque1((int)jCBoxTanque1.getSelectedItem());
            controle.setCanalTanque2((int)jCBoxTanque2.getSelectedItem());
            
            /*****SEGUNDA ORDEM*****/
            controle.getSinalSaida().setTipoTempoSubida(jCBTempoSubida.getSelectedIndex());
            controle.getSinalSaida().setTipoOvershoot(jCBOvershoot.getSelectedIndex());
            controle.getSinalSaida().setTipoTempoAcomodacao(jCBTempoAcomodacao.getSelectedIndex());
            if(jCBOvershoot.getSelectedIndex()==0)
            {
                jLabelOvershootUnit.setText("%");
            }
            else if(jCBOvershoot.getSelectedIndex()==1)
            {
                jLabelOvershootUnit.setText("cm");
            }
            
            
            
            System.out.println("Mudou Tanque Controlado para: " + controle.getTanqueSelecionado());
            controle.getSinalSaida().setPeriodo((double)jSpinner_Periodo.getValue());
            controle.getSinalSaida().setOffset((double)jSpinner_Offset.getValue());

            controle.setCanalSaida((int) jCBoxCanalSaida.getSelectedItem());

            controle.getSinalSaida().setKp((double)jSpinnerKp.getValue());
            controle.getSinalSaida().setKd((double)jSpinnerKd.getValue());
            controle.getSinalSaida().setKi((double)jSpinnerKi.getValue());
            controle.getSinalSaida().setTd((double)jSpinnerTd.getValue());
            controle.getSinalSaida().setTi((double)jSpinnerTi.getValue());
            controle.getSinalSaida().setTipoControle(jCBoxTipoControle.getSelectedIndex());
            controle.getSinalSaida().setAntiWindup(jCBAntiWindup.isSelected());
            controle.getSinalSaida().setIntegracaoCondicional(jCBIntCondicional.isSelected());
            controle.getSinalSaida().setFiltroDerivativo(jCBFiltroDerivativo.isSelected());
            controle.getSinalSaida().setTauAW((double)jSpinner_Taw.getValue());
            
            
            if(jCBoxTanqueSelected.getSelectedIndex()==2)
            {
               controle.getSinalSaida().setKp_MI((double)jSpinnerKp_MI.getValue());
               controle.getSinalSaida().setKd_MI((double)jSpinnerKd_MI.getValue());
               controle.getSinalSaida().setKi_MI((double)jSpinnerKi_MI.getValue());
               controle.getSinalSaida().setTd_MI((double)jSpinnerTd_MI.getValue());
               controle.getSinalSaida().setTi_MI((double)jSpinnerTi_MI.getValue());
               controle.getSinalSaida().setTipoControle_MI(jCBoxTipoControle_MI.getSelectedIndex());
               controle.getSinalSaida().setAntiWindup_MI(jCBAntiWindup_MI.isSelected());
               controle.getSinalSaida().setIntegracaoCondicional_MI(jCBIntCondicional_MI.isSelected());
               controle.getSinalSaida().setFiltroDerivativo_MI(jCBFiltroDerivativo_MI.isSelected());
               controle.getSinalSaida().setTauAW_MI((double)jSpinnerTaw_MI.getValue());   
            }
            
            //ArrayList itensDeGrafico = new ArrayList();
            if(jRadioButtonMF.isSelected())
            {
                AdicionaItemDeGrafico("Erro", 1);
                jCBPlotErro.setSelected(true);
                jCBPlotErro.setVisible(true);

                AdicionaItemDeGrafico("Acao P", 0);
                jCBPlotAcaoP.setSelected(true);
                jCBPlotAcaoP.setVisible(true);

                if(jCBoxTipoControle.getSelectedIndex() > 1)
                {
                    AdicionaItemDeGrafico("Acao I", 0);
                    jCBPlotAcaoI.setSelected(true);
                    jCBPlotAcaoI.setVisible(true);

                }
                else
                {
                    RemoveItemDeGrafico("Acao I", 0);
                    jCBPlotAcaoI.setSelected(false);
                    jCBPlotAcaoI.setVisible(false);
                }

                if(jCBoxTipoControle.getSelectedIndex() == 1 || jCBoxTipoControle.getSelectedIndex() > 2)
                {
                    AdicionaItemDeGrafico("Acao D", 0);
                    jCBPlotAcaoD.setSelected(true);
                    jCBPlotAcaoD.setVisible(true);
                }
                else
                {
                    RemoveItemDeGrafico("Acao D", 0);
                    jCBPlotAcaoD.setSelected(false);
                    jCBPlotAcaoD.setVisible(false);
                }

            }
            else
            {
                RemoveItemDeGrafico("Erro", 1);
                jCBPlotErro.setSelected(false);
                jCBPlotErro.setVisible(false);

                RemoveItemDeGrafico("Acao P", 0);
                jCBPlotAcaoP.setSelected(false);
                jCBPlotAcaoP.setVisible(false);

                RemoveItemDeGrafico("Acao I", 0);
                jCBPlotAcaoI.setSelected(false);
                jCBPlotAcaoI.setVisible(false);

                RemoveItemDeGrafico("Acao D", 0);
                jCBPlotAcaoD.setSelected(false);
                jCBPlotAcaoD.setVisible(false);
            }


            if(jCB_Pt0.isSelected())
            {
                AdicionaItemDeGrafico("PV 0", 1);
                jCBPlotP0.setSelected(true);
                jCBPlotP0.setVisible(true);
            }
            else
            {
                RemoveItemDeGrafico("PV 0", 1);
                jCBPlotP0.setSelected(false);
                jCBPlotP0.setVisible(false);
            }

            if(jCB_Pt1.isSelected())
            {
                AdicionaItemDeGrafico("PV 1", 1);
                jCBPlotP1.setSelected(true);
                jCBPlotP1.setVisible(true);
            }
            else
            {
                RemoveItemDeGrafico("PV 1", 1);
                jCBPlotP1.setSelected(false);
                jCBPlotP1.setVisible(false);
            }

            demo.updateSeries(vetorDeItensDeGrafico);
            
            
        }
        
    }//GEN-LAST:event_jButton_EnviarActionPerformed

    private void jRBDegrauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBDegrauActionPerformed
        if(jRBDegrau.isSelected()){
            
            boolean malhaFechada = jRadioButtonMF.isSelected();
            
            jLabel_Am_mi .setEnabled(false);
            jSpinner_Am_mi.setEnabled(false);

            jLabel_Du_ma .setEnabled(false);
            jSpinner_Du_ma.setEnabled(false);

            jLabel_Du_mi .setEnabled(false);
            jSpinner_Du_mi.setEnabled(false);

            jLabel_Periodo.setEnabled(false);
            jSpinner_Periodo.setEnabled(false);

            jLabel_Offset.setEnabled(false);
            jSpinner_Offset.setEnabled(false);
            jSpinner_Offset.setValue(0.0);

            jLabel_4v.setEnabled(!malhaFechada);
            jSpinner_4v.setEnabled(!malhaFechada);

            jLabel_SetPoint.setEnabled(malhaFechada);
            jSpinner_0a30.setEnabled(malhaFechada);

        }
    }//GEN-LAST:event_jRBDegrauActionPerformed

    private void jRBSenoidalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBSenoidalActionPerformed
        if(jRBSenoidal.isSelected()){

            boolean malhaFechada = jRadioButtonMF.isSelected();
            
            jLabel_Am_mi .setEnabled(false);
            jSpinner_Am_mi.setEnabled(false);

            jLabel_Du_ma .setEnabled(false);
            jSpinner_Du_ma.setEnabled(false);

            jLabel_Du_mi .setEnabled(false);
            jSpinner_Du_mi.setEnabled(false);

            jLabel_Periodo.setEnabled(true);
            jSpinner_Periodo.setEnabled(true);

            jLabel_Offset.setEnabled(true);
            jSpinner_Offset.setEnabled(true);

            jLabel_4v.setEnabled(!malhaFechada);
            jSpinner_4v.setEnabled(!malhaFechada);

            jLabel_SetPoint.setEnabled(malhaFechada);
            jSpinner_0a30.setEnabled(malhaFechada);

        }
    }//GEN-LAST:event_jRBSenoidalActionPerformed

    private void jRBQuadradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBQuadradoActionPerformed
        if(jRBQuadrado.isSelected()){

            boolean malhaFechada = jRadioButtonMF.isSelected();
            
            jLabel_Am_mi .setEnabled(false);
            jSpinner_Am_mi.setEnabled(false);

            jLabel_Du_ma .setEnabled(false);
            jSpinner_Du_ma.setEnabled(false);

            jLabel_Du_mi .setEnabled(false);
            jSpinner_Du_mi.setEnabled(false);

            jLabel_Periodo.setEnabled(true);
            jSpinner_Periodo.setEnabled(true);

            jLabel_Offset.setEnabled(true);
            jSpinner_Offset.setEnabled(true);

            jLabel_4v.setEnabled(!malhaFechada);
            jSpinner_4v.setEnabled(!malhaFechada);

            jLabel_SetPoint.setEnabled(malhaFechada);
            jSpinner_0a30.setEnabled(malhaFechada);

        }
    }//GEN-LAST:event_jRBQuadradoActionPerformed

    private void jRBDenteDeSerraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBDenteDeSerraActionPerformed
        if(jRBDenteDeSerra.isSelected()){

            boolean malhaFechada = jRadioButtonMF.isSelected();
            
            jLabel_Am_mi .setEnabled(false);
            jSpinner_Am_mi.setEnabled(false);

            jLabel_Du_ma .setEnabled(false);
            jSpinner_Du_ma.setEnabled(false);

            jLabel_Du_mi .setEnabled(false);
            jSpinner_Du_mi.setEnabled(false);

            jLabel_Periodo.setEnabled(true);
            jSpinner_Periodo.setEnabled(true);

            jLabel_Offset.setEnabled(true);
            jSpinner_Offset.setEnabled(true);

            jLabel_4v.setEnabled(!malhaFechada);
            jSpinner_4v.setEnabled(!malhaFechada);

            jLabel_SetPoint.setEnabled(malhaFechada);
            jSpinner_0a30.setEnabled(malhaFechada);

        }
    }//GEN-LAST:event_jRBDenteDeSerraActionPerformed

    private void jRBAleatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBAleatorioActionPerformed
        if(jRBAleatorio.isSelected()){
            
            boolean malhaFechada = jRadioButtonMF.isSelected();
            
            jLabel_Am_mi .setEnabled(true);
            jSpinner_Am_mi.setEnabled(true);

            jLabel_Du_ma .setEnabled(true);
            jSpinner_Du_ma.setEnabled(true);

            jLabel_Du_mi .setEnabled(true);
            jSpinner_Du_mi.setEnabled(true);

            jLabel_Periodo.setEnabled(false);
            jSpinner_Periodo.setEnabled(false);

            jLabel_Offset.setEnabled(false);
            jSpinner_Offset.setEnabled(false);
            jSpinner_Offset.setValue(0.0);
            
            jLabel_4v.setEnabled(!malhaFechada);
            jSpinner_4v.setEnabled(!malhaFechada);

            jLabel_SetPoint.setEnabled(malhaFechada);
            jSpinner_0a30.setEnabled(malhaFechada);
        }
    }//GEN-LAST:event_jRBAleatorioActionPerformed

    private void jRadioButtonMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMFActionPerformed
        if(jRadioButtonMF.isSelected()){

            jLabel_SetPoint.setEnabled(true);
            jSpinner_0a30.setEnabled(true);

            jLabel_4v.setEnabled(false);
            jSpinner_4v.setEnabled(false);

            jLabelControleTanque.setEnabled(true);
            jCBoxTanqueSelected.setEnabled(true);
            
            jTabbedPane.setEnabledAt(2, true);
        }
    }//GEN-LAST:event_jRadioButtonMFActionPerformed

    private void jRadioButtonMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMAActionPerformed
        if(jRadioButtonMA.isSelected()){

            jLabel_SetPoint.setEnabled(false);
            jSpinner_0a30.setEnabled(false);

            jLabel_4v.setEnabled(true);
            jSpinner_4v.setEnabled(true);

            jLabelControleTanque.setEnabled(false);
            jCBoxTanqueSelected.setEnabled(false);
            
            jTabbedPane.setEnabledAt(2, false);
        }
    }//GEN-LAST:event_jRadioButtonMAActionPerformed

    private void jCBPlotP0StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotP0StateChanged
        if(jCBPlotP0.isVisible())
        {
            if(jCBPlotP0.isSelected())
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("PV 0"), true);
            }
            else
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("PV 0"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotP0StateChanged

    private void jCBPlotP1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotP1StateChanged
        if(jCBPlotP1.isVisible())
        {
            if(jCBPlotP1.isSelected())
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("PV 1"), true);
            }
            else
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("PV 1"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotP1StateChanged

    private void jCBoxTipoControleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBoxTipoControleActionPerformed
        if(jCBoxTipoControle.getSelectedIndex() == 0)//P
        {
            jCBFiltroDerivativo.setEnabled(false);
            jCBFiltroDerivativo.setSelected(false);
            jCBIntCondicional.setEnabled(false);
            jCBAntiWindup.setEnabled(false);
            jCBIntCondicional.setSelected(false);
            jCBAntiWindup.setSelected(false);
            
            jSpinnerGama.setEnabled(false);
            jSpinner_Taw.setEnabled(false);
            jSpinnerGama.setValue(0.1);
            jSpinner_Taw.setValue(1.0);
        }
        else if(jCBoxTipoControle.getSelectedIndex() == 1)//PD
        {
            jCBFiltroDerivativo.setEnabled(true);
            jCBIntCondicional.setEnabled(false);
            jCBAntiWindup.setEnabled(false);
            jCBIntCondicional.setSelected(false);
            jCBAntiWindup.setSelected(false);
            
            //jSpinnerGama.setEnabled(true);
            jSpinner_Taw.setEnabled(false);
            jSpinner_Taw.setValue(1.0);
        }
        else if(jCBoxTipoControle.getSelectedIndex() == 2)//PI
        {
            jCBFiltroDerivativo.setEnabled(false);
            jCBFiltroDerivativo.setSelected(false);
            jCBIntCondicional.setEnabled(true);
            jCBAntiWindup.setEnabled(true);
            
            jSpinnerGama.setEnabled(false);
            jSpinnerGama.setValue(0.1);
            //jSpinner_Taw.setEnabled(true);
        }
        else
        {
            jCBFiltroDerivativo.setEnabled(true);
            jCBIntCondicional.setEnabled(true);
            jCBAntiWindup.setEnabled(true);
            
            //jSpinnerGama.setEnabled(true);
            //jSpinner_Taw.setEnabled(true);
        }
    }//GEN-LAST:event_jCBoxTipoControleActionPerformed

    private void jSpinnerKpValueChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerKpValueChanged
        if((double)jSpinnerKp.getValue() != 0.0)
        {
            jSpinnerTd.setValue( ( (double)jSpinnerKd.getValue() )/ ( (double)jSpinnerKp.getValue() ) );            
        }
        else
        {
            jSpinnerTd.setValue( 0.0 );            
        }
        
        if((double)jSpinnerKi.getValue() != 0.0)
        {
            jSpinnerTi.setValue( ( (double)jSpinnerKp.getValue() )/ ( (double)jSpinnerKi.getValue() ) );
        }
        else
        {
            jSpinnerTi.setValue( 0.0 );
        }
    }//GEN-LAST:event_jSpinnerKpValueChanged

    private void jSpinnerKdValueChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerKdValueChanged
        if((double)jSpinnerKp.getValue() != 0.0)
        {
            jSpinnerTd.setValue( ( (double)jSpinnerKd.getValue() )/ ( (double)jSpinnerKp.getValue() ) );            
        }
        else
        {
            jSpinnerTd.setValue( 0.0 );
        }
    }//GEN-LAST:event_jSpinnerKdValueChanged

    private void jSpinnerKiValueChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerKiValueChanged
        if((double)jSpinnerKi.getValue() != 0.0)
        {
            jSpinnerTi.setValue( ( (double)jSpinnerKp.getValue() )/ ( (double)jSpinnerKi.getValue() ) );
        }
        else
        {
            jSpinnerTi.setValue( 0.0 );
        }
    }//GEN-LAST:event_jSpinnerKiValueChanged

    private void jSpinnerTdValueChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTdValueChanged
        jSpinnerKd.setValue( ( (double)jSpinnerKp.getValue() )* ( (double)jSpinnerTd.getValue() ) );
        if(jCBoxTipoControle.getSelectedIndex() < 2)
        {
            jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue()));
        }
        else
        {
            jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue() * (double)jSpinnerTd.getValue() ));
        }
    }//GEN-LAST:event_jSpinnerTdValueChanged

    private void jSpinnerTiValueChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTiValueChanged
        if((double)jSpinnerTi.getValue() != 0.0)
        {
            jSpinnerKi.setValue( ( (double)jSpinnerKp.getValue() )/ ( (double)jSpinnerTi.getValue() ) );
            if(jCBoxTipoControle.getSelectedIndex() < 3)
            {
                jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue() ));
            }
            else
            {
                jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue() * (double)jSpinnerTd.getValue() ));
            }
        }
        else
        {
            jSpinnerKi.setValue( 0.0 );
        }
    }//GEN-LAST:event_jSpinnerTiValueChanged

    private void jCBIntCondicionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIntCondicionalActionPerformed
        jSpinner_Taw.setEnabled(false);
        jCBAntiWindup.setSelected(false);
    }//GEN-LAST:event_jCBIntCondicionalActionPerformed

    private void jCBAntiWindupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAntiWindupActionPerformed
        jSpinner_Taw.setEnabled(jCBAntiWindup.isSelected());
        if((double)jSpinnerTi.getValue() != 0.0)
        {
            if(jCBoxTipoControle.getSelectedIndex() < 3)
            {
                jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue() ));
            }
            else
            {
                jSpinner_Taw.setValue( sqrt( (double)jSpinnerTi.getValue() * (double)jSpinnerTd.getValue() ));
            }
        }
        jCBIntCondicional.setSelected(false);
    }//GEN-LAST:event_jCBAntiWindupActionPerformed

    private void jCBPlotSPStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotSPStateChanged
        if(jCBPlotSP.isVisible())
        {
            if(jCBPlotSP.isSelected())
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("Set Point"), true);
            }
            else
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("Set Point"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotSPStateChanged

    private void jCBPlotMVStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotMVStateChanged
        if(jCBPlotMV.isVisible())
        {
            if(jCBPlotMV.isSelected())
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("MV"), true);
            }
            else
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("MV"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotMVStateChanged

    private void jCBPlotMVSatStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotMVSatStateChanged
        if(jCBPlotMVSat.isVisible())
        {
            if(jCBPlotMVSat.isSelected())
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("MV Saturada"), true);
            }
            else
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("MV Saturada"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotMVSatStateChanged

    private void jCBPlotErroStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotErroStateChanged
        if(jCBPlotErro.isVisible())
        {
            if(jCBPlotErro.isSelected())
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("Erro"), true);
            }
            else
            {
                demo.getRenderer()[1].setSeriesVisible(demo.getDatasets()[1].getSeriesIndex("Erro"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotErroStateChanged

    private void jCBPlotAcaoPStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotAcaoPStateChanged
        if(jCBPlotAcaoP.isVisible())
        {
            if(jCBPlotAcaoP.isSelected())
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao P"), true);
            }
            else
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao P"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotAcaoPStateChanged

    private void jCBPlotAcaoIStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotAcaoIStateChanged
        if(jCBPlotAcaoI.isVisible())
        {
            if(jCBPlotAcaoI.isSelected())
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao I"), true);
            }
            else
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao I"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotAcaoIStateChanged

    private void jCBPlotAcaoDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCBPlotAcaoDStateChanged
        if(jCBPlotAcaoD.isVisible())
        {
            if(jCBPlotAcaoD.isSelected())
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao D"), true);
            }
            else
            {
                demo.getRenderer()[0].setSeriesVisible(demo.getDatasets()[0].getSeriesIndex("Acao D"), false);
            }
        }
    }//GEN-LAST:event_jCBPlotAcaoDStateChanged

    private void simularConexao()
    {
        System.out.println("Conectou!");
        setConectado(true);
    }
    private void conectarAPlanta()
    {
        try 
        {
            quanserClient = new QuanserClient(jTxtIP.getText(), Integer.parseInt(jTxtPorta.getText()));
            System.out.println("Conectou!");
            setConectado(true);
        } 
        catch (QuanserClientException ex) {
            ex.printStackTrace();
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //conectarAPlanta();
        simularConexao();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    void conexaoModificada(boolean ativou, int canal){
        if(ativou)
        {
            controle.getCanaisEntrada().add(canal);
            jCBoxTanque1.addItem(canal);
            jCBoxTanque2.addItem(canal);
        }
        else
        {
            controle.getCanaisEntrada().remove((Object) canal);
            jCBoxTanque1.removeItem(canal);
            jCBoxTanque2.removeItem(canal);
        }
    }
    
    private void jCB_Pt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt1ActionPerformed
        conexaoModificada(jCB_Pt1.isSelected(), 1);
    }//GEN-LAST:event_jCB_Pt1ActionPerformed

    private void jCB_Pt0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt0ActionPerformed
        conexaoModificada(jCB_Pt0.isSelected(), 0);
    }//GEN-LAST:event_jCB_Pt0ActionPerformed

    private void jCB_Pt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt2ActionPerformed
        conexaoModificada(jCB_Pt2.isSelected(), 2);
    }//GEN-LAST:event_jCB_Pt2ActionPerformed

    private void jCB_Pt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt3ActionPerformed
        conexaoModificada(jCB_Pt3.isSelected(), 3);
    }//GEN-LAST:event_jCB_Pt3ActionPerformed

    private void jCB_Pt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt4ActionPerformed
        conexaoModificada(jCB_Pt4.isSelected(), 4);
    }//GEN-LAST:event_jCB_Pt4ActionPerformed

    private void jCB_Pt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt5ActionPerformed
        conexaoModificada(jCB_Pt5.isSelected(), 5);
    }//GEN-LAST:event_jCB_Pt5ActionPerformed

    private void jCB_Pt6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt6ActionPerformed
        conexaoModificada(jCB_Pt6.isSelected(), 6);
    }//GEN-LAST:event_jCB_Pt6ActionPerformed

    private void jCB_Pt7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_Pt7ActionPerformed
        conexaoModificada(jCB_Pt7.isSelected(), 7);
    }//GEN-LAST:event_jCB_Pt7ActionPerformed

    private void jCBoxTanque1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBoxTanque1ItemStateChanged
        if(jCBoxTanque1.getItemCount() != 0)
        {
            int canalAtivo = (int) jCBoxTanque1.getSelectedItem();
            switch(canalAtivo)
            {
                case 0:
                {
                    jCB_Pt0.setSelected(true);
                    break;
                }
                case 1:
                {
                    jCB_Pt1.setSelected(true);
                    break;
                }
                case 2:
                {
                    jCB_Pt2.setSelected(true);
                    break;
                }
                case 3:
                {
                    jCB_Pt3.setSelected(true);
                    break;
                }
                case 4:
                {
                    jCB_Pt4.setSelected(true);
                    break;
                }
                case 5:
                {
                    jCB_Pt5.setSelected(true);
                    break;
                }
                case 6:
                {
                    jCB_Pt6.setSelected(true);
                    break;
                }
                case 7:
                {
                    jCB_Pt7.setSelected(true);
                    break;
                }
            }
        }
    }//GEN-LAST:event_jCBoxTanque1ItemStateChanged

    private void jCBPlotP1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPlotP1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBPlotP1ActionPerformed

    private void jCBFiltroDerivativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBFiltroDerivativoActionPerformed
        jSpinnerGama.setEnabled(jCBFiltroDerivativo.isSelected());
    }//GEN-LAST:event_jCBFiltroDerivativoActionPerformed

    private void jCBAntiWindup_MIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAntiWindup_MIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBAntiWindup_MIActionPerformed

    private void jCBIntCondicional_MIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBIntCondicional_MIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBIntCondicional_MIActionPerformed

    private void jCBFiltroDerivativo_MIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBFiltroDerivativo_MIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBFiltroDerivativo_MIActionPerformed

    private void jCBoxTanqueSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBoxTanqueSelectedActionPerformed
        switch(jCBoxTanqueSelected.getSelectedIndex())
        {
            case 0:
            {
                jTabbedPane.setEnabledAt(3, false);
                break;
            }
            case 1:
            {
                jTabbedPane.setEnabledAt(3, false);
                break;
            }
            case 2:
            {
                jTabbedPane.setEnabledAt(3, true);
                break;
            }
        }
    }//GEN-LAST:event_jCBoxTanqueSelectedActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupMalha;
    private javax.swing.ButtonGroup buttonGroupSinal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_Enviar;
    private javax.swing.JButton jButton_Parar;
    private javax.swing.JCheckBox jCBAntiWindup;
    private javax.swing.JCheckBox jCBAntiWindup_MI;
    private javax.swing.JCheckBox jCBFiltroDerivativo;
    private javax.swing.JCheckBox jCBFiltroDerivativo_MI;
    private javax.swing.JCheckBox jCBIntCondicional;
    private javax.swing.JCheckBox jCBIntCondicional_MI;
    private javax.swing.JComboBox jCBOvershoot;
    private javax.swing.JCheckBox jCBPlotAcaoD;
    private javax.swing.JCheckBox jCBPlotAcaoI;
    private javax.swing.JCheckBox jCBPlotAcaoP;
    private javax.swing.JCheckBox jCBPlotErro;
    private javax.swing.JCheckBox jCBPlotMV;
    private javax.swing.JCheckBox jCBPlotMVSat;
    private javax.swing.JCheckBox jCBPlotP0;
    private javax.swing.JCheckBox jCBPlotP1;
    private javax.swing.JCheckBox jCBPlotSP;
    private javax.swing.JComboBox jCBTempoAcomodacao;
    private javax.swing.JComboBox jCBTempoSubida;
    private javax.swing.JCheckBox jCB_Pt0;
    private javax.swing.JCheckBox jCB_Pt1;
    private javax.swing.JCheckBox jCB_Pt2;
    private javax.swing.JCheckBox jCB_Pt3;
    private javax.swing.JCheckBox jCB_Pt4;
    private javax.swing.JCheckBox jCB_Pt5;
    private javax.swing.JCheckBox jCB_Pt6;
    private javax.swing.JCheckBox jCB_Pt7;
    private javax.swing.JComboBox jCBoxCanalSaida;
    private javax.swing.JComboBox jCBoxTanque1;
    private javax.swing.JComboBox jCBoxTanque2;
    private javax.swing.JComboBox jCBoxTanqueSelected;
    private javax.swing.JComboBox jCBoxTipoControle;
    private javax.swing.JComboBox jCBoxTipoControle_MI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelControleTanque;
    private javax.swing.JLabel jLabelGama;
    private javax.swing.JLabel jLabelKd;
    private javax.swing.JLabel jLabelKi;
    private javax.swing.JLabel jLabelKp;
    private javax.swing.JLabel jLabelOvershoot;
    private javax.swing.JLabel jLabelOvershootUnit;
    private javax.swing.JLabel jLabelTAcomodacao;
    private javax.swing.JLabel jLabelTPico;
    private javax.swing.JLabel jLabelTSubida;
    private javax.swing.JLabel jLabelTau_d;
    private javax.swing.JLabel jLabelTau_i;
    private javax.swing.JLabel jLabel_4v;
    private javax.swing.JLabel jLabel_Am_mi;
    private javax.swing.JLabel jLabel_Du_ma;
    private javax.swing.JLabel jLabel_Du_mi;
    private javax.swing.JLabel jLabel_Offset;
    private javax.swing.JLabel jLabel_Periodo;
    private javax.swing.JLabel jLabel_SetPoint;
    private javax.swing.JLabel jLabel_Taw;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel2Ordem;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelControle;
    private javax.swing.JPanel jPanelGrafico;
    private javax.swing.JRadioButton jRBAleatorio;
    private javax.swing.JRadioButton jRBDegrau;
    private javax.swing.JRadioButton jRBDenteDeSerra;
    private javax.swing.JRadioButton jRBQuadrado;
    private javax.swing.JRadioButton jRBSenoidal;
    private javax.swing.JRadioButton jRadioButtonMA;
    private javax.swing.JRadioButton jRadioButtonMF;
    private javax.swing.JSpinner jSpinnerGama;
    private javax.swing.JSpinner jSpinnerGama_MI;
    private javax.swing.JSpinner jSpinnerKd;
    private javax.swing.JSpinner jSpinnerKd_MI;
    private javax.swing.JSpinner jSpinnerKi;
    private javax.swing.JSpinner jSpinnerKi_MI;
    private javax.swing.JSpinner jSpinnerKp;
    private javax.swing.JSpinner jSpinnerKp_MI;
    private javax.swing.JSpinner jSpinnerTaw_MI;
    private javax.swing.JSpinner jSpinnerTd;
    private javax.swing.JSpinner jSpinnerTd_MI;
    private javax.swing.JSpinner jSpinnerTi;
    private javax.swing.JSpinner jSpinnerTi_MI;
    private javax.swing.JSpinner jSpinner_0a30;
    private javax.swing.JSpinner jSpinner_4v;
    private javax.swing.JSpinner jSpinner_Am_mi;
    private javax.swing.JSpinner jSpinner_Du_ma;
    private javax.swing.JSpinner jSpinner_Du_mi;
    private javax.swing.JSpinner jSpinner_Offset;
    private javax.swing.JSpinner jSpinner_Periodo;
    private javax.swing.JSpinner jSpinner_Taw;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTextField jTxtIP;
    private javax.swing.JTextField jTxtPorta;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the conectado
     */
    public boolean isConectado() {
        return conectado;
    }

    /**
     * @param conectado the conectado to set
     */
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    /**
     * @return the quanserClient
     */
    public QuanserClient getQuanserClient() {
        return quanserClient;
    }

    /**
     * @param quanserClient the quanserClient to set
     */
    public void setQuanserClient(QuanserClient quanserClient) {
        this.quanserClient = quanserClient;
    }
   
}

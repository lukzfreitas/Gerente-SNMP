/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.SNMPModel;
import model.UtilizacaoDeLinkModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import services.SNMPGet;
import services.SnmpT2;

/**
 *
 * @author Lucas
 */
public class UtilizacaoDeLinkController extends ApplicationFrame {

    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final int COUNT = 2 * 6;
    private static final int SLOW = 100 * 5;    
    private Timer timer;
    private SnmpT2 snmpT2;
    private UtilizacaoDeLinkModel utilizacaoDeLinkModel;    
    private int i;

    public UtilizacaoDeLinkController(
            SnmpT2 snmpT2, 
            UtilizacaoDeLinkModel utilizacaoDeLinkModel
    ) throws IOException, InterruptedException {
        super("Utilização de Link");        
        this.snmpT2 = snmpT2;
        this.utilizacaoDeLinkModel = utilizacaoDeLinkModel;
        final DynamicTimeSeriesCollection dataset =
                new DynamicTimeSeriesCollection(1, COUNT, new Second());
        dataset.setTimeBase(new Second());
        dataset.addSeries(insertData(), 0, "Gráfico");
        JFreeChart chart = createChart(dataset);

        final JButton run = new JButton(STOP);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
                    run.setText(START);
                } else {
                    timer.start();
                    run.setText(STOP);
                }
            }
        });

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        this.add(btnPanel, BorderLayout.SOUTH);
        this.add(new ChartPanel(chart), BorderLayout.CENTER);

        timer = new Timer(SLOW, new ActionListener() {

            float[] newData = new float[1];

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    newData[0] = generateData();
                    dataset.advanceTime();
                    dataset.appendData(newData);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    private float generateData() throws IOException, InterruptedException {
        int valueInt = utilizacaoDeLinkModel.getUtilizacaoDeLink(i);
        float valueFloat = 0;
        valueFloat = (float) valueInt;
        return valueFloat;
    }

    private float[] insertData() throws IOException, InterruptedException {
        float[] a = new float[COUNT];
        for (i = 1; i < a.length; i++) {
            
            a[i] = generateData();
        }
        return a;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Utilização de Link",
                "tempo",
                "Utilização de link",
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        return result;
    }

    public void start() {
        timer.start();
    }

    public static void createGraphic(
            String ipAddress,
            String community,
            int time,
            SnmpT2 snmpT2,
            int ifSpeed            
    ) throws IOException, InterruptedException {        
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                UtilizacaoDeLinkController snmpController = null;
                try {
                    
                    UtilizacaoDeLinkModel utilizacaoDeLinkModel = new UtilizacaoDeLinkModel(ifSpeed, snmpT2);        
                    snmpController = new UtilizacaoDeLinkController(snmpT2, utilizacaoDeLinkModel);
                    snmpController.pack();
                    RefineryUtilities.centerFrameOnScreen(snmpController);
                    snmpController.setVisible(true);
                    snmpController.start();
                } catch (IOException ex) {
                    Logger.getLogger(SNMPController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SNMPController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        });
    }
}

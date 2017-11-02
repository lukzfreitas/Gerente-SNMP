package controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import model.SNMPModel;
import services.SnmpT2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lucas on 28/05/2017.
 */
public class SNMPController extends ApplicationFrame {

    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final int COUNT = 2 * 6;
    private static final int SLOW = 100 * 10;
    private String title;
    private String description;
    private Timer timer;
    private SnmpT2 snmpT2;
    private SNMPModel snmpModel;    
    private int time;

    public SNMPController(
            String title, 
            SnmpT2 snmpT2, 
            SNMPModel snmpModel, 
            String description,
            int time
    ) throws IOException, InterruptedException {
        super(title);
        this.title = title;
        this.snmpT2 = snmpT2;
        this.snmpModel = snmpModel;
        this.description = description;
        this.time = time;
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
        int valueInt = snmpModel.getData(snmpT2);
        float valueFloat = 0;
        valueFloat = (float) valueInt;
        return valueFloat;
    }

    private float[] insertData() throws IOException, InterruptedException {
        float[] a = new float[COUNT];
        for (int i = 0; i < a.length; i++) {            
            a[i] = generateData();
        }
        return a;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                title,
                "tempo",
                description,
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
        timer.setDelay(time);
    }

    public static void createGraphic(
            String ipAddress,
            String community,
            int time,
            String title,
            String oid,
            String description
    ) throws IOException, InterruptedException {
        SnmpT2 snmpT2 = SnmpT2.snmpFactory(ipAddress, community);
        SNMPModel snmpModel = new SNMPModel(oid);
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SNMPController snmpController = null;
                try {
                    snmpController = new SNMPController(title, snmpT2, snmpModel, description, time);
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

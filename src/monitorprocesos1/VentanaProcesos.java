/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitorprocesos1;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.*;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class VentanaProcesos extends JFrame {

    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JTextField campoBusqueda;
    private final Timer timer;

    public VentanaProcesos() {
      
        setTitle("Monitor de Procesos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
  
        modelo = new DefaultTableModel(new Object[]{"Nombre", "PID", "Memoria (MB)"}, 0);
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Consolas", Font.PLAIN, 14));
        tabla.setRowHeight(22);
        tabla.setFillsViewportHeight(true);
        
        campoBusqueda = new JTextField();
        campoBusqueda.setToolTipText("Buscar proceso...");
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabla(campoBusqueda.getText());
            }
          
        });
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(campoBusqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panel);

        timer = new Timer(2000, e -> cargarProcesos());
        timer.start();

        cargarProcesos(); // primera carga
 
    }

    private void cargarProcesos() {
        modelo.setRowCount(0); // limpiar tabla

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        List<OSProcess> procesos = os.getProcesses(
            p -> true,
            Comparator.comparingLong(OSProcess::getResidentSetSize).reversed(),
            30
        );

        for (OSProcess p : procesos) {
            double memoriaMB = p.getResidentSetSize() / (1024.0 * 1024);
            modelo.addRow(new Object[]{
                p.getName(),
                p.getProcessID(),
                String.format("%.2f", memoriaMB)
            });
        }
    }

    private void filtrarTabla(String texto) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }
}
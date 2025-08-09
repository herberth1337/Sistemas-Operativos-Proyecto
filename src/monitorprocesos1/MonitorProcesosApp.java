/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitorprocesos1;

import javax.swing.SwingUtilities;
/**
 *
 * @author HP
 */
public class MonitorProcesosApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaProcesos().setVisible(true);
        });
    }
    
}

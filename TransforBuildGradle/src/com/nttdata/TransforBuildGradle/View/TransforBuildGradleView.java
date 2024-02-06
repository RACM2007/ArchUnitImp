
package com.nttdata.TransforBuildGradle.View;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TransforBuildGradleView {
    
    public static String obtenerUbicacionCarpeta(String mensaje) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(mensaje);

        fileChooser.setCurrentDirectory(new File("C:\\"));

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File carpetaSeleccionada = fileChooser.getSelectedFile();
            return carpetaSeleccionada.getAbsolutePath();
        } else {
            return null;
        }
    }
    
    public static String obtenerUbicacionArchivoTxt(String mensaje) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(mensaje);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String nombreArchivo = archivoSeleccionado.getName();
            if (nombreArchivo.endsWith(".txt")) {
                return archivoSeleccionado.getAbsolutePath();
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, selecciona un archivo de texto (.txt).", "Error", JOptionPane.ERROR_MESSAGE);
                return obtenerUbicacionArchivoTxt(mensaje);
            }
        } else {
            return null;
        }
    }
    
    
    public static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje , "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje,"Error", JOptionPane.ERROR_MESSAGE);
    }
    
}

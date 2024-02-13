package com.nttdata.TransforBuildGradle.Util;

import com.nttdata.TransforBuildGradle.View.TransforBuildGradleView;



public class TransforBuildGradleUtil {
    
    public static TransforBuildGradleView view = new TransforBuildGradleView();

    public TransforBuildGradleUtil() {
    }
    
    public static String pedirUbicacionCarpeta(String mensaje) {
        String ubi = view.obtenerUbicacionCarpeta(mensaje);
        
        while (ubi == null){
            view.mostrarMensaje("No se ha seleccionado ninguna carpeta.");
            ubi = view.obtenerUbicacionCarpeta(mensaje);
        }
        
        System.out.println("La ubicación de la carpeta seleccionada es: " + ubi);
        
        return ubi;
    }
    
    public static String pedirUbicacionArchivo(String mensaje) {
        String ubi = view.obtenerUbicacionArchivoTxt(mensaje);
        
        while (ubi == null){
            view.mostrarMensaje("No se ha seleccionado ningún Archivo.");
            ubi = view.obtenerUbicacionArchivoTxt(mensaje);
        }
        
        System.out.println("La ubicación del archivo seleccionado es: " + ubi);
        
        return ubi;
    }
    
    public static void FalloProceso(String mensaje) {
        view.mostrarMensaje(mensaje);
        System.exit(0);
    }
    
    public static int obtenerSistemaOperativo() {
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        
        if (sistemaOperativo.contains("win")) {
            return 1;
        } else {
            if (sistemaOperativo.contains("mac")) {
                return 2;
            }else{
                return 3;
            }
        }
    }
    
}

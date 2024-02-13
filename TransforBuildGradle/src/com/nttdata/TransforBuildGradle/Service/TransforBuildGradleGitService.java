package com.nttdata.TransforBuildGradle.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class TransforBuildGradleGitService {
    
    public static TransforBuildGradleTxtService modelTxt = new TransforBuildGradleTxtService();
    private static TransforBuildGradleComandService command = new TransforBuildGradleComandService();

    public int ClonarRepos(String ubiTxt, String ubi, int so) {
        int  respuesta;
        
        respuesta = clonarRepositorios(ubiTxt, ubi, so);
        
        return respuesta;
    }
    
    public int clonarRepositorios(String archivoURLs, String carpetaDestino, int so) {
        File archivo = new File(archivoURLs);
        String pathNewTxt = archivo.getParent()+"/ubiRepos.txt";
        int contador=0;
        File archivoTxtNew = new File(pathNewTxt);

        // Verificar si el archivo existe
        if (archivoTxtNew.exists()) {
            if (archivoTxtNew.delete()) {
                System.out.println("El archivo ubiRepos ha sido eliminado exitosamente.");
            } else {
                System.out.println("No se pudo eliminar el archivo ubiRepos.");
            }
        } 

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                contador++;
                clonarRepositorio(linea, carpetaDestino, pathNewTxt, so);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        
        return contador;
    }
    
    private static void clonarRepositorio(String urlRepositorio, String carpetaDestino, String pathNewTxt, int so) {
        
        String nombreRepositorio = obtenerNombreRepositorio(urlRepositorio);
        String rutaDestino = carpetaDestino + "/" + nombreRepositorio;
        try {
            
            command.ejecutarComando("git clone " + urlRepositorio, carpetaDestino, so);
            
            //escribir archivo
            String carpetaApp = buscarCarpetaApp(rutaDestino);
            modelTxt.agregarTexto(pathNewTxt, rutaDestino + "/" + carpetaApp);
            
        } catch (Exception e) {
            System.err.println("Error al clonar el repositorio: " + urlRepositorio);
            
            //escribir archivo
            modelTxt.agregarTexto(pathNewTxt, "xxx"+rutaDestino);
            
            e.printStackTrace();
        }
    }
    
    private static String obtenerNombreRepositorio(String urlRepositorio) {
        String[] partesURL = urlRepositorio.split("/");
        String nombreRepositorioConExtension = partesURL[partesURL.length - 1];
        return nombreRepositorioConExtension.split("\\.")[0];
    }
    
    public static String buscarCarpetaApp(String directorio) {
        File dir = new File(directorio);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] archivos = dir.listFiles();
            
            for (File archivo : archivos) {
                if (archivo.isDirectory() && archivo.getName().startsWith("app")) {
                    return archivo.getName();
                }
            }
        }
        return null;
    }
    
}

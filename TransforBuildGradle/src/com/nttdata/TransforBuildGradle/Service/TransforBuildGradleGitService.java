
package com.nttdata.TransforBuildGradle.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.eclipse.jgit.api.Git;


public class TransforBuildGradleGitService {
    
    public static TransforBuildGradleTxtService modelTxt = new TransforBuildGradleTxtService();

    public boolean ClonarRepos(String ubiTxt, String ubi) {
        boolean respuesta;
        
        respuesta = clonarRepositorios(ubiTxt, ubi);
        
        return respuesta;
    }
    
    public boolean clonarRepositorios(String archivoURLs, String carpetaDestino) {
        File archivo = new File(archivoURLs);
        String pathNewTxt = archivo.getParent();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                clonarRepositorio(linea, carpetaDestino, pathNewTxt);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    private static void clonarRepositorio(String urlRepositorio, String carpetaDestino, String pathNewTxt) {
        try {
            // Construye la URL de destino
            String nombreRepositorio = obtenerNombreRepositorio(urlRepositorio);
            String rutaDestino = carpetaDestino + "/" + nombreRepositorio;

            // Clona el repositorio
            Git.cloneRepository()
                    .setURI(urlRepositorio)
                    .setDirectory(new File(rutaDestino))
                    .call();
            
            //escribir archivo
            modelTxt.agregarTexto(pathNewTxt+"/ubiRepos.txt", rutaDestino);
            
            System.out.println("Repositorio clonado exitosamente: " + urlRepositorio);
            
        } catch (Exception e) {
            System.err.println("Error al clonar el repositorio: " + urlRepositorio);
            e.printStackTrace();
        }
    }
    
    private static String obtenerNombreRepositorio(String urlRepositorio) {
        // Obtiene el nombre del repositorio desde la URL
        String[] partesURL = urlRepositorio.split("/");
        String nombreRepositorioConExtension = partesURL[partesURL.length - 1];
        return nombreRepositorioConExtension.split("\\.")[0];
    }
    
}

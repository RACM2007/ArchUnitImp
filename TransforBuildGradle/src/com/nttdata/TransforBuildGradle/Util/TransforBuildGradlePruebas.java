package com.nttdata.TransforBuildGradle.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class TransforBuildGradlePruebas {
    public static void main(String[] args) {
      
        String archivoURLs = "D:/listaGit.txt";
        String carpetaDestino = "D:/queso";

        clonarRepositorios(archivoURLs, carpetaDestino);
        
        
    }
    
    public static void clonarRepositorios(String archivoURLs, String carpetaDestino) {
        File archivo = new File(archivoURLs);

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                clonarRepositorio(linea, carpetaDestino);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void clonarRepositorio(String urlRepositorio, String carpetaDestino) {
        try {
            // Construye la URL de destino
            String nombreRepositorio = obtenerNombreRepositorio(urlRepositorio);
            String rutaDestino = carpetaDestino + "/" + nombreRepositorio;

            // Clona el repositorio
            Git.cloneRepository()
                    .setURI(urlRepositorio)
                    .setDirectory(new File(rutaDestino))
                    .call();

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
    


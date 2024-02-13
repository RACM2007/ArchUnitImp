package com.nttdata.TransforBuildGradle.Util;

import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleService;
import static com.nttdata.TransforBuildGradle.Service.TransforBuildGradleService.validarBuildGradle;
import com.nttdata.TransforBuildGradle.model.Parametros;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class TransforBuildGradlePruebas {
    
    public static TransforBuildGradleService modelTxt = new TransforBuildGradleService();
    
    

    public static void main(String[] args) throws IOException {
        String ubicacionArchivo = "C:\\Archivos Prueba/build.gradle"; // Ruta del archivo build.gradle
        boolean esVersion1x = validarVersion1x(ubicacionArchivo, null);
        System.out.println(esVersion1x);
    }
    
    public static boolean validarVersion1x(String ubi, Parametros param) throws IOException {
        //Encontrar archivo build.gradle
        File arBuildGradle = validarBuildGradle(ubi, "build.gradle");
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(ubi)))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().startsWith("springBootVersion")) {
                    String[] partes = linea.split("\\s+");
                    for (String parte : partes) {
                        if (parte.contains("1.")) {
                            return false;
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return true;
        
    }
    
}
    


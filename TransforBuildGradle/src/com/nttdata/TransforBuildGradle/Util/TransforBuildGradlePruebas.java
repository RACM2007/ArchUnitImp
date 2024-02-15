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
    
    

    public static void main(String[] args) {
        String ruta = "C://src/tat/queso/null";
        String nombreCarpetaAnterior = obtenerCarpetaAnterior(ruta);
        System.out.println("Carpeta anterior: " + nombreCarpetaAnterior);
    }

    public static String obtenerCarpetaAnterior(String ruta) {
        String[] segmentos = ruta.split("/");
        if (segmentos.length >= 2) {
            return segmentos[segmentos.length - 2];
        } else {
            return ""; // O manejar el caso en el que no haya suficientes segmentos
        }
    }
    
}
    


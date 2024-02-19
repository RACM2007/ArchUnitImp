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
        String ruta = "C://Archivos Prueba";
       obtenerCarpetaAnterior(ruta);
    }

    public static void obtenerCarpetaAnterior(String ruta) throws IOException {
        modelTxt.modTestGradle(ruta);
    }
    
}
    


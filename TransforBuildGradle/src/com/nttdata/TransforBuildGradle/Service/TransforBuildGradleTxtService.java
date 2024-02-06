package com.nttdata.TransforBuildGradle.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TransforBuildGradleTxtService {
    
    public static void agregarTexto(String rutaArchivo, String ubi) {
        File archivo = new File(rutaArchivo);

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            FileWriter fw = new FileWriter(archivo, true); 
            BufferedWriter bw = new BufferedWriter(fw);

            if (archivo.length() != 0) {
                bw.newLine(); 
            }

            bw.write(ubi);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

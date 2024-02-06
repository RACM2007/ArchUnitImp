package com.nttdata.TransforBuildGradle.Service;

import java.io.File;
import java.io.IOException;

public class TransforBuildGradleComandService {

    public boolean ejeComand(String ubi) {
        boolean respuesta = true;
        
        try {
            // Comando: gradle clean
            respuesta = ejecutarComando("gradle clean", ubi);

            // Comando: gradle clean build
            if (respuesta)
            ejecutarComando("gradle clean build", ubi);

            // Comando: gradle clean architectureTest
            if (respuesta)
            ejecutarComando("gradle clean architectureTest", ubi);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        return respuesta;
    }
    
    private boolean ejecutarComando(String comando, String directorio) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd", "/c", comando);
        builder.directory(new File(directorio));
        Process proceso = builder.start();
        int resultado = proceso.waitFor();
        if (resultado != 0) {
            System.err.println("Error al ejecutar el comando: " + comando);
            return false;
        } else {
            System.out.println("Comando ejecutado exitosamente: " + comando);
            return true;
        }
        
    }
    
}

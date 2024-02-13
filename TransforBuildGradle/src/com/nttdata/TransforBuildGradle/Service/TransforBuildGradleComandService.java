package com.nttdata.TransforBuildGradle.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class TransforBuildGradleComandService {

    public boolean ejeComand(String ubicacion, int so) {
        boolean respuesta = true;
        
        try {
            if (so==1) {
                // Comando: gradle clean
                respuesta = ejecutarComando("gradle clean", ubicacion, so);

                // Comando: gradle clean build
                if (respuesta){
                    ejecutarComando("gradle clean build", ubicacion, so);

                    // Comando: gradle clean architectureTest
                    ejecutarComando("gradle clean architectureTest", ubicacion, so);
                }
                
            }else{
                File carpeta = new File(ubicacion);
                File carpetaPadre = carpeta.getParentFile();
                String ubi = carpetaPadre.getAbsolutePath();

                System.out.println("chmod 777 ./gradlew " + ubi + " respuesta: " + ejecutarComando("chmod 777 ./gradlew", ubi, so));

                // Comando: gradle clean
                System.out.println("Ruta clean " + ubi);
                respuesta = ejecutarComando("./gradlew clean architectureTest", ubi, 120, TimeUnit.SECONDS);

            }
            
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        return respuesta;
    }
    
    public boolean ejecutarComando(String comando, String directorio, int so) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        
        if (so==1) {
            builder.command("cmd", "/c", comando);
        }else{
            builder.command("bash", "-c", comando);
        }
        
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
    
    public boolean ejecutarComando(String comando, String directorio, long time, TimeUnit timeUnit) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("bash", "-c", comando);
        builder.directory(new File(directorio));
        File logFile = Paths.get("/Users/fbrione/Documents/learchunit/repos/log.log").toFile();
        builder.redirectErrorStream(true);
        builder.redirectOutput(logFile);
        Process proceso = builder.start();
        boolean waitFor = proceso.waitFor(time, timeUnit);
        System.out.println("waitFor " + waitFor);
        return waitFor;
    }
    
}

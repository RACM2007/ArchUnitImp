package com.nttdata.TransforBuildGradle;

import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleComandService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleGitService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleResultService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleService;
import com.nttdata.TransforBuildGradle.Util.TransforBuildGradleUtil;
import com.nttdata.TransforBuildGradle.View.TransforBuildGradleView;
import java.io.File;
import java.io.IOException;

public class TransforBuildGradleMain {

    public static TransforBuildGradleUtil util = new TransforBuildGradleUtil();
    public static TransforBuildGradleService model = new TransforBuildGradleService();
    public static TransforBuildGradleComandService modelCom = new TransforBuildGradleComandService();
    public static TransforBuildGradleResultService modelRes = new TransforBuildGradleResultService();
    public static TransforBuildGradleGitService modelGit = new TransforBuildGradleGitService();
    public static TransforBuildGradleView view = new TransforBuildGradleView();
    
    public static void main(String[] args) throws IOException {
        String ubi;
        String ubiTxt;
        boolean respuesta= false;
        
        //Pedir Archivo txt, lista repos
        ubiTxt = util.pedirUbicacionArchivo("Seleccione la ubicación del archivo de repositorios");
        
        
        //Pedir ubicación para guardar repos
        ubi = util.pedirUbicacionCarpeta("Seleccione la ubicación para guardar los proyectos");
         
        //realizar el clonado de repos y escribir txt
        respuesta = modelGit.ClonarRepos(ubiTxt, ubi);
        
        //Implementar y ejecutar archunit a cada repo
        if (respuesta)
            respuesta = model.procesar(new File(ubiTxt).getParent()+"/ubiRepos.txt");
        
//        //Mostrar resultados
//        if (respuesta)
//        respuesta= modelRes.MostrarResultados(ubi);
        
        //Proceso Terminado
        if (respuesta) {
            view.mostrarMensaje("El proceso ha finalizado exitosamente");
        }else{
            view.mostrarMensajeError("El proceso ha fallado");
        }
        
    }
    
}

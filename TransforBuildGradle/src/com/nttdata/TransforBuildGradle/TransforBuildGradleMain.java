package com.nttdata.TransforBuildGradle;

import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleComandService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleGitService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleResultService;
import com.nttdata.TransforBuildGradle.Service.TransforBuildGradleService;
import com.nttdata.TransforBuildGradle.Util.TransforBuildGradleUtil;
import com.nttdata.TransforBuildGradle.View.TransforBuildGradleView;
import com.nttdata.TransforBuildGradle.model.Parametros;
import com.nttdata.TransforBuildGradle.model.Resultados;
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
        int totRepos=0;
        Parametros param;
        
        //Pedir Archivo txt, lista repos
        ubiTxt = util.pedirUbicacionArchivo("Seleccione la ubicación del archivo de repositorios");
        
        //Pedir ubicación para guardar repos
        ubi = util.pedirUbicacionCarpeta("Seleccione la ubicación para guardar los proyectos");
        
        //Cargar Parámetros
        param = new Parametros(0);
        
        //realizar el clonado de repos y escribir txt
        totRepos = modelGit.ClonarRepos(ubiTxt, ubi, param.getSO());
        
        //Implementar y ejecutar archunit a cada repo
        if (totRepos > 0)
            respuesta = model.procesar(new File(ubiTxt).getParent()+"/ubiRepos.txt", param);
        
        Resultados resu = modelRes.CrearResultados(new File(ubiTxt).getParent()+"/ubiRepos.txt", totRepos);
        
        //Mostrar resultados
        if (respuesta) {
            modelRes.crearExcelResultados(resu);
            respuesta= modelRes.MostrarResultados(new File(ubiTxt).getParent()+"/ubiRepos.txt");
        }
            
        //Proceso Terminado
        if (respuesta) {
            view.mostrarMensaje("El proceso ha finalizado exitosamente");
        }else{
            view.mostrarMensajeError("El proceso ha fallado");
        }
        
    }
    
}

package com.nttdata.TransforBuildGradle.Service;

import com.nttdata.TransforBuildGradle.model.Resultados;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.select.Elements;

public class TransforBuildGradleResultService {

    public boolean MostrarResultados(String rutaArchivo) {
        
        try {
            FileReader fileReader = new FileReader(rutaArchivo);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                
                String filePath = linea+"/build/reports/tests/architectureTest/index.html";

                try {
                    String valor = obtenerValorDelElemento(filePath);
                    System.out.println("Resultado de "+linea+": " + valor);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            bufferedReader.close();
            
            return true;
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return false;
        }
        
    }
    
    public void crearExcelResultados(Resultados resu) {
        try {
            // Crear los directorios si no existen
            File archivo = new File(resu.getUbicacionArchivo());
            File directorio = archivo.getParentFile();
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            
            // Crear un nuevo libro de trabajo Excel
            Workbook libroExcel = new XSSFWorkbook();
            
            Sheet hojaExcel = libroExcel.createSheet("Resultados ArchUnit");

            Row fila = hojaExcel.createRow(0);
            Cell celda = fila.createCell(0);
            celda.setCellValue("Aplicativos");
            
            Cell celda1 = fila.createCell(1);
            celda1.setCellValue("Resultados Finales");
            
            Cell celda2 = fila.createCell(2);
            celda2.setCellValue("cl.bci.architecture.rules.advanced");
            
            Cell celda3 = fila.createCell(3);
            celda3.setCellValue("cl.bci.architecture.rules.advanced.configuration	");
            
            Cell celda4 = fila.createCell(4);
            celda4.setCellValue("cl.bci.architecture.rules.advanced.controller");
            
            Cell celda5 = fila.createCell(5);
            celda5.setCellValue("cl.bci.architecture.rules.advanced.repository");
            
            Cell celda6 = fila.createCell(6);
            celda6.setCellValue("cl.bci.architecture.rules.advanced.service");
            
            Cell celda7 = fila.createCell(7);
            celda7.setCellValue("cl.bci.architecture.rules.basic.configuration");
            
            Cell celda8 = fila.createCell(8);
            celda8.setCellValue("cl.bci.architecture.rules.basic.constants");
            
            Cell celda9 = fila.createCell(9);
            celda9.setCellValue("cl.bci.architecture.rules.basic.consumer");
            
            Cell celda10 = fila.createCell(10);
            celda10.setCellValue("cl.bci.architecture.rules.basic.controller");
            
            Cell celda11 = fila.createCell(11);
            celda11.setCellValue("cl.bci.architecture.rules.basic.exception");
            
            Cell celda12 = fila.createCell(12);
            celda12.setCellValue("cl.bci.architecture.rules.basic.helper");
            
            Cell celda13 = fila.createCell(13);
            celda13.setCellValue("cl.bci.architecture.rules.basic.repository");
            
            Cell celda14 = fila.createCell(14);
            celda14.setCellValue("cl.bci.architecture.rules.basic.service");
            
            Cell celda15 = fila.createCell(15);
            celda15.setCellValue("cl.bci.architecture.rules.basic.helper");
            
            for (int i = 1; i < resu.getTotalapps()+1; i++) {
                Row filaF = hojaExcel.createRow(i);
                
                Cell celdaF = filaF.createCell(0);
                celdaF.setCellValue(resu.getAplicativos()[i-1]);
                
                Cell celdaF1 = filaF.createCell(1);
                celdaF1.setCellValue(resu.getResultadosfinales()[i-1]);

                Cell celdaF2 = filaF.createCell(2);
                celdaF2.setCellValue(resu.getResultadosadvanced()[i-1]);

                Cell celdaF3 = filaF.createCell(3);
                celdaF3.setCellValue(resu.getResultadosadvancedConfiguration()[i-1]);

                Cell celdaF4 = filaF.createCell(4);
                celdaF4.setCellValue(resu.getResultadosadvancedController()[i-1]);

                Cell celdaF5 = filaF.createCell(5);
                celdaF5.setCellValue(resu.getResultadosadvancedRepository()[i-1]);

                Cell celdaF6 = filaF.createCell(6);
                celdaF6.setCellValue(resu.getResultadosadvancedService()[i-1]);

                Cell celdaF7 = filaF.createCell(7);
                celdaF7.setCellValue(resu.getResultadosbasicConfiguration()[i-1]);

                Cell celdaF8 = filaF.createCell(8);
                celdaF8.setCellValue(resu.getResultadosbasicConstants()[i-1]);

                Cell celdaF9 = filaF.createCell(9);
                celdaF9.setCellValue(resu.getResultadosbasicConsumer()[i-1]);

                Cell celdaF10 = filaF.createCell(10);
                celdaF10.setCellValue(resu.getResultadosbasicController()[i-1]);

                Cell celdaF11 = filaF.createCell(11);
                celdaF11.setCellValue(resu.getResultadosbasicException()[i-1]);

                Cell celdaF12 = filaF.createCell(12);
                celdaF12.setCellValue(resu.getResultadosbasicHelper()[i-1]);

                Cell celdaF13 = filaF.createCell(13);
                celdaF13.setCellValue(resu.getResultadosbasicRepository()[i-1]);

                Cell celdaF14 = filaF.createCell(14);
                celdaF14.setCellValue(resu.getResultadosbasicService()[i-1]);

                Cell celdaF15 = filaF.createCell(15);
                celdaF15.setCellValue(resu.getResultadosbasicUtil()[i-1]);
                
                
            }
            
            FileOutputStream salida = new FileOutputStream(archivo);
            libroExcel.write(salida);
            salida.close();

            System.out.println("Archivo Excel creado exitosamente en: " + resu.getUbicacionArchivo());
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al crear el archivo Excel.");
        }
    }
    
   
    public static String obtenerValorDelElemento(String filePath) {
        
        try{
            File input = new File(filePath);
            Document document = Jsoup.parse(input, "UTF-8");
            Element divElement = document.selectFirst("div.percent");

            if (divElement != null) {
                // Obtener el texto dentro del elemento
                String valor = divElement.text();
                return valor;
            } else {
                return "No se encontró el Resultado";
            }
        }catch(IOException e){
            return "Repositorio presenta errores, no existe o sin permisos";
        }
        
    }
    
    public static String obtenerValorElemento(String filePath) throws IOException {
        // Cargar el archivo HTML usando Jsoup
        File input = new File(filePath);
        Document document = Jsoup.parse(input, "UTF-8");

        // Buscar el elemento <div> con la clase "percent"
        Element divElement = document.selectFirst("div.percent");

        // Verificar si se encontró el elemento
        if (divElement != null) {
            // Obtener el texto dentro del elemento
            String valor = divElement.text();
            return valor;
        } else {
            return "No se encontró el elemento <div class=\"percent\">";
        }
    }

    public Resultados CrearResultados(String rutaArchivo, int totRepos) {
        
        Resultados resu = new Resultados();
        
        String [] aplicativos = new String[totRepos];
        String [] resultadosF = new String[totRepos];
        String[] rAdvancedConfiguration = new String[totRepos];
        String[] rAdvanced              = new String[totRepos];
        String[] rAdvancedController    = new String[totRepos];
        String[] rAdvancedRepository    = new String[totRepos];
        String[] rAdvancedService       = new String[totRepos];
        String[] rBasicConfiguration    = new String[totRepos];
        String[] rBasicConstants        = new String[totRepos];
        String[] rBasicConsumer         = new String[totRepos];
        String[] rBasicController       = new String[totRepos];
        String[] rBasicException        = new String[totRepos];
        String[] rBasicHelper           = new String[totRepos];
        String[] rBasicRepository       = new String[totRepos];
        String[] rBasicService          = new String[totRepos];
        String[] rBasicUtil             = new String[totRepos];
        
        File archivo = new File(rutaArchivo);
        
        resu.setUbicacionArchivo(archivo.getParent()+"/resultados.xlsx");
        
        try {
            
            FileReader fileReader = new FileReader(rutaArchivo);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            
            int contador=0;

            while ((linea = bufferedReader.readLine()) != null) {
                
                aplicativos[contador] = obtenerNombreapp(linea);
                
                String filePath = linea+"/build/reports/tests/architectureTest/index.html";

                try {
                    String valor = obtenerValorDelElemento(filePath);
                    resultadosF[contador] = valor;
                    if (valor.startsWith("R")) {
                        rAdvanced  [contador] = "-";
                        rAdvancedConfiguration[contador] = "-";
                        rAdvancedController[contador]    = "-";
                        rAdvancedRepository[contador]    = "-";
                        rAdvancedService   [contador]    = "-";
                        rBasicConfiguration  [contador]  = "-";
                        rBasicConstants    [contador]    = "-";
                        rBasicConsumer    [contador]     = "-";
                        rBasicController    [contador]   = "-";
                        rBasicException   [contador]     = "-";
                        rBasicHelper    [contador]       = "-";
                        rBasicRepository   [contador]    = "-";
                        rBasicService    [contador]      = "-";
                        rBasicUtil     [contador]        = "-";
                    }else{
                        
                        try {
                            File archivoHtml = new File(filePath);
                            Document doc = Jsoup.parse(archivoHtml, "UTF-8");
                            Elements elementosSuccess = doc.select("td.success, td.failures");

                            rAdvanced  [contador] = elementosSuccess.get(1).text();
                            rAdvancedConfiguration[contador] = elementosSuccess.get(3).text();
                            rAdvancedController[contador]    = elementosSuccess.get(5).text();
                            rAdvancedRepository[contador]    = elementosSuccess.get(7).text();
                            rAdvancedService   [contador]    = elementosSuccess.get(9).text();
                            rBasicConfiguration  [contador]  = elementosSuccess.get(11).text();
                            rBasicConstants    [contador]    = elementosSuccess.get(13).text();
                            rBasicConsumer    [contador]     = elementosSuccess.get(15).text();
                            rBasicController    [contador]   = elementosSuccess.get(17).text();
                            rBasicException   [contador]     = elementosSuccess.get(19).text();
                            rBasicHelper    [contador]       = elementosSuccess.get(21).text();
                            rBasicRepository   [contador]    = elementosSuccess.get(23).text();
                            rBasicService    [contador]      = elementosSuccess.get(25).text();
                            rBasicUtil     [contador]        = elementosSuccess.get(27).text();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                contador ++;
            }

            resu.setAplicativos(aplicativos);
            resu.setResultadosfinales(resultadosF);
            resu.setTotalapps(contador);
            
            resu.setResultadosadvanced				(rAdvanced);
            resu.setResultadosadvancedConfiguration (rAdvancedConfiguration);
            resu.setResultadosadvancedController    (rAdvancedController);
            resu.setResultadosadvancedRepository    (rAdvancedRepository);
            resu.setResultadosadvancedService       (rAdvancedService);
            resu.setResultadosbasicConfiguration    (rBasicConfiguration);
            resu.setResultadosbasicConstants        (rBasicConstants);
            resu.setResultadosbasicConsumer         (rBasicConsumer);
            resu.setResultadosbasicController       (rBasicController);
            resu.setResultadosbasicException        (rBasicException);
            resu.setResultadosbasicHelper           (rBasicHelper);
            resu.setResultadosbasicRepository       (rBasicRepository);
            resu.setResultadosbasicService          (rBasicService);
            resu.setResultadosbasicUtil             (rBasicUtil);
            
            bufferedReader.close();
            
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
        
        return resu;
        
    }

    private String obtenerNombreapp(String ruta) {

        String[] segmentos = ruta.split("/");
        if (segmentos.length >= 2) {
            return segmentos[segmentos.length - 2];
        } else {
            return ""; 
        }
        
    }

    
    
}

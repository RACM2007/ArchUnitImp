package com.nttdata.TransforBuildGradle.Service;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TransforBuildGradleResultService {

    public boolean MostrarResultados(String ubi) {
        // Ruta del archivo HTML
        String filePath = ubi+"\\build\\reports\\tests\\architectureTest\\index.html";

        // Llamar al método para extraer el valor del elemento
        try {
            String valor = obtenerValorDelElemento(filePath);
            System.out.println("Resultado ArchUnit: " + valor);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String obtenerValorDelElemento(String filePath) throws IOException {
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
            return "No se encontró el Resultado";
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
    
}

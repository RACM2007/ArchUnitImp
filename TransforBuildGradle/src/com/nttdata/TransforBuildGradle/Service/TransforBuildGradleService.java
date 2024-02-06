package com.nttdata.TransforBuildGradle.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransforBuildGradleService {

    public static TransforBuildGradleComandService modelCom = new TransforBuildGradleComandService();
    
    public boolean procesar(String ubiTextRepo) {
       
        File archivo = new File(ubiTextRepo);
        boolean respuesta= true;
        
        if (!archivo.exists() || !archivo.isFile()) {
            System.out.println("El archivo no existe o no es válido.");
            return false;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                
                respuesta= modificaciones(linea);
                
                if (respuesta) {
                    modelCom.ejeComand(linea);
                }else{
                    return false;
                }
                
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }
        
        return true;
        
    }

    
    
    public boolean modificaciones(String ubi) throws IOException {
    
        boolean respuesta = true;
        
        respuesta = modBuildGradle(ubi);
        
        respuesta = modTestGradle(ubi);
        
        respuesta = modSuiteTest(ubi);
        
        return respuesta;
    }
    
    public static void actualizaImports(File archivoBuildGradle) throws IOException {
        String versionSpringBoot = identificarVersionSpringBoot(archivoBuildGradle);
        String limite = "2.7";
        
        if (versionSpringBoot != null && esVersionMenorQue(versionSpringBoot, limite)) {
            actualizarImportMB(archivoBuildGradle);
        } 
        
    }
    
    public static void actualizarImportMB(File archivoBuildGradle) throws IOException {
        String contenido = new String(Files.readAllBytes(archivoBuildGradle.toPath()));
        boolean flgl1;

        if (!contenido.contains("imports {")) {
            Files.write(archivoBuildGradle.toPath(), "\nimports {\n    mavenBom \"org.junit:junit-bom:5.8.2\" \n}\n".getBytes(), StandardOpenOption.APPEND);
        } else {
            // agregar las líneas faltantes
            String lineasAAgregar = "\n mavenBom \"org.junit:junit-bom:5.8.2\"";
            
            // Verificamos si alguna de las líneas ya existe antes de agregarlas
            flgl1 = contenido.contains("mavenBom \"org.junit:junit-bom:5.8.2\"");
            
            
            if (!flgl1){
                contenido = contenido.replace("imports {", "imports {" + lineasAAgregar);
            
                Files.write(archivoBuildGradle.toPath(), contenido.getBytes());
            }
        }
    }
    
    
    
    public static boolean esVersionMenorQue(String version, String limite) {
        // Dividir las versiones en sus componentes
        String[] componentesVersion = version.split("\\.");
        String[] componentesLimite = limite.split("\\.");

        int maxLength = Math.max(componentesVersion.length, componentesLimite.length);

        for (int i = 0; i < maxLength; i++) {
            int componenteVersion = (i < componentesVersion.length) ? Integer.parseInt(componentesVersion[i]) : 0;
            int componenteLimite = (i < componentesLimite.length) ? Integer.parseInt(componentesLimite[i]) : 0;

            if (componenteVersion < componenteLimite) {
                return true;
            } else if (componenteVersion > componenteLimite) {
                return false;
            }
        }

        return false;
    }
    
    public static String identificarVersionSpringBoot(File archivoBuildGradle) throws IOException {
        Pattern pluginPattern = Pattern.compile(".*id\\s*'org.springframework.boot'\\s*version\\s*'(.+)'");

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoBuildGradle))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pluginPattern.matcher(line);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
        }
    
        return null;
    }
    
    public static void actualizarDependencias(File archivoBuildGradle) throws IOException {
        String contenido = new String(Files.readAllBytes(archivoBuildGradle.toPath()));
        boolean flgl1;

        if (!contenido.contains("dependencies {")) {
            Files.write(archivoBuildGradle.toPath(), "\ndependencies {\n    architectureTestImplementation 'cl.bci:lib-architecture-test:1.0.0'\n}\n".getBytes(), StandardOpenOption.APPEND);
        } else {
            String lineasAAgregar = "\n architectureTestImplementation 'cl.bci:lib-architecture-test:1.0.0'";
            
            flgl1 = contenido.contains("architectureTestImplementation 'cl.bci:lib-architecture-test:1.0.0'");
            
            if (!flgl1){
                contenido = contenido.replace("dependencies {", "dependencies {" + lineasAAgregar);
            
                Files.write(archivoBuildGradle.toPath(), contenido.getBytes());
            }
        }
    }
    
     public static void actualizarConfiguraciones(File archivoBuildGradle) throws IOException {
        String contenido = new String(Files.readAllBytes(archivoBuildGradle.toPath()));
        String configuraciones = "\nconfigurations {\n    architectureTestImplementation.extendsFrom testCompile\n    architectureTestRuntime.extendsFrom testRuntime\n}\n";

        int indexDependencies = contenido.indexOf("dependencies {");
        if (indexDependencies != -1) {
            // Insertar las configuraciones justo antes de la línea de dependencies
            contenido = contenido.substring(0, indexDependencies) + configuraciones + contenido.substring(indexDependencies);
        } else {
            // Si no se encuentra 'dependencies {', agregar las configuraciones al final del archivo
            contenido += configuraciones;
        }

        Files.write(archivoBuildGradle.toPath(), contenido.getBytes());
    }
    
    
    public static void eliminarComentarios(File archivoBuildGradle) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoBuildGradle))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String lineaSinComentarios = eliminarComentariosDeLinea(linea);
                contenido.append(lineaSinComentarios).append("\n");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoBuildGradle))) {
            bw.write(contenido.toString());
        }
    }
    
    private static String eliminarComentariosDeLinea(String linea) {
        Pattern patronComentario = Pattern.compile("/\\*.*?\\*/|//.*");
        Matcher matcher = patronComentario.matcher(linea);
        return matcher.replaceAll("");
    }
    
    public static File validarBuildGradle(String rutaCarpeta, String nombreArch) {
        if (rutaCarpeta == null || rutaCarpeta.isEmpty()) {
            System.out.println("La ruta de la carpeta no puede ser nula o vacía.");
            return null;
        }

        String rutaBuildGradle = rutaCarpeta + File.separator + nombreArch;

        File archivoBuildGradle = new File(rutaBuildGradle);

        return archivoBuildGradle;
        
    }

    private boolean modBuildGradle(String ubi) throws IOException {
        /*
        MODIFICACIONES BUILD.GRADLE
        */
        
        //Encontrar archivo build.gradle
        File arBuildGradle = validarBuildGradle(ubi, "build.gradle");
        
        if (arBuildGradle.exists()) {
            System.out.println("El archivo build.gradle existe en la ubicación proporcionada.");
        } else {
            System.out.println("El archivo build.gradle no existe en la ubicación proporcionada.");
            return false;
        }
        
        //Eliminar comentarios
        eliminarComentarios(arBuildGradle);
        System.out.println("Se eliminaron los comentarios. build.gradle");
        
               
        //Desferagmentar el build
               
                
        //agregar los cambios
        actualizarConfiguraciones(arBuildGradle);
        actualizarDependencias(arBuildGradle);
        
        
        //Imports
        actualizaImports(arBuildGradle);
        
        return true;
    }

    private boolean modTestGradle(String ubi) throws IOException {
        /*
        MODIFICACIONES test.GRADLE
        */
        
        //Encontrar archivo build.gradle
        File arBuildGradle = validarBuildGradle(ubi, "test.gradle");
        
        if (arBuildGradle.exists()) {
            System.out.println("El archivo build.gradle existe en la ubicación proporcionada.");
        } else {
            System.out.println("El archivo build.gradle no existe en la ubicación proporcionada.");
            return false;
        }
        
        //Eliminar comentarios
        eliminarComentarios(arBuildGradle);
        System.out.println("Se eliminaron los comentarios. test.gradle");
        
        taskTestGradle(ubi+"/test.gradle", ubi);
        
        agregarConfiguracionarchitectureTest(arBuildGradle);
        
        return true;
    }
    
    public static void agregarConfiguracionarchitectureTest (File archivoGradle) {
        try {
            // Leer el contenido del archivo Gradle
            List<String> lineas = leerLineas(archivoGradle);

            // Buscar la sección "sourceSets"
            int indiceSourceSets = buscarIndiceSourceSets(lineas);

            if (indiceSourceSets != -1) {
                // Verificar si ya existe la configuración
                boolean existeConfiguracion = verificarExistenciaConfiguracion(lineas, indiceSourceSets);

                if (!existeConfiguracion) {
                    // Agregar la nueva configuración si no existe
                    agregarNuevaConfiguracion(lineas, indiceSourceSets);

                    // Guardar el archivo modificado
                    guardarArchivoModificado(archivoGradle, lineas);
                    System.out.println("Configuración agregada con éxito.");
                } else {
                    System.out.println("La configuración ya existe, no se ha agregado nada.");
                }
            } else {
                System.out.println("No se encontró la sección \"sourceSets\" en el archivo Gradle.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> leerLineas(File archivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }

    private static int buscarIndiceSourceSets(List<String> lineas) {
        for (int i = 0; i < lineas.size(); i++) {
            if (lineas.get(i).trim().contains("sourceSets")) {
                return i;
            }
        }
        return -1;
    }

    private static boolean verificarExistenciaConfiguracion(List<String> lineas, int indiceSourceSets) {
        for (int i = indiceSourceSets + 1; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.equals("}")) {
                break;  // Fin de la sección sourceSets
            } else if (linea.equals("architectureTest {")) {
                return true;  // Ya existe la configuración
            }
        }
        return false;  // La configuración no existe
    }

    private static void agregarNuevaConfiguracion(List<String> lineas, int indiceSourceSets) {
        lineas.add(indiceSourceSets + 1, "    architectureTest {");
        lineas.add(indiceSourceSets + 2, "        java {");
        lineas.add(indiceSourceSets + 3, "            compileClasspath += main.output");
        lineas.add(indiceSourceSets + 4, "            runtimeClasspath += main.output");
        lineas.add(indiceSourceSets + 5, "            srcDir file('src/architecture-test/java')");
        lineas.add(indiceSourceSets + 6, "        }");
        lineas.add(indiceSourceSets + 7, "        resources.srcDir file('src/architecture-test/resources')");
        lineas.add(indiceSourceSets + 8, "    }");
    }

    private static void guardarArchivoModificado(File archivo, List<String> lineas) throws IOException {
        try (FileWriter writer = new FileWriter(archivo)) {
            for (String linea : lineas) {
                writer.write(linea + System.lineSeparator());
            }
        }
    }
    
    
    
    public static void taskTestGradle(String rutaArchivoGradle, String ubi2) {
        File archivoGradle = new File(rutaArchivoGradle);

        // Verificar si el archivo existe
        if (!archivoGradle.exists()) {
            System.out.println("El archivo .gradle no existe.");
            return;
        }

        try {
            // Leer el contenido actual del archivo
            BufferedReader reader = new BufferedReader(new FileReader(archivoGradle));
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            String paquete = obtenerRutaClaseApplication(ubi2);
            
            // Verificar si el código ya existe
            String codigoAgregar = "tasks.register('architectureTest', Test) {\n" +
                    "    useJUnitPlatform()\n" +
                    "    environment 'verification_package', '"+paquete+"'\n" +
                    "    description = 'Runs architecture tests.'\n" +
                    "    group = 'verification'\n" +
                    "\n" +
                    "    testClassesDirs = sourceSets.architectureTest.output.classesDirs\n" +
                    "    classpath = sourceSets.architectureTest.runtimeClasspath\n" +
                    "}\n";

            if (contenido.toString().contains(codigoAgregar)) {
                System.out.println("El código ya existe en el archivo.");
                return;
            }

            // Agregar el código al final del archivo
            try (PrintWriter writer = new PrintWriter(new FileWriter(archivoGradle, true))) {
                writer.println(codigoAgregar);
                System.out.println("Código agregado con éxito.");
            } catch (IOException e) {
                System.err.println("Error al escribir en el archivo: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    
    public static String obtenerRutaClaseApplication(String rutaBase) {
        File directorioBase = new File(rutaBase+"/src/main/java");

        if (!directorioBase.exists() || !directorioBase.isDirectory()) {
            System.out.println("La ruta base no es válida.");
            return null;
        }

        return buscarClaseApplication(directorioBase);
    }

    private static String buscarClaseApplication(File directorio) {
        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    String resultado = buscarClaseApplication(archivo);
                    if (resultado != null) {
                        return resultado;
                    }
                } else {
                    String nombreArchivo = archivo.getName();
                    if (nombreArchivo.endsWith("Application.java")) {
                        String ruta = archivo.getAbsolutePath();
                        String paquete = obtenerPaqueteDesdeRuta(ruta);
                        return paquete.replace(File.separator, ".");
                    }
                }
            }
        }

        return null;
    }

    private static String obtenerPaqueteDesdeRuta(String rutaArchivo) {
         File archivo = new File(rutaArchivo);

        if (!archivo.exists() || !archivo.isFile()) {
            System.out.println("La ruta del archivo no es válida.");
            return null;
        }

        String rutaBase = "src\\main\\java";
        int indice = rutaArchivo.indexOf(rutaBase);

        if (indice == -1) {
            System.out.println("La estructura de la ruta no es válida.");
            return null;
        }

        String rutaRelativa = rutaArchivo.substring(indice + rutaBase.length() + 1, rutaArchivo.lastIndexOf(File.separator));
        String paquete = rutaRelativa.replace(File.separator, ".");

        return paquete;
    }

    private boolean modSuiteTest(String ubi) {
        /*
        MODIFICACIONES BUILD.GRADLE
        */
        crearClaseSuiteTest(ubi+"/src");
        
        
        return true;
    }
    
    public static void crearClaseSuiteTest(String ubicacion) {
        String rutaJava = ubicacion + "/java/cl/bci/architecture";
        String rutaClase = rutaJava + "/ArchitectureSuiteTest.java";

        // Verificar si la clase ya existe
        if (new File(rutaClase).exists()) {
            System.out.println("La clase ArchitectureSuiteTest ya existe.");
            return;
        }

        // Crear las carpetas si no existen
        File carpetaJava = new File(rutaJava);
        carpetaJava.mkdirs();

        // Crear la clase
        try (FileWriter writer = new FileWriter(rutaClase)) {
            String contenidoClase = "package cl.bci.architecture;\n\n"
                    + "import org.junit.platform.suite.api.SelectPackages;\n"
                    + "import org.junit.platform.suite.api.Suite;\n\n"
                    + "@SelectPackages({\"cl.bci.architecture.rules\"})\n"
                    + "@Suite\n"
                    + "class ArchitectureSuiteTest {\n"
                    + "}\n";

            writer.write(contenidoClase);
            System.out.println("Estructura y clase creadas con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir la clase: " + e.getMessage());
        }
    }

}

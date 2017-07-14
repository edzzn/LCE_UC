package SGBD_LCE_UC.capa.logica;

import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by edzzn on 7/12/17.
 */
public class Script {
    // Este script permite leer un archivo csv, y recibe parametros de longitud del archivo para crear una nueva tabla
    
    public Script(){}
    
    public static void operar(String[] argv){
        /*
        *
        * argv[0] dirección del archivo
        * argv[1] campo_clave
        * argv[2] longitudes "1,2,3,4,5"
        *
        * */
//        String filedir = "filesBD\\nombreTabla.bd";
        String filedir = argv[0];
//        System.out.println(filedir.substring(filedir.lastIndexOf("\\")+1, filedir.lastIndexOf(".")));

        if(!new File(filedir).exists())
            throw new Error("Archivo "+ filedir + " no encontrado.");

        try {
            CsvReader archivoCSV = new CsvReader(filedir);
            

            boolean primeraPasada = true;
            while(archivoCSV.readRecord()) {
                
                int columnas = archivoCSV.getColumnCount();
                //System.out.println("Columnxas: "+ archivoCSV.getColumnCount());
                String nombre_tabla = filedir.substring(filedir.lastIndexOf("\\")+1, filedir.lastIndexOf("."));

                if(primeraPasada == true) {
                    // primera fila, leemos la cabecera y creamos la tabla

                /*
                * Como se parametriza el comando
                * [0] = String nombre_tabla1
                * [1] = ArrayList<String> campos
                * [2] = String campo_clave
                * [3] = ArrayList<Integer> Longitudes
                * [4] = ArrayList<String> campos encriptados
                *
                * */

                    // no se puede determinar la encriptación, asi que es null
                    String campo_clave = argv[1];
                    ArrayList<String> camposEncriptados = new ArrayList<String>();
                    //camposEncriptados.add("campN");     // Seleccionar el nombre de campo a encriptar...
                    
                    ArrayList<String> campos = new ArrayList<String>();
                    ArrayList<String> longitudes = new ArrayList<String>(Arrays.asList(argv[2].split(",")));


                    // obtenemos los campos
                    for (int i=0; i < columnas; i++){
                        try {
                            campos.add(archivoCSV.get(i));
                            //System.out.println("texto: "+ archivoCSV.get(i));
                        }catch (Exception e){
                            System.out.println("Error al leer la cabecera");
                            break;
                        }
                    }

                    //Gestion_Tablas nuevaTabla = new GestionTablas(nombre_tabla, campos, campo_clave,longitudes, campos_encriptados);
                    GestionTablas nuevaTabla = new GestionTablas(nombre_tabla, campos, campo_clave, longitudes, camposEncriptados);

                    nuevaTabla.crearTabla();
                    primeraPasada = false;
                } // Fin primera pasada
                else {
                    // desde la segunda pasada en adelante se agregaran los nuevos registros
                    ArrayList<String> vCampos = new ArrayList<String>();

                    for (int i=0; i < columnas; i++){
                        try {
                            vCampos.add(archivoCSV.get(i));
                        }catch (Exception e){
                            System.out.println("Error al leer la cabecera");
                            break;
                        }
                    }
                    String registros = vCampos.toString();
                    //System.out.println(registros);
                    registros = registros.substring(1, registros.length()-1);
                    //System.out.println(registros);

                    String codigo = "INSERTAR EN " + nombre_tabla + " VALORES"+registros;
                    GestorDBAdapter gestor = GestorDBAdapter.getGestor();
                    gestor.operar(codigo);
            
                }
            }
            archivoCSV.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error, Error al intentar abrir el archivo");
        } catch (IOException ex) {
            System.out.println("Error, IOException");
        }
        System.out.println("Tabla creada y registros agregados.");
    }
}

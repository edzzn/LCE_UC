package lib;

import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Created by edzzn on 7/12/17.
 */
public class Script {
    // Este script permite leer un archivo csv, y recibe parametros de longitud del archivo para crear una nueva tabla
    public void main(String[] argv){
        /*
        *
        * argv[0] dirección del archivo
        * argv[1] campo_clave
        * argv[2] longitudes "1,2,3,4,5"
        *
        * */
        String filedir = "Filedir.csv";

        if(!new File(filedir).exists())
            throw new Error("Archivo "+ filedir + " no encontrado.");

        try {
            CsvReader archivoCSV = new CsvReader(filedir);
            int columnas = archivoCSV.getColumnCount();

            boolean primeraPasada = true;
            while(archivoCSV.readRecord()) {
                String nombre_tabla = filedir.replace("csv", "");

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
                    ArrayList<String> campos = new ArrayList<String>();
                    ArrayList<String> longitudes = new ArrayList<String>(Arrays.asList(argv[2].split(",")));


                    // obtenemos los campos
                    for (int i=0; i < columnas; i++){
                        try {
                            campos.add(archivoCSV.get(i));
                        }catch (Exception e){
                            System.out.println("Error al leer la cabecera");
                            break;
                        }
                    }

                    //Gestion_Tablas nuevaTabla = new Gestion_Tablas(nombre_tabla, campos, campo_clave,longitudes, campos_encriptados);
                    Gestion_Tablas nuevaTabla = new Gestion_Tablas(nombre_tabla, campos, campo_clave, longitudes, camposEncriptados);

                    nuevaTabla.crearTabla();
                } // Fin primera pasada

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
                Gestion_Registros registroNuevo = new Gestion_Registros(nombre_tabla, vCampos);
                registroNuevo.crearRegistros();
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

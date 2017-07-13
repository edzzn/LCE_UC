
package lib;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author JHON
 */
public class Gestion_Tablas {
    
    String nombreTabla, campoClave;
    ArrayList<String> campos;
    ArrayList<String> longitudCampos;
    ArrayList<String> camposEncriptados = null;
    ArrayList<String> camposEncriptadosTF = new ArrayList<String>();
    
    String fileMETA = "ArchivosBD\\META.bd";
    
    public Gestion_Tablas(String nombreTabla, ArrayList<String> campos, String campoClave, ArrayList<String> longitudCampos, ArrayList<String> camposEncriptados){
        this.nombreTabla = nombreTabla;
        this.campos = campos;
        this.campoClave = campoClave;
        this.longitudCampos = longitudCampos;
        this.camposEncriptados = camposEncriptados;
    }
    
    public Gestion_Tablas(String nombreTabla, ArrayList<String> campos, String campoClave, ArrayList<String> longitudCampos){
        this.nombreTabla = nombreTabla;
        this.campos = campos;
        this.campoClave = campoClave;
        this.longitudCampos = longitudCampos;
    }
    
    public void crearTabla(){
        //  Funcion
        System.out.println("INFORMACION - TABLA CREADA");
        System.out.println("Nombre tabla: " + this.nombreTabla);
        System.out.println("Campos: " + this.campos);
        System.out.println("Campo clave: " + this.campoClave);
        System.out.println("Longitud Campos: " + this.longitudCampos);
        System.out.println("Campos Encriptados: " + this.camposEncriptados);
        
        //  Cambiar Campos Encriptados a T y F
        if(this.camposEncriptados != null){
            for (int i = 0; i < this.campos.size(); i++) {
                if (this.camposEncriptados.contains(this.campos.get(i))) {
                    camposEncriptadosTF.add("T");} else {camposEncriptadosTF.add("F");
                }
            }
        }
        System.out.println("Campos EncriptadosTF: " + this.camposEncriptadosTF);
        
        //  Funcion
        new File("ArchivosBD\\").mkdirs();//Crea el directorio en caso de que no exista
        try{
            PrintWriter flujoSalida = new PrintWriter("ArchivosBD" + "\\" + nombreTabla + ".bd");
            flujoSalida.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        //  Funcion - Actualizar META
        try {
            File file = new File(this.fileMETA);
            if(file.exists()){
                Scanner scan =  new Scanner(new File(this.fileMETA)).useDelimiter("\\A");
                String contenido = scan.next();
                scan.close();
                file.delete();
                PrintWriter wr = new PrintWriter(this.fileMETA);
                wr.print(contenido + "\n");
                if (this.camposEncriptados != null){
                    wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave
                            + "," + this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.camposEncriptadosTF.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    wr.close();
                } else {
                    wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave
                            + "," + this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    wr.close();
                }
            }else{
                PrintWriter wr = new PrintWriter(this.fileMETA);
                if (this.camposEncriptados != null){
                    wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave
                            + "," + this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.camposEncriptadosTF.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    wr.close();
                } else {
                    wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave
                            + "," + this.longitudCampos.toString().replace("[", "").replace("]", "").replace(" ", "")
                            + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                    wr.close();
                }
            }
        } catch (FileNotFoundException ex) {System.out.println(ex);}
    }
    
    private String nombreCampo;
    private String nuevoCampo;
    private int posCampo = -1;
    private int numCamposLeidos = 0;    // FunModificar
    private ArrayList<String> valCampos = new ArrayList<>();
    
    //  Constructor
    public Gestion_Tablas(String nombreTabla, String nombreCampo, String nuevoCampo){
        this.nombreTabla = nombreTabla;
        this.nombreCampo = nombreCampo;
        this.nuevoCampo = nuevoCampo;
    }
    
    public void modificarTabla(){
        this.numCamposLeidos = 0;
        try{
            //Nombre de Archivo a modificar
            File mod = new File("ArchivosBD\\" + this.nombreTabla + ".bd");
            if(mod.exists())
            {
                //Renombrar el archivo
                mod.renameTo(new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd"));
                CsvReader flujoEntrada = new CsvReader("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd");
                
                //Busqueda de la posicion del campo
                this.posCampo = -1;
                flujoEntrada.readRecord();
                
                //Contador de Campos en la Tabla
                this.numCamposLeidos = flujoEntrada.getColumnCount();
                
                for (int i = 0; i< flujoEntrada.getColumnCount();i++){
                    if(flujoEntrada.get(i).equals(this.nombreCampo)){
                        this.posCampo = i;
                        this.valCampos.add(this.nuevoCampo);
                    } else
                        this.valCampos.add(flujoEntrada.get(i));
                }
                
                if(this.posCampo == -1)throw new Error("Lo sentimos, no se ha encontrado el campo a modificar");
                
                //Cambiar la cabecera del archivo
                PrintWriter flujoSalida = new PrintWriter("ArchivosBD\\" + this.nombreTabla + ".bd");//Crear el archivo luego de superar la condicion
                flujoSalida.println(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ",""));

                System.out.println("Cambia Cabecera de Archivo: "+ this.valCampos.toString().replace("[", "").replace("]", "").replace(" ",""));
                
                //El siguiente proceso es por si el archivo contiene registros, es decir una tabla con registros
                //Lectura y escritura
                while(flujoEntrada.readRecord()){
                    flujoSalida.println(flujoEntrada.getRawRecord());
                }
                    
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd").delete();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        //  Funcion - Actualizar META
        try{
            //  Conidicion si Existe el Archivo META
            if(new File(this.fileMETA).exists())
            {
                File file = new File(this.fileMETA);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "AuxiliarMETAFILE.bd"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "AuxiliarMETAFILE.bd");
                
                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);
                
                while(flujoEntrada.readRecord()){
                    if(!this.nombreTabla.equals(flujoEntrada.get(0))){
                        flujoSalida.println(flujoEntrada.getRawRecord());
                        System.out.println("Flujo Salida NO equals: "+ flujoEntrada.getRawRecord());
                        
                    }else{
                        for(int i = 0; i < (this.numCamposLeidos*2)-1; i++){     //Son Num de Campos *2 desde la Derecha
                            //  Condicion para Modificacion de Campo Calve en META
                            if (this.nombreCampo.equals(flujoEntrada.get(i))) {
                                flujoSalida.print(this.nuevoCampo+",");
                                System.out.println("---Flujo de Entrada (Campo Clave): "+(flujoEntrada.get(i) +","));
                                i++;
                            }
                            flujoSalida.print(flujoEntrada.get(i) +",");
                            System.out.println("---Flujo de Entrada: "+(flujoEntrada.get(i) +","));
                            
                        }
                        //  Escribe los Campos Modificados
                        flujoSalida.print(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        System.out.println("Flujo Salida equals:" + this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));

                        for(int i = (3+this.numCamposLeidos*2); i < flujoEntrada.getColumnCount(); i++){
                            flujoSalida.print(","+flujoEntrada.get(i));
                            System.out.println("---Flujo de Entrada T y F: "+(","+flujoEntrada.get(i)));
                        }flujoSalida.println("");
                        
                    }
                }
                    
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "AuxiliarMETAFILE.bd").delete();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error:"+ex);
        } catch (IOException ex) {
            System.out.println("Error:"+ex);
        }
    }
    
    //  Constructor Eliminar
    public Gestion_Tablas(String nombreTabla){
        this.nombreTabla = nombreTabla;
    }
    
    public void eliminarTabla(){
        
        // Eliminar Archivo
        File file = new File(this.fileMETA);
        File eliminar = new File(file.getParent() + "\\" + this.nombreTabla + ".bd");
        if(eliminar.exists()){
            eliminar.delete();
        }
        
        //  Funcion - Actualizar META
        try{
            if(new File(this.fileMETA).exists())
            {
                file = new File(this.fileMETA);
                
                //Renombrar el archivo
                CsvReader FX = new CsvReader(file.getParent() + "\\" + "META.bd");
                
                file.setWritable(true);
                file.renameTo(new File(file.getParent() + "\\" + "AuxiliarMETAFILE.bd"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "AuxiliarMETAFILE.bd");
                
                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);//Crear el archivo luego de superar la condicion
                while(flujoEntrada.readRecord()){
                    if(!this.nombreTabla.equals(flujoEntrada.get(0)))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                }
                    
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "AuxiliarMETAFILE.bd").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        }
        
        file = new File(this.fileMETA);
        eliminar = new File(file.getParent() + "\\" + this.nombreTabla + ".bd");
        //Si el archivo esta vacio, eliminar dicho archivo
        if(eliminar.length() == 0)
            eliminar.delete();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SGBD_LCE_UC.capa.logica;

/**
 *
 * @author mariabelen
 */
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionRegistros {

    String nombreTabla, campoClave;
    ArrayList<String> campos;
    ArrayList<String> valoresCampos;
    String fileMETA = "ArchivosBD\\META.bd";

    public GestionRegistros(String nombreTabla, ArrayList<String> valoresCampos) {
        this.nombreTabla = nombreTabla;
        this.valoresCampos = valoresCampos;
    }

    public void crearRegistros() {
        System.out.println("INFORMACION -  REGISTROS CREADOS ");
        System.out.println("Nombre tabla: " + this.nombreTabla);
        System.out.println("Valores Campos: " + this.valoresCampos);
        
        try {
            File file = new File("ArchivosBD\\" + this.nombreTabla + ".bd");
            if (file.exists()) {
                Scanner db = new Scanner(new File("ArchivosBD\\" + this.nombreTabla + ".bd")).useDelimiter("\\A");
                String contenido = db.next();
                db.close();
                file.delete();
                
                // Aqui se debe encriptar los campos
                ArrayList<String> vCampos = this.valoresCampos;
                ArrayList<String> encriptados = new ArrayList<String>();
                //leemos el meta para obtener los campos encriptados
                
                PrintWriter wr = new PrintWriter("ArchivosBD\\" + this.nombreTabla + ".bd");
                int numCampos = 0;
                try {
                    CsvReader ar = new CsvReader(this.fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(this.nombreTabla)){
                            numCampos = (ar.getColumnCount()-3)/3;
                            int inicioEncriptados = 3 + 2* numCampos;
                            for(int k =inicioEncriptados; k < ar.getColumnCount(); k++){
                                encriptados.add(ar.get(k));
                            }
                        }
                    }
                    ar.close();
                    
                    for(int l=0; l < numCampos; l++){
                        if(encriptados.get(l).equals("T")){
                           //String claveSecreta = "qweqweq";
                           String encriptado = AES.encrypt(vCampos.get(l),claveSecreta);
                           // Encriptar aqui
                           // is the passfrase = "And is there honey still for tea?"
              
//                           
//                            StringBuilder s = new StringBuilder();
//                            for(byte b : ciphertext) {
//                                s.append(Integer.toBinaryString(0x100 | 0xFF & b).substring(1, 9));
//                            }
//                            encriptado = s.toString();
                            
                           vCampos.set(l, encriptado);
                        }                        
                    }
                }catch (FileNotFoundException ex) {
                    System.out.println("ERROR: "+ex);
                }catch (IOException ex) {
                    System.out.println("ERROR: "+ex);
                } 
                
                String nuevoRegistro = contenido + "\n" + vCampos.toString().replace("[", "").replace("]", "").replace(" ", "");
                wr.println(nuevoRegistro);
                wr.close();
            } else {

            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        //  Actualiza Archivo Meta
        try {
            if (new File(this.fileMETA).exists()) {
                File file = new File(this.fileMETA);
                //  Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "AuxiliarFILE.bd"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "AuxiliarFILE.bd");

                //  Leer y Escribirr
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);
                while (flujoEntrada.readRecord()) {
                    if (!this.nombreTabla.equals(flujoEntrada.get(0))) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    } else {
                        flujoSalida.print(flujoEntrada.get(0) + ",");
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1)) + 1) + ",");
                        for (int i = 2; i < flujoEntrada.getColumnCount(); i++) {
                            flujoSalida.print(flujoEntrada.get(i));
                            if (i + 1 != flujoEntrada.getColumnCount()) {
                                flujoSalida.print(",");
                            }
                        }
                        flujoSalida.println();
                    }
                }

                flujoEntrada.close();
                flujoSalida.close();
                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "AuxiliarFILE.bd").delete();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
           System.out.println(ex);
        }
    }
    
    // Modificar Registros
    String nombreCampo;
    String valorCampo;
    private int posicionCampoClave;

    //Constructor
    public GestionRegistros(String nombreTabla, String campoClave, String nombreCampo, String valorCampo, int posicionCampoClave) {
        this.nombreTabla = nombreTabla;
        this.campoClave = campoClave;
        this.nombreCampo = nombreCampo;
        this.valorCampo = valorCampo;
        this.posicionCampoClave = posicionCampoClave;
    }

    public void modificarRegistros() {
        try{
            File mod = new File("ArchivosBD\\" + this.nombreTabla + ".bd");
            if(mod.exists())
            {
                //Renombrar el archivo
                mod.renameTo(new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd"));
                CsvReader flujoEntrada = new CsvReader("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd");
                PrintWriter flujoSalida = new PrintWriter("ArchivosBD\\" + this.nombreTabla + ".BD");
                
                //Leer y Escribirr
                flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());
                int posicionCampoCambio = -1;
                //System.out.println("Campos de Tabla: " + flujoEntrada.getRawRecord());
                for(int i = 0; i<flujoEntrada.getColumnCount();i++){
                    if(flujoEntrada.get(i).equals(this.nombreCampo)){
                        posicionCampoCambio = i;
                        break;
                    }
                }
                
                if(posicionCampoCambio==-1)
                    System.out.println("Error: Datos que se estan ingresando (Poscicion del Campo)");

                while(flujoEntrada.readRecord()){
                    
                    System.out.println("flujoEntrada.getRawRecord():  "+ flujoEntrada.getRawRecord());
                    System.out.println("DIF_flujoEntrada.get(this.posicionCampoClave):  "+ flujoEntrada.get(this.posicionCampoClave));
                    System.out.println("DIF_this.campoClave:  "+ this.campoClave);
                    System.out.println("");
                    
                    if(!flujoEntrada.get(this.posicionCampoClave).equals(this.campoClave)){
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    }else{
                        for(int i = 0; i<flujoEntrada.getColumnCount(); i++){
                            if(i!=posicionCampoCambio){
                                flujoSalida.print(flujoEntrada.get(i));
                                System.out.println("flujoEntrada.get(i):  "+ flujoEntrada.get(i));
                                
                            }else{
                                flujoSalida.print(this.valorCampo);
                                System.out.println("this.valorCampo" + this.valorCampo);
                            }
                            if(i+1 == flujoEntrada.getColumnCount()){
                                flujoSalida.print("\n");
                            }else{
                                flujoSalida.print(",");
                            }
                        }flujoSalida.println("");
                    }
                }
                flujoEntrada.close();
                flujoSalida.close();
                
                new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd").delete();
                flujoEntrada.close();
                flujoSalida.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: "+ex);   //Archivos
        } catch (IOException ex) {
            System.out.println("ERROR: "+ex);
        }
    }
    
    // Elimnar Registro
    String valorCampoClave;

    public GestionRegistros(String nombreTabla, String valorCampoClave, int posicionCampoClave) {
        this.nombreTabla = nombreTabla;
        this.valorCampoClave = valorCampoClave;
        this.posicionCampoClave = posicionCampoClave;
    }

    public void eliminarRegistros() {
        try {

            File mod = new File("ArchivosBD\\" + this.nombreTabla + ".bd");
            if (mod.exists()) {
                
                mod.renameTo(new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd"));
                CsvReader flujoEntrada = new CsvReader("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd");
                PrintWriter flujoSalida = new PrintWriter("ArchivosBD\\" + this.nombreTabla + ".bd");

                //  Leer y Escribir Archivo
                flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());
                while (flujoEntrada.readRecord()) {
                    if (!flujoEntrada.get(this.posicionCampoClave).equals(this.valorCampoClave)) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    }
                }

                flujoEntrada.close();
                flujoSalida.close();

                new File("ArchivosBD\\" + this.nombreTabla + "AuxiliarFILE.bd").delete();
                flujoEntrada.close();
                flujoSalida.close();
            }
        } catch (FileNotFoundException ex) {
           System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //  Actualizar Archivo Meta
        try 
            if (new File(this.fileMETA).exists()) {
                File file = new File(this.fileMETA);
                
                file.renameTo(new File(file.getParent() + "\\" + "AuxiliarFILE.bd"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "AuxiliarFILE.bd");

                //  Leer y Escribir Archivo
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);
                while (flujoEntrada.readRecord()) {
                    if (!this.nombreTabla.equals(flujoEntrada.get(0))) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    } else {
                        flujoSalida.print(flujoEntrada.get(0) + ",");
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1)) - 1) + ",");
                        for (int i = 2; i < flujoEntrada.getColumnCount(); i++) {
                            flujoSalida.print(flujoEntrada.get(i));
                            if (i + 1 != flujoEntrada.getColumnCount()) {
                                flujoSalida.print(",");
                            }
                        }
                        flujoSalida.println();
                    }
                }

                flujoSalida.close();
                flujoEntrada.close();

                new File(file.getParent() + "\\" + "AuxiliarFILE.bd").delete();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
          System.out.println(ex);
        }
    }
}

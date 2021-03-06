package SGBD_LCE_UC.capa.logica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;


/**
 * Created by edzzn on 7/8/17.
 */
public class GestorDBAdapter {
//    Encargado de la parserización de la data y la validación.


    private GestorDB gestor = GestorDB.getGestor();
    private String comando;
    private Object[] comandoParsed;
    private static GestorDBAdapter instance = null;
    private int operacion;


//    Implementación del patron Singleton
    protected GestorDBAdapter() {
        // Evita una nueva instanciación
    }

    public static GestorDBAdapter getGestor() {
        if(instance == null) {
            instance = new GestorDBAdapter();
        }
        return instance;
    }

    private boolean BuscarTabla(String nombreTabla){
        if(!new File("ArchivosBD\\META.bd").exists())
            return false;
        String fileMETA = "ArchivosBD\\META.bd";
        try {
            CsvReader ar = new CsvReader(fileMETA);
            while(ar.readRecord()){
                if(ar.get(0).equals(nombreTabla)){
                    ar.close();
                    return true;
                }
            }
            ar.close();
        }
        catch (FileNotFoundException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        }
        return false;
    }

    public void operar(String comando){
        this.comando = comando;
        Parser parser = new Parser(comando);
        operacion = parser.getOperacion();
        comandoParsed = parser.getComando();
        System.out.println("comando: " + comando);
        System.out.println("Operacion: " + operacion);
        System.out.println("comandoParsed: " + comandoParsed);

        // validamos que se pudo parserizar el comando
        if(comandoParsed == null){
            throw new SecurityException("lib.Comando ingresado no es parte de la sintaxis.");
        }

        switch (operacion){
            case 1: // Crear Tabla

                /*
                * [0] = String nombre_tabla1
                * [1] = ArrayList<String> campos
                * [2] = String campo_clave
                * [3] = ArrayList<Integer> Longitudes
                * [4] = ArrayList<String> campos encriptados
                *
                * */

                // Validamos la existencia de la tabla
                String nombreTabla = (String) comandoParsed[0];
                if(this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla que se desea crear ya existe");
                // Validamos la longitud
                ArrayList<Integer> longitudes = (ArrayList<Integer>) (List) comandoParsed[3];

                for(int i=0; i<longitudes.size(); i++) {
                    int longitud = longitudes.get(i);
                    if(longitud <= 0){
                        System.out.println("Longitud " + longitudes.get(i));
                        throw new SecurityException("Error, la longitud de los campos debe ser mayor a cero");
                    }
                }
                System.out.println(longitudes);

                // Validamos que el campo clave se encuentre dentro de los campos
                ArrayList<String> campos = (ArrayList<String>) (List) comandoParsed[1];
                String campo_clave = (String) comandoParsed[2];
                if(!campos.contains(campo_clave)){
                    throw new SecurityException("Error, El campo claveno se encuentra en los campos");

                }

                // A este punto debe ser valida la entrada
                gestor.operar(comandoParsed, operacion);
                break;

            case 2: // Modificar Tabla
                /*
                * [0] = String nombre_tabla
                * [1] = String nombre_campo
                * [2] = String nuevo_nombre_campo
                *
                * */
                // Validamos que la tabla existe
                nombreTabla = (String) comandoParsed[0];
                if(!this.BuscarTabla(nombreTabla)){
                    throw new SecurityException("Error, la tabla especificada no existe");
                }

                // Validamos la existencia del campo
                String nombreCampo = (String) comandoParsed[1];
                ArrayList<String> camposMeta = new ArrayList<>();
                String fileMETA = "ArchivosBD\\META.bd";
                boolean esClave = false;
                try {
                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla)){
                            for (int j = 4; j < ar.getColumnCount();j++){
                                camposMeta.add(ar.get(j));
                            }
                            System.out.println("ar.get(2): "+ar.get(2));
                            System.out.println("nombreCampo: "+nombreCampo);
                            if(ar.get(2).equals(nombreCampo))
                                esClave = true;
                        }

                    }
                    ar.close();
                }
                catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }

                if(!camposMeta.contains(nombreCampo))
                    throw new SecurityException("Error, el nombre del campo especificado no existe");
//
//                if(esClave==true)
//                    throw new SecurityException("Error, no se puede modificar el campo clave");


                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;
            case 3: // Eliminar Tabla
                /*
                * [0] = nombre_tabla a eliminar
                * */
                // Valida existencia de la tabla
                nombreTabla = (String) comandoParsed[0];

                if(!this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla especificada no existe");

                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;

            case 4: // UnirTabla

                            /*
            * [0] = nombre_tabla1
            * [1] = nombre_tabla2
            * [2] = nombre_campo
            * [3] = valor campo
            * [4] = Ordenado asc/dsc
            * [5] = numElementosMostrar
            *
            * */
                //Validacion de tabla1 existente
                String nombreTabla1 = (String) comandoParsed[0];
                String nombreTabla2 = (String) comandoParsed[1];
                nombreCampo = (String) comandoParsed[2];


                if(!this.BuscarTabla(nombreTabla1))
                    throw new SecurityException("Error, la tabla "+ nombreTabla1 +" no existe");

                if(!this.BuscarTabla(nombreTabla2))
                    throw new SecurityException("Error, la tabla "+ nombreTabla2 +" no existe");

                //Validacion campo existentes en las tablas
                boolean estadoTabla1 = false;
                boolean estadoTabla2 = false;
                CsvReader read;
                CsvReader read2;
                try {

                    read = new CsvReader("ArchivosBD\\" + nombreTabla1 + ".BD");
                    read.readRecord();
                    for(int i = 0; i<read.getColumnCount();i++){
                        if(!read.getRawRecord().isEmpty())
                            if(read.get(i).equals(nombreCampo)){
                                estadoTabla1 = true;
                                break;
                            }
                    }
                    read.close();
                    read2 = new CsvReader("ArchivosBD\\" + nombreTabla2 + ".BD");
                    read2.readRecord();
                    for(int i = 0; i<read2.getColumnCount();i++){
                        if(!read2.getRawRecord().isEmpty())
                            if(read2.get(i).equals(nombreCampo)){
                                estadoTabla2 = true;
                                break;
                            }
                    }
                    read2.close();
                }
                catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }
                read2.close();
                read.close();

                if(estadoTabla1==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla1 + " no posee el campo " + nombreCampo);

                if(estadoTabla2==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla2 + " no posee el campo " + nombreCampo);

                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;

            case 5: // CrearRegistro

                // Validamos la existencia de la tabla
                nombreTabla = (String) comandoParsed[0];
                if(!this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla especificada no existe");

                // Validamos la longitud y el numero de campos
                int nroCampos = - 1;
                ArrayList<String> valoresCampos = (ArrayList<String>) comandoParsed[1];
                longitudes = new  ArrayList<Integer>();
                int numeroCampos = valoresCampos.size();
                String campoClave = null;
                fileMETA = "ArchivosBD\\META.bd";
                try {
                    CsvReader ar = new CsvReader(fileMETA);
                    //nroCampos = ar.getColumnCount()-4;
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla))
                        {
                            nroCampos = (ar.getColumnCount()-3)/3 ;
                            campoClave = ar.get(2);
                            for(int i=3; i < numeroCampos + 3; i++){
                                longitudes.add(Integer.parseInt(ar.get(i)));
                            }
                        }
                    }
                    ar.close();
                } catch(IOException | NumberFormatException e){
                    throw new Error("Error, algo salio mal con los archivos internos");
                }

                if(nroCampos ==-1 || campoClave == null)
                    throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");

                System.out.println("numeroCampos:  "+ numeroCampos);
                System.out.println("nroCampos:  "+ nroCampos);
                
                if(numeroCampos != nroCampos)
                    throw new SecurityException("Error, el numero total de valores ingresados no coincide con el numero de campos registrados");

                for(int i=0; i < numeroCampos; i++){
                    if(valoresCampos.get(i).length() > longitudes.get(i)){
                        throw new SecurityException("Error, en uno de los campos excede la longitud maxima");
                    }
                }

                // Validar que el campo clave no se repita
                try {

                    CsvReader ar = new CsvReader("ArchivosBD\\"+nombreTabla+".BD");
                    int posicion = -1;
                    ar.readRecord();
                    for(int k = 0; k<ar.getColumnCount(); k++){
                        System.out.println("val: " + ar.get(k));
                        if(ar.get(k).equals(campoClave)){
                            posicion = k;
                        }
                    }

                    if(posicion == -1)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                    while(ar.readRecord()){
                        if(ar.get(posicion).equals(valoresCampos.get(posicion)))
                            throw new SecurityException("Error, el valor correspondiente al campo clave ya existe");
                    }
                    ar.close();

                }catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");
                }catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}


                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;

            case 6: // modificarRegistro
                //Validacion de tabla existente
                nombreTabla = (String) comandoParsed[0];
                System.out.println("TABLA: "+ nombreTabla);
                if(!this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla especificada no existe");

                //Validacion tabla con registros
                try {

                    read = new CsvReader("ArchivosBD\\"+nombreTabla+".bd");
                    int cont = 0;
                    while(read.readRecord()) cont++;
                    if(cont<=1) throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                    read.close();
                }
                catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}

                //Validaciones de existencia y longitud
                nombreCampo = (String) comandoParsed[2];
                String valorCampoClave = (String) comandoParsed[1];

                String campoClaveReal = null;
                fileMETA = "ArchivosBD\\META.bd";
                int posicionCampo = -1;
                try {
                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        //Existencia del campo
                        if(ar.get(0).equals(nombreTabla))
                        {
                            boolean estadoEncontrado = false;
                            boolean estadoEncontradoClave = false;
                            campoClaveReal = ar.get(2);
                            int inicioCampos = 3+((ar.getColumnCount()-3)/3);
                            for(int w = inicioCampos; w<ar.getColumnCount() - ((ar.getColumnCount()-3)/3);w++){
                                if(ar.get(w).equals(nombreCampo)){
                                    estadoEncontrado = true;
                                }
                                if(ar.get(w).equals(campoClaveReal)){
                                    estadoEncontradoClave = true;
                                    posicionCampo = w;
                                }
                            }
                            posicionCampo = posicionCampo - inicioCampos;

                            //Errores varios
                            if(estadoEncontrado == false)
                                throw new SecurityException("Error, el nombre del campo especificado no existe");
                            if(estadoEncontradoClave == false)
                                throw new SecurityException("Error, el nombre del campo clave especificado no existe");
                            if(ar.get(2).equals(nombreCampo))
                                throw new SecurityException("Lo sentimos, no es posible modificar el valor del campo clave");
                            break;
                        }
                    }
                    ar.close();


                    read = new CsvReader("ArchivosBD\\"+nombreTabla+".bd");
                    boolean estadoEncontrado = false;
                    read.readRecord();
                    while(read.readRecord()){
                        System.out.println("read.get(posicionCampo): "+ read.get(posicionCampo));
                        System.out.println("valorCampoClave: "+ valorCampoClave);
                        if(read.get(posicionCampo).equals(valorCampoClave)){
                            estadoEncontrado = true;
                            break;
                        }
                    }
                    read.close();
                    read.close();
                    if(estadoEncontrado==false)
                        throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                }
                catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}

                if(campoClaveReal == null)
                    throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");

                
                // agregamos la posición del campo a el comando
               comandoParsed[4] = posicionCampo;
               
                // Datos validados
                gestor.operar(comandoParsed, operacion);

                break;
            case 7: //  eliminarRegistro
                //Validacion de tabla existente
                nombreTabla = (String) comandoParsed[0];
                campoClave = (String) comandoParsed[1];

                if(!this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla especificada no existe");

                //Validacion tabla con registros
                try {

                    read = new CsvReader("ArchivosBD\\"+nombreTabla+".bd");
                    int cont = 0;
                    while(read.readRecord()) cont++;
                    if(cont<=1)
                        throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                    read.close();
                    read.close();
                }
                catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}

                //Validacion de valor de campo clave existente
                fileMETA = "ArchivosBD\\META.bd";
                int posicion = -1;
                try {
                    campoClaveReal = null;
                    int inicioCampos = 0;
                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla)){
                            campoClaveReal = ar.get(2);
                            inicioCampos = 3+((ar.getColumnCount()-3)/3);
                            for(int k = inicioCampos; k<ar.getColumnCount() - ((ar.getColumnCount()-3)/3); k++){
                                System.out.println("ar.get(k): "+ar.get(k));
                                System.out.println("campoClaveReal: "+campoClaveReal);
                                if(ar.get(k).equals(campoClaveReal)){
                                    posicion = k;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    posicion = posicion - inicioCampos;
                    ar.close();
                    ar.close();
                    if(campoClaveReal == null || posicion == -1)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");

                    read = new CsvReader("ArchivosBD\\"+nombreTabla+".bd");
                    boolean estadoEncontrado = false;
                    read.readRecord();
                    while(read.readRecord()){
                        if(read.get(posicion).equals(campoClave)){
                            estadoEncontrado = true;
                            break;
                        }
                    }
                    read.close();
                    read.close();
                    if(estadoEncontrado==false)
                        throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                }
                catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }

                // agregamos al comando la posición campo clave
                 comandoParsed[2] = posicion;
                
                // Datos validados
                gestor.operar(comandoParsed, operacion);

                break;
            case 8: // seleccionarRegistro

                //Validacion de tabla existente
                nombreTabla = (String)comandoParsed[0];
                nombreCampo = (String)comandoParsed[1];
                if(!this.BuscarTabla(nombreTabla)) throw new SecurityException("Error, el nombre de la tabla especificada no existe");

                //Validacion de que el campo especificado exista...

                posicion = -1;
                fileMETA = "ArchivosBD\\META.bd";

                try {
                    CsvReader ar = new CsvReader(fileMETA);

                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla)){
                            for(int k = 4; k<ar.getColumnCount(); k++){
                                if(ar.get(k).equals(nombreCampo)){
                                    posicion = k;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if(posicion!=-1)
                        posicion = posicion - 4;

                    ar.close();

                } catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }
                if(posicion == -1) throw new SecurityException("Error interno, la tabla " + nombreTabla + " no posee ese campo");

                comandoParsed[5] = posicion;
                // Datos validados
                gestor.operar(comandoParsed, operacion);

                break;
            default:
                throw new SecurityException("Lo sentimos, no se ha entendido esa orden.");
        }
    }

}

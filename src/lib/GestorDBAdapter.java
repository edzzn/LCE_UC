package lib;

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

    Mensaje mensaje = new Mensaje();
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
        if(!new File("filesBD\\META.bd").exists())
            return false;
        String fileMETA = "filesBD\\META.bd";
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
            mensaje.mostrarMensaje("Error, No se pudo leer el archivo Meta");
        } catch (IOException ex) {
            mensaje.mostrarMensaje("Error, No se pudo leer el archivo Meta");
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
            mensaje.mostrarMensaje("El comando ingresado no es parte de la Sintaxis.");
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
                    mensaje.mostrarMensaje("Error, Ya existe una tabla con ese nombre.");

                // Validamos la longitud
                ArrayList<Integer> longitudes = (ArrayList<Integer>) (List) comandoParsed[3];

                for(int i=0; i<longitudes.size(); i++) {
                    int longitud = longitudes.get(i);
                    if(longitud <= 0){
                        System.out.println("Longitud " + longitudes.get(i));
                        mensaje.mostrarMensaje("Error, Uno de los campos tiene una longitud menor que cero.");
                    }
                }
                System.out.println(longitudes);

                // Validamos que el campo clave se encuentre dentro de los campos
                ArrayList<String> campos = (ArrayList<String>) (List) comandoParsed[1];
                String campo_clave = (String) comandoParsed[2];

                if(!campos.contains(campo_clave)){
                    mensaje.mostrarMensaje("Error, El campo Clave no se encuentra en la lista de campos.");
                }

                // Validad que los campos en encriptado existan en el listado de campos
                ArrayList<String> camposEciptados =  (ArrayList<String>) (List) comandoParsed[4];

                int contadorCamposEncriptados = 0;
                for(int i=0; i < camposEciptados.size(); i++){
                    if(campos.contains(camposEciptados.get(i))){
                        contadorCamposEncriptados++;

                    }
                }
                if (contadorCamposEncriptados != camposEciptados.size()){
                    mensaje.mostrarMensaje("Error, Algun campo Encriptado no se encuentra en la lista de campos.");
                }

                // Validamos que el tamaño de longitudes es igual al del numero de campos
                if(longitudes.size() != campos.size()){
                    mensaje.mostrarMensaje("Error, El numero de longitudes no es igual al de campos.");
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
                    mensaje.mostrarMensaje("Error, La tabla que desea modificar no existe.");
                }

                // Validamos la existencia del campo
                String nombreCampo = (String) comandoParsed[1];
                ArrayList<String> camposMeta = new ArrayList<>();
                String fileMETA = "filesBD\\META.bd";
                boolean esClave = false;
                try {
                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla)){
                            for (int j = 4; j < ar.getColumnCount();j++){
                                camposMeta.add(ar.get(j));
                            }
                            if(ar.get(2).equals(nombreCampo))
                                esClave = true;
                        }

                    }
                    ar.close();
                }
                catch (FileNotFoundException ex) {
                    mensaje.mostrarMensaje("Error, No se pudo leer el archivo Meta");
                } catch (IOException ex) {
                    mensaje.mostrarMensaje("Error, No se pudo leer el archivo Meta");
                }

                if(!camposMeta.contains(nombreCampo))
                    mensaje.mostrarMensaje("Error, El campo especificado no existe");

                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;
            case 3: // Eliminar Tabla
                /*
                * [0] = nombre_tabla a eliminar
                * */
                // Valida existencia de la tabla
                nombreTabla = (String) comandoParsed[0];

                System.out.println("Nombre Tabla:  " + nombreTabla);

                if(!this.BuscarTabla(nombreTabla))
                    mensaje.mostrarMensaje("Error, La tabla no existe en el registro");


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
                    mensaje.mostrarMensaje("Error, la Tabla "+ nombreTabla1 +" no existe");

                if(!this.BuscarTabla(nombreTabla2))
                    mensaje.mostrarMensaje("Error, la Tabla "+ nombreTabla2 +" no existe");

                //Validacion campo existentes en las tablas
                boolean estadoTabla1 = false;
                boolean estadoTabla2 = false;
                CsvReader read;
                CsvReader read2;
                try {

                    read = new CsvReader("filesBD\\" + nombreTabla1 + ".BD");
                    read.readRecord();
                    for(int i = 0; i<read.getColumnCount();i++){
                        if(!read.getRawRecord().isEmpty())
                            if(read.get(i).equals(nombreCampo)){
                                estadoTabla1 = true;
                                break;
                            }
                    }
                    read.close();
                    read2 = new CsvReader("filesBD\\" + nombreTabla2 + ".BD");
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
                    throw new Error("Error, No se pudo leer el archivo Meta");
                } catch (IOException ex) {
                    throw new Error("Error, No se pudo leer el archivo Meta");
                }
                read2.close();
                read.close();

                if(estadoTabla1==false)
                    mensaje.mostrarMensaje("Error, la tabla " + nombreTabla1 + " no posee el campo " + nombreCampo);

                if(estadoTabla2==false)
                    mensaje.mostrarMensaje("Error, la tabla " + nombreTabla2 + " no posee el campo " + nombreCampo);

                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;

            case 5: // CrearRegistro

                // Validamos la existencia de la tabla
                nombreTabla = (String) comandoParsed[0];
                if(!this.BuscarTabla(nombreTabla))
                    mensaje.mostrarMensaje("Error, La tabla no existe.");

                // Validamos la longitud y el numero de campos
                int nroCampos = - 1;
                ArrayList<String> valoresCampos = (ArrayList<String>) comandoParsed[1];
                longitudes = new  ArrayList<Integer>();
                int numeroCampos = valoresCampos.size();
                String campoClave = null;
                fileMETA = "filesBD\\META.bd";
                try {
                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla))
                        {
                            nroCampos = ar.getColumnCount()-4;
                            campoClave = ar.get(2);
                            for(int i=3; i < numeroCampos + 3; i++){
                                longitudes.add(Integer.parseInt(ar.get(i)));
                            }
                        }
                    }
                    ar.close();
                } catch(IOException | NumberFormatException e){
                    mensaje.mostrarMensaje("Error al leer el archivo meta");
                }

                if(nroCampos ==-1 || campoClave == null)
                    mensaje.mostrarMensaje("Error, no se encontro los datos en la tabla");

                if(numeroCampos != nroCampos)
                    mensaje.mostrarMensaje("Error, el numero de valores ingresado es distinto al numero de campos");

                for(int i=0; i < numeroCampos; i++){
                    if(valoresCampos.get(i).length() > longitudes.get(i)){
                        mensaje.mostrarMensaje("Error, en uno de los campos excede la longitud maxima");
                    }
                }

                // Validar que el campo clave no se repita
                try {

                    CsvReader ar = new CsvReader("filesBD\\"+nombreTabla+".BD");
                    int posicion = -1;
                    ar.readRecord();
                    for(int k = 0; k<ar.getColumnCount(); k++){
                        System.out.println("val: " + ar.get(k));
                        if(ar.get(k).equals(campoClave)){
                            posicion = k;
                        }
                    }

                    if(posicion == -1)
                        throw new SecurityException("No se pudo leer el archivo META.db");
                    while(ar.readRecord()){
                        if(ar.get(posicion).equals(valoresCampos.get(posicion)))
                            throw new SecurityException("No se pudo leer el archivo META.db");
                    }
                    ar.close();

                }catch (FileNotFoundException ex) {

                }catch (IOException ex) {mensaje.mostrarMensaje("Error, No se puedo leer el archivo");
                }


                // Datos validados
                gestor.operar(comandoParsed, operacion);
                break;

            case 6: // modificarRegistro
                //Validacion de tabla existente
                nombreTabla = (String) comandoParsed[0];
                if(!this.BuscarTabla(nombreTabla))
                    mensaje.mostrarMensaje("Error, la tabla no existe.");

                //Validacion tabla con registros
                try {

                    read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                    int cont = 0;
                    while(read.readRecord()) cont++;
                    if(cont<=1) mensaje.mostrarMensaje("Error, La tabla no tiene registros");
                    read.close();
                }
                catch (FileNotFoundException ex) {mensaje.mostrarMensaje("Error, No se puedo leer el archivo");}
                catch (IOException ex) {mensaje.mostrarMensaje("Error, No se puedo leer el archivo");}

                //Validaciones de existencia y longitud
                nombreCampo = (String) comandoParsed[2];
                String valorCampoClave = (String) comandoParsed[1];

                String campoClaveReal = null;
                fileMETA = "filesBD\\META.bd";
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
                            for(int w = 4;w<ar.getColumnCount();w++){
                                if(ar.get(w).equals(nombreCampo)){
                                    estadoEncontrado = true;
                                }
                                if(ar.get(w).equals(campoClaveReal)){
                                    estadoEncontradoClave = true;
                                    posicionCampo = w;
                                }
                            }
                            posicionCampo = posicionCampo - 4;

                            //Errores varios
                            if(estadoEncontrado == false)
                                mensaje.mostrarMensaje("Error, El campo no existe");
                            if(estadoEncontradoClave == false)
                                mensaje.mostrarMensaje("Error, El campo clave no existe");
                            break;
                        }
                    }
                    ar.close();


                    read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                    boolean estadoEncontrado = false;
                    read.readRecord();
                    while(read.readRecord()){
                        System.out.println("");
                        if(read.get(posicionCampo).equals(valorCampoClave)){
                            estadoEncontrado = true;
                            break;
                        }
                    }
                    read.close();
                    read.close();
                    if(estadoEncontrado==false)
                        mensaje.mostrarMensaje("Error, No existe un registro con este valor");
                }
                catch(IOException | NumberFormatException e){mensaje.mostrarMensaje("Error, No se puedo leer el archivo");}

                if(campoClaveReal == null)
                    mensaje.mostrarMensaje("Error, No se encontraron los datos en la tabla");

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
                    mensaje.mostrarMensaje("La tabla no existe");

                //Validacion tabla con registros
                try {

                    read = new CsvReader("filesBD\\"+nombreTabla+".bd");
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
                fileMETA = "filesBD\\META.bd";
                int posicion = -1;
                try {
                    campoClaveReal = null;

                    CsvReader ar = new CsvReader(fileMETA);
                    while(ar.readRecord()){
                        if(ar.get(0).equals(nombreTabla)){
                            campoClaveReal = ar.get(2);
                            for(int k = 4; k<ar.getColumnCount(); k++){
                                if(ar.get(k).equals(campoClaveReal)){
                                    posicion = k;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    posicion = posicion - 4;
                    ar.close();
                    ar.close();
                    if(campoClaveReal == null || posicion == -1)
                        mensaje.mostrarMensaje("Error, no se encontraron los datos en la tabla");

                    read = new CsvReader("filesBD\\"+nombreTabla+".bd");
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
                        mensaje.mostrarMensaje("No existe un registro con ese valor de CampoClave");
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
                if(!this.BuscarTabla(nombreTabla)) mensaje.mostrarMensaje("La tabla especificada no existe");

                //Validacion de que el campo especificado exista...

                posicion = -1;
                fileMETA = "filesBD\\META.bd";

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

                // Agregamos el parametro posición del campo en
                // la tabla
                comandoParsed[5] = posicion;


                // Datos validados
                gestor.operar(comandoParsed, operacion);

                break;
            default:
                mensaje.mostrarMensaje("No se entendio el comando.");
        }
    }

}

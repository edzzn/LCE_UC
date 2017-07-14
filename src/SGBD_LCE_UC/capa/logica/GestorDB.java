package SGBD_LCE_UC.capa.logica;

import SGBD_LCE_UC.capa.GUI.Mensaje;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edzzn on 7/8/17.
 */
public class GestorDB {
    Mensaje mensaje = new Mensaje();                  //  CLASE MENSAJE
    private static GestorDB instance = null;

    //    Implementación del patron Singleton
    protected GestorDB() {
        // Evita una nueva instanciación
    }

    public static GestorDB getGestor() {
        if(instance == null) {
            instance = new GestorDB();
        }
        return instance;
    }


    public void operar(Object[] comando, int operacion) {

        if(comando.length<=0 || comando == null)
            mensaje.mostrarMensaje("Error en la lectura.");           

        System.out.println("------------------------------------------");
        switch(operacion){
            case 1:     // CREAR TABLA
                System.out.println("Crear tabla");
                System.out.println("Nombre tabla: " + comando[0]);
                System.out.println("Campos: " + comando[1]);
                System.out.println("Campo clave: " + (String) comando[2]);
                System.out.println("Longitud: " + comando[3]);
                System.out.println("Campos a encriptar: " + (List) comando[4]);

                System.out.println("Creando Tabla...");
                GestionTablas nuevaTabla = new GestionTablas((String) comando[0], (ArrayList<String>) comando[1], (String) comando[2],(ArrayList<String>) comando[3], (ArrayList<String>) comando[4]);
                nuevaTabla.crearTabla();
                System.out.println("AVSO: Tabla creada...");
                
                break;
            case 2:     // MODIFICA TABLA
                System.out.println("Modificar tabla");

                System.out.println("Tabla: " + comando[0]);
                System.out.println("Nombre del campo a modificar: " + comando[1]);
                System.out.println("Nuevo valor del campo: " + comando[2]);

                System.out.println("Modificando Tabla...");
                GestionTablas tablaModificar = new GestionTablas((String) comando[0], (String) comando[1], (String) comando[2]);
                tablaModificar.modificarTabla();
                System.out.println("AVSO: Tabla modificada...");
                
                break;
            case 3:     // ELIMINAR TABLA
                System.out.println("Eliminar");
                System.out.println("Tabla: " + comando[0]);

                System.out.println("Eliminando Tabla...");
                GestionTablas tablaEliminar = new GestionTablas((String) comando[0]);
                tablaEliminar.eliminarTabla();
                System.out.println("AVISO: Tabla Eliminada...");
                
                break;
            case 4:     // UNIR TABLAS
                System.out.println("Unir tabla");
                System.out.println("Tabla 1: " + comando[0]);
                System.out.println("Tabla 2: " + comando[1]);
                System.out.println("Campo: " + comando[2]);
                System.out.println("Valor Campo: " + comando[3]);
                System.out.println("Ordenado: " + comando[4]);
                System.out.println("Ver: " + comando[5]);
               
                System.out.println("Uniendo Tabla...");
                //GestionTablas tablaEliminar = new GestionTablas((String) comando[0]);
                //tablaEliminar.eliminarTabla();
                System.out.println("AVISO: Tabla Unida...");
                
                break;
            case 5:     // AGREGAR REGISTRO
                System.out.println("Agregar Registro");
                System.out.println("Tabla: " + comando[0]);
                System.out.println("Valores de los campos: " + (ArrayList<String>) (List) comando[1]);

                System.out.println("Agregando Registro...");
                GestionRegistros registroNuevo = new GestionRegistros( (String) comando[0], (ArrayList<String>) comando[1]);
                registroNuevo.crearRegistros();
                //GestorDBAdapter gestor = new GestorDBAdapter();   // No Funciona
                //gestor.operar(FramePrincipalBD.ingresoComandos.getText());
                System.out.println("AVISO: Registro ingresado...");
                
                break;
            case 6:     // MODIFICAR REGISTRO
                System.out.println("Modificar Registro");
                String nombreTabla = (String) comando[0];
                String campoClave = (String) comando[1];
                String nombreCampo = (String) comando[2];
                String valorCampo = (String) comando[3];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo clave: " + campoClave);
                System.out.println("Nombre del campo a modificar: " + nombreCampo);
                System.out.println("Nuevo valor del campo: " + valorCampo);

                System.out.println("Modificando Registro...");                                              //  FALTA POSICION CAMPO CLAVE
                GestionRegistros registroModificar = new GestionRegistros(nombreTabla, campoClave, nombreCampo, valorCampo, (int) comando[4]);   
                                                //    nom_tabla   valorCampoClave     campoModif    valordeCampoModif   posicCampoClave
                registroModificar.modificarRegistros();
                System.out.println("AVISO: Registro Modificado...");
                
                break;
            case 7:     // ELIMINAR REGISTRO
                System.out.println("Eliminar Registro");
                System.out.println("Tabla: " + comando[0]);
                System.out.println("Valor campo clave: " + (String) comando[1]);

                System.out.println("Eliminando Registro...");
                GestionRegistros registroEliminar = new GestionRegistros((String) comando[0], (String) comando[1], (int) comando[2]);
                registroEliminar.eliminarRegistros();
                System.out.println("AVISO: Registro Eliminado ...");
                
                break;
            case 8:     //  SELECCIONAR TABLA
                System.out.println("Seleccionar Registro");
                System.out.println("Tabla: " + comando[0]);
                System.out.println("Campo: " + comando[1]);
                System.out.println("Valor: " + comando[2]);
                System.out.println("Ordenado: " + comando[3]);
                System.out.println("Ver: " + comando[4]);
                System.out.println("CampoTabla: " + comando[5]);

                
                System.out.println("Seleccionado Archivo...");
                //  CODIGO DE SELECCIONAR TABLA
                /*
                * [0] = nombre_tabla
                * [1] = nombre_campo
                * [2] = valor campo
                * [3] = Ordenado asc/dsc
                * [4] = numElementosMostrar
                * [5] = ubicacion del campo en la tabla
                *
                * */
                Seleccionar select = new Seleccionar(comando);
                select.operar();
                System.out.println("AVISO: Seleccionado ...");
//                
                break;
            default:
                mensaje.mostrarMensaje("No se entendio el comando.");
        }
    }

}
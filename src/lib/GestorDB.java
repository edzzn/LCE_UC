package lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edzzn on 7/8/17.
 */
public class GestorDB {
    Mensaje mensaje = new Mensaje();
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
                Gestion_Tablas nuevaTabla = new Gestion_Tablas((String) comando[0], (ArrayList<String>) comando[1], (String) comando[2],(ArrayList<String>) comando[3], (ArrayList<String>) comando[4]);
                nuevaTabla.crearTabla();
                System.out.println("AVSO: Tabla creada...");

                break;
            case 2:     // MODIFICA TABLA
                System.out.println("Modificar tabla");

                System.out.println("Tabla: " + comando[0]);
                System.out.println("Nombre del campo a modificar: " + comando[1]);
                System.out.println("Nuevo valor del campo: " + comando[2]);

                System.out.println("Modificando Tabla...");
                Gestion_Tablas tablaModificar = new Gestion_Tablas((String) comando[0], (String) comando[1], (String) comando[2]);
                tablaModificar.modificarTabla();
                System.out.println("AVSO: Tabla modificada...");

                break;
            case 3:     // ELIMINAR TABLA
                System.out.println("Eliminar");
                System.out.println("Tabla: " + comando[0]);

                System.out.println("Eliminando Tabla...");
                Gestion_Tablas tablaEliminar = new Gestion_Tablas((String) comando[0]);
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
//
//                new UnirTablas(comando).Visualizar();
//
//                Aplicacion.estadoOperacion = "Se uniron las tablas " + (String) comando[0] + " y " + (String) comando[1];
                break;
            case 5:     // AGREGAR REGISTRO
                System.out.println("Agregar Registro");
                System.out.println("Tabla: " + comando[0]);
                System.out.println("Valores de los campos: " + comando[1]);

                System.out.println("Agregando Registro...");
                //  CODIGO DE CREAR REGISTRO
                System.out.println("AVISO: Registro ingresado...");

                break;
            case 6:     // MODIFICAR REGISTRO
                System.out.println("Modificar Registro");
                String nombreTabla = (String) comando[1];
                String campoClave = (String) comando[2];
                String nombreCampo = (String) comando[3];
                String valorCampo = (String) comando[4];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo clave: " + campoClave);
                System.out.println("Nombre del campo a modificar: " + nombreCampo);
                System.out.println("Nuevo valor del campo: " + valorCampo);

                System.out.println("Modificando Registro...");
                //  CODIGO DE MODIFICAR REGISTRO
                System.out.println("AVISO: Registro Modificado...");

                break;
            case 7:     // ELIMINAR REGISTRO
                System.out.println("Eliminar Registro");
                System.out.println("Tabla: " + comando[0]);
                System.out.println("Valor campo clave: " + comando[1]);

                System.out.println("Eliminando Registro...");
                //  CODIGO DE ELIMINAR REGISTRO
                System.out.println("AVISO: Tabla ...");

                break;
            case 8:     //  SELECCIONAR TABLA
                System.out.println("Seleccionar Registro");
                System.out.println("Tabla: " +  comando[0]);
                System.out.println("Campo: " +  comando[1]);
                System.out.println("Valor: " + comando[2]);
                System.out.println("Ordenado: " + comando[3]);
                System.out.println("Ver: " + comando[4]);

                System.out.println("Eliminando Registro...");
                //  CODIGO DE SELECCIONAR TABLA
                System.out.println("AVISO: Tabla ...");

                break;
            default:
                mensaje.mostrarMensaje("No se entendio el comando.");
        }
    }

}
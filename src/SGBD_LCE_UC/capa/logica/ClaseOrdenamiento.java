/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SGBD_LCE_UC.capa.logica;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author JHON
 */
public class ClaseOrdenamiento {
    
    public static int campoOrdenar;     // Variable Static para usar en la clase FORMATO ARCHIVO
    public static String tipoOrdenamiento;     // Variable Static para usar en la clase FORMATO ARCHIVO
    String nombreTabla;
    
    public ClaseOrdenamiento(){}
    
    public void realizarOrdenamiento(int posicion, String nombreTabla, int columnas, String ordenamiento){
        try {
            campoOrdenar = posicion;                   // Posicion de Campos: 0 = 1erCampo, 1 = 2doCampo...
            FormatoArchivo.numCampos = columnas;       // Numero de Campos de la Tabla
            tipoOrdenamiento = ordenamiento;           // o "des"
            this.nombreTabla = nombreTabla;
            OrdenamientoPolifase.Procesar(campoOrdenar, this.nombreTabla);   //Direccion de Documento
            
            System.out.println("Terminado...");
        } catch (IOException | ParseException e) {
            System.out.println("ERROR:"+e);
        }
    }
}

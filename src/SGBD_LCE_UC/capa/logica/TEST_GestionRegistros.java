/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SGBD_LCE_UC.capa.logica;

import java.util.ArrayList;

/**
 *
 * @author JHON
 */
public class TEST_GestionRegistros {
    
    public static void main(String[] args) {
        
        
        String comando = "SELECCIONAR DE nombre_tabla DONDE cedulacamp1 =\"123\"";
        
        
        //String comando = "INSERTAR EN nombre_tabla1 VALORES 333,trew, 345";
        
        GestorDBAdapter gestor = new GestorDBAdapter();
        gestor.operar(comando);
        
        //  INGRESAR REGISTRO
        //Gestion_Registros registroNuevo = new Gestion_Registros("nombre_tabla", valoresCampos);
        //registroNuevo.crearRegistros();
        
        //  MODIFCAR REGISTRO
//        Gestion_Registros registroModificar = new Gestion_Registros("nombre_tabla1", "111", "camp2", "NUEVO_VALOR", 0);
//                                                //    nom_tabla   valorCampoClave     campoModif    valordeCampoModif   posicCampoClave
//        registroModificar.modificarRegistros();
        
        //gestor.operar(comando);
        
        //  ELIMINAR REGISTRO
        //Gestion_Registros registroEliminar = new Gestion_Registros("nombre_tabla", "222", 0);
        //registroEliminar.eliminarRegistros();
        
        gestor.operar(comando);
        
    }
    
}

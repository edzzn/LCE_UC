/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SGBD_LCE_UC.capa.logica;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author JHON
 */
public class OrdenamientoPolifase {
    
    public static void Procesar(int campo, String ruta) throws IOException, FileNotFoundException, ParseException {
        
        Intercalacion ordenar = new Intercalacion(Integer.parseInt((String) "3"),ruta,campo+1);
        ordenar.start();
    }
    
}

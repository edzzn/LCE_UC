package lib;

/**
 * Created by edzzn on 7/8/17.
 */
public class SGDB {
    public static void main(String[] argv){
//        String comando = "MODIFICAR TABLA nombre_tabla CAMPO nombre_campo POR nombre_campo";
        String comando = "CREAR TABLA nombre_tabla CAMPOS camp1, camp2, camp3, camp4 CLAVE camp2 LONGITUD 1, 2, 2, 4";
        GestorDBAdapter gestor = GestorDBAdapter.getGestor();
        gestor.operar(comando);
    }
}

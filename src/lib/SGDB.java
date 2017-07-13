package lib;

/**
 * Created by edzzn on 7/8/17.
 */
public class SGDB {
    public static void main(String[] argv){
//        String comando = "MODIFICAR TABLA nombre_tabla CAMPO nombre_campo POR nombre_campo";
        String comando = "ELIMINAR TABLA nombre_tabla";
        GestorDBAdapter gestor = GestorDBAdapter.getGestor();
        gestor.operar(comando);
        Mensaje mensaje = new Mensaje();
//        mensaje.mostrarMensaje("Texto de Mensaje");
//        mensaje.mostrarMensaje("Error, algo paso");
    }

}

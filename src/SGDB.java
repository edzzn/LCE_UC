/**
 * Created by edzzn on 7/8/17.
 */
public class SGDB {
    public static void main(String[] argv){
        System.out.println("Main");
        String crearT = "CREAR TABLA nombreTabala CAMPOS camp1, camp2, camp3, camp4 CLAVE camp2 LONGITUD l1, l2, l3, l4n ENCRIPTADO camp3, camp4";

        GestorDBAdapter comand = new GestorDBAdapter(crearT);


    }
}

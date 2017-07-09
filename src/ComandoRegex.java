/**
 * Created by edzzn on 7/8/17.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ComandoRegex {

    public static void main(String args[]){
        String comando = "CREAR TABLA nombre_tabla CAMPOS camp1, camp2, camp3, camp4 CLAVE camp2 LONGITUD l1, l2, l3, l4n ENCRIPTADO camp3, camp4";
        comando = comando.replace(" ","");
        System.out.println(comando);
        String pattern = "([A-Z]+(?=[a-z]))" +
                         "([a-z_]+(?=[A-Z]))" +
                         "([A-Z]+(?=[a-z]))" +
                         "([^A-Z]+(?=[A-Z]))" +
                         "([A-Z]+(?=[a-z]))" +
                         "([^A-Z]+(?=[A-Z]))" +
                         "([A-Z]+(?=[a-z]))" +
                         "([^A-Z]+(?=[A-Z]))"+
                         "(.+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(comando);
        if (m.find( )) {
            System.out.println("Comando: " + m.group(0) );
            System.out.println("Primer valor: " + m.group(1) );
            System.out.println("Segundo valor: " + m.group(2) );
            System.out.println("Tercer Valor: " + m.group(3) );
            System.out.println("Cuearto Valor: " + m.group(4) );
            System.out.println("Quito Valor: " + m.group(5) );
            System.out.println("Sexto Valor: " + m.group(6) );
            System.out.println("Septimo Valor: " + m.group(7) );
            System.out.println("Octavo Valor: " + m.group(8) );;
            System.out.println("Noveno Valor: " + m.group(9) );


        }else {
            System.out.println("NO MATCH");
        }
    }
}

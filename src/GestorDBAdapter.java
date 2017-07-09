import java.util.ArrayList;

/**
 * Created by edzzn on 7/8/17.
 */
public class GestorDBAdapter {
    GestorDB gestor;

    GestorDBAdapter(String comandoStr){
        ArrayList comando = this.extraerComando(comandoStr);
        this.gestor = new GestorDB(comando);
    }

    ArrayList extraerComando(String comandoStr){
//        Este metodo toma como entrada el comando ingresado por el usuario
//        lo valida y retorna un arrays de objetos,el cual el gestor puede manejar
        ArrayList<Object> parametros = new ArrayList();
        if(comandoStr.contains("CREAR TABLA")){
            parametros.add("Crea tabla");
        } else {
            System.out.println("Detalle del Error");
            return parametros;
        }
        return parametros;

    }
}

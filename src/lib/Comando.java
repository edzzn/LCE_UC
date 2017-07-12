package lib;

/**
 * Created by edzzn on 7/8/17.
 */
public class Comando {
    String comando;

    Comando (String crearT){
        System.out.println("Crenado lib.Comando");
        this.comando = crearT;
    }

    public void mostrarComando(){
        System.out.println(comando);
    }
}

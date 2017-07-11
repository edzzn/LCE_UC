import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edzzn on 7/8/17.
 */
public class GestorDB {

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
            throw new Error("Lo sentimos, algo ha salido mal");

        System.out.println("------------------------------------------");
        switch(operacion){
            case 1: // crearTabla
                System.out.println("Nombre tabla: " + (String) comando[0]);
                System.out.println("Campos: " + (ArrayList<String>) (List) comando[1]);
                System.out.println("Campo clave: " + (String) comando[2]);
                System.out.println("Longitud: " + (ArrayList<String>) comando[3]);
                System.out.println("Campos a encriptar: " + (ArrayList<String>) (List) comando[4]);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new TablaFactory();
                CreacionTemplate crear = factory.creacionProceso();
                crear.operation(comando);
                Aplicacion.estadoOperacion = "Se ha creado la tabla " + (String) comando[0] + "";
                break;
            case 2: // modificarTabla

                System.out.println("Tabla: " + (String) comando[0]);
                System.out.println("Nombre del campo a modificar: " + (String) comando[1]);
                System.out.println("Nuevo valor del campo: " + (String) comando[2]);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new TablaFactory();
                ModificacionTemplate modificar = factory.modificacionProceso();
                modificar.operation(comando);

                Aplicacion.estadoOperacion = "Se ha modificado la tabla " + (String) comando[0] + "";
                break;
            case 3: // eliminarTabla
                System.out.println("Tabla: " + (String) comando[0]);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new TablaFactory();
                EliminacionTemplate eliminar = factory.eliminacionProceso();
                eliminar.operation(comando);

                Aplicacion.estadoOperacion = "Se ha eliminado la tabla " + (String) comando[0] + "";
                break;
            case 4: // unirTabla
                System.out.println("Tabla 1: " + (String) comando[0]);
                System.out.println("Tabla 2: " + (String) comando[1]);
                System.out.println("Campo: " + (String) comando[2]);
                System.out.println("Valor Campo: " + (String) comando[3]);
                System.out.println("Ordenado: " + (String) comando[4]);
                System.out.println("Ver: " + (String) comando[5]);

                new UnirTablas(comando).Visualizar();

                Aplicacion.estadoOperacion = "Se uniron las tablas " + (String) comando[0] + " y " + (String) comando[1];
                break;
            case 5: // crearRegistro

                System.out.println("Tabla: " + (String) comando[0]);
                System.out.println("Valores de los campos: " + (ArrayList<String>) comando[1]);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new RegistroFactory();
                CreacionTemplate crear = factory.creacionProceso();
                crear.operation(comando);

                Aplicacion.estadoOperacion = "Se agrego el registro a la tabla " + (String) comando[0] + "";
                break;;
            case 6: // modificarRegistro
                String nombreTabla = (String) comando[1];
                String campoClave = (String) comando[2];
                String nombreCampo = (String) comando[3];
                String valorCampo = (String) comando[4];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo clave: " + campoClave);
                System.out.println("Nombre del campo a modificar: " + nombreCampo);
                System.out.println("Nuevo valor del campo: " + valorCampo);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new RegistroFactory();
                ModificacionTemplate modificar = factory.modificacionProceso();
                modificar.operation(comando);

                Aplicacion.estadoOperacion = "Se modifico un registro de la tabla " + nombreTabla + "";
                break;
            case 7: // eliminarRegistro
                System.out.println("Tabla: " + );
                System.out.println("Valor campo clave: " + (String) comando[1]);

                //Instanciación a través del Abstract Factory

                ProcesosFactory factory = new RegistroFactory();
                EliminacionTemplate eliminar = factory.eliminacionProceso();
                eliminar.operation(comando);

                Aplicacion.estadoOperacion = "Se elimino un registro de la tabla " + (String) comando[1] + "";
                break;
            case 8: // seleccionarRegistro

                System.out.println("Tabla: " + (String) comando[0]);
                System.out.println("Campo: " + (String) comando[1]);
                System.out.println("Valor: " + (String) comando[2]);
                System.out.println("Ordenado: " + (String) comando[3]);
                System.out.println("Ver: " + (String) comando[4]);


                try {new SeleccionarTabla(comando).Visualizar();}
                catch (IOException ex) {throw new Error(ex.getMessage());}

                Aplicacion.estadoOperacion = "Se seleccionarion registros de la tabla " + comando[0] + "";
                break;
            default:
                throw new SecurityException("Algo salio mal.");
        }
    }

}

}

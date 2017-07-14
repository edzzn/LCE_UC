package SGBD_LCE_UC.capa.logica;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class DistribucionTramos {
    
    private int nivel;
    private final int numFileOut;
    private final String rutaArchivo;
    private final int[] numTramos;
    private final int[] numTramosFicticios;
    private int totalTramos;

    public DistribucionTramos(int numFile, String rutaArchivo) {
        this.nivel = 1;
        this.rutaArchivo = rutaArchivo;
        this.numFileOut = numFile-1;
        this.numTramos = inicializaTramos(numFile-1);
        this.numTramosFicticios = inicializaTramos(numFile-1);
    }

    public int getNivel() {
        return nivel;
    }

    public int[] getNumTramos() {
        return numTramos;
    }

    public int[] getNumTramosFicticios() {
        return numTramosFicticios;
    }

    
    
    public void start() throws FileNotFoundException, IOException, ParseException {
        
        //System.out.println("\tETAPA DE INICIALIZACIÓN\n\n");

        // i Es el indice del archivo en el que se va a guardaar la informacion
        int i = 0; 
        
        // vectir auxiliar que ayudara en el proceso de redimencion de tramos
        int [] numTramosParaPasarDeNivel = inicializaTramos(this.numFileOut);
        
        // Se abre el Archivo Base en modo Lectura
        CsvReader archivoBase = new CsvReader(new FileReader(this.rutaArchivo));
                        
        // Se crea los Archivos de salida
        // (modo escritura)(Mantener abierta durante todo este metodo)
        CsvWriter[] ArchivoDeEscritura = new CsvWriter[this.numFileOut];
        
        for(int j = 0;j < this.numFileOut;j++){
            System.out.println(" Archivo Auxiliar Creado: Archi_Auxiliar"+String.valueOf(j+1)+".csv");
            ArchivoDeEscritura[j] = new CsvWriter("Archi_Auxiliar"+String.valueOf(j+1)+".csv");
        }
            
        FormatoArchivo registro = new FormatoArchivo();
        
        // Se pasa el primer nivel
        for(int j = 0; j < this.numFileOut; j++){
            if(archivoBase.readHeaders()){
                registro.setCampos(archivoBase.getHeaders());
                agregar_a_Archivo(registro,j,ArchivoDeEscritura[j]);
            }
        }
        
        //System.out.println("\n---------------------------------------------\n");
            
        //verVector(" TRAMOS DE NIVEL: ",this.numTramos);
        //verVector(" TRAMOS POR ESCRIBIR: ",this.numTramosFicticios);
        
        boolean hayDatos=true;
        
        // si aun hay registros en el archivo base
        while(hayDatos){
            
            //System.out.println("\tNivel: " + this.nivel + " Terminado\n");
             
            // Se aumentan los Tramos para el nuevo nivel
            definirNuevosTramos(numTramosParaPasarDeNivel);
            
            
            while(!finDeNivel()){ 
                if(this.numTramosFicticios[i]!=0){
                    hayDatos=archivoBase.readHeaders();
                    if(hayDatos){
                        registro.setCampos(archivoBase.getHeaders());
                        agregar_a_Archivo(registro,i,ArchivoDeEscritura[i]);
                    }else{ 
                        redimensionarTramo(numTramosParaPasarDeNivel);
                        break;
                    }
                }
                i = (i+1 < numFileOut) ? i+1 : 0; 
            }
        }
        
        archivoBase.close();
                        
        for(int j = 0;j < this.numFileOut;j++){
            ArchivoDeEscritura[j].close();
        }
        
    }       
    
    private boolean finDeNivel(){
        
        for(int i=0;i<this.numTramosFicticios.length;i++){
            if(this.numTramosFicticios[i]!=0) return false;
        }
        
        return true;
    }

    private void agregar_a_Archivo(FormatoArchivo registro, int i,CsvWriter archivo) throws IOException {
        archivo.writeRecord(registro.getCsv());
        this.numTramosFicticios[i]--;
        this.totalTramos++;
    }

    private void definirNuevosTramos(int[] numTramosParaPasarDeNivel){
        
        this.nivel++;
        int primerTramo=this.numTramos[0],i,auxiliar;
        for(i = 0; i < this.numTramos.length-1; i++){
            auxiliar = this.numTramos[i];
            this.numTramos[i]=primerTramo+this.numTramos[i+1];
            // define los tramos con completar en el siguiente nivel
            this.numTramosFicticios[i]=this.numTramos[i]-auxiliar;
            numTramosParaPasarDeNivel[i]=this.numTramosFicticios[i];
        }
        this.numTramosFicticios[i]=primerTramo-this.numTramos[i];
        this.numTramos[i]=primerTramo;
        numTramosParaPasarDeNivel[i]=this.numTramosFicticios[i];
    }
    
    private int[] inicializaTramos(int numFileOut) {
        int i;
        int[] tramos = new int[numFileOut];
        for(i =0; i<numFileOut;i++){
            tramos[i]=1;
        }
        return tramos;
    }
    
    private void redimensionarTramo(int [] numTramosParaPasarDeNivel) {

        
        for(int i=0;i<this.numTramos.length;i++){
            if(numTramosParaPasarDeNivel[i]!=this.numTramosFicticios[i])
                return;
        }
      
        for(int i=0;i<this.numTramos.length;i++){
            this.numTramos[i] = this.numTramos[i] - numTramosParaPasarDeNivel[i];
            this.numTramosFicticios[i]=0;
        }
        this.nivel--;
        
    }

}

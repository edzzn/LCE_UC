/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SGBD_LCE_UC.capa.logica;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 *
 * @author JHON
 */
public class Intercalacion {
    /*
     * Esta clase implementa una distribucion incial disInicial
     * determinara el arranque en la intercalacion polifasica
     */
    private final DistribucionTramos distInicial;     
    private final int m;
    private int[] a;
    private int [] deUltima;
    private final String rutaArchivoBase;
    /* El Codigo Para Ordenar (CPO) corresponde al siguiente formato:
     *    
     *              CPO = 1 : Para campo1 --> int
     *              CPO = 2 : Para campo2 --> String
     *              CPO = 3 : Para campo3 --> boolean
     *              CPO = 4 : Para campo4 --> dd/mm/yyyy
     */
    
    public Intercalacion(int numFile, String rutaArchivo, int CPO) throws IOException, FileNotFoundException, ParseException{
        FormatoArchivo.CampoDeOrdenacion = CPO;
        distInicial = new DistribucionTramos(numFile,rutaArchivo);
        m = numFile;
        distInicial.start();
        this.rutaArchivoBase = rutaArchivo;
    }

    public void start() throws FileNotFoundException, IOException{
        
        int[] c = new int[m];
        
        this.deUltima = new int[m];
        
        a = completar(distInicial.getNumTramos());
        
        int[] aC = iniciarNumEjecuciones(a.length);
        
        int[] d = completar(distInicial.getNumTramosFicticios());
        
        int[] cd = new int[m];
        
        int[] actv = new int[m];
        encerar(actv);
        
        /* Unico Archivo de Escritura*/
        CsvWriter ArchivoEntrada;
        
        /* Vector de archivos de Salida*/
        CsvReader[] archivosLectura = new CsvReader[m-1];        
        
        System.out.println("Archivos de Salida:");
            
        for(int i = 0; i < m;i++){
            // para referir a los archivos como "ArchivoT" + c[i]
            //                          ejemplo: ArchivoT1
            c[i]=i+1;
            this.deUltima[i]=i;
            if(i!=m-1){
                
                System.out.println("   Archi_Auxiliar"+String.valueOf(i+1)+".csv");
                
                archivosLectura[i] = 
                    new CsvReader(new FileReader("Archi_Auxiliar"+(i+1)+".csv"));   
            }
        }
        
        int z;
        for(int j = 0; j < distInicial.getNivel();j++){
            //System.out.println("\t\t FASE: "+(j+1));
            // Mezcla de tramos de los archivos c[0]....c[m-2]
            z = a[m-2];             
            //System.out.println("\n\tTotal tramos a Evaluar: "+z+"\n");
            d[m-1] = 0;               // inicializar los tramos ficticios en archivo de salida
            // Se prepara el archivo para escritura
            //System.out.println("Archivo de Entrada:\n   archivoT"+c[m-1]+".csv");
            ArchivoEntrada = new CsvWriter("Archi_Auxiliar"+c[m-1]+".csv");
            
            do{
                
                int k = 0; // Número de ficheros activos
                int tempo=0;
                // evalua solo el margen de archivos de lectura o de salida
                for(int i = 0; i < m-1; i++){
                    boolean kEstaActivo=false;
                    for(int n=0;n<aC[i];n++){
                        if(d[i]>0){ // es un tramo ficticio
                            //System.out.print(" X");
                            d[i]--;
                            d[m-1]++;
                        }else{ // es un tramo real
                          kEstaActivo=true;
                          actv[k]++;
                        }
                    }
                    if(kEstaActivo){
                        cd[k]=c[i];
                        tempo+=actv[k]; // para definir el vector
                        k++;
                    }
                }
                
                
                if(k>0){
                    
                    FormatoArchivo [] listaTemporal = new FormatoArchivo[tempo];
                    
                    tempo=0;
                    
                    // SE AGREGA EN UNA LISTA Y SE ORDENA SOLA (POR AHORA)
                    // ME PARECE QUE ESTO ES CON MEMORIA ESTATICA NO DINAMICA
                    // HAY QUE MEJORAR ESTA PARTE
                   
                    //System.out.println("");
                    for(int i=0;i<k;i++){
                        //System.out.println("archivo en posicion: "+this.deUltima[cd[i]-1]+" archivoT"+cd[i]);
                        for(int n=0;n<actv[i];n++){
                            //System.out.print("-");
                                
                            if(archivosLectura[this.deUltima[cd[i]-1]].readHeaders())
                                listaTemporal[tempo++] = new FormatoArchivo(archivosLectura[this.deUltima[cd[i]-1]].getHeaders());
                                    
                        }
                    //System.out.println("");
                    }
                    
                    // Se Ordena la lista Temporal
                    
                    Arrays.sort(listaTemporal);
                    
                    // AGREGA AL ARCHIVO DE ESCRITURA TODO LO QUE ESTA EN MI LISTA
                    
                    for (FormatoArchivo registroOr : listaTemporal) 
                        //System.out.print(" "+registroOr.getCsv()[FormatoArchivo.CampoDeOrdenacion-1]);
                        agregar_a_Archivo(registroOr,ArchivoEntrada);
                    
                    
                    //System.out.println(" -> representa un tramo");
                }
                
                z--;    // tramo ya             
                a[m-1]++; // se agrega un tramo al de salida
                encerar(actv);
            }while(z!=0);
                    
            // Se han mezclado todos los tramos, el archivo de salida pasa a ser de entrada
            
            //System.out.println("\nSE CIERRA ESCRITURA: archivoT"+c[m-1]+".csv");
            ArchivoEntrada.endRecord();
            ArchivoEntrada.close();
            // <Preparar para lectura F[c[m]]>
        

            //System.out.println("\nSE CIERRA LECTURA: archivoT"+c[m-2]+".csv");
            archivosLectura[this.deUltima[c[m-2]-1]].close();
            //System.out.println("-----");
            
            
            //System.out.println("\nSE ABRE LECTURA: archivoT"+c[m-1]+".csv");
            archivosLectura[this.deUltima[c[m-2]-1]] = new CsvReader(new FileReader("Archi_Auxiliar"+c[m-1]+".csv"));
        
            // Rotar los arhcivos en la tabla de correspondecia c[]
            // calcular los numeros fibonacci, a[i], del siguiente nivel
            // rotarTramos hace los dos procesos 
            
            if(j != distInicial.getNivel()-1)
                rotarTramos(aC,d,c,actv);      
               
                
        }
        
        // SE PASA LOS REGISTROS AL ARCHIVO BASE
        
        ArchivoEntrada = new CsvWriter(this.rutaArchivoBase);

        while(archivosLectura[this.deUltima[c[m-2]-1]].readHeaders()){
            ArchivoEntrada.writeRecord(archivosLectura[this.deUltima[c[m-2]-1]].getHeaders());
        }
        
        
        // Se cierran los archivos empleados
        ArchivoEntrada.endRecord();
        ArchivoEntrada.close();
        
        
        for(int i = 0; i < m-1;i++){
                archivosLectura[i].close();
        }
        
        
        
    }
    
    private int[] completar(int[] vectorIncompleto) {
        
       int vector[] = new int[vectorIncompleto.length+1],i;
       for(i =0;i<vectorIncompleto.length;i++){
           vector[i]=vectorIncompleto[i];
       } 
       vector[i]=0;
       
       return vector;
    }
    
    private void encerar(int[] vector) {
       for(int i = 0;i<vector.length;i++){
           vector[i]=0;
       } 
    }
    
    private int[] iniciarNumEjecuciones(int tamano) {
        int[] vector = new int[tamano];
        int i;
       for(i = 0;i<tamano-1;i++){
           vector[i]=1;
       } 
       vector[i]=0;
       return vector;
    }
 
    private void agregar_a_Archivo(FormatoArchivo registro,CsvWriter archivo) throws IOException {
        archivo.writeRecord(registro.getCsv());
        this.a[m-1]++;
    }

    private void rotarTramos(int[] aC,int[] d,int[] c,int[] actv) {
        int auxA=a[m-2],auxAC=0,auxD=d[m-1],auxC=c[m-1],i;

        for(i =0;i<m-1;i++){
            auxAC=auxAC+aC[i];
            actv[i]=0;
        }
        actv[i]=0;
//        int pequenoCont=m-1;
        for(i = m-1; i>0; i--){
            aC[i]=aC[i-1];
            a[i]=a[i-1]-auxA;
            d[i]=d[i-1];
            c[i]=c[i-1];
//            this.deUltima[c[i]-1]=pequenoCont--;
        }
        
        aC[m-1]=0;
        aC[i]=auxAC;
        a[i]=auxA;
        d[i]=auxD;
        c[i]=auxC;
//        this.deUltima[c[i]-1]=pequenoCont;
        
// SE CREA UN Switch entre el archivo salida y de entrada para los nuevos tramos

    this.deUltima[c[0]-1]=this.deUltima[c[m-1]-1]; // como archivo salida
    this.deUltima[c[m-1]-1]=m-1; // como archivo de entrada

      /*
      this.verVector("c", c);
      this.verVector("u", this.deUltima);
      this.verVector("a", a);
      this.verVector("ac", aC);
      this.verVector("d", d);
      */
    }
    
}
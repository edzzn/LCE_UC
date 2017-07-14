package SGBD_LCE_UC.capa.logica;

public class FormatoArchivo implements Comparable<FormatoArchivo>{
    
    private int campoOrdenarFormatoA = TEST_Ordenamiento.campoOrdenar;     // Uso el Valor de donde se Instancia FormatoArchivo
    public static int numCampos;
    String camposOrd[] = new String[numCampos];
    public static int CampoDeOrdenacion = 1;
    
    public FormatoArchivo(){}
    
    public FormatoArchivo(String[] campos) {
        for (int i = 0; i < numCampos; i++) {
            this.camposOrd[i] = campos[i];
        }
    }
    
    public String[] getCsv(){
        return camposOrd;
    }
    
    @Override
    public int compareTo(FormatoArchivo otroArchivo) {
        //System.out.println("CampoordenarFormatoA: "+ (campoOrdenarFormatoA+1));
        if (FormatoArchivo.CampoDeOrdenacion == (campoOrdenarFormatoA+1)) {
            if(TEST_Ordenamiento.tipoOrdenamiento.equals("asc")){  //  VALIDA STRING asc y des que se ingresa al Ejecutar TEST_Ordenamiento
                //  Acendente
                return this.camposOrd[campoOrdenarFormatoA].compareTo(otroArchivo.camposOrd[campoOrdenarFormatoA]);
            } else {
                //  Descendiente
                return otroArchivo.camposOrd[campoOrdenarFormatoA].compareTo(this.camposOrd[campoOrdenarFormatoA]);
            }
            
        } else {
            return 0;
        }        
    }

    void setCampos(String[] campos) {
        for (int i = 0; i < numCampos; i++) {
            this.camposOrd[i] = campos[i];
        }
    }
    
}

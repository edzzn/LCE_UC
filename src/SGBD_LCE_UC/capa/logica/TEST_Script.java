/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SGBD_LCE_UC.capa.logica;

import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author JHON
 */
public class TEST_Script {
    public static void main(String[] args) {
        iniciar();
    }
    
    public static void iniciar(){
        /*
        *
        * argv[0] dirección del archivo
        * argv[1] campo_clave
        * argv[2] longitudes "1,2,3,4,5"
        *
        * */
        long inicio = System.nanoTime();
        
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                new Random().nextDouble();
            }
        }
        
        String[] cadena = new String[3];
        
        cadena[0] = (String) "C:\\Users\\JHON\\Documents\\archivoPrueba.csv";
        cadena[1] = (String) "codigo";
        cadena[2] = (String) "6,5,5,5,10";
        
        try {
            Script procesarScript = new Script();
            procesarScript.operar(cadena);
        } catch (Exception e) {
            long fin = System.nanoTime();
            System.out.println("El Programa Dura: " + (double) ((fin - inicio) * 1.0e-9) + "  segundos");
        }
    }
    
    
    int tiempoTotal = 5;
    public class Tiempo {
        public Tiempo() {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }
        TimerTask timerTask = new TimerTask() {
            public void run() {
                tiempoTotal++;
            }
        };
    }
    public String getTiempo(int segundos)
    {
        int horas =segundos/3600;
        int minutos =(segundos-(3600*horas))/60;
        int seg = segundos-((horas*3600)+(minutos*60));
        
        return String.format("%02d",horas) + "h:" + String.format("%02d",minutos) + "m:" + String.format("%02d",seg) + "s";
        
    }
}

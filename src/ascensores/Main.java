/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enrique
 */
public class Main {

    private static final String JNDI_NAME = "Ascensores";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {

        //Creamos los pisos en una cola
        Queue<Persona>[] colaPiso = new Queue[21];
        for (int i = 0; i < colaPiso.length; i++) {
            colaPiso[i] = new LinkedList<>();
        }

        //Iniciamos el motor
        Motor m1 = new Motor(colaPiso);

        //Publica la implementacion del modulo vigilante por RMI
        try {
            InterfazVigilante interfaz = new InterfazVigilanteRemota(m1);

            //Se realiza la publicacion
            Registry reg = LocateRegistry.createRegistry(1099);
            Naming.rebind(JNDI_NAME, interfaz);
            System.out.println("El objeto ha quedado registrado");
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Lanza el hilo socket para el Monitor, hilo para mantener escuchando 
        //por si viene algun mensaje por el socket
        HiloSocketControlador hilo = new HiloSocketControlador(m1);
        hilo.start();

        //Crea 60 personas con un id unico y el ascensor que este en funcionamiento en ese momento
        for (int i = 0; i < 60; i++) {
            Persona p = new Persona(i, m1.comprobarAscensor());
            //Se añade cada persona a la planta que corresponda con su origen
            colaPiso[p.getPlanta()].offer(p);
            try {
                Thread.sleep(500 + (int) (Math.random() * 1500));
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            p.start();
        }
    }
}

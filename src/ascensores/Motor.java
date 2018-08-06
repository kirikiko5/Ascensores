/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enrique
 */
public class Motor extends Thread {

    private final Ascensor ascensor;
    private final Ascensor ascensor2;
    private int movimiento;

    public Motor(Queue<Persona>[] colaPiso) {
        this.ascensor = new Ascensor(1, colaPiso);
        this.ascensor2 = new Ascensor(2, colaPiso);
        this.movimiento = 0;
        start();
    }

    @Override
    public void run() {
        estropearAscensor();
        while (movimiento <= 100) {
            long t0 = (new Date()).getTime();                       //Capturamos el tiempo en el que llega aqui
            long t1 = 0;                                            //Para despues poder utilizar el ascensor
            double ran = (5000 + (int) (Math.random() * 2000));     //Durante 5 a 7 segundos tras los que se estropea
            while ((t1 - t0) < ran && movimiento <= 100) {
                System.out.println("Movimiento: " + movimiento);
                if (comprobarAscensor().getEstado() == 'P') {
                    escribir();
                    do {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } while (comprobarAscensor().getEstado() == 'P');
                } else {
                    comprobarAscensor().comprobar2();
                    comprobarAscensor().evolucionar();
                    crearFichero();
                    escribir();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    movimiento++;
                    t1 = (new Date()).getTime();
                }
            }
            estropearAscensor();

        }
        System.out.println("Calculando las personas no atendidas...");

        setFichero("Calculando las personas no atendidas...");
        for (int j = 0; j < 21; j++) {
            while (!comprobarAscensor().getColaPiso(j).isEmpty()) {
                String persona = comprobarAscensor().getColaPiso(j).poll().getIdentificador();
                System.out.print(persona + " ");
                setFichero(persona);
            }

        }
        System.out.println("\nPuede parar el programa");
    }

    //Estrope el ascensor y cambia su estado a estropeado e inicia el otro ascensor
    public synchronized void estropearAscensor() {
        char aux = comprobarAscensor().getEstado();
        if (ascensor.getEstado() != 'E' && ascensor2.getEstado() == 'E') {
            ascensor.setEstado('E');
            ascensor.vaciar();
            ascensor2.setEstado(aux);
        } else if (ascensor.getEstado() == ascensor2.getEstado()) {
            ascensor.setEstado(aux);
            ascensor2.setEstado('E');
            ascensor2.vaciar();
        } else {
            ascensor2.setEstado('E');
            ascensor2.vaciar();
            ascensor.setEstado(aux);
        }
    }

    //Comprueba y devuelve el ascensor que NO este estropeado
    public synchronized Ascensor comprobarAscensor() {
        if (ascensor.getEstado() == 'E') {
            return ascensor2;
        } else {
            return ascensor;
        }
    }

    //Escribe por consola la informacion del sistema
    public synchronized void escribir() {
        System.out.println("Piso:\tAscensor1:\tAscensor2:\tPulsado:\tDestinos del Interior:");
        for (int i = 0; i < 21; i++) {
            if (comprobarAscensor() == ascensor) { //Si el ascensor en funcionamiento es el 1...
                if (ascensor.getPlantaActual() == ascensor.getPisos(i)
                        && ascensor.getPisos(i) == ascensor2.getPlantaActual()) {
                    System.out.println(comprobarAscensor().getPisos(i) + "\t" + ascensor.getEstado()
                            + "#" + ascensor.getPersonas().size() + "\t\t " + ascensor2.getEstado() + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i));
                } else if (ascensor2.getPlantaActual() == ascensor.getPisos(i)) {
                    System.out.println(comprobarAscensor().getPisos(i) + "\t -" + "\t\t " + ascensor2.getEstado() + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i));
                } else if (ascensor.getPlantaActual() == ascensor.getPisos(i)) {
                    System.out.println(comprobarAscensor().getPisos(i) + "\t" + ascensor.getEstado()
                            + "#" + ascensor.getPersonas().size() + "\t\t - " + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i));
                } else {
                    System.out.println(comprobarAscensor().getPisos(i) + "\t -" + "\t\t -" + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i));
                }
            } else //Si esta funcionando el 2...
            if (ascensor2.getPlantaActual() == ascensor2.getPisos(i)
                    && ascensor2.getPisos(i) == ascensor.getPlantaActual()) {
                System.out.println(comprobarAscensor().getPisos(i) + "\t " + ascensor.getEstado()
                        + "\t\t" + ascensor2.getEstado() + "#" + ascensor2.getPersonas().size() + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i));
            } else if (ascensor.getPlantaActual() == ascensor2.getPisos(i)) {
                System.out.println(comprobarAscensor().getPisos(i) + "\t " + ascensor.getEstado() + "\t\t -" + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i));
            } else if (ascensor2.getPlantaActual() == ascensor2.getPisos(i)) {
                System.out.println(comprobarAscensor().getPisos(i) + "\t -" + "\t\t" + ascensor2.getEstado()
                        + "#" + ascensor2.getPersonas().size() + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i));
            } else {
                System.out.println(comprobarAscensor().getPisos(i) + "\t -" + "\t\t -" + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i));
            }
        }
    }

    //Convierte la informacion del metodo escribir() en una cadena para poder crear el fichero
    public synchronized String conseguirCadena() {
        String aux = "";
        for (int i = 0; i < 21; i++) {
            if (comprobarAscensor() == ascensor) {
                if (ascensor.getPlantaActual() == ascensor.getPisos(i)
                        && ascensor.getPisos(i) == ascensor2.getPlantaActual()) {
                    aux = aux + (comprobarAscensor().getPisos(i) + "\t" + ascensor.getEstado()
                            + "#" + ascensor.getPersonas().size() + "\t\t " + ascensor2.getEstado() + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i)) + "\r\n";
                } else if (ascensor2.getPlantaActual() == ascensor.getPisos(i)) {
                    aux = aux + (comprobarAscensor().getPisos(i) + "\t -" + "\t\t " + ascensor2.getEstado() + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i)) + "\r\n";
                } else if (ascensor.getPlantaActual() == ascensor.getPisos(i)) {
                    aux = aux + (comprobarAscensor().getPisos(i) + "\t" + ascensor.getEstado()
                            + "#" + ascensor.getPersonas().size() + "\t\t - " + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i)) + "\r\n";
                } else {
                    aux = aux + (comprobarAscensor().getPisos(i) + "\t -" + "\t\t -" + "\t\t"
                            + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                            + comprobarAscensor().getDestinos(i)) + "\r\n";
                }
            } else if (ascensor2.getPlantaActual() == ascensor2.getPisos(i)
                    && ascensor2.getPisos(i) == ascensor.getPlantaActual()) {
                aux = aux + (comprobarAscensor().getPisos(i) + "\t" + ascensor2.getEstado()
                        + "#" + ascensor.getPersonas().size() + "\t\t " + ascensor2.getEstado() + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i)) + "\r\n";
            } else if (ascensor.getPlantaActual() == ascensor2.getPisos(i)) {
                aux = aux + (comprobarAscensor().getPisos(i) + "\t " + ascensor.getEstado() + "\t\t -" + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i)) + "\r\n";
            } else if (ascensor2.getPlantaActual() == ascensor2.getPisos(i)) {
                aux = aux + (comprobarAscensor().getPisos(i) + "\t -" + "\t\t" + ascensor2.getEstado()
                        + "#" + ascensor2.getPersonas().size() + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i)) + "\r\n";
            } else {
                aux = aux + (comprobarAscensor().getPisos(i) + "\t -" + "\t\t -" + "\t\t"
                        + !comprobarAscensor().getColaPiso(i).isEmpty() + "\t\t\t "
                        + comprobarAscensor().getDestinos(i)) + "\r\n";
            }
        }
        return aux;
    }

    //Crea el txt con la informacion del sistema
    public void crearFichero() {
        try {
            //Creamos el fichero. El segundo parametro es TRUE para poder escribir a continuacion en el mismo fichero
            FileWriter escribir = new FileWriter("evolucionAscensor.txt", true);
            //Escribe la cabecera
            escribir.write("Movimiento: " + this.movimiento + "\r\n" + "Piso:\tAscensor1:\tAscensor2:\tPulsado:\tDestinos del Interior:\n");
            //Escribe la informacion del sistema
            escribir.write(conseguirCadena());
            escribir.flush();
            //Cierra el fichero
            escribir.close();
        } catch (IOException ex) {
            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setFichero(String persona) {
        try {
            try (FileWriter escribir = new FileWriter("evolucionAscensor.txt", true)) {
                escribir.write(persona);
                escribir.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

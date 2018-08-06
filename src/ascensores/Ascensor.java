/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author enrique
 */
public class Ascensor implements Serializable {

    private final int id;
    private char estado;
    private final int[] pisos;
    private final String[] destinos;
    private int plantaActual;
    private Queue<Persona>[] colaPiso = new Queue[21];
    private final ArrayList<Persona> ascensor;

    public Ascensor(int id, Queue<Persona>[] colaPiso) {
        this.id = id;
        this.estado = 'S';
        this.destinos = new String[21];
        this.plantaActual = 0;
        this.colaPiso = colaPiso;
        this.pisos = new int[21];
        this.ascensor = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            this.pisos[i] = i;
        }
    }

    //Vacia el ascensor en la planta en la que se encuentre
    public void vaciar() {
        while (!ascensor.isEmpty()) {
            for (int i = 0; i < ascensor.size(); i++) {
                colaPiso[plantaActual].offer(ascensor.remove(i));
            }
        }
    }

    //Comprueba los destinos de las personas dentro del ascensor
    public String[] comprobar2() {
        for (int i = 0; i < 21; i++) {
            destinos[i] = "-";
            for (int j = 0; j < ascensor.size(); j++) {
                if (pisos[i] == ascensor.get(j).getDestino()) {
                    destinos[i] = destinos[i] + ascensor.get(j).getIdentificador();
                }
            }
        }
        return destinos;
    }

    //Cambia la planta en la que se encuentra el ascensor
    //Se hace de forma sincrona para evitar que accedan los dos ascensores al mismo tiempo
    //Al finalizar se notifica el cambio de planta
    public synchronized void evolucionar() {
        if (estado == 'E' || estado == 'P') {
            plantaActual = plantaActual;
        } else if (estado == 'S' && plantaActual < 20) {
            plantaActual++;
        } else if (estado == 'S' && plantaActual == 20) {
            estado = 'B';
            plantaActual--;
        } else if (estado == 'B' && plantaActual > 0) {
            plantaActual--;
        } else {
            estado = 'S';
            plantaActual++;
        }
        System.out.println("");

        notifyAll();
    }

    //Añade las personas al ascensor si este se encuentra en su misma planta y hay espacio
    public synchronized void entrar(Persona p) throws InterruptedException {
        while (p.getPlanta() != plantaActual || ascensor.size() >= 8) {
            wait();
        }
        ascensor.add(colaPiso[plantaActual].poll());
    }

    //Saca las personas del ascensor si han llegado a su destino
    public synchronized void salir(Persona p) throws InterruptedException {
        while (p.getDestino() != plantaActual) {
            wait();
        }
        ascensor.remove(p);
        notifyAll();
    }

    public ArrayList<Persona> getPersonas() {
        return ascensor;
    }

    public char getEstado() {
        return estado;
    }

    public int getPisos(int i) {
        return pisos[i];
    }

    public String getDestinos(int i) {
        return destinos[i];
    }

    public int getPlantaActual() {
        return plantaActual;
    }

    public Queue<Persona> getColaPiso(int i) {
        return colaPiso[i];
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

}

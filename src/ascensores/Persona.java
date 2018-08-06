/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author enrique
 */
public class Persona extends Thread implements Serializable {

    private String identificador;
    private int planta;
    private int destino;
    private final Ascensor a;

    public Persona(int identificador, Ascensor a) {
        this.identificador = "P" + identificador;
        this.planta = (int) (Math.random() * Integer.MAX_VALUE) % 20;
        do {
            this.destino = (int) (Math.random() * Integer.MAX_VALUE) % 20;
        } while (this.destino == this.planta);
        this.a = a;
    }

    @Override
    public void run() {
        try {
            a.entrar(this);
            a.salir(this);
        } catch (InterruptedException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIdentificador() {
        return identificador;
    }
    
    public int getPlanta() {
        return planta;
    }
    
    public int getDestino() {
        return destino;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author enrique
 */
public class InterfazVigilanteRemota extends UnicastRemoteObject implements InterfazVigilante {

    private Motor motor;

    public InterfazVigilanteRemota(Motor motor) throws RemoteException {
        this.motor = motor;
    }

    //Devuelve el ascensor en funcionamiento
    @Override
    public Ascensor getEstado() throws RemoteException {
        return motor.comprobarAscensor();
    }
}

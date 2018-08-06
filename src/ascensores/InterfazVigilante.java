/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author enrique
 */
public interface InterfazVigilante extends Remote {

    //Llamada al metodo getEstado() para realizar la parada o reanudacion del ascensor
    Ascensor getEstado() throws RemoteException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensores;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author enrique
 */
public class HiloSocketControlador extends Thread {

    private ServerSocket server;
    private static final int PUERTO = 56001;
    private static final String PASSWORD = "rascacielos2016";
    private int conectados = 0;
    private final Motor motor;

    public HiloSocketControlador(Motor motor) {
        this.motor = motor;
    }

    //Este hilo permite la comunicacion con el ModuloControlador
    @Override
    public void run() {
        char estado = 'P';
        try {
            //Iniciamos el socket
            server = new ServerSocket(PUERTO);
            while (conectados == 0) {

                conectados = 1;
                //Aceptamos la conexion
                Socket socket = server.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // Leo password que manda cliente
                String password = dis.readUTF();
                if (password.equals(PASSWORD)) {
                    while (true) {
                        String comando = dis.readUTF();
                        if (comando.equals("0")) { //parar
                            estado = motor.comprobarAscensor().getEstado();
                            motor.comprobarAscensor().setEstado('P');
                        } else if (comando.equals("1")) {  //reanudar
                            motor.comprobarAscensor().setEstado(estado);
                        }
                    }
                } else {
                    conectados = 0;
                    //Cerramos la conexion
                    socket.close();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

package ru.gr0946x.net;

import java.net.Socket;

public class ConnectedClient {
    private Communicator communicator;

    public ConnectedClient(Socket socket){
        communicator = new Communicator(socket);
        communicator.addDataListener(this::parseData);
    }
    public void start(){
        communicator.start();
    }

    public void sendData(String data){
        communicator.sendData(data);
    }

    private void parseData(String data){
        System.out.println("Клиент прислал: " + data);
    }

    public void stop(){
        communicator.stop();
    }
}

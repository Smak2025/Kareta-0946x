package ru.gr0946x.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectedClient {
    private final Communicator communicator;
    private final static List<ConnectedClient> clients = new ArrayList<>();
    private String name = null;

    public ConnectedClient(Socket socket){
        communicator = new Communicator(socket);
        communicator.addDataListener(this::parseData);
        clients.add(this);
    }
    public void start(){
        communicator.start();
        sendData(MessageType.REQUEST + ":" + "Введите имя:");
    }

    public void sendData(String data){
        communicator.sendData(data);
    }

    private void parseData(String data){
        if (name == null){
            if (data.isBlank()){
                sendData(MessageType.ERROR + ":" + "Такое имя не подходит");
                sendData(MessageType.REQUEST + ":" + "Введите имя");
                return;
            }
            if (isInUse(data)){
                sendData(MessageType.ERROR + ":" + "Такое имя уже занято");
                sendData(MessageType.REQUEST + ":" + "Введите имя");
                return;
            }
            name = data;
            sendForAll("Пользователь "+ name + " вошел в чат");
        } else {
            sendForAll(data);
        }

    }

    private void sendForAll(String data){
        for (var client: clients) {
            client.sendData(MessageType.MESSAGE + ":" + name + ":" + data);
        }
    }
    private boolean isInUse(String name){
        for (var client: clients) {
            if (client.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    public void stop(){
        communicator.stop();
    }
}

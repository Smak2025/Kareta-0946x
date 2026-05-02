package ru.gr0946x.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectedClient {
    private final Communicator communicator;
    private final static List<ConnectedClient> clients = new ArrayList<>();
    private String name = null;

    public ConnectedClient(Socket socket) throws IOException {
        communicator = new Communicator(socket);
        communicator.addDataListener(this::parseData);
        synchronized (clients) {
            clients.add(this);
        }
    }
    public void start(){
        communicator.start();
        sendData(MessageType.REQUEST
                + ProtocolConstants.COMMAND_SEPARATOR
                + "Введите имя:");
    }

    public void sendData(String data){
        communicator.sendData(data);
    }

    private void parseData(String data){
        if (name == null){
            if (data.isBlank()){
                sendData(MessageType.ERROR
                        + ProtocolConstants.COMMAND_SEPARATOR
                        + "Такое имя не подходит");
                sendData(MessageType.REQUEST
                        + ProtocolConstants.COMMAND_SEPARATOR
                        + "Введите имя");
                return;
            }
            if (isInUse(data)){
                sendData(MessageType.ERROR
                        + ProtocolConstants.COMMAND_SEPARATOR
                        + "Такое имя уже занято");
                sendData(MessageType.REQUEST
                        + ProtocolConstants.COMMAND_SEPARATOR
                        + "Введите имя");
                return;
            }
            name = data;
            sendForAll("Пользователь "+ name + " вошел в чат");
        } else {
            sendForAll(data);
        }

    }

    private void sendForAll(String data){
        // TODO: ИСПРАВЛЕНИЕ
            for (var client : clients) {
                client.sendData(MessageType.MESSAGE
                        + ProtocolConstants.COMMAND_SEPARATOR
                        + name
                        + ProtocolConstants.AUTHOR_SEPARATOR
                        + data);
            }

    }
    private boolean isInUse(String name){
        synchronized (clients) {
            for (var client : clients) {
                if (client.name.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void stop(){
        communicator.stop();
    }
}

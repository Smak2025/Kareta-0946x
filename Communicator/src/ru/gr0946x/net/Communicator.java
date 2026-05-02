package ru.gr0946x.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Communicator {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean isActive;

    private final List<Consumer<String>> dataListeners = new ArrayList<>();

    public void addDataListener(Consumer<String> c){
        dataListeners.add(c);
    }

    public void removeDataListener(Consumer<String> c){
        dataListeners.remove(c);
    }

    public Communicator(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(
            new InputStreamReader(
                socket.getInputStream(),
                StandardCharsets.UTF_8
            ));
        out = new PrintWriter(
                socket.getOutputStream(),
                true,
                StandardCharsets.UTF_8
        );
    }

    public void start(){
        isActive = true;
        new Thread(()-> {
            try {
                while (isActive) {
                    var data = in.readLine();
                    if (data == null) break;
                    for (var dataListener : dataListeners) {
                        dataListener.accept(data);
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка чтения данных из сети");
                System.err.println(e.getMessage());
            }
            finally {
                stop();
            }
        }).start();
    }

    public void sendData(String data){
        if (isActive && !socket.isClosed())
            out.println(data);
    }

    public void stop(){
        isActive = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии ресурсов");
            System.err.println(e.getMessage());
        }
    }
}

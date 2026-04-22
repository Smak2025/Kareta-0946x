package ru.gr0946x.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public Client(String host, int port){
        try (var socket = new Socket(host, port)){
            var pw = new PrintWriter(socket.getOutputStream());
            pw.println("Привет серверу от клиента!");
            pw.flush();
            var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var string = br.readLine();
            System.out.println("Сервер ответил: " + string);
        } catch (Exception _){
            System.out.println("Ошибка подключения");
        }
    }
}

package ru.gr0946x.ui;

import ru.gr0946x.net.MessageType;
import ru.gr0946x.net.ProtocolConstants;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleUi implements Ui{

    private final List<Consumer<String>> listeners = new ArrayList<>();

    public void start(){
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        new Thread(()-> {
            while (true) {
                var userData = scanner.nextLine();
                for (var listener : listeners) {
                    listener.accept(userData);
                }
            }
        }).start();
    }

    @Override
    public void showInfo(String data, MessageType type) {
        switch (type){
            case MESSAGE -> {
                var message = data.split(
                        ProtocolConstants.AUTHOR_SEPARATOR,
                        2
                );
                System.out.println(message[0]+" написал: ");
                System.out.println(message[1]);
            }
            case ERROR -> {
                System.err.println(data);
            }
            default -> {
                System.out.println(data);
            }
        }
    }

    @Override
    public void addUserDataListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeUserDataListener(Consumer<String> listener) {
        listeners.remove(listener);
    }
}

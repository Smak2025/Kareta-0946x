package ru.gr0946x.ui;

import ru.gr0946x.net.MessageType;

import java.util.function.Consumer;

public interface Ui {
    void showInfo(String data, MessageType type);
    void addUserDataListener(Consumer<String> listener);
    void removeUserDataListener(Consumer<String> listener);
}

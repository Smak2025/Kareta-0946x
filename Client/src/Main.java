import ru.gr0946x.net.Client;

void main() {
    try {
        var c = new Client("localhost", 9460);
        c.start();
        c.sendData("Строка 1");
        c.sendData("Строка 2");
        c.sendData("Строка 3");
        c.stop();
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}
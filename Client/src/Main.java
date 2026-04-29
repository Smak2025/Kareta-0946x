import ru.gr0946x.net.Client;
import ru.gr0946x.ui.ConsoleUi;

void main() {
    try {
        var c = new Client("localhost", 9460);
        var ui = new ConsoleUi();
        ui.addUserDataListener(c::sendData);
        c.addDataListener(ui::showInfo);
        c.start();
        ui.start();
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
}
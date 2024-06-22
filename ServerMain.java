package serverr;

public class ServerMain {
    public static void main(String[] args) {
        Login login = new Login();
    }

    public static void startServer() {
        Server server = new Server();
        server.startServer();
    }
}

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.Duration;

//Stores information about each connected client
public class ClientInfo {
    private String name;
    private Socket socket;
    private LocalDateTime connectTime;
    private PrintWriter out;

    //Client info
    public ClientInfo(String name, Socket socket, PrintWriter out) {
        this.name = name;
        this.socket = socket;
        this.out = out;
        this.connectTime = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public LocalDateTime getConnectTime() {
        return connectTime;
    }

    public PrintWriter getWriter() {
        return out;
    }

    //Returns how long the client has been connected in seconds
    public long getConnectedDurationSeconds() {
        Duration duration = Duration.between(connectTime, LocalDateTime.now());
        return duration.getSeconds();
    }

    public String getClientAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}

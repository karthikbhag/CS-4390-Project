import java.io.*;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class MathServer {

    private static final int PORT = 6789;

    // Shared registry of all currently connected clients, keyed by client name.
    // ConcurrentHashMap allows ClientHandler threads to read/write it safely.

    private static final ConcurrentHashMap<String, ClientInfo> connectedClients = new ConcurrentHashMap<>();

    public static void main (String[] args) {
        System.out.println("Math Server started on port " + PORT);
        System.out.println("Waiting for clients...\n");

        try (ServerSocket welcomeSocket = new ServerSocket(PORT)) {
            while (true) {
                //Block until client connects, then hand off to new thread
                Socket connectionSocket = welcomeSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(connectionSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    //Registry helpers called by ClientHandler

    public static boolean registerClient(ClientInfo info) {
        return connectedClients.putIfAbsent(info.getName(), info) == null;
    }

    public static void unregisterClient(String name) {
        connectedClients.remove(name);
    }

    public static Collection<ClientInfo> getConnectedClients() {
        return connectedClients.values();
    }

    public static void logConnectedClients() {
        if (connectedClients.isEmpty()) {
            System.out.println("[Registry] No clients currently connected.");
            return;
        }
        System.out.println("[Registry] Connected clients (" + connectedClients.size() + "):");
        for (ClientInfo c : connectedClients.values()) {
            System.out.println(" -" + c.getName() + " | address: " + c.getClientAddress() + " | conected: " + c.getConnectedDurationSeconds() + "s");
        }
    }
}
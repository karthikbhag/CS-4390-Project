import java.io.*;
import java.net.Socket;

//Handles communication between server and client
public class ClientHandler implements Runnable {
    private Socket socket;
    private ClientInfo clientInfo;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                //Input stream to receive messages from client
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                //Output stream to send messages to client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            //First message should be JOIN|name
            String firstMessage = in.readLine();

            if (firstMessage == null || !Protocol.getType(firstMessage).equals("JOIN")) {
                out.println("ERROR|First message must be JOIN|name");
                socket.close();
                return;
            }

            //Extract client name from JOIN message
            String[] parts = Protocol.getParts(firstMessage);
            String name = parts.length > 1 ? parts[1].trim() : "";

            if (name.isEmpty()) {
                out.println("ERROR|Name cannot be empty");
                socket.close();
                return;
            }

            //Create client record
            clientInfo = new ClientInfo(name, socket, out);

            if (!MathServer.registerClient(clientInfo)) {
                out.println("ERROR|Name already in use. Choose a different name.");
                socket.close();
                return;
            }

            //Log connection and show clients
            System.out.println("[+]" + clientInfo.getName() + " connected from " + clientInfo.getClientAddress());

            MathServer.logConnectedClients();

            out.println(Protocol.AckMsg(clientInfo.getName()));

            //Handle Client Requests
            String message;
            while ((message = in.readLine()) != null) {
                message = message.trim();

                if (Protocol.getType(message).equals("CLOSE")) {
                    out.println(Protocol.DisconnectMsg(clientInfo.getName()));
                    break;
                }  else if (Protocol.getType(message).equals("CALC")) {
//                    System.out.println("DEBUG server received: " + message);
                    String response = processCalculation(message);
                    String resultValue = response.substring(response.indexOf("|") + 1);
                    System.out.println("[CALC] " + clientInfo.getName() + " -> " + message);
                    System.out.println("[RESULT] " + clientInfo.getName() + " <- " + resultValue);
                    out.println(response);
//                    System.out.println("Sent to " + clientInfo.getName() + ": " + response);
                } else {
                    out.println(Protocol.ErrorMsg("Unknown command"));
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        } finally {
            if (clientInfo != null) {
                MathServer.unregisterClient(clientInfo.getName());
                System.out.println("[-] " + clientInfo.getName() + " disconnected after " + clientInfo.getConnectedDurationSeconds() + " seconds");
                MathServer.logConnectedClients();
            }

            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    //Handles parsing and execution of CALC messages
    private String processCalculation(String message) {
        try {
            String[] parts = message.split("\\|");

            if (parts.length != 4) {
                return "ERROR|Invalid format. Use CALC|OP|num1|num2";
            }

            String operation = parts[1].toUpperCase();
            double num1 = Double.parseDouble(parts[2]);
            double num2 = Double.parseDouble(parts[3]);

            double result;

            //Do requested operation
            switch (operation) {
                case "ADD":
                    result = num1 + num2;
                    break;
                case "SUB":
                    result = num1 - num2;
                    break;
                case "MUL":
                    result = num1 * num2;
                    break;
                case "DIV":
                    if (num2 == 0) {
                        return "ERROR|Division by zero";
                    }
                    result = num1 / num2;
                    break;
                default:
                    return "ERROR|Unsupported operation";
            }

            if (result == (long) result) {
                return Protocol.CalcSolutionMsg(String.valueOf((long) result));
            } else {
                return Protocol.CalcSolutionMsg(String.valueOf(result));
            }
        } catch (NumberFormatException e) {
            return "ERROR|Operands must be numbers";
        }
    }
}

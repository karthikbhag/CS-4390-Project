import java.io.*;
import java.net.*;

public class MathClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6789;

    public static void main(String[] args) throws Exception {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        //Get client name
        System.out.print("Enter your name: ");
        String clientName = console.readLine().trim();

        Socket socket = new Socket(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Connected to server at " + HOST + ":" + PORT);

        //Send JOIN, wait for ACK
        out.println(Protocol.JoinMsg(clientName));
        String ack = in.readLine();
        if (ack == null || !ack.startsWith("ACK|")) {
            System.out.println("Handshake failed: " + ack);
            socket.close();
            return;
        }
        System.out.println("Server: " + ack.substring("ACK|".length()));

        //Interactive request loop
        System.out.println("\nEnter calculations as: ADD 5 3 | SUB 10 4 | MUL 3 3 | DIV 9 3");
        System.out.println("Type QUIT to disconnect.\n");

        String input;
        while (true) {
            System.out.print("> ");
            input = console.readLine();
            if (input == null) break;
            input = input.trim();

            if (input.equalsIgnoreCase("QUIT")) {
                // Graceful disconnect
                out.println(Protocol.close());
                String bye = in.readLine();
                if (bye != null) {
                    System.out.println("Server: " + bye.substring(bye.indexOf("|") + 1));
                }
                break;
            }

            //Parse "OP num1 num2" into CALC|OP|num1|num2
            String[] parts = input.split("\\s+");
            if (parts.length != 3) {
                System.out.println("Usage: ADD 5 3");
                continue;
            }

            String request = Protocol.CalcRequestMsg(parts[0].toUpperCase(), parts[1], parts[2]);
//            System.out.println("DEBUG client sending: " + request);
            out.println(request);

            String response = in.readLine();
            if (response != null) {
                System.out.println("Result: " + response.substring(response.indexOf("|") + 1));
            }
        }

        socket.close();
        System.out.println("Disconnected.");
    }
}

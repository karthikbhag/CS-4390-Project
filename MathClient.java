import java.io.*;
import java.net.*;
import java.util.Random;

public class MathClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6789;
    private static final String[] OPERATIONS = {"ADD", "SUB", "MUL", "DIV"};
    private static final int NUM_REQUESTS = 3;

    public static void main(String[] args) throws Exception {
        // Get client name from args or prompt
        String clientName;
        if (args.length > 0) {
            clientName = args[0];
        } else {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            clientName = console.readLine().trim();
        }

        Socket socket = new Socket(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Connected to server at " + HOST + ":" + PORT);

        // Issue #7 — send JOIN, wait for ACK
        out.println("JOIN|" + clientName);
        String ack = in.readLine();
        if (ack == null || !ack.startsWith("ACK|")) {
            System.out.println("Handshake failed: " + ack);
            socket.close();
            return;
        }
        System.out.println("Server: " + ack.substring("ACK|".length()));

        // Issue #8 — send at least 3 CALC requests at random intervals
        Random rand = new Random();
        for (int i = 0; i < NUM_REQUESTS; i++) {
            String op = OPERATIONS[rand.nextInt(OPERATIONS.length)];
            int num1 = rand.nextInt(100) + 1;
            int num2 = rand.nextInt(100) + 1;

            // Avoid division by zero
            if (op.equals("DIV") && num2 == 0) num2 = 1;

            String request = "CALC|" + op + "|" + num1 + "|" + num2;
            out.println(request);
            System.out.println("Sent: " + request);

            String response = in.readLine();
            System.out.println("Server: " + response);

            // Random delay between requests (1-3 seconds)
            int delay = (rand.nextInt(3) + 1) * 1000;
            Thread.sleep(delay);
        }

        // Issue #9 — graceful disconnect
        out.println("CLOSE");
        String bye = in.readLine();
        if (bye != null) {
            System.out.println("Server: " + bye.substring(bye.indexOf("|") + 1));
        }

        socket.close();
        System.out.println("Disconnected.");
    }
}

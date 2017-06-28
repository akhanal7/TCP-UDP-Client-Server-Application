import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * smsclientTCP class used for implementing client side.
 *
 * @author Ankit Khanal
 * @version 1.0
 */

public class smsclientTCP {

    public static void main(String args[]) {
        smsclientTCP cli = new smsclientTCP();

        if (args.length < 3 || args.length > 3) {
            cli.errorMessage();
        } else {
            cli.run(args[0], args[1], args[2]);
        }
    }


    /**
     * Helper method that reads the message from a text file, sends it to the
     * server and receives a response that includes number of spam words in the
     * message, spam score, and the spam words in the message.
     *
     * @param ipaddress host
     * @param port port number
     * @param message text file that contains the message that is sent to the
     *                server
     */
    private void run(String ipaddress, String port, String message) {
        // Declare all the required variables
        Scanner file = null;
        String ip = ipaddress;
        int portnum = Integer.parseInt(port);
        String servermessage = "";
        String serverOutput;


        try {
            file = new Scanner(new File(message));
        } catch (FileNotFoundException e) {
            System.out.println("Please enter a valid text file.");
        }

        // Read the message file
        while (file.hasNext()) {
            String val = file.next();
            servermessage = servermessage.concat(" " + val);
            // System.out.println(val);
        }


        try {
            Socket sock = new Socket(ip, portnum);
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            InputStreamReader tempval = new InputStreamReader(sock.getInputStream());
            BufferedReader inFromServer = new BufferedReader(tempval);

            out.writeBytes(servermessage + '\n');
            serverOutput = inFromServer.readLine();
            System.out.println("FROM SERVER (# of words, spam score, spam words)" +
                    ": " + serverOutput);

            sock.close();
        } catch (IOException e) {
            System.out.println("Failed to connect to the server. Please try again.\n");
        }
    }

    /**
     * Helper method that prints the error message if the number of arguments
     * passed in the command line is incorrect.
     *
     */
    private void errorMessage() {
        System.out.println("\nUsage: smsclientTCP host port textfile\n");
        System.out.println("example smsclientTCP 127.0.0.1 4000 msg.txt\n\n");
    }
}

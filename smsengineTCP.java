import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * smsclientTCP class used for implementing server side.
 *
 * @author Ankit Khanal
 * @version 1.0
 */

public class smsengineTCP {

    public static void main(String args[]) {
        smsengineTCP ser = new smsengineTCP();

        if (args.length < 2 || args.length > 2) {
            ser.errorMessage();
        } else {
            ser.run(args[0], args[1]);
        }
    }


    /**
     * Helper method that initializes the server, receives the message from the
     * client, calculate the spam score based on the list provided.
     *
     * @param port port number
     * @param message text file that contains the suspicious word
     */
    public void run(String port, String message) {
        // Declare all the required variables
        Scanner file = null;
        File temp = new File(message);
        int portnum = Integer.parseInt(port);
        List<String> suspiciousWord = new ArrayList<>();
        List<String> ssWord = new ArrayList<>();
        String retval = "";
        String clientSentence = "";
        StringBuffer jata = new StringBuffer();

        try {
            file = new Scanner(temp);
        } catch (FileNotFoundException e) {
            System.out.println("Please enter a valid text file.");
        }

        while (file.hasNext()) {
            String val = file.next();
            suspiciousWord.add(val);
            // System.out.println(val);
        }

        try {
            ServerSocket sock = new ServerSocket(portnum);
            System.out.print("######## ");
            System.out.print("Created a socket to use.");
            System.out.println(" ########\n");

            while (true) {
                Socket serversocket = sock.accept();
                InputStreamReader tempval = new InputStreamReader(
                        serversocket.getInputStream());
                BufferedReader inFromClient = new BufferedReader(tempval);
                DataOutputStream outToClient = new DataOutputStream(
                        serversocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println("RECEIVED: " + clientSentence);

                // Split the sentences in single word using \\s+ regex
                //String[] temparr = clientSentence.split("\\s+");
                String[] temparr = clientSentence.replaceAll(
                        "[^a-zA-Z0-9]", " ").split("\\s+");
                float spamScore = 0;

                // Check to see if the spam word is in the client message.
                for (int i = 0; i < suspiciousWord.size(); i++) {
                    if (clientSentence.toLowerCase().indexOf(suspiciousWord.get(i).toLowerCase()) != -1) {
                        ssWord.add(suspiciousWord.get(i));
                        for (int j = 0; j < temparr.length; j++) {
                            if (temparr[j].equals(suspiciousWord.get(i))) {
                                spamScore++;
                            }
                        }
                    }
                }

                for (String tt: ssWord) {
                    jata = jata.append(" " + tt);
                }

                // Calculate the spam score
                float finalScore = (float) (spamScore / (temparr.length - 1));

                if (temparr.length <= 1 || temparr.length > 1000) {
                    retval = "0 -1 ERROR\n";
                } else {
                    retval = suspiciousWord.size() + " " + finalScore + " " + jata + '\n';
                }
                ssWord.clear();
                jata.delete(0, jata.length());

                outToClient.writeBytes(retval);
            }
        } catch (IOException e) {
            System.out.println("Failed to create socket. Please try again.\n");
        }
    }

    /**
     * Helper method that prints the error message if the number of arguments
     * passed in the command line is incorrect.
     *
     */
    private void errorMessage() {
        System.out.println("\nUsage: smsengineTCP port textfile\n");
        System.out.println("example smsengineTCP 4000 msg.txt\n\n");
    }
}

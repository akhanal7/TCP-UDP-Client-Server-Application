import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


/**
 * smsclientUDP class used for implementing server side.
 *
 * @author Ankit Khanal
 * @version 1.0
 */
public class smsengineUDP {
    public static void main(String args[]) {
        smsengineUDP ser = new smsengineUDP();

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
        int portnum = Integer.parseInt(port);
        List<String> suspiciousWord = new ArrayList<>();
        List<String> ssWord = new ArrayList<>();
        String retval = "";
        StringBuffer jata = new StringBuffer();

        try {
            file = new Scanner(new File(message));
        } catch (FileNotFoundException e) {
            System.out.println("Please enter a valid text file.");
        }

        while (file.hasNext()) {
            String val = file.next();
            suspiciousWord.add(val);
            // System.out.println(val);
        }


        try {
            DatagramSocket serverSocket = new DatagramSocket(portnum);
            byte[] sendData;

            System.out.print("######## ");
            System.out.print("Created a socket to use.");
            System.out.println(" ########\n");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                //String[] temparr = sentence.split("\\s+");
                String[] temparr = sentence.replaceAll(
                        "[^a-zA-Z0-9]", " ").split("\\s+");
                InetAddress IPAddress = receivePacket.getAddress();
                int revievedport = receivePacket.getPort();
                float spamScore = 0;

                for (int i = 0; i < suspiciousWord.size(); i++) {
                    if (sentence.toLowerCase().indexOf(suspiciousWord.get(i).toLowerCase()) != -1) {
                        ssWord.add(suspiciousWord.get(i));
                        for (int ii = 0; ii < temparr.length; ii++) {
                            if (temparr[ii].equals(suspiciousWord.get(i))) {
                                spamScore++;
                            }
                        }
                    }
                }

                for (String tt: ssWord) {
                    jata = jata.append(" " + tt);
                }

                float finalScore = (float) (spamScore / (temparr.length - 1));

                if (temparr.length <= 0 || temparr.length > 1000) {
                    retval = "0 -1 ERROR\n";
                } else {
                    retval = suspiciousWord.size() + " " + finalScore + " " + jata + '\n';
                }
                ssWord.clear();
                jata.delete(0, jata.length());

                sendData = retval.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, IPAddress, revievedport);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to the server. Please try again.\n");
        }
    }

    /**
     * Helper method that prints the error message if the number of arguments
     * passed in the command line is incorrect.
     *
     */
    private void errorMessage() {
        System.out.println("\nUsage: smsengineUDP port textfile\n");
        System.out.println("example smsengineUDP 4000 msg.txt\n\n");
    }
}

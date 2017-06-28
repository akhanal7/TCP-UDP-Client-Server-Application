import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * smsclientUDP class used for implementing client side.
 *
 * @author Ankit Khanal
 * @version 1.0
 */
public class smsclientUDP {

    public static void main(String args[]) throws Exception {
        smsclientUDP cli = new smsclientUDP();

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
     * @param ipaddress IP address of the host
     * @param port port number
     * @param message text file that contains the message that is sent to the
     *                server
     */
    private void run(String ipaddress, String port, String message) throws Exception {
        // Declare all the required variables
        Scanner file = null;
        String ip = ipaddress;
        int portnum = Integer.parseInt(port);
        String servermessage = "";
        int count = 0;
        int anotherone = 0;
        byte[] sendData;
        byte[] receiveData = new byte[1024];


        try {
            file = new Scanner(new File(message));
        } catch (FileNotFoundException e) {
            System.out.println("Please enter a valid text file.");
        }

        while (file.hasNext()) {
            String val = file.next();
            servermessage = servermessage.concat(" " + val);
            // System.out.println(val);
        }

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(ip);
        clientSocket.setSoTimeout(2000);
        sendData = servermessage.getBytes();

        while (count < 4 && anotherone == 0) {
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, IPAddress, portnum);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println("FROM SERVER (# of words, spam score, spam " +
                        "words): " + modifiedSentence);
                clientSocket.close();
                anotherone++;
            } catch (SocketTimeoutException e) {
                count++;
                if (count > 3) {
                    System.out.println();
                    System.out.println("The server did not respond. Please " +
                            "try again.\n");
                } else {
                    System.out.println("The server has not answered in the last two seconds.\n" +
                            "Retrying...");
                }
            }
        }
    }

    /**
     * Helper method that prints the error message if the number of arguments
     * passed in the command line is incorrect.
     *
     */
    private void errorMessage() {
        System.out.println("Usage: smsclientUDP host port textfile\n\n");
        System.out.println("example smsclientUDP 127.0.0.1 4000 msg.txt\n\n");
    }
}

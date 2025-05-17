package Serwer;

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class ServerThreads implements Runnable{



    Socket socket;
    public ServerThreads(Socket clientSocket) {
        this.socket = clientSocket;
    }


    public void run() {

        System.out.println("polaczono z: " + socket.getLocalAddress());
        BufferedReader input = null;
        DataOutputStream output = null;
        String threadName = Thread.currentThread().getName();



        try {
            input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException i) {
            System.out.println(threadName + " " + i);
        }

        String input_line = null;

        // logowanie
        boolean zalogowano = false;
        while(!zalogowano) {
        try {
         input_line = input.readLine();
         if (input_line.equals("0005")) {
             String nr_konta = input_line;
             System.out.println("Zalogowano uzytkownika na konto: " + nr_konta);
             output.writeBytes("tak" + "\r");
             output.flush();
             zalogowano = true;
             output.writeBytes("Zalogowano" + "\r");
         }
         else {
             output.writeBytes("nieistniejacy numer konta" + "\r");
             output.flush();
         }
         }catch (IOException i) {
            System.out.println(i);
        }}

        // petla glowna
        while (true) {
            try {
                input_line = input.readLine();
                System.out.print(threadName + ": ");


                //warunki wylacznia threada
                if (input_line.equals("exit") || input_line == null) {
                    System.out.println("rozlaczono z: " + socket.getLocalAddress());
                    socket.close();
                    return;
                }
                //
                 // zeby poprawnie wyslac linie tekstu do klienta to na koncu musi byc \r koniecznie
                if (input_line.equals("123")) {
                    output.writeBytes("przyjeto komende 1" + "\r");
                    System.out.println("wykonac komende 1?");
                }
                else {
                    output.writeBytes("nieznana komenda" + "\r");
                    System.out.println("otrzymano nieznana komende: " + input_line + "\r");
                }
            }

            catch (IOException i) {
                System.out.println(i);
                return;
            }
        }




    }


}

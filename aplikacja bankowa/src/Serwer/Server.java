package Serwer;

import java.net.*;
import java.io.*;

public class Server {

    public Server(int port) {


        Socket s = null;
        ServerSocket s2 = null;


        try {
            s2 = new ServerSocket(port);
        } catch (IOException i) {
            System.out.println(i);
        }
        System.out.println("serwer wlaczony na porcie: " + port);


        while (true) {
            try {
                s = s2.accept();
            } catch (IOException i) {
                System.out.println(i);
            }
            new Thread(new ServerThreads(s)).start();
        }


    }


public static void main(String[] args) {


        Server serwer1 = new Server(8512);


    }}


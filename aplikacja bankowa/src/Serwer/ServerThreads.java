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
        String nr_konta = "";
        while(!zalogowano) {
        try {
         input_line = input.readLine();
         if (Dane.sprawdzCzyKontoIstnieje(input_line)) {
             nr_konta = input_line;
             output.writeBytes("Prosze podac haslo do konta nr " + input_line + "\r");
             output.flush();
             input_line = input.readLine();
             if (Dane.znajdz_dane_konta(nr_konta)[5].equals(input_line)) {
                 output.writeBytes("tak" + "\r");
                 output.flush();
                 System.out.println(threadName + ": " + "zalogowano uzytkownika: " + nr_konta);
                 zalogowano = true;
             }
             else {
                 output.writeBytes("Podane haslo nie jest poprawne." + "\r");
                 output.flush();
             }
         }
         else {
             output.writeBytes("Nieistniejacy numer konta." + "\r");
             output.flush();
         }
         }catch (IOException i) {
            System.out.println(i);
            return;
        }}

        // petla glowna
        while (true) {
            try {
                input_line = input.readLine();


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
                else if (input_line.equals("stan konta")) {
                    output.writeBytes("Stan konta: " + Dane.znajdz_dane_konta(nr_konta)[4] + "\r");
                    output.flush();
                    System.out.println(threadName + ": wyswietlono stan konta");
                }
                else if (input_line.equals("przelew")) {
                    output.writeBytes("Na jakie konto chcialbys przelac pieniadze?" + "\r");
                    output.flush();
                    System.out.println(threadName + ": zapytano o konto do wykonania przelewu");
                    input_line = input.readLine();
                    if (Dane.sprawdzCzyKontoIstnieje(input_line)) {
                        String kontoOdbiorcy = input_line;
                        output.writeBytes("Podaj kwote przelewu: " + "\r");
                        output.flush();
                        input_line = input.readLine();
                        try {
                            int amount = Integer.parseInt(input_line);
                            if (amount > 0) {
                                if (amount <= Integer.parseInt(Dane.znajdz_dane_konta(nr_konta)[4]) ) {
                                    int currentamountuser = Integer.parseInt(Dane.znajdz_dane_konta(nr_konta)[4]);
                                    int currentamount = Integer.parseInt(Dane.znajdz_dane_konta(kontoOdbiorcy)[4]);
                                    Dane.nadpisz_dane_konta(nr_konta, 4, String.valueOf(currentamountuser - amount));
                                    Dane.nadpisz_dane_konta(kontoOdbiorcy, 4, String.valueOf(currentamount + amount));
                                    System.out.println(threadName + ": wykonano przelew z konta " + nr_konta + " na konto " + kontoOdbiorcy + " o wartosci " + amount);
                                    output.writeBytes("Poprawnie wykonano przelew o wartosci " + amount + " na konto " + kontoOdbiorcy + "\r");
                                    output.flush();
                                }
                                else {
                                    output.writeBytes("Nie posiadasz takiej kwoty");
                                    output.flush();
                                    System.out.println(threadName + ": uzytkownik podal za duza kwote");
                                }
                            } else {
                                output.writeBytes("Kwota transakcji musi byc dodatnia, przelew nie zostal wykonany" + "\r");
                                output.flush();
                                System.out.println(threadName + ": uzytkownik podal niepoprawna kwote przelewu");
                            }
                        } catch (NumberFormatException g) {
                            System.out.println(threadName + ": " + g);
                            return;
                        }
                    }
                    else {
                        output.writeBytes("Podany nr konta odbiorcy nie jest poprawny.");
                        output.flush();
                        System.out.println(threadName + ": uzytkownik podal nieprawidlowy nr konta");
                    }

                }
                else if (input_line.equals("wplata")) {
                    output.writeBytes("Ile chcialbys wplacic?: " + "\r");
                    output.flush();
                    System.out.println(threadName + ": wyslano zapytanie zwiazane z wplata srodkow");
                    input_line = input.readLine();
                    int amount = Integer.parseInt(input_line);
                    if ( amount > 0 ) {
                        int currentamount = Integer.parseInt(Dane.znajdz_dane_konta(nr_konta)[4]);
                        int newamount = currentamount+amount;
                        Dane.nadpisz_dane_konta(nr_konta,4,String.valueOf(newamount));
                        System.out.println(threadName + ": wykonano wplate " + amount + "zl na konto " + nr_konta);
                        output.writeBytes("Poprawnie wplacono " + amount + "zl na konto" + "\r");
                        output.flush();
                    }
                    else {
                        System.out.println(threadName+ ": uzytkownik podal nieprawidlowa ilosc do wplaty");
                        output.writeBytes("Nie mozna wykonac wplaty o ujemnej lub zerowej wartosci" + "\r");
                        output.flush();
                    }
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

import java.io.*;
import java.net.*;

public class Client {

    Socket s = null;
    BufferedReader input = null;
    BufferedReader server_input = null;
    DataOutputStream output = null;


    public Client(String adres, int port) {
        try {
            s = new Socket(adres, port);
        } catch (UnknownHostException u) {
            System.out.println("niepoprawny host " + u);
            return;
        } catch (IOException i) {
            System.out.println(i);
            return;
        }
        System.out.println("polaczono z: " + s);


        String input_string = null;
        String server_string = null;
        try {
            input = new BufferedReader(
                    new InputStreamReader(System.in));
            output = new DataOutputStream(s.getOutputStream());
            server_input = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));
        } catch (IOException i) {
            System.out.println(i);
        }
        // logowanie
        System.out.println("Podaj numer konta na ktore chcesz sie zalogowac: ");
        boolean zalogowano = false;
        while(!zalogowano) {
            try {
            input_string = input.readLine();
            String nrkonta = input_string;
            output.writeBytes(input_string + "\r");
            output.flush();
            server_string = server_input.readLine();
            if (server_string.equals("tak")) {
                zalogowano = true;
                System.out.println("Poprawnie zalogowano na podane konto ");
            }
            else {System.out.println(server_string);
                if (server_string.equals("Podane haslo nie jest poprawne")) {
                System.out.println("Podaj numer konta na ktore chcesz sie zalogowac: ");
            }}
        } catch (IOException i) {
            System.out.println(i);
        }
        }
        // petla glowna
        System.out.println("Dostepne komendy: " + "\n" + "\"stan konta\" - aby sprawdzic stan konta"
        + "\n" + "\"przelew\" - aby wykonac przelew srodkow na inne konto"
        + "\n" + "\"wplata\" - aby dokonac wplaty srodkow na konto"
        + "\n" + "\"wyplata\" - aby wyplacic srodki z konta"
        + "\n" + "\"wyswietl dane\" - aby wyswietlic dane konta" );
        while (true) {
            try {

                input_string = input.readLine();
                if (input_string != null) {
                    System.out.println("Wyslano do serwera komende: " + "\"" + input_string + "\"");
                    output.writeBytes(input_string + "\r");
                    output.flush();
                }
                server_string = server_input.readLine();
                System.out.println(server_string);
            }
            catch (IOException i) {
                System.out.println(i);
                return;
            }
            if (input_string.equals("exit")) {
                try {
                    input.close();
                    server_input.close();
                    s.close();
                }
                catch (IOException i) {
                    System.out.println(i);
                    return;
                }
            }
        }



    }

    public static void main(String[] args) {

        Client klient1 = new Client("localhost",8512);
    }








}

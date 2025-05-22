import java.io.*;
import java.net.*;

public class Admin {

    Socket s = null;
    BufferedReader input = null;
    BufferedReader server_input = null;
    DataOutputStream output = null;


    public Admin(String adres, int port) {
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
        boolean zalogowano = false;
        try {
            while (!zalogowano) {

                output.writeBytes("admin" + "\r");
                output.flush();

                server_string = server_input.readLine();
                System.out.println(server_string);
                input_string = input.readLine();
                output.writeBytes(input_string + "\r");
                output.flush();
                if (server_string.equals("zalogowano na konto administratorskie")) {
                    server_string = server_input.readLine();
                    System.out.println(server_string);
                    zalogowano = true;
                }

            }

        }
        catch (IOException i) {
            System.out.println(i);
        }

        // petla glowna
        System.out.println("Dostepne komendy: " + "\n" + "\"zmien dane\" - aby zmienic wybrane dane konta");
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

        Admin admin1 = new Admin("localhost",8512);
    }








}

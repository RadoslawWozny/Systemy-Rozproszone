package Serwer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dane {

    public static String[] znajdz_dane_konta(String nr_konta) throws IOException {


        File dane = new File("src/Serwer/DaneUzytkownikow.csv");
        Scanner sc = new Scanner(dane);
        sc.useDelimiter(",");
        String[] danekonta = new String[6];
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(",");

            if (parts.length > 0 && parts[0].equals(nr_konta)) {
                danekonta = parts;
                break;
            }
        }
        sc.close();
    return danekonta;
    }

    public static void nadpisz_dane_konta(String numer_konta, int dana_do_nadpisania, String nowa_wartosc) throws IOException {

        File dane = new File("src/Serwer/DaneUzytkownikow.csv");
        List<String> wszystkieLinie = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(dane));
        String linia;

        while ((linia = reader.readLine()) != null) {
            String[] pola = linia.split(",");
            if (pola.length > 0 && pola[0].equals(numer_konta)) {
                if (pola.length > dana_do_nadpisania) {
                    pola[dana_do_nadpisania] = nowa_wartosc;
                }
                linia = String.join(",", pola);
            }
            wszystkieLinie.add(linia);
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(dane, false));
        for (String l : wszystkieLinie) {
            writer.write(l);
            writer.newLine();
        }
        writer.close();
    }

    public static boolean sprawdzCzyKontoIstnieje(String nrkonta) throws FileNotFoundException {
        boolean istnieje = false;
        File dane = new File("src/Serwer/DaneUzytkownikow.csv");
        Scanner sc = new Scanner(dane);
        sc.useDelimiter(",");

        while (sc.hasNextLine()) {
            if (nrkonta.equals(sc.next())) {
                istnieje = true;
            } else sc.nextLine();
        }

        return istnieje;

    }








}

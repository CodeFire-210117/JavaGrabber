package ua.com.codefire;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public static void main(String[] args) {
        StringBuilder buffer = new StringBuilder();

        try {
            URL url = new URL(API_URL);

            // GET PAGE CONTENT
            Scanner contentScan = new Scanner(url.openStream());

            while (contentScan.hasNextLine()) {
                String line = contentScan.nextLine();
                buffer.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream("currency.json")) {
            fos.write(buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonResponse = buffer.toString();
//        System.out.println(jsonResponse);

        try {
            // PARSE JSON ARRAY
            JsonArray currencies = (JsonArray) Jsoner.deserialize(jsonResponse);

            for (int i = 0; i < currencies.size(); i++) {
                // GET JSON OBJECT FROM ARRAY
                JsonObject currency = (JsonObject) currencies.get(i);

                // GET DATA FROM OBJECT
                String ccy = currency.getString("ccy");
                String bcy = currency.getString("base_ccy");
                double buy = currency.getDouble("buy");
                double sale = currency.getDouble("sale");

                // PRINT DATA
                System.out.printf("%s %s B: %6.3f S: %6.3f [Q:1]\n", ccy, bcy, buy, sale);
            }
        } catch (DeserializationException e) {
            e.printStackTrace();
        }
    }
}

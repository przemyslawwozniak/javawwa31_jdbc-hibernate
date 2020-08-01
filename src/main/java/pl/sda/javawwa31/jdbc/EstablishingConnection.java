package pl.sda.javawwa31.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.TimeZone;

public class EstablishingConnection {

    public static void main(String[] args) {
        Properties configuration = new Properties();

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream resourceFileInputStream = classloader.getResourceAsStream("config.properties");

            configuration.load(resourceFileInputStream);
        }
        catch(IOException ioe) {
            System.err.println("Nie mozna otworzyc pliku konfiguracyjnego: " + ioe);
        }

        final String query = "select customerName, country from customers";
        //od tej linii obiekt klasy Properties zawiera odczytana konfiguracje
        try(Connection connection = DriverManager.getConnection(
                //configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                configuration.get("db.url").toString(),
                configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString());

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
        ) {
            System.out.println("Nawiazano polaczenie z baza danych.");

            System.out.println("Oto nasi klienci z podzialem na kraje");
            System.out.println("Client name | Country");

            while(result.next()) {
                System.out.println(result.getString("customerName") + " | " + result.getString("country"));
            }
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }
    }

}

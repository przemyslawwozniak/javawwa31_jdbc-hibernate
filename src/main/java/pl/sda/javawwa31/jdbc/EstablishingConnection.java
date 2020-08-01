package pl.sda.javawwa31.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

        //od tej linii obiekt klasy Properties zawiera odczytana konfiguracje
        try(Connection connection = DriverManager.getConnection(
                //configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                configuration.get("db.url").toString(),
                configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString()
        )) {
            System.out.println("Nawiazano polaczenie z baza danych.");
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }
    }

}

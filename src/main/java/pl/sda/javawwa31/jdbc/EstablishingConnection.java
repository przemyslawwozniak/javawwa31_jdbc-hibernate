package pl.sda.javawwa31.jdbc;

import pl.sda.javawwa31.jdbc.domain.Payment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

            //Statement statement = connection.createStatement();
            //ResultSet result = statement.executeQuery(query);
        ) {
            System.out.println("Nawiazano polaczenie z baza danych.");

            /*System.out.println("Oto nasi klienci z podzialem na kraje");
            System.out.println("Client name | Country");

            while(result.next()) {
                System.out.println(result.getString("customerName") + " | " + result.getString("country"));
            }*/

            //printPaymentsForYearAndAmountAbove(2004, 10000.0, connection);

            List<Payment> payments = retrievePaymentsForYearAndAmountAbove(2004, 10000.0, connection);
            System.out.println("10ta pozycja na liscie to: " + payments.get(9));
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }
    }

    public static void printPaymentsForYearAndAmountAbove(final int year, final double minAmount, final Connection connection) {
        try {
            ResultSet resultSet = getResultSetForPaymentForYearAndAmountAbove(year, minAmount, connection);
            System.out.printf("Oto platnosci za rok %d w kwocie przekraczajacej %f\n", year, minAmount);
            while(resultSet.next()) {
                System.out.println("Customer number | Check number | Payment date | Amount");
                System.out.printf("%s | %s | %s | %f\n",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDouble(4));
            }
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

    public static List<Payment> retrievePaymentsForYearAndAmountAbove(final int year, final double minAmount, final Connection connection) {
        List<Payment> payments = new ArrayList<>();

        try {
            ResultSet resultSet = getResultSetForPaymentForYearAndAmountAbove(year, minAmount, connection);

            while(resultSet.next()) {
                payments.add(new Payment(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDouble(4)));
            }

        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }

        return payments;
    }

    public static ResultSet getResultSetForPaymentForYearAndAmountAbove(final int year, final double minAmount, final Connection connection) {
        String parametrizedQuery = "select * from payments WHERE paymentDate BETWEEN ? AND ? AND amount > ?";

        try(PreparedStatement prepStmt = connection.prepareStatement(parametrizedQuery)) {
            prepStmt.setDate(1, Date.valueOf(LocalDate.of(year, 1, 1)));
            prepStmt.setDate(2, Date.valueOf(LocalDate.of(year, 12, 31)));
            prepStmt.setDouble(3, minAmount);

            return prepStmt.executeQuery();
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }

        return null;
    }

}

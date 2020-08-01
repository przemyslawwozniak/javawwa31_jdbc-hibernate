package pl.sda.javawwa31.jdbc;

import pl.sda.javawwa31.jdbc.domain.Office;
import pl.sda.javawwa31.jdbc.domain.Payment;
import pl.sda.javawwa31.jdbc.domain.ProductLine;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

            /*List<Payment> payments = retrievePaymentsForYearAndAmountAbove(2004, 10000.0, connection);
            System.out.println("10ta pozycja na liscie to: " + payments.get(9));*/

            System.out.println("Zysk min 75%:");
            printProductsWithinProductLineForReturnValue(0.75, ProductLine.CLASSIC_CARS, connection);
            System.out.println("Zysk min 100%:");
            printProductsWithinProductLineForReturnValue(1.0, ProductLine.CLASSIC_CARS, connection);
            System.out.println("Zysk min 150%:");
            printProductsWithinProductLineForReturnValue(1.5, ProductLine.CLASSIC_CARS, connection);

            /*Payment payment = new Payment("114", "yumyum7", LocalDate.now(), 10000.0);
            insert(payment, connection);*/

            //deleteOrder(10100, connection);

            /*Payment payment = new Payment("114", "yumyum7", LocalDate.now(), 500.0);
            updatePayment(payment, connection);*/

            /*Office warsawOffice = Office.builder()
                                    .officeCode("8")
                                    .city("Warsaw")
                                    .phone("+48 22 777 55 22")
                                    .addressLine1("Pelczynskiego 14D/100")
                                    .country("Poland")
                                    .postalCode("01-471")
                                    .teritory("EMEA")
                                    .build();

            insert(warsawOffice, connection);*/
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }
    }

    public static void printPaymentsForYearAndAmountAbove(final int year, final double minAmount, final Connection connection) {
        String parametrizedQuery = "select * from payments WHERE paymentDate BETWEEN ? AND ? AND amount > ?";

        try(PreparedStatement prepStmt = connection.prepareStatement(parametrizedQuery)) {
            prepStmt.setDate(1, Date.valueOf(LocalDate.of(year, 1, 1)));
            prepStmt.setDate(2, Date.valueOf(LocalDate.of(year, 12, 31)));
            prepStmt.setDouble(3, minAmount);

            ResultSet resultSet = prepStmt.executeQuery();
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
        String parametrizedQuery = "select * from payments WHERE paymentDate BETWEEN ? AND ? AND amount > ?";
        List<Payment> payments = new ArrayList<>();

        try(PreparedStatement prepStmt = connection.prepareStatement(parametrizedQuery)) {
            prepStmt.setDate(1, Date.valueOf(LocalDate.of(year, 1, 1)));
            prepStmt.setDate(2, Date.valueOf(LocalDate.of(year, 12, 31)));
            prepStmt.setDouble(3, minAmount);

            ResultSet resultSet = prepStmt.executeQuery();

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

    /*
    Cwiczenie #1: Bazujac na metodzie printPaymentsForYearAndAmountAbove napisz wlasna metode, ktora z tabeli 'products' wypisze produkty, ktore generuja zarobek okreslony jako
        (msrp-buyPrice)/buyprice>0.75
    i przynaleza do zadanej kategorii productLine = Motorcycles, Classic Cars itd.)
     */
    public static void printProductsWithinProductLineForReturnValue(final double retVal, final ProductLine productLine, final Connection connection) {
        String parametrizedQuery = "select * from products where (MSRP-buyPrice)/buyPrice > ? AND productLine=?;";

        try(PreparedStatement prepStmt = connection.prepareStatement(parametrizedQuery)) {
            prepStmt.setDouble(1, retVal);
            prepStmt.setString(2, productLine.toString());  //1 odpowiada kolumnie productLine

            ResultSet resultSet = prepStmt.executeQuery();
            System.out.printf("Oto produkty, ktore przynosza zarobek co najmniej %f i naleza do kategorii %s\n",
                    retVal, productLine.toString());
            while(resultSet.next()) {
                System.out.println("Product name | Product Vendor | MSRP");
                System.out.printf("%s | %s | %f\n",
                        resultSet.getString("productName"),
                        resultSet.getString("productVendor"),
                        resultSet.getDouble("MSRP"));
            }
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

    //inserting into database example
    public static void insert(final Payment payment, final Connection connection) {
        final String query = "insert into payments values(?,?,?,?)";

        try(PreparedStatement prepStmt = connection.prepareStatement(query)) {
            prepStmt.setString(1, payment.getCustomerNumber());
            prepStmt.setString(2, payment.getCheckNo());
            prepStmt.setDate(3, java.sql.Date.valueOf(payment.getDate()));
            prepStmt.setDouble(4, payment.getAmount());

            final int rowsAffected = prepStmt.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " rows.");
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

    public static void deleteOrder(final int orderNumber, final Connection connection) {
        final String query = "delete from orders where orderNumber=?";

        try(PreparedStatement prepStmt = connection.prepareStatement(query)) {
            prepStmt.setInt(1, orderNumber);

            final int rowsAffected = prepStmt.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " rows.");
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

    //lepiej rozbic na metody aktualizujace poszczegolne pola - antypattern
    //problem: i tak nie mozna przekazac payment.date == null, poniewaz konstruktor jest nierozwiazywalny
    public static void updatePayment(final Payment payment, final Connection connection) {
        String query = "";
        if(payment.getAmount() > 0.0 && payment.getDate() != null)
            query = "update payments set amount=?,paymentDate=? where customerNumber=? AND checkNumber=?";
        else if(payment.getAmount() > 0.0)
            query = "update payments set amount=? where customerNumber=? AND checkNumber=?";
        else if(payment.getDate() != null)
            query = "update payments set paymentDate=? where customerNumber=? AND checkNumber=?";

        try(PreparedStatement prepStmt = connection.prepareStatement(query)) {
            if(payment.getAmount() > 0.0 && payment.getDate() != null) {
                prepStmt.setDouble(1, payment.getAmount());
                prepStmt.setDate(2, java.sql.Date.valueOf(payment.getDate()));
                prepStmt.setString(3, payment.getCustomerNumber());
                prepStmt.setString(4, payment.getCheckNo());
            }
            else if(payment.getAmount() > 0.0) {
                prepStmt.setDouble(1, payment.getAmount());
                prepStmt.setString(2, payment.getCustomerNumber());
                prepStmt.setString(3, payment.getCheckNo());
            }
            else if(payment.getDate() != null) {
                prepStmt.setDate(1, java.sql.Date.valueOf(payment.getDate()));
                prepStmt.setString(2, payment.getCustomerNumber());
                prepStmt.setString(3, payment.getCheckNo());
            }

            final int rowsAffected = prepStmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s).");
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

    //insert into offices values(8, 'Warsaw', '22456789', 'ulica testowa', 'ul test 2', 'Maz', 'Poland', 22222, 'POLAND');
    public static void insert(final Office office, final Connection connection) {
        final String query = "insert into offices values(?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement prepStmt = connection.prepareStatement(query)) {
            prepStmt.setString(1, office.getOfficeCode());
            prepStmt.setString(2, office.getCity());
            prepStmt.setString(3, office.getPhone());
            prepStmt.setString(4, office.getAddressLine1());
            prepStmt.setString(5, office.getAddressLine2());
            prepStmt.setString(6, office.getState());
            prepStmt.setString(7, office.getCountry());
            prepStmt.setString(9, office.getTeritory());
            prepStmt.setString(8, office.getPostalCode());

            final int rowsAffected = prepStmt.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " rows.");
        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych: " + sex);
        }
    }

}

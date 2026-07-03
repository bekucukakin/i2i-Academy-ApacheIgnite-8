package com.i2iacademy.ignite;

import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.sql.IgniteSql;
import org.apache.ignite.sql.ResultSet;
import org.apache.ignite.sql.SqlRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        try (IgniteClient client = IgniteClient.builder()
                .addresses("127.0.0.1:10800")
                .build()) {

            IgniteSql sql = client.sql();


            sql.execute(null,
                    "CREATE TABLE IF NOT EXISTS Subscriber (" +
                            "customerId VARCHAR PRIMARY KEY, " +
                            "dataUsage DOUBLE, " +
                            "smsUsage INT, " +
                            "callUsage INT)");


            sql.execute(null, "DELETE FROM Subscriber");


            List<Subscriber> subscribers = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                subscribers.add(new Subscriber("CUST-00" + i, 0.0, 0, 0));
            }

            for (Subscriber s : subscribers) {
                sql.execute(null,
                        "INSERT INTO Subscriber (customerId, dataUsage, smsUsage, callUsage) VALUES (?, ?, ?, ?)",
                        s.getCustomerId(), s.getDataUsage(), s.getSmsUsage(), s.getCallUsage());
            }

            System.out.println("5 subscribers inserted (initial state).");


            List<Subscriber> loadedSubscribers = new ArrayList<>();
            ResultSet<SqlRow> loadedRows = sql.execute(null,
                    "SELECT customerId, dataUsage, smsUsage, callUsage FROM Subscriber ORDER BY customerId");
            while (loadedRows.hasNext()) {
                SqlRow row = loadedRows.next();
                loadedSubscribers.add(new Subscriber(
                        row.stringValue("customerId"),
                        row.doubleValue("dataUsage"),
                        row.intValue("smsUsage"),
                        row.intValue("callUsage")));
            }

            Random random = new Random();
            for (Subscriber s : loadedSubscribers) {
                double newData = s.getDataUsage() + random.nextDouble() * 500;
                int newSms = s.getSmsUsage() + random.nextInt(50);
                int newCall = s.getCallUsage() + random.nextInt(120);

                sql.execute(null,
                        "UPDATE Subscriber SET dataUsage = ?, smsUsage = ?, callUsage = ? WHERE customerId = ?",
                        newData, newSms, newCall, s.getCustomerId());
            }

            System.out.println("Usage simulation complete, records updated.\n");


            System.out.println("=== Final Subscriber State ===");
            ResultSet<SqlRow> result = sql.execute(null, "SELECT * FROM Subscriber ORDER BY customerId");
            while (result.hasNext()) {
                SqlRow row = result.next();
                System.out.printf("CustomerId: %s | Data: %.2f MB | SMS: %d | Call: %d min%n",
                        row.stringValue("customerId"),
                        row.doubleValue("dataUsage"),
                        row.intValue("smsUsage"),
                        row.intValue("callUsage"));
            }
        }

        System.out.println("\nProgram finished.");
        System.exit(0);
    }
}
package SQL;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SqlHelperCash {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
        );
    }

    @SneakyThrows
    public static void cleanDefaultData() {
        val deletePay = "DELETE FROM payment_entity ";
        val deleteOrder = "DELETE FROM order_entity";
        try (
                val conn = connect();
                val dataStmt = conn.createStatement();
        ) {
            dataStmt.executeUpdate(deletePay);
            dataStmt.executeUpdate(deleteOrder);
        }
    }

    @SneakyThrows
    public static String getCardIdPayment() {
        var cardIdPay = "SELECT transaction_id FROM payment_entity WHERE status = 'APPROVED'";
        Thread.sleep(500);
        try (
                var con = connect();
                var dataStmt = con.createStatement();
        ) {
            try (var rs = dataStmt.executeQuery(cardIdPay)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardIdOrder() {
        var cardIdOrder = "SELECT payment_id FROM order_entity";
        try (
                var con = connect();
                var dataStmt = con.createStatement();
                var cardsStmt = con.prepareStatement(cardIdOrder);
        ) {
            try (var rs = dataStmt.executeQuery(cardIdOrder)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardStatusApproved() {
        var cardStatusApproved = "SELECT status FROM payment_entity";
        try (
                var con = connect();
                var dataStmt = con.createStatement();
                var cardsStmt = con.prepareStatement(cardStatusApproved);
        ) {
            try (var rs = dataStmt.executeQuery(cardStatusApproved)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

    @SneakyThrows
    public static String getCardStatusDeclined() {
        var cardStatusDeclined = "SELECT status FROM payment_entity";
        try (
                var con = connect();
                var dataStmt = con.createStatement();
                var cardsStmt = con.prepareStatement(cardStatusDeclined);
        ) {
            try (var rs = dataStmt.executeQuery(cardStatusDeclined)) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Error";
    }

}





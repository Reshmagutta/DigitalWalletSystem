package service;



import java.sql.*;
import db.DBConnection;

public class UserService {

    // ---------------- REGISTER ----------------
    public void register(String name, String email, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("User Registered Successfully!");
        } catch (Exception e) {
            System.out.println("Registration Failed!");
            e.printStackTrace();
        }
    }

    // ---------------- LOGIN ----------------
    public int login(String email, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT user_id FROM users WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ---------------- CHECK BALANCE ----------------
    public double checkBalance(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT balance FROM users WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------------- DEPOSIT ----------------
    public void deposit(int userId, double amount) {
        try (Connection con = DBConnection.getConnection()) {

            String updateBalance = "UPDATE users SET balance = balance + ? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(updateBalance);
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();

            String insertTransaction = "INSERT INTO transactions(sender_id,receiver_id,amount,type) VALUES(?,?,?,?)";
            PreparedStatement ps2 = con.prepareStatement(insertTransaction);
            ps2.setInt(1, userId);
            ps2.setInt(2, userId);
            ps2.setDouble(3, amount);
            ps2.setString(4, "DEPOSIT");
            ps2.executeUpdate();

            System.out.println("Amount Deposited Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- WITHDRAW ----------------
    public void withdraw(int userId, double amount) {
        try (Connection con = DBConnection.getConnection()) {

            double balance = checkBalance(userId);

            if (balance < amount) {
                System.out.println("Insufficient Balance!");
                return;
            }

            String updateBalance = "UPDATE users SET balance = balance - ? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(updateBalance);
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();

            String insertTransaction = "INSERT INTO transactions(sender_id,receiver_id,amount,type) VALUES(?,?,?,?)";
            PreparedStatement ps2 = con.prepareStatement(insertTransaction);
            ps2.setInt(1, userId);
            ps2.setInt(2, userId);
            ps2.setDouble(3, amount);
            ps2.setString(4, "WITHDRAW");
            ps2.executeUpdate();

            System.out.println("Amount Withdrawn Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- TRANSFER (IMPORTANT - TRANSACTION MANAGEMENT) ----------------
    public void transfer(int senderId, int receiverId, double amount) {

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);  // START TRANSACTION

            double senderBalance = checkBalance(senderId);

            if (senderBalance < amount) {
                System.out.println("Insufficient Balance!");
                return;
            }

            // Deduct from sender
            String deduct = "UPDATE users SET balance = balance - ? WHERE user_id=?";
            PreparedStatement ps1 = con.prepareStatement(deduct);
            ps1.setDouble(1, amount);
            ps1.setInt(2, senderId);
            ps1.executeUpdate();

            // Add to receiver
            String add = "UPDATE users SET balance = balance + ? WHERE user_id=?";
            PreparedStatement ps2 = con.prepareStatement(add);
            ps2.setDouble(1, amount);
            ps2.setInt(2, receiverId);
            ps2.executeUpdate();

            // Insert transaction
            String insert = "INSERT INTO transactions(sender_id,receiver_id,amount,type) VALUES(?,?,?,?)";
            PreparedStatement ps3 = con.prepareStatement(insert);
            ps3.setInt(1, senderId);
            ps3.setInt(2, receiverId);
            ps3.setDouble(3, amount);
            ps3.setString(4, "TRANSFER");
            ps3.executeUpdate();

            con.commit();   // SUCCESS
            System.out.println("Transfer Successful!");

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();   // FAILURE
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------- VIEW TRANSACTION HISTORY ----------------
    public void viewTransactions(int userId) {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM transactions WHERE sender_id=? OR receiver_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Transaction ID: " + rs.getInt("transaction_id")
                        + " | Type: " + rs.getString("type")
                        + " | Amount: " + rs.getDouble("amount")
                        + " | Date: " + rs.getTimestamp("date_time"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

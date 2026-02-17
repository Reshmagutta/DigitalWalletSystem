package main;



import java.util.Scanner;
import service.UserService;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService service = new UserService();

        while (true) {

            System.out.println("\n===== DIGITAL WALLET SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose Option: ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    service.register(name, email, password);
                    break;

                case 2:
                    System.out.print("Enter Email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String loginPassword = sc.nextLine();

                    int userId = service.login(loginEmail, loginPassword);

                    if (userId != -1) {
                        System.out.println("Login Successful!");
                        userMenu(sc, service, userId);
                    } else {
                        System.out.println("Invalid Credentials!");
                    }
                    break;

                case 3:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Option!");
            }
        }
    }

    // ---------------- USER DASHBOARD ----------------
    public static void userMenu(Scanner sc, UserService service, int userId) {

        while (true) {

            System.out.println("\n----- USER DASHBOARD -----");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Transactions");
            System.out.println("6. Logout");
            System.out.print("Choose Option: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    double balance = service.checkBalance(userId);
                    System.out.println("Current Balance: " + balance);
                    break;

                case 2:
                    System.out.print("Enter Amount to Deposit: ");
                    double depositAmount = sc.nextDouble();
                    service.deposit(userId, depositAmount);
                    break;

                case 3:
                    System.out.print("Enter Amount to Withdraw: ");
                    double withdrawAmount = sc.nextDouble();
                    service.withdraw(userId, withdrawAmount);
                    break;

                case 4:
                    System.out.print("Enter Receiver User ID: ");
                    int receiverId = sc.nextInt();

                    System.out.print("Enter Amount to Transfer: ");
                    double transferAmount = sc.nextDouble();

                    service.transfer(userId, receiverId, transferAmount);
                    break;

                case 5:
                    service.viewTransactions(userId);
                    break;

                case 6:
                    System.out.println("Logged Out Successfully!");
                    return;

                default:
                    System.out.println("Invalid Option!");
            }
        }
    }
}


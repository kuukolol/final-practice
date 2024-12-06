import java.sql.*;
import java.util.Scanner;

public class EmployeeDatabase {
    final static String DatabaseName = "Employee";
    final static String url = "jdbc:mysql://localhost:3306";
    final static Scanner input = new Scanner(System.in);

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    // employee null details
    String empCode = null;
    String job = null;
    int salary;

    // employee name, employee job (maintenance, facility, office worker), employee
    // id(unique), salary
    // putcha nalimot ko HAHAHA andito ka paa ba nanonood HAHAHA
    public EmployeeDatabase() {
        String sqlUserName = "shino";
        String sqlPassWord = "Admin123";
        String checkDatabase = "SHOW DATABASES LIKE '" + DatabaseName + "'"; // OOPSIE
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DatabaseName;
        String useDatabase = "USE " + DatabaseName;
        try {
            conn = DriverManager.getConnection(url, sqlUserName, sqlPassWord);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(checkDatabase);
            if (rs.next()) {
                stmt.executeUpdate(useDatabase); // drop ko lang ung schema brb
                createTable();
                System.out.println("Database and table loaded");
            } else {
                stmt.executeUpdate(createDatabase);
                stmt.executeUpdate(useDatabase);
                createTable();
                System.out.println("Database and table created");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // creating table if exist
    void createTable() {
        String tableQuery = "CREATE TABLE IF NOT EXISTS accounts(" +
                "Id VARCHAR(255) NOT NULL, " +
                "Name VARCHAR(255) NOT NULL, " +
                "Job VARCHAR(50) NOT NULL, " +
                "Salary INT, " +
                "PRIMARY KEY(Id))";
        try {
            stmt.executeUpdate(tableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // register employee data with employee code
    // ito ung custom made ko mag generate ID
    String generateRandomCode(int length, String startCode) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // all characters uppercase lang and numbers
        StringBuilder result = new StringBuilder(); // string builder basically empty value siya na pede mo gawan new
                                                    // string
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length()); // generate random value kung ilan ung
                                                                           // "characters (ilan letter and number)" sa
                                                                           // characters
            result.append(characters.charAt(randomIndex)); // so every increment ng i hanggang mas malaki na ung length
                                                           // mag aappend ung random character
        }
        return startCode + "-" + result.toString();
    }

    int RegisterEmployee() {
        System.out.print("Enter Employee Full Name: ");
        String name = input.nextLine();
        System.out.print(
                "Select Job Type:\n" +
                        "[1] - Maintenance Crew\n" + // MNTCR
                        "[2] - Facility Manager\n" + // FCMGR
                        "[3] - Staff Worker\n" + // STFWR
                        "Select: ");
        int selectedJob = input.nextInt();
        String insertEmployeeQuery = "INSERT INTO accounts (ID, Name, Job, Salary) Values (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertEmployeeQuery)) {
            pstmt.setString(2, name);
            if (selectedJob == 1) {
                job = "Maintenance Crew";
                salary = 5000;
                empCode = generateRandomCode(12, "MNTCR");
                pstmt.setString(1, empCode);
                pstmt.setString(3, "Maintenance Crew");
                pstmt.setInt(4, salary);
            } else if (selectedJob == 2) {
                job = "Facility Manager";
                salary = 6000;
                empCode = generateRandomCode(12, "FCMGR");
                pstmt.setString(1, empCode);
                pstmt.setString(3, "Facility Manager");
                pstmt.setInt(4, salary);
            } else if (selectedJob == 3) {
                job = "Staff Worker";
                salary = 7000;
                empCode = generateRandomCode(12, "STFWR");
                pstmt.setString(1, empCode);
                pstmt.setString(3, "Staff Worker"); // di ko pala napalitan HAHAHAHA
                pstmt.setInt(4, salary);
            }
            int row = pstmt.executeUpdate();
            if (row > 0) {
                System.out.printf("%-10s: %-20s%n" +
                        "%-10s: %-20s%n" +
                        "%-10s: %-20s%n" +
                        "%-10s: %-20s%n", "Name", name, "ID", empCode, "Job", job, "Salary", salary);
                return 1; // success
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // error
    }

    void viewEmployeeData() {
        // meron trick para i check IF ung certain keyword lalabas wait lang try ko muna
        // sa workbench okay got it
        System.out.print("Enter employee Name: ");
        String name = input.nextLine();
        String searchAccData = "SELECT * FROM accounts WHERE NAME LIKE \"%" + name + "%\""; // query for search user
                                                                                            // with target name
        try {
            rs = stmt.executeQuery(searchAccData);
            if (rs.next()) {
                String employee_name = rs.getString("Name");
                String employee_id = rs.getString("Id");
                String employee_job = rs.getString("Job");
                int employee_salary = rs.getInt("Salary");
                System.out.printf("%-10s: %-20s%n" +
                        "%-10s: %-20s%n" +
                        "%-10s: %-20s%n" +
                        "%-10s: %-20s%n", "Name", employee_name, "ID", employee_id, "Job", employee_job, "Salary",
                        employee_salary);
            } else {
                System.out.println("Employee doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // testing
    public static void main(String[] args) {
        EmployeeDatabase yawa = new EmployeeDatabase();
        yawa.viewEmployeeData();
        // int test = yawa.RegisterEmployee();
        // if (test == 1) {
        // System.out.println("Successfully created employee data");
        // } else if (test == -1) {
        // System.out.println("Failed to create employee data");
        // } else {
        // System.out.println("Something went wrong");
        // }
    }
}
import java.util.Scanner;

public class Main {
    final static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        EmployeeDatabase emp = new EmployeeDatabase();
        int choice;
        System.out.println("Welcome to Employee Managerment Tool");
        do {
            System.out.print("[1] - Create new Employee data\n" +
                    "[2] - View Employee data\n" +
                    "[3] - Exit\n" +
                    ">: ");
            choice = input.nextInt();
            if (choice == 1) {
                emp.RegisterEmployee();
            } else if (choice == 2) {
                emp.viewEmployeeData();
            } else {
                System.out.println("Invalid choice");
            }
        } while (choice != 3);
        System.out.println("Thank you for using the Employee Management Tool");
    }

}

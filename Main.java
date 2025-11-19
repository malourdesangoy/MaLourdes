import java.util.*;

// USER CLASS
class User {
    private String username;
    private String password;
    private String role;
    private String twoFA;

    public User(String username, String password, String role, String twoFA) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.twoFA = twoFA;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getTwoFA() { return twoFA; }

    public void setUsername(String newU) { this.username = newU; }
    public void setPassword(String newP) { this.password = newP; }
    public void setRole(String newR) { this.role = newR; }
    public void setTwoFA(String newT) { this.twoFA = newT; }
}

// STUDENT CLASS (only name and course)
class StudentRecord {
    String name, course;

    public StudentRecord(String name, String course) {
        this.name = name;
        this.course = course;
    }
}

// SECURITY
class SecurityLayer {
    public boolean intrusionCheck(String s) {
        return !(s.contains("'") || s.contains(";") || s.contains("DROP") || s.contains("--"));
    }

    public boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }
}

// MAIN SYSTEM
public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<User> accounts = new ArrayList<>();
    static ArrayList<StudentRecord> students = new ArrayList<>();
    static SecurityLayer sec = new SecurityLayer();

    // SAFE STRING INPUT
    public static String getString(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine();
            if (!sec.isEmpty(s) && sec.intrusionCheck(s))
                return s;
            System.out.println("Invalid input. Try again.");
        }
    }

    // ROLE SELECTION
    public static String getRole() {
        while (true) {
            System.out.print("Choose role (student / teacher / admin): ");
            String r = sc.nextLine().toLowerCase();
            if (r.equals("student") || r.equals("teacher") || r.equals("admin"))
                return r;
            System.out.println("Invalid role. Try again.");
        }
    }

    // 2FA INPUT (6 digits)
    public static String get2FA() {
        while (true) {
            System.out.print("Enter 6-digit 2FA: ");
            String code = sc.nextLine();
            if (code.matches("\\d{6}"))
                return code;
            System.out.println("2FA must be exactly 6 numbers.");
        }
    }

    // MENU CHOICE
    public static String getMenuChoice(int max) {
        while (true) {
            System.out.print("> ");
            String c = sc.nextLine();
            if (c.matches("\\d+") && Integer.parseInt(c) >= 1 && Integer.parseInt(c) <= max)
                return c;
            System.out.println("Invalid choice.");
        }
    }

    // ACCOUNT CREATION
    public static void createAccount() {
        System.out.println("\n=== CREATE ACCOUNT ===");

        String user;
        while (true) {
            user = getString("Enter username: ");

            boolean exists = false;
            for (User u : accounts)
                if (u.getUsername().equals(user)) exists = true;

            if (!exists) break;
            System.out.println("Username already exists.");
        }

        String pass;
        while (true) {
            pass = getString("Enter password: ");
            String confirm = getString("Confirm password: ");
            if (pass.equals(confirm)) break;
            System.out.println("Passwords do not match.");
        }

        String role = getRole();
        String twoFA = get2FA();

        accounts.add(new User(user, pass, role, twoFA));
        System.out.println("Account created.");
    }

    // LOGIN
    public static User login() {
        System.out.println("\n=== LOGIN ===");

        String userInput = getString("Username: ");

        for (User u : accounts) {
            if (u.getUsername().equals(userInput)) {

                while (true) {
                    String passInput = getString("Password: ");
                    if (passInput.equals(u.getPassword())) break;
                    System.out.println("Incorrect password. Try again.");
                }

                while (true) {
                    String twoFAin = getString("Enter 2FA: ");
                    if (twoFAin.equals(u.getTwoFA())) break;
                    System.out.println("Incorrect 2FA. Try again.");
                }

                System.out.println("Login successful.");
                return u;
            }
        }

        System.out.println("Account not found.");
        return null;
    }

    // STUDENT FUNCTIONS
    public static void addStudent() {
        System.out.println("\n=== ADD STUDENT ===");
        String name = getString("Name: ");
        String course = getString("Course: ");

        students.add(new StudentRecord(name, course));
        System.out.println("Student added.");
    }

    public static void viewStudents() {
        System.out.println("\n=== STUDENT RECORDS ===");

        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (StudentRecord s : students) {
            System.out.println("Name: " + s.name + " | Course: " + s.course);
        }
    }

    // EDIT STUDENT (name & course)
    public static void editStudent() {
        System.out.println("\n=== EDIT STUDENT ===");
        String name = getString("Enter student name to edit: ");

        for (StudentRecord s : students) {
            if (s.name.equals(name)) {
                s.name = getString("New Name: ");
                s.course = getString("New Course: ");
                System.out.println("Student updated.");
                return;
            }
        }

        System.out.println("Student not found.");
    }

    // ADMIN ACCOUNT EDITING
    public static void editAccount() {
        System.out.println("\n=== EDIT ACCOUNT ===");
        String user = getString("Enter username to edit: ");

        for (User u : accounts) {
            if (u.getUsername().equals(user)) {

                String newU = getString("New Username: ");
                String newP;
                while (true) {
                    newP = getString("New Password: ");
                    String confirm = getString("Confirm Password: ");
                    if (newP.equals(confirm)) break;
                    System.out.println("Passwords do not match.");
                }

                String newR = getRole();
                String newT = get2FA();

                u.setUsername(newU);
                u.setPassword(newP);
                u.setRole(newR);
                u.setTwoFA(newT);

                System.out.println("Account updated.");
                return;
            }
        }

        System.out.println("Account not found.");
    }

    // DELETE ACCOUNT
    public static void deleteAccount() {
        String user = getString("Enter username to delete: ");
        accounts.removeIf(a -> a.getUsername().equals(user));
        System.out.println("Account removed if it existed.");
    }

    // ADMIN MENU
    public static void adminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add Account");
            System.out.println("2. Edit Account");
            System.out.println("3. Delete Account");
            System.out.println("4. View Students");
            System.out.println("5. Add Student");
            System.out.println("6. Edit Student");
            System.out.println("7. Logout");

            switch (getMenuChoice(7)) {
                case "1" -> createAccount();
                case "2" -> editAccount();
                case "3" -> deleteAccount();
                case "4" -> viewStudents();
                case "5" -> addStudent();
                case "6" -> editStudent();
                case "7" -> { return; }
            }
        }
    }

    // TEACHER MENU
    public static void teacherMenu() {
        while (true) {
            System.out.println("\n=== TEACHER MENU ===");
            System.out.println("1. View Students");
            System.out.println("2. Edit Student");
            System.out.println("3. Add Student");
            System.out.println("4. Logout");

            switch (getMenuChoice(4)) {
                case "1" -> viewStudents();
                case "2" -> editStudent();
                case "3" -> addStudent();
                case "4" -> { return; }
            }
        }
    }

    // STUDENT MENU
    public static void studentMenu() {
        System.out.println("\n=== STUDENT VIEW ===");
        viewStudents();
    }

    // MAIN PROGRAM
    public static void main(String[] args) {

        accounts.add(new User("admin", "admin123", "admin", "000000")); // default 2FA 6 digits

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");

            switch (getMenuChoice(3)) {
                case "1" -> {
                    User u = login();
                    if (u == null) break;

                    switch (u.getRole()) {
                        case "admin" -> adminMenu();
                        case "teacher" -> teacherMenu();
                        case "student" -> studentMenu();
                    }
                }
                case "2" -> createAccount();
                case "3" -> {
                    System.out.println("Goodbye.");
                    return;
                }
            }
        }
    }
}
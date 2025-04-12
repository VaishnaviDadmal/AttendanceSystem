// AttendanceSystem.java

import java.io.*;
import java.util.*;

abstract class User {
    protected String name;
    protected String role;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public abstract void showMenu();
}

class AttendanceRecord {
    public static String formatRecord(String studentName, String date, boolean isPresent) {
        return studentName + "," + date + "," + (isPresent ? "Present" : "Absent");
    }
}

class Admin extends User {
    public Admin(String name) {
        super(name, "Admin");
    }

    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Student Attendance");
            System.out.println("2. View All Records");
            System.out.println("3. Delete Attendance File");
            System.out.println("4. Logout");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String student = sc.nextLine();
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    String date = sc.nextLine();
                    System.out.print("Present? (true/false): ");
                    boolean status = Boolean.parseBoolean(sc.nextLine());
                    addAttendance(student, date, status);
                    break;
                case 2:
                    viewAllRecords();
                    break;
                case 3:
                    deleteFile();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addAttendance(String student, String date, boolean present) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("attendance.txt", true))) {
            String record = AttendanceRecord.formatRecord(student, date, present);
            bw.write(record);
            bw.newLine();
            System.out.println("Attendance added.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    private void viewAllRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            System.out.println("\nAttendance Records:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No records found.");
        }
    }

    private void deleteFile() {
        File file = new File("attendance.txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Attendance file deleted.");
            } else {
                System.out.println("Failed to delete file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}

class Student extends User {
    public Student(String name) {
        super(name, "Student");
    }

    public void showMenu() {
        System.out.println("\nStudent Menu:");
        System.out.println("Attendance for: " + name);
        viewAttendance();
    }

    private void viewAttendance() {
        try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(name + ",")) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found) System.out.println("No attendance found for you.");
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }
}

class Faculty extends User {
    public Faculty(String name) {
        super(name, "Faculty");
    }

    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nFaculty Menu:");
        System.out.print("Enter student name: ");
        String student = sc.nextLine();
        System.out.print("Enter date (yyyy-mm-dd): ");
        String date = sc.nextLine();
        System.out.print("Present? (true/false): ");
        boolean status = Boolean.parseBoolean(sc.nextLine());
        markAttendance(student, date, status);
    }

    private void markAttendance(String student, String date, boolean present) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("attendance.txt", true))) {
            String record = AttendanceRecord.formatRecord(student, date, present);
            bw.write(record);
            bw.newLine();
            System.out.println("Attendance marked.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
}

public class AttendanceSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Attendance Management System");
        while (true) {
            System.out.println("\nLogin as:\n1. Admin\n2. Student\n3. Faculty\n4. Exit");
            int choice = Integer.parseInt(sc.nextLine());
            if (choice == 4) break;

            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            User user = null;
            if (choice == 1) {
                user = new Admin(name);
            } else if (choice == 2) {
                user = new Student(name);
            } else if (choice == 3) {
                user = new Faculty(name);
            } else {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            user.showMenu();
        }
    }
}

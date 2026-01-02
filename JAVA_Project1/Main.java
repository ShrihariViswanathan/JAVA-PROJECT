import java.util.*;
import java.io.*;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print(CYAN + "Enter Student USN: " + RESET);
        String usn = sc.nextLine().trim();

        Student student;
        try {
            student = FileManager.load(usn);
            System.out.println(GREEN + "Data loaded for " + usn + RESET);
        } catch (Exception e) {
            student = new Student(usn);
            System.out.println(YELLOW + "New profile created for " + usn + RESET);
        }

        while (true) {
            System.out.println("\n" + BLUE + "1. View Academic Dashboard");
            System.out.println("2. Add/Update Semester Record");
            System.out.println("3. Save and Exit" + RESET);
            int choice = getValidInt(sc, "Choose: ", 1, 3);

            switch (choice) {
                case 1:
                    student.displayAll();
                    break;
                case 2:
                    int sem = getValidInt(sc, "Semester (1-8): ", 1, 8);
                    SemesterRecord sr;

                    // REQUIREMENT 1: Ask Update or Replace
                    if (student.records.containsKey(sem)) {
                        System.out.println(YELLOW + "Record for Semester " + sem + " already exists.");
                        System.out.println("1. Replace (Delete old courses)");
                        System.out.println("2. Update (Add to existing courses)" + RESET);
                        int mode = getValidInt(sc, "Select mode: ", 1, 2);
                        if (mode == 1) sr = new SemesterRecord(sem);
                        else sr = student.records.get(sem);
                    } else {
                        sr = new SemesterRecord(sem);
                    }

                    int count = getValidInt(sc, "How many courses to add? ", 1, 10);
                    for (int i = 0; i < count; i++) {
                        System.out.print("Course Name: ");
                        String name = sc.nextLine();
                        int m = getValidInt(sc, "Marks (0-100): ", 0, 100);
                        int c = getValidInt(sc, "Credits (1-10): ", 1, 10);
                        sr.addCourse(name, m, c);
                    }
                    student.addSemester(sr);
                    System.out.println(GREEN + "Semester record updated!" + RESET);
                    break;
                case 3:
                    try {
                        FileManager.save(student);
                        System.out.println(GREEN + "Data saved. Goodbye!" + RESET);
                    } catch (IOException e) {
                        System.out.println(RED + "Error saving data." + RESET);
                    }
                    return;
            }
        }
    }

    private static int getValidInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine());
                if (val >= min && val <= max) return val;
            } catch (Exception e) { }
            System.out.println(RED + "Invalid input. Please try again." + RESET);
        }
    }
}
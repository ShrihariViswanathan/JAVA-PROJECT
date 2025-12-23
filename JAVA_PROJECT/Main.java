import java.util.*;
import java.io.*;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";

    static class Course {
        String name;
        int marks, credits;
        Course(String n, int m, int c) { name = n; marks = m; credits = c; }
    }

    static class SemesterRecord {
        int semesterNum;
        List<Course> courses = new ArrayList<>();
        double sgpa;

        SemesterRecord(int num) { this.semesterNum = num; }

        void addCourse(String name, int marks, int credits) {
            courses.add(new Course(name, marks, credits));
        }

        private int getGradePoint(int mark) {
            if (mark >= 90) return 10;
            else if (mark >= 80) return 9;
            else if (mark >= 70) return 8;
            else if (mark >= 60) return 7;
            else if (mark >= 50) return 6;
            else if (mark >= 40) return 5;
            else return 0;
        }

        public void calculateSGPA() {
            int totalCredits = 0;
            int totalPoints = 0;
            for (Course c : courses) {
                totalPoints += getGradePoint(c.marks) * c.credits;
                totalCredits += c.credits;
            }
            this.sgpa = (totalCredits == 0) ? 0 : (double) totalPoints / totalCredits;
        }
    }

    static class Student {
        String usn;
        Map<Integer, SemesterRecord> records = new TreeMap<>();
        Student(String usn) { this.usn = usn; }

        void addSemester(SemesterRecord sr) {
            sr.calculateSGPA();
            records.put(sr.semesterNum, sr);
        }

        double calculateCGPA() {
            if (records.isEmpty()) return 0;
            double totalSGPA = 0;
            for (SemesterRecord r : records.values()) {
                totalSGPA += r.sgpa;
            }
            return totalSGPA / records.size();
        }
    }

    static class FileManager {
        public static void save(Student s) throws IOException {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(s.usn + ".txt"))) {
                for (SemesterRecord sr : s.records.values()) {
                    bw.write("SEM:" + sr.semesterNum);
                    bw.newLine();
                    for (Course c : sr.courses) {
                        bw.write(c.name + ":" + c.marks + ":" + c.credits);
                        bw.newLine();
                    }
                    bw.write("END_SEM");
                    bw.newLine();
                }
            }
        }

        public static Student load(String usn) throws Exception {
            Student student = new Student(usn);
            try (BufferedReader br = new BufferedReader(new FileReader(usn + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("SEM:")) {
                        int semNum = Integer.parseInt(line.split(":")[1]);
                        SemesterRecord sr = new SemesterRecord(semNum);
                        while (!(line = br.readLine()).equals("END_SEM")) {
                            String[] parts = line.split(":");
                            sr.addCourse(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        }
                        student.addSemester(sr);
                    }
                }
            }
            return student;
        }
    }

    // Helper method for validated integer input
    private static int getValidInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine());
                if (val >= min && val <= max) return val;
                System.out.println(RED + "Invalid range! Enter a value between " + min + " and " + max + "." + RESET);
            } catch (Exception e) {
                System.out.println(RED + "Invalid input! Please enter a numeric whole number." + RESET);
            }
        }
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        clearConsole();
        
        System.out.println(BLUE + "==============================================");
        System.out.println("        STUDENT ACADEMIC DASHBOARD");
        System.out.println("==============================================" + RESET);
        
        String usn = "";
        while (usn.isEmpty()) {
            System.out.print(CYAN + "Enter USN: " + RESET);
            usn = sc.nextLine().trim();
        }

        Student student;
        File file = new File(usn + ".txt");

        if (file.exists()) {
            try {
                student = FileManager.load(usn);
                System.out.println(GREEN + "Existing data loaded from " + usn + ".txt" + RESET);
            } catch (Exception e) {
                student = new Student(usn);
                System.out.println(RED + "Error loading file. Created new record." + RESET);
            }
        } else {
            student = new Student(usn);
            System.out.println(YELLOW + "Created a new record for USN: " + usn + RESET);
        }

        while (true) {
            printMenu();
            int choice = getValidInt(sc, YELLOW + "Choose an option (1-3): " + RESET, 1, 3);

            switch (choice) {
                case 1:
                    clearConsole();
                    System.out.println(CYAN + "--- ACADEMIC RECORDS ---" + RESET);
                    if (student.records.isEmpty()) {
                        System.out.println(RED + "No records found." + RESET);
                    } else {
                        for (SemesterRecord sr : student.records.values()) {
                            System.out.printf("\n\u001B[36mSemester %d\u001B[0m\n", sr.semesterNum);
                            System.out.printf("%-20s | %-7s | %-7s\n", "Course", "Marks", "Credits");
                            System.out.println("----------------------------------------------");
                            for (Course c : sr.courses) {
                                System.out.printf("%-20s | %-7d | %-7d\n", c.name, c.marks, c.credits);
                            }
                            System.out.printf(YELLOW + "SGPA: %.2f\n" + RESET, sr.sgpa);
                        }
                        System.out.println("\n" + GREEN + "OVERALL CGPA: " + String.format("%.2f", student.calculateCGPA()) + RESET);
                    }
                    break;

                case 2:
                    clearConsole();
                    System.out.println(CYAN + "--- ADD SEMESTER ---" + RESET);
                    int sem = getValidInt(sc, "Semester (1-8): ", 1, 8);
                    SemesterRecord sr = new SemesterRecord(sem);
                    int n = getValidInt(sc, "Number of courses (1-15): ", 1, 15);

                    for (int i = 1; i <= n; i++) {
                        String name = "";
                        while (name.isEmpty()) {
                            System.out.print("Course " + i + " Name: ");
                            name = sc.nextLine().trim();
                        }
                        int m = getValidInt(sc, "  Marks (0-100): ", 0, 100);
                        int c = getValidInt(sc, "  Credits (1-10): ", 1, 10);
                        sr.addCourse(name, m, c);
                    }
                    student.addSemester(sr);
                    System.out.println(GREEN + "Semester updated successfully!" + RESET);
                    break;

                case 3:
                    try {
                        FileManager.save(student);
                        System.out.println(GREEN + "Progress saved to " + usn + ".txt" + RESET);
                    } catch (IOException e) {
                        System.out.println(RED + "Error saving file." + RESET);
                    }
                    System.out.println(BLUE + "Goodbye!" + RESET);
                    sc.close();
                    return;
            }
        }
    }

    static void printMenu() {
        System.out.println("\n" + BLUE + "==============================================");
        System.out.println("        STUDENT ACADEMIC DASHBOARD");
        System.out.println("==============================================" + RESET);
        System.out.println(BLUE + "1. View Records (SGPA/CGPA)");
        System.out.println(BLUE + "2. Add/Update Semester");
        System.out.println(BLUE + "3. Save and Exit" + RESET);
    }
}
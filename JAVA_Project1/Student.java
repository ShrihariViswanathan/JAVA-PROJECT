import java.util.*;

public class Student extends AbstractStudent {
    public Map<Integer, SemesterRecord> records = new TreeMap<>();

    public Student(String usn) {
        super(usn);
    }

    public void addSemester(SemesterRecord sr) {
        sr.calculateSGPA();
        records.put(sr.semester, sr);
    }

    public double calculateCGPA() {
        if (records.isEmpty()) return 0;
        double totalSGPA = 0;
        for (SemesterRecord r : records.values()) {
            totalSGPA += r.sgpa;
        }
        return totalSGPA / records.size();
    }

    public void displayAllFailedCourses() {
        System.out.println("\n\u001B[31m--- GLOBAL FAILED COURSES SUMMARY ---\u001B[0m");
        boolean anyFailures = false;
        for (SemesterRecord sr : records.values()) {
            for (int i = 0; i < sr.marks.size(); i++) {
                if (sr.marks.get(i) < 40) {
                    System.out.println("Sem " + sr.semester + ": " + sr.courses.get(i) + " (" + sr.marks.get(i) + " marks)");
                    anyFailures = true;
                }
            }
        }
        if (!anyFailures) System.out.println("No failed courses across all semesters.");
    }

    @Override
    public void displayAll() {
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (SemesterRecord sr : records.values()) {
            sr.displayRecord();
        }
        displayAllFailedCourses();
        System.out.printf("\n\u001B[32mOVERALL CGPA: %.2f\u001B[0m\n", calculateCGPA());
    }
}
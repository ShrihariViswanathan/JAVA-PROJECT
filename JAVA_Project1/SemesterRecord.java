import java.util.ArrayList;

public class SemesterRecord implements Record {
    public int semester;
    public ArrayList<String> courses;
    public ArrayList<Integer> marks;
    public ArrayList<Integer> credits;
    public double sgpa;

    public SemesterRecord(int semester) {
        this.semester = semester;
        this.courses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.credits = new ArrayList<>();
    }

    public void addCourse(String course, int mark, int credit) {
        courses.add(course);
        marks.add(mark);
        credits.add(credit);
    }

    public void calculateSGPA() {
        int totalCredits = 0;
        int totalPoints = 0;
        for (int i = 0; i < marks.size(); i++) {
            int mark = marks.get(i);
            // Internal grade point logic for SGPA calculation
            int gp = (mark >= 90) ? 10 : (mark >= 80) ? 9 : (mark >= 70) ? 8 : 
                     (mark >= 60) ? 7 : (mark >= 50) ? 6 : (mark >= 40) ? 5 : 0;
            totalPoints += gp * credits.get(i);
            totalCredits += credits.get(i);
        }
        this.sgpa = (totalCredits == 0) ? 0 : (double) totalPoints / totalCredits;
    }

    // Updated logic based on your SGPA ranges
    public String getClassResult() {
        if (sgpa >= 9.0) return "DISTINCTION CLASS";
        if (sgpa >= 8.0) return "1ST CLASS";
        if (sgpa >= 7.0) return "2ND CLASS";
        if (sgpa >= 5.0) return "PASS";
        return "FAIL"; // 4.99 and below
    }

    @Override
    public void displayRecord() {
        calculateSGPA(); 
        System.out.printf("\n--- SEMESTER %d ---\n", semester);
        System.out.printf("%-15s | %-7s | %-7s\n", "Course", "Marks", "Credits");
        System.out.println("-------------------------------------");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%-15s | %-7d | %-7d\n", courses.get(i), marks.get(i), credits.get(i));
        }
        
        // Displays the SGPA and the new Class based on that SGPA
        System.out.printf("SGPA: %.2f | Class: %s\n", sgpa, getClassResult());
    }
}
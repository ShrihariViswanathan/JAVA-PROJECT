import java.util.ArrayList;

public class SemesterRecord implements Record {

    int semester;
    ArrayList<String> courses;
    ArrayList<Integer> marks;
    ArrayList<Integer> credits;
    double sgpa;

    public SemesterRecord(int semester) {
        this.semester = semester;
        courses = new ArrayList<>();
        marks = new ArrayList<>();
        credits = new ArrayList<>();
    }

    public void addCourse(String course, int mark, int credit) {
        courses.add(course);
        marks.add(mark);
        credits.add(credit);
    }

    // Calculate grade point from marks
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

        for (int i = 0; i < marks.size(); i++) {
            int gp = getGradePoint(marks.get(i));
            totalPoints += gp * credits.get(i);
            totalCredits += credits.get(i);
        }

        if (totalCredits != 0)
            sgpa = (double) totalPoints / totalCredits;
    }

    @Override
    public void displayRecord() {
        System.out.println("Semester: " + semester);
        for (int i = 0; i < courses.size(); i++) {
            System.out.println(courses.get(i)
                    + " | Marks: " + marks.get(i)
                    + " | Credits: " + credits.get(i));
        }
        System.out.printf("SGPA: %.2f\n", sgpa);
    }
}
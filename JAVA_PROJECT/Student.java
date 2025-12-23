import java.util.HashMap;

public class Student extends AbstractStudent {

    HashMap<Integer, SemesterRecord> records;

    public Student(String usn) {
        super(usn);
        records = new HashMap<>();
    }

    public void addSemester(SemesterRecord record) {
        record.calculateSGPA();
        records.put(record.semester, record);
    }

    public double calculateCGPA() {
        if (records.isEmpty()) return 0;

        double total = 0;
        for (SemesterRecord r : records.values()) {
            total += r.sgpa;
        }
        return total / records.size();
    }

    @Override
    public void displayAll() {
        if (records.isEmpty()) {
            System.out.println("No records available.");
            return;
        }

        for (SemesterRecord r : records.values()) {
            r.displayRecord();
            System.out.println("---------------------");
        }

        System.out.printf("CGPA: %.2f\n", calculateCGPA());
    }
}
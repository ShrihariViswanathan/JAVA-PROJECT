import java.io.*;

public class FileManager {
    public static void save(Student student) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(student.usn + ".txt"))) {
            for (SemesterRecord r : student.records.values()) {
                bw.write(r.semester + "," + r.sgpa);
                bw.newLine();
                for (int i = 0; i < r.courses.size(); i++) {
                    bw.write(r.courses.get(i) + ":" + r.marks.get(i) + ":" + r.credits.get(i));
                    bw.newLine();
                }
                bw.write("END");
                bw.newLine();
            }
        }
    }

    public static Student load(String usn) throws IOException {
        Student student = new Student(usn);
        File file = new File(usn + ".txt");
        if (!file.exists()) return student;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] header = line.split(",");
                int sem = Integer.parseInt(header[0]);
                SemesterRecord sr = new SemesterRecord(sem);
                while (!(line = br.readLine()).equals("END")) {
                    String[] data = line.split(":");
                    sr.addCourse(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                }
                student.addSemester(sr);
            }
        }
        return student;
    }
}
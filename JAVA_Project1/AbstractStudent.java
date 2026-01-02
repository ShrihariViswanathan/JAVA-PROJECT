public abstract class AbstractStudent {
    protected String usn;

    public AbstractStudent(String usn) {
        this.usn = usn;
    }

    public abstract void displayAll();
}
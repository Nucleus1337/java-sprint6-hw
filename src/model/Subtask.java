package model;

public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, TaskStatus status, long id, long epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, long id) {
        super(name, description, status, id);
    }

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus().toString() + '\'' +
                ", id=" + this.getId() +
                ", epicId=" + epicId +
                '}' + "\n";
    }
}

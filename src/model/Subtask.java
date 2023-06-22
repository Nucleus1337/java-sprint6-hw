package model;

import static model.TaskType.SUBTASK;

public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, TaskStatus status, long id, long epicId) {
        super(name, description, status, id);
        super.setType(SUBTASK);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, long id) {
        super(name, description, status, id);
        super.setType(SUBTASK);
    }

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        super.setType(SUBTASK);
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
                ", type=" + super.getType() + '\'' +
                '}' + "\n";
    }
}

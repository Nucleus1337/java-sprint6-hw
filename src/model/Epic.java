package model;

import java.util.ArrayList;
import java.util.List;

import static model.TaskType.EPIC;

public class Epic extends Task {
    private final List<Long> subtaskId = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status, long id) {
        super(name, description, status, id);
        super.setType(EPIC);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        super.setType(EPIC);
    }

    public Epic(String name, String desc) {
        super.setName(name);
        super.setDescription(desc);
        super.setType(EPIC);
    }

    public List<Long> getSubtaskId() {
        return subtaskId;
    }

    public void addSubtaskId(long subtaskId) {
        this.subtaskId.add(subtaskId);
    }

    public void clearSubtaskId() {
        subtaskId.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus().toString() + '\'' +
                ", id=" + this.getId() +
                ", type=" + super.getType() + '\'' +
                '}' + "\n";
    }
}

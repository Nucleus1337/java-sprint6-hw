package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Long> subtaskId = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status, long id) {
        super(name, description, status, id);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(String name, String desc) {
        super.setName(name);
        super.setDescription(desc);
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
                '}' + "\n";
    }
}

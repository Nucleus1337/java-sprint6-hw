package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String pathToSave = "resources/";
    private String fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
    }

    @Override
    public List<Subtask> getAllSubtasksByEpicId(long epicId) {
        return super.getAllSubtasksByEpicId(epicId);
    }

    @Override
    public Task getTaskById(long taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(long epicId) {
        return super.getEpicById(epicId);
    }

    @Override
    public Subtask getSubtaskById(long subtaskId) {
        return super.getSubtaskById(subtaskId);
    }

    @Override
    public void removeTaskById(long taskId) {
        super.removeTaskById(taskId);
    }

    @Override
    public void removeSubtaskById(long subtaskId) {
        super.removeSubtaskById(subtaskId);
    }

    @Override
    public void removeEpicById(long epicId) {
        super.removeEpicById(epicId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + fileName))) {
            for (Task task : super.getAllTasks()) {
                System.out.println(toString(task));
                writer.write(toString(task));
                writer.newLine();
            }

            for (Task task : super.getAllEpics()) {
                System.out.println(toString(task));
            }

            for (Task task : super.getAllSubtasks()) {
                System.out.println(toString(task));
            }
        } catch (IOException e) {
            System.out.println("Не удалось найти путь для сохранения");
        }
    }

    private String toString(Task task) {
        String taskType = TASK.name();

        if (task instanceof Subtask) {
            return String.format("%s,%s,%s,%s,%s", task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getStatus(),
                    ((Subtask) task).getEpicId());
        }
        return String.format("%s,%s,%s,%s,%s", task.getId(),
                                               taskType,
                                               task.getName(),
                                               task.getDescription(),
                                               task.getStatus());
    }
}

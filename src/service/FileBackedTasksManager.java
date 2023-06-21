package service;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.TaskStatus.*;
import static model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String pathToSave = "resources/";
    private final String fileName;

    public FileBackedTasksManager(String fileName) {
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
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public List<Subtask> getAllSubtasksByEpicId(long epicId) {
        return super.getAllSubtasksByEpicId(epicId);
    }

    @Override
    public Task getTaskById(long taskId) {
        Task task = super.getTaskById(taskId);
        save();

        return task;
    }

    @Override
    public Epic getEpicById(long epicId) {
        Epic epic = super.getEpicById(epicId);
        save();

        return epic;
    }

    @Override
    public Subtask getSubtaskById(long subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();

        return subtask;
    }

    @Override
    public void removeTaskById(long taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeSubtaskById(long subtaskId) {
        super.removeSubtaskById(subtaskId);
        save();
    }

    @Override
    public void removeEpicById(long epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void createTaskFromFile(Task task) {
        super.createTask(task);
    }

    private void updateTaskFromFile(Task task) {
        super.updateTask(task);
    }

    private void createEpicFromFile(Epic epic) {
        super.createEpic(epic);
    }

    private void updateEpicFromFile(Epic epic) {
        super.updateEpic(epic);
    }

    private void createSubtaskFromFile(Subtask subtask) {
        super.createSubtask(subtask);
    }

    private void updateSubtaskFromFile(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + fileName))) {
            for (Task task : super.getAllTasks()) {
                writeTaskToFile(writer, task);
            }

            for (Task task : super.getAllEpics()) {
                writeTaskToFile(writer, task);
            }

            for (Task task : super.getAllSubtasks()) {
                writeTaskToFile(writer, task);
            }

            StringBuilder tasksHistoryIds = new StringBuilder();
            for (Task task : super.getHistory()) {
                tasksHistoryIds.append(task.getId()).append(",");
            }

            writer.newLine();
            writer.write(tasksHistoryIds.toString());
        } catch (IOException e) {
            System.out.printf("Не удалось найти путь для сохранения: %s\n", pathToSave + fileName);
        }
    }

    private void writeTaskToFile(BufferedWriter writer, Task task) throws IOException {
        writer.write(taskToString(task));
        writer.newLine();
    }

    private String taskToString(Task task) {
        if (task instanceof Epic) {
            List<Long> epicsSubtasks = ((Epic) task).getSubtaskId();
            String subtasks = epicsSubtasks.stream()
                    .map(Objects::toString)
                    .collect(Collectors.joining(","));

            return String.format("%s,%s,%s,%s,%s,%s", task.getId(),
                    EPIC.name(),
                    task.getName(),
                    task.getDescription(),
                    task.getStatus(),
                    subtasks);
        } else if (task instanceof Subtask) {
            return String.format("%s,%s,%s,%s,%s,%s", task.getId(),
                    SUBTASK.name(),
                    task.getName(),
                    task.getDescription(),
                    task.getStatus(),
                    ((Subtask) task).getEpicId());
        }

        return String.format("%s,%s,%s,%s,%s", task.getId(),
                TASK.name(),
                task.getName(),
                task.getDescription(),
                task.getStatus());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file.getName());

        try {
            for (String row : Files.readAllLines(Path.of(file.toURI()))) {
                if (!row.isEmpty()) break;

                String[] elements = row.split(",");

                if (TaskType.valueOf(elements[1]) == EPIC) {
                    tasksManager.createEpicFromFile((Epic) taskFromString(row));
                    tasksManager.updateEpicFromFile((Epic) taskFromString(row));
                } else if (TaskType.valueOf(elements[1]) == SUBTASK) {
                    tasksManager.createSubtaskFromFile((Subtask) taskFromString(row));
                    tasksManager.updateSubtaskFromFile((Subtask) taskFromString(row));
                } else {
                    tasksManager.createTaskFromFile(taskFromString(row));
                    tasksManager.updateTaskFromFile(taskFromString(row));
                }

            }
        } catch (IOException e) {
            System.out.printf("Невозможно найти файл для загрузки: %s\n", file);
        }

        return tasksManager;
    }

    private static Task taskFromString(String value) {
        String[] taskString = value.split(",");
        
        if (TaskType.valueOf(taskString[1]) == EPIC) {
            Epic epic = new Epic(taskString[2],
                    taskString[3],
                    TaskStatus.valueOf(taskString[4]),
                    Long.parseLong(taskString[0]));

            for (int i = 5; i < taskString.length; i++) {
                epic.addSubtaskId(Long.parseLong(taskString[i]));
            }

            return epic;

        } else if (TaskType.valueOf(taskString[1]) == SUBTASK) {
            return new Subtask(taskString[2],
                    taskString[3],
                    TaskStatus.valueOf(taskString[4]),
                    Long.parseLong(taskString[0]),
                    Long.parseLong(taskString[5]));

        } else {
            return new Task(taskString[2],
                    taskString[3],
                    TaskStatus.valueOf(taskString[4]),
                    Long.parseLong(taskString[0]));
        }
    }

    public static void main(String[] args) {
        FileBackedTasksManager manager = loadFromFile(new File("resources/fileToSave.csv"));

//        manager.createTask(new Task("Task1", "TaskDesc1")); /*1*/
//        manager.createEpic(new Epic("Epic1", "Epic1 desc")); /*2*/
//        manager.createSubtask(new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", 2)); /*3*/
//
//        manager.updateTask(new Task("Task1", "TaskDesc1", DONE, 1));
//
//        manager.getEpicById(2);
//        manager.getTaskById(1);
    }
}

package service;

import model.*;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.Sequence.getNextId;
import static model.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> taskIdToTask = new HashMap<>();
    private final Map<Long, Epic> epicIdToEpic = new HashMap<>();
    private final Map<Long, Subtask> subtaskIdToSubtask = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void createTask(Task task) {
        task.setId(getNextId());
        task.setStatus(NEW);

        taskIdToTask.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        epic.setStatus(NEW);

        epicIdToEpic.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtask.setStatus(NEW);

        subtaskIdToSubtask.put(subtask.getId(), subtask);
        epicIdToEpic.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskIdToTask.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicIdToEpic.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskIdToSubtask.values());
    }

    @Override
    public void clearAllTasks() {
        taskIdToTask.values().forEach(task -> historyManager.remove(task.getId()));
        taskIdToTask.clear();
    }

    @Override
    public void clearAllEpics() {
        epicIdToEpic.values().forEach(epic -> historyManager.remove(epic.getId()));
        epicIdToEpic.clear();

        subtaskIdToSubtask.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtaskIdToSubtask.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtaskIdToSubtask.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtaskIdToSubtask.clear();

        epicIdToEpic.values().forEach(epic -> {
            epic.setStatus(NEW);
            epic.clearSubtaskId();
        });
    }

    @Override
    public List<Subtask> getAllSubtasksByEpicId(long epicId) {
        return subtaskIdToSubtask.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(long taskId) {
        historyManager.add(taskIdToTask.get(taskId));

        return taskIdToTask.get(taskId);
    }

    @Override
    public Epic getEpicById(long epicId) {
        historyManager.add(epicIdToEpic.get(epicId));

        return epicIdToEpic.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(long subtaskId) {
        historyManager.add(subtaskIdToSubtask.get(subtaskId));

        return subtaskIdToSubtask.get(subtaskId);
    }

    @Override
    public void removeTaskById(long taskId) {
        taskIdToTask.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeSubtaskById(long subtaskId) {
        Epic epic = epicIdToEpic.get(subtaskIdToSubtask.get(subtaskId).getEpicId());
        epic.getSubtaskId().remove(subtaskId);

        subtaskIdToSubtask.remove(subtaskId);
        historyManager.remove(subtaskId);

        calcEpicStatus(epic);
    }

    @Override
    public void removeEpicById(long epicId) {
        epicIdToEpic.get(epicId).getSubtaskId().forEach(subtaskId -> {
            subtaskIdToSubtask.remove(subtaskId);
            historyManager.remove(subtaskId);
        });

        epicIdToEpic.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void updateTask(Task task) {
        updateMainTaskInfo(task, taskIdToTask.get(task.getId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask subtaskForUpdate = subtaskIdToSubtask.get(subtask.getId());
        updateMainTaskInfo(subtask, subtaskForUpdate);

        calcEpicStatus(epicIdToEpic.get(subtaskForUpdate.getEpicId()));
    }

    @Override
    public void updateEpic(Epic epic) {
        updateMainTaskInfo(epic, epicIdToEpic.get(epic.getId()));
    }

    private void updateMainTaskInfo(Task newTask, Task taskForUpdate) {
        taskForUpdate.setName(newTask.getName());
        taskForUpdate.setDescription(newTask.getDescription());
        taskForUpdate.setStatus(newTask.getStatus());
    }

    private void calcEpicStatus(Epic epic) {
        long newStatusCounter = 0;
        long doneStatusCounter = 0;
        long allSubtaskCounter = 0;

        for (Subtask subtask : subtaskIdToSubtask.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                switch (subtask.getStatus()) {
                    case NEW:
                        newStatusCounter++;
                        break;
                    case IN_PROGRESS:
                        break;
                    case DONE:
                        doneStatusCounter++;
                        break;
                }

                allSubtaskCounter++;
            }
        }

        if (allSubtaskCounter == 0 || newStatusCounter == allSubtaskCounter) {
            epic.setStatus(NEW);
        } else if (doneStatusCounter == allSubtaskCounter) {
            epic.setStatus(DONE);
        } else {
            epic.setStatus(IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }
}

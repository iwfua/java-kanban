package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDeaultHistoryManager();
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    // добавление новых задач
    @Override
    public int addNewTask(Task newTask) {
        newTask.setId(idGenerator());
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int addNewEpic(Epic newEpic) {
        if (newEpic.getId() == null) {
            newEpic.setId(idGenerator());
        } else {
            newEpic.setId(newEpic.getId());
        }
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return 0;
        }
        subtask.setId(idGenerator());
        subtasks.put(id, subtask); // Добавляем подзадачу в мапу subtasks
        epic.addSubtaskId(subtask.getId());
        updateStatus(epic);
        return id;
    }

    // удаление всех задач
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasksOfEpic();
            updateStatus(epic);
        }
        subtasks.clear();
    }

    // получение задачи по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    // получение списка задач определенного эпика
    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        List<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtaskIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }


    // получение списка всех задач
    @Override
    public List<Task> getTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(tasks.get(task.getId()));
        }
        return allTasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epics.get(epic.getId()));
        }
        return allEpics;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> allSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtasks.get(subtask.getId()));
        }
        return allSubtasks;
    }

    // удаление по идентификатору
    @Override
    public void deleteTaskId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(id);
        updateStatus(epic);
    }

    // обновление задачи
    @Override
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateStatus(epic);
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        Epic savedEpic = epics.get(updateEpic.getId());
        if (savedEpic == null) {
            return;
        }
        updateEpic.setSubtaskIds(savedEpic.getSubtaskIds());
        updateEpic.setStatus(savedEpic.getTaskStatus());
        epics.get(updateEpic.getId()).setName(updateEpic.getName());
        epics.get(updateEpic.getId()).setDescription(updateEpic.getDescription());
    }

    //получение истории
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateStatus(Epic epic) {

        if (epic.getSubtaskIds().isEmpty()) {
            for (Integer subtask : epic.getSubtaskIds()) {
                subtasks.get(subtask).setStatus(TaskStatus.NEW);
            }
            return;
        }

        int done = 0;
        int newW = 0;
        int inProgress = 0;


        for (Integer id : epic.getSubtaskIds()) {
            switch (subtasks.get(id).getTaskStatus()) {
                case NEW:
                    newW += 1;
                    break;
                case DONE:
                    done += 1;
                    break;
                case IN_PROGRESS:
                    inProgress += 1;
                    break;
            }
        }

        if ((done > 0 && newW > 0) || inProgress > 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (newW > 0 && done < 0) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    // генерация id
    private int idGenerator() {
        id++;
        return id;
    }
}
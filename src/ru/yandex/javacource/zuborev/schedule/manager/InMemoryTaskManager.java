package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager;
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDeaultHistoryManager();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        // Преобразование TreeSet в список для возврата
        return new ArrayList<>(prioritizedTasks);
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
    public void addNewTask(Task newTask) {
        newTask.setId(idGenerator());
        tasks.put(newTask.getId(), newTask);

        try {
            prioritize(newTask);
        } catch (ManagerSaveException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isOverTimeLap(Task task1, Task task2) {
        final LocalDateTime endTimeTask1 = task1.getEndTime();
        final LocalDateTime startTimeTask2 = task2.getStartTime();

        return endTimeTask1.isAfter(startTimeTask2);
    }

    @Override
    public int addNewEpic(Epic newEpic) {
        final Integer epicId = newEpic.getId();
        if (epicId == null | epics.containsKey(epicId)) {
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
        Integer suntaskId = subtask.getId();
        if (suntaskId == null | subtasks.containsKey(suntaskId)) {
            subtask.setId(idGenerator());
        }
        try {
            prioritize(subtask);
        } catch (ManagerSaveException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId(), subtask.getDuration(), subtask.getStartTime());
        updateEpicDurationAndStatus(epic.getId());
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
        epics.values().forEach(epic -> {
            epic.deleteAllSubtasksOfEpic();
            updateEpicDurationAndStatus(epic.getId());
        });
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
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .toList();
    }

    // получение списка всех задач
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream().toList();
    }

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
        updateEpicDurationAndStatus(epic.getId());
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
        updateEpicDurationAndStatus(epic.getId());
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

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void updateEpicDurationAndStatus(int epicId) {
        Epic epic = epics.get(epicId);
        updateStatus(epic);
        updateEpicDuration(epic);
    }

    private void prioritize(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();
        for (Task t : prioritizedTasks) {
            final LocalDateTime existStart = t.getStartTime();
            final LocalDateTime existEnd = t.getEndTime();
            // newTimeEnd <= existTimeStart
            if (!endTime.isAfter(existStart)) {
                continue;
            }
            // existTimeEnd <= newTimeStart
            if (!existEnd.isAfter(startTime)) {
                continue;
            }
            throw new ManagerSaveException("Задача id=" + task.getId()
                    + " пересекаются с id=" + t.getId() + " c " + existStart + " по " + existEnd);
        }
        prioritizedTasks.add(task);
    }


    private void updateEpicDuration(Epic epic) {
        List<Integer> subs = epic.getSubtaskIds();
        if (subs.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            final LocalDateTime startTime = subtask.getStartTime();
            final LocalDateTime endTime = subtask.getEndTime();
            if (startTime != null && endTime != null && subtask.getDuration() != null) {
                if (startTime.isBefore(start)) {
                    start = startTime;
                }
                if (endTime.isAfter(end)) {
                    end = endTime;
                }
                duration.plus(subtask.getDuration());
            }
        }
        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
    }

    private void updateStatus(Epic epic) {
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        int done = 0;
        int newW = 0;
        int inProgress = 0;

        for (Integer id : epic.getSubtaskIds()) {
            switch (subtasks.get(id).getTaskStatus()) {
                case NEW:
                    newW++;
                    break;
                case DONE:
                    done++;
                    break;
                case IN_PROGRESS:
                    inProgress++;
                    break;
            }
        }

        if (newW > 0 && done == 0 && inProgress == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else if ((done > 0 && newW > 0) || inProgress > 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (newW > 0 && done == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    private int idGenerator() {
        id++;
        return id;
    }
}
package ru.yandex.javacource.zuborev.schedule.task;

import com.sun.jdi.request.DuplicateRequestException;
import ru.yandex.javacource.zuborev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> epicSubtaskIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.epicSubtaskIds = new ArrayList<>();
        setDuration(Duration.ZERO);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.epicSubtaskIds = new ArrayList<>();
        setDuration(Duration.ZERO);
    }

    public void addSubtaskId(int id, Duration subtaskDuration, LocalDateTime subtaskStartTime) {
        epicSubtaskIds.add(id);
        sumDuration(subtaskDuration);
        updateStartTime(subtaskStartTime);
    }

    private void sumDuration(Duration subtaskDuration) {
        if (subtaskDuration != null)  {
            setDuration(getDuration().plus(subtaskDuration));
        }
    }

    private void updateStartTime(LocalDateTime subtaskStartTime) {
        if (subtaskStartTime == null) {
            return;
        }
        if (getStartTime() == null || subtaskStartTime.isBefore(getStartTime())) {
            setStartTime(subtaskStartTime);
        }
        if (getDuration() != null){
            if (endTime == null || subtaskStartTime.plus(getDuration()).isAfter(endTime)) {
                endTime = subtaskStartTime.plus(getDuration());
            }
        }
    }

    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Duration duration1 = Duration.ofMinutes(100);
        LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 11, 12, 13);
        Epic epic = new Epic("name", "desrp", 3);
        System.out.println(epic.endTime + "\n");
        Subtask subtask = new Subtask("name", "descrp", TaskStatus.NEW,localDateTime,duration1,1, epic.getId());
        Subtask subtask1 = new Subtask("name", "descrp", TaskStatus.NEW,1, epic.getId());
        Subtask subtask2 = new Subtask("name", "descrp", TaskStatus.NEW,localDateTime,duration1,1, epic.getId());
        TaskManager taskManager = Managers.getDefaultTaskManager();
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        System.out.println( "startTime" + epic.getStartTime());
        System.out.println(epic.endTime.format(dateTimeFormatter) + "\n");
        taskManager.addNewSubtask(subtask2);
        System.out.println( "startTime" + epic.getStartTime());

        System.out.println(epic.endTime.format(dateTimeFormatter) + "\n");
    }

    public void deleteSubtask(int id) {
        epicSubtaskIds.remove(Integer.valueOf(id));
    }

    public void deleteAllSubtasksOfEpic() {
        epicSubtaskIds.clear();
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(epicSubtaskIds);
    }

    public void setSubtaskIds(List<Integer> epicSubtaskIds) {
        this.epicSubtaskIds = epicSubtaskIds;
    }

    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                "epicSubtaskIds=" + epicSubtaskIds +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + endTime +
                '}';
    }
}
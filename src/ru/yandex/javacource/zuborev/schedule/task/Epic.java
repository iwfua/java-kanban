package ru.yandex.javacource.zuborev.schedule.task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> epicSubtaskIds;
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

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
        if (subtaskDuration != null) {
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
        if (getDuration() != null) {
            if (endTime == null || subtaskStartTime.plus(getDuration()).isAfter(endTime)) {
                endTime = subtaskStartTime.plus(getDuration());
            }
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
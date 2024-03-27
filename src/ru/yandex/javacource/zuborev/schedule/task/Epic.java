package ru.yandex.javacource.zuborev.schedule.task;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> epicSubtaskIds;
    String name;
    String description;
    TaskStatus taskStatus;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.name = name;
        this.description = description;
        epicSubtaskIds = new ArrayList<>();
    }

    public void addNewSubtask(int id){
        epicSubtaskIds.add(Integer.valueOf(id));
    }

    public void deleteSubtask(int id){
        epicSubtaskIds.remove(Integer.valueOf(id));
    }

    public void deleteAllSubtasksOfEpic(){
        epicSubtaskIds.clear();
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(epicSubtaskIds);
    }

    public void setSubtaskIds(List<Integer> epicSubtaskIds) {
        this.epicSubtaskIds = epicSubtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" +  name + '\'' +
                ", description='" + description + '\'' +
                "epicSubtaskIds=" + epicSubtaskIds +
                '}';
    }
}
package ru.yandex.javacource.Zuborev.schedule.task;

import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> epicSubtaskIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
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

    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(epicSubtaskIds);
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.Zuborev.ru.yandex.javacource.Zuborev.schedule.task.Epic{" +
                "epicSubtaskIds=" + epicSubtaskIds +
                '}';
    }
}
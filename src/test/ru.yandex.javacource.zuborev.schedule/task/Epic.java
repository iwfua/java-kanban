package test.ru.yandex.javacource.zuborev.schedule.task;

import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> epicSubtaskIds;

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

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(epicSubtaskIds);
    }

    public void setSubtaskIds(List<Integer> epicSubtaskIds) {
        this.epicSubtaskIds = epicSubtaskIds;
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.Zuborev.schedule.task.Epic{" +
                "epicSubtaskIds=" + epicSubtaskIds +
                '}';
    }
}
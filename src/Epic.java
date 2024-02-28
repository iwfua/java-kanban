import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> epicSubtasks;
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        epicSubtasks = new ArrayList<>();
    }

    public void addNewSubtask(int id){
        epicSubtasks.add(Integer.valueOf(id));
    }

    public void deleteSubtask(int id){
        epicSubtasks.remove(Integer.valueOf(id));
    }

    public void deleteAllSubtasksOfEpic(){
        epicSubtasks.clear();
    }

    public ArrayList<Integer> getEpicSubtasks() {
        return new ArrayList<>(epicSubtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicSubtasks=" + epicSubtasks +
                '}';
    }
}
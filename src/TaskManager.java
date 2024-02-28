import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    // генерация id
    private int IdGenerator() {
        id++;
        return id;
    }

    // добавление новых задач
    public int addNewTask(Task newTask) {
        newTask.setId(IdGenerator());
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public int addNewEpic(Epic newEpic) {
        newEpic.setId(IdGenerator());
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    public void addNewSubTask(Subtask subtask) { //метод добавления новой подзадачи.
        subtask.setId(IdGenerator());
        subtasks.put(subtask.getId(), subtask); //добавление сабтаски в мапу по заранее сгенереному id
        Epic epic = epics.get(subtask.getEpicId()); //получение эпика, к которому принадлежит сабтаска
        epic.addNewSubtask(subtask.getId()); //занесение id сабтаски в лист сабтасок эпика
        updateStatus(getEpicById(subtask.getEpicId()));
    }

    // изменение статуса задачи
    public void updateStatus(Epic epic) {

        if (epic.getEpicSubtasks().isEmpty()) {
            for (Integer subtask: epic.getEpicSubtasks()) {
                subtasks.get(subtask).setTaskStatus(TaskStatus.NEW);
            }
            return;
        }

        int done = 0;
        int New = 0;
        int inProgress = 0;


        for (Integer id : epic.getEpicSubtasks()) {
            switch (subtasks.get(id).getTaskStatus()) {
                case NEW:
                    New += 1;
                    break;
                case DONE:
                    done += 1;
                    break;
                case IN_PROGRESS:
                    inProgress += 1;
                    break;
            }
        }

        if ((done > 0 && New > 0) || inProgress > 0) {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            return;
        } else if (New > 0 && done < 0) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            epic.setTaskStatus(TaskStatus.DONE);
        }




    }
    // удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasksOfEpic();
            updateStatus(epic);
        }
        subtasks.clear();
    }


    // получение задачи по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // получение списка задач определенного эпика
    public ArrayList<Integer> getSubtasksOfEpic(int epicId) {
        return new ArrayList<>(epics.get(epicId).getEpicSubtasks());
    }


    // получение списка всех задач
    public ArrayList<Task> getFullTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(tasks.get(task.getId()));
        }
        return allTasks;
    }

    public ArrayList<Epic> getFullEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epics.get(epic.getId()));
        }
        return allEpics;
    }

    public ArrayList<Subtask> getFullSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtasks.get(subtask.getId()));
        }
        return allSubtasks;
    }

    // удаление по идентификатору

    public void deleteTaskId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteEpicId(int id){
        if(epics.containsKey(id)) {
            for (Subtask subtask : subtasks.values()) {
                if(subtask.getEpicId() == id){
                    subtasks.remove(subtask.getId());
                }
            }
            epics.remove(Integer.valueOf(id));
        }
    }

    public void deleteSubtaskId(int id) {
        epics.get(subtasks.get(id).getEpicId()).getEpicSubtasks().remove(Integer.valueOf(id));
        if (subtasks.containsKey(id)) {
            subtasks.remove(Integer.valueOf(id));
        }
        updateStatus(epics.get(subtasks.get(id).getEpicId()));
    }


    // обновление задачи
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    public void updateSubtask(Subtask updateSubtask) {
        if ((subtasks.containsKey(updateSubtask.getId())) && (subtasks.get(updateSubtask.getId()).getEpicId() == updateSubtask.getEpicId())) {
            subtasks.put(updateSubtask.getId(), updateSubtask);
            updateStatus(epics.get(updateSubtask.getEpicId()));
        }
    }

    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.get(updateEpic.getId()).setName(updateEpic.getName());
            epics.get(updateEpic.getId()).setDescription(updateEpic.getDescription());
        }
    }
}
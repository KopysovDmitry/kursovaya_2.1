package service;

import exeption.TaskNotFoundException;
import model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskService {
    private static final Map<Integer, Task> TASKS = new HashMap<>();

    private TaskService(){

    }
    public static void add(Task task){
        TASKS.put(task.getId(), task);
    }
    public static Collection<Task> getTasksByDay (LocalDate day){
        Collection<Task> tasksByDay = new ArrayList<>();
        Collection<Task> allTasks = TASKS.values();
        for (Task task: allTasks){
            LocalDateTime currentDateTime = task.getDateTime();
            LocalDateTime nextDateTime = task.getRepeatability().nextTime(currentDateTime);
            if (nextDateTime == null || currentDateTime.toLocalDate().equals(day)){
                tasksByDay.add(task);
                continue;
            }
            do {
                if (nextDateTime.toLocalDate().equals(day)){
                    tasksByDay.add(task);
                    break;
                }
                nextDateTime = task.getRepeatability().nextTime(nextDateTime);
            }while (nextDateTime.toLocalDate().isBefore(day));
        }
        return tasksByDay;
    }

    public static void removeById(int id) throws TaskNotFoundException {
        if (TASKS.remove(id)==null){
            throw new TaskNotFoundException(id);
        }
    }
}

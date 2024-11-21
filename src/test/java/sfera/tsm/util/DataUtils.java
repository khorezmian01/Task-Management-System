package sfera.tsm.util;

import sfera.tsm.entity.Task;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;

public class DataUtils {


    public static Task getTask1Transient(){
        return Task.builder()
                .title("Task1")
                .description("task1")
                .status(Status.WAITING)
                .priority(Priority.LOW)
                .build();
    }

    public static Task getTask1Persisted(){
        return Task.builder()
                .id(1L)
                .title("Task1")
                .description("task1")
                .status(Status.WAITING)
                .priority(Priority.LOW)
                .build();
    }

    public static Task getTask2Persisted(){
        return Task.builder()
                .id(2L)
                .title("Task2")
                .description("task2")
                .status(Status.WAITING)
                .priority(Priority.MEDIUM)
                .build();
    }

    public static Task getTask3Persisted(){
        return Task.builder()
                .id(3L)
                .title("Task3")
                .description("task3")
                .status(Status.WAITING)
                .priority(Priority.MEDIUM)
                .build();
    }
}

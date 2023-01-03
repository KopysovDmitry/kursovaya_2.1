import exeption.IncorrectTaskParameterException;
import exeption.TaskNotFoundException;
import model.*;
import service.TaskService;
import util.Constant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");

    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
                label:
                while (true) {
                    printMenu();
                    System.out.print("Выберите пункт меню: ");
                    if (scanner.hasNextInt()) {
                        int menu = scanner.nextInt();
                        switch (menu) {
                            case 1:
                                addTask(scanner);
                                break;
                            case 2:
                                removeTask(scanner);
                                break;
                            case 3:
                                printTasksByDay(scanner);
                                break;
                            case 0:
                                break label;
                        }
                    } else {
                        scanner.next();
                        System.out.println("Выберите пункт меню из списка!");
                    }
                }
    }

    private static void printTasksByDay(Scanner scanner) {
            do {
                System.out.println("Введите дату задачи в формате: дд.мм.гггг: ");
                if (!scanner.hasNext(DATE_PATTERN)) {
                    LocalDate day = parseDate(scanner.next(DATE_PATTERN));
                    if (day == null){
                        System.out.println("Некорректный формат даты");
                        continue;
                    }
                    Collection<Task> taskByDay = TaskService.getTasksByDay(day);
                    if (taskByDay.isEmpty()){
                        System.out.println("Задачи на"+day.format(Constant.DATE_FORMATTER)+"не найдены");
                    }
                    System.out.println("Задачи на "+day.format(Constant.DATE_FORMATTER)+":");
                    for (Task task: taskByDay){
                        System.out.println(task);
                    }
                    break;
                } else {
                    scanner.next();
                }
            }while (true);

    }

    private static void removeTask(Scanner scanner)  {
        try {
            do {
                System.out.println("Введите id задачи: ");
                if (!scanner.hasNextInt()) {
                    int id = scanner.nextInt();
                    TaskService.removeById(id);
                    System.out.println("Задача с id "+id+"удалена");
                    break;
                }else {
                    scanner.next();
                }
            }while (true);
        }catch (TaskNotFoundException e){
            System.out.println(e.getMessage());;
        }

    }


    private static void addTask(Scanner scanner) {
            try {
                System.out.print("Введите название задачи: ");
                String title = scanner.next();
                System.out.print("Введите описание задачи: ");
                String description = scanner.next();
                Type type = inputType(scanner);
                LocalDateTime dateTime = inputDateTime(scanner);
                Repeatability repeatability = inputRapeatability(scanner);
                Task task = new Task(title,description,type,dateTime,repeatability);
                TaskService.add(task);
                System.out.println("Задача "+task+" добавлена");
            } catch (IncorrectTaskParameterException e) {
                System.out.println(e.getMessage());
            }
    }

    private static Type inputType(Scanner scanner){
                Type type;
                do {
                    System.out.println("Введите тип задачи:\n1.Личная\n2.Рабочая\nТип задачи: ");
                    if (!scanner.hasNextInt()) {
                        int number = scanner.nextInt();
                        if (number != 1 && number != 2) {
                            System.out.println("Введите 1 или 2");
                            continue;
                        }
                        if (number == 1) {
                            type = Type.PERSONAL;
                        }else {
                            type = Type.WORK;
                        }
                        break;
                    }else {
                        scanner.next();
                    }
                }while (true);
            return type;
    }
    private static LocalDateTime inputDateTime(Scanner scanner){
        LocalDateTime dateTime;
        do {
            System.out.println("Введите дату и время задачи в формате: \"дд.мм.гггг чч:мм\"");
            if (!scanner.hasNext(DATE_TIME_PATTERN)) {
               dateTime = parseDateTime(scanner.next(DATE_TIME_PATTERN));
                if (dateTime ==null){
                    System.out.println("Некорректный формат даты и времени");
                    continue;
                }
                break;
            }else {
                scanner.next();
            }
        }while (true);
        return dateTime;
    }
    private static LocalDateTime parseDateTime(String dateTime){
        try {
            return LocalDateTime.parse(dateTime, Constant.DATE_TIME_FORMATTER);
        }catch (DateTimeParseException e){
            return null;
        }
    }
    private static LocalDate parseDate(String date){
        try {
            return LocalDate.parse(date, Constant.DATE_FORMATTER);
        }catch (DateTimeParseException e){
            return null;
        }
    }
    private static Repeatability inputRapeatability(Scanner scanner){
        Repeatability repeatability = null;
        do {
            System.out.println("Введите тип повторяемости задачи: 1. Однократная\n2. Ежедневная\n3. Еженедельная\n4. Ежемесячная\n5. Ежегодная ");
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                if (number <1 || number >5){
                    System.out.println("Введите число от 1 до 5");
                    continue;
                }
                switch (number){
                    case 1:
                        repeatability = new OneTime();
                        break;
                    case 2:
                        repeatability = new Daily();
                        break;
                    case 3:
                        repeatability= new Weekly();
                        break;
                    case 4:
                        repeatability = new Monthly();
                        break;
                    case 5:
                        repeatability = new Yearly();
                        break;

                }
                break;
            }else {
                scanner.next();
            }
        }while (true);
        return repeatability;
    }

    private static void printMenu() {
            System.out.println(
                    """
                            1. Добавить задачу
                            2. Удалить задачу
                            3. Получить задачу на указанный день
                            0. Выход
                            """
            );
    }

}
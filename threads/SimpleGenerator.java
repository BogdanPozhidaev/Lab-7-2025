package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                // Проверка прерывания
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Generator interrupted");
                    return;
                }

                // Случайные параметры
                double base = 1 + random.nextDouble() * 9;
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                // Установка задания
                task.setTask(new Log(base), leftBorder, rightBorder, step);

                // Вывод исходных данных
                System.out.printf("Generator %d: Source %.4f %.4f %.4f%n",
                        i + 1, leftBorder, rightBorder, step);

                // Задержка 10 мс
                Thread.sleep(10);

            } catch (Exception e) {
                System.err.println("Generator error: " + e.getMessage());
                break;
            }
        }

        System.out.println("Generator completed all tasks");
    }
}
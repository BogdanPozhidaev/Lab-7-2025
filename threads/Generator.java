package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Generator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run() {
        Random random = new Random();

        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверка прерывания
                if (isInterrupted()) {
                    System.out.println("Generator interrupted");
                    return;
                }

                // Ждем разрешения на генерацию данных
                dataProcessed.acquire();

                // Генерация новых данных
                double base = 1 + random.nextDouble() * 9;
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                // Установка данных в задание
                task.setFunction(new Log(base));
                task.setLeftBorder(leftBorder);
                task.setRightBorder(rightBorder);
                task.setDiscretizationStep(step);


                // Вывод исходных данных
                System.out.printf("Generator %d: Source %.4f %.4f %.4f%n",
                        i + 1, leftBorder, rightBorder, step);

                // Сигнализируем, что данные готовы
                dataReady.release();

                Thread.sleep(10);
            }

            System.out.println("Generator completed all tasks");

        } catch (InterruptedException e) {
            System.out.println("Generator interrupted during operation");
        }
    }
}
package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Integrator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверка прерывания
                if (isInterrupted()) {
                    System.out.println("Integrator interrupted");
                    return;
                }

                // Ждем, пока данные будут готовы
                dataReady.acquire();

                // Получение параметров
                double leftBorder = task.getLeftBorder();
                double rightBorder = task.getRightBorder();
                double step = task.getDiscretizationStep();

                // Вычисление интеграла
                double result = Functions.integrate(task.getFunction(), leftBorder, rightBorder, step);

                // Вывод результата
                System.out.printf("Integrator %d: Result %.4f %.4f %.4f %.8f%n", i + 1, leftBorder, rightBorder, step, result);

                // Сигнализируем, что данные обработаны
                dataProcessed.release();

                Thread.sleep(10);
            }

            System.out.println("Integrator completed");

        } catch (InterruptedException e) {
            System.out.println("Integrator interrupted during operation");
        }
    }
}
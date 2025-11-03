package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        while (!task.isCompleted()) {
            try {
                // Проверка прерывания
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Integrator interrupted");
                    return;
                }

                // Ждем новое задание
                task.getTask();

                if (task.isReady()) {
                    // Получение параметров
                    double leftBorder = task.getLeftBorder();
                    double rightBorder = task.getRightBorder();
                    double step = task.getDiscretizationStep();

                    // Вычисление интеграла
                    double result = Functions.integrate(task.getFunction(), leftBorder, rightBorder, step);

                    // Вывод результата
                    System.out.printf("Integrator %d: Result %.4f %.4f %.4f %.8f%n",
                            task.getCurrentTask(), leftBorder, rightBorder, step, result);

                    // Помечаем задание как обработанное
                    task.taskProcessed();

                    // Задержка 10 мс
                    Thread.sleep(10);
                }

            } catch (Exception e) {
                System.err.println("Integrator error: " + e.getMessage());
                break;
            }
        }

        System.out.println("Integrator completed");
    }
}
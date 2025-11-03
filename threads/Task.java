package threads;

import functions.Function;
import functions.basic.Log;

public class Task {
    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double discretizationStep;
    private int tasksCount;
    private int currentTask = 0;
    private boolean isReady = false;

    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    // Геттеры
    public Function getFunction() { return function; }
    public double getLeftBorder() { return leftBorder; }
    public double getRightBorder() { return rightBorder; }
    public double getDiscretizationStep() { return discretizationStep; }
    public int getTasksCount() { return tasksCount; }
    public int getCurrentTask() { return currentTask; }
    public boolean isReady() { return isReady; }

    // Сеттеры
    public void setFunction(Function function) { this.function = function; }
    public void setLeftBorder(double leftBorder) { this.leftBorder = leftBorder; }
    public void setRightBorder(double rightBorder) { this.rightBorder = rightBorder; }
    public void setDiscretizationStep(double discretizationStep) { this.discretizationStep = discretizationStep; }
    public void setTasksCount(int tasksCount) { this.tasksCount = tasksCount; }

    // Синхронизированные методы для взаимодействия потоков
    public synchronized void setTask(Function function, double leftBorder, double rightBorder, double step) {
        while (isReady) {
            try {
                wait(); // Ждем, пока предыдущее задание не будет обработано
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        this.function = function;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.discretizationStep = step;
        this.isReady = true;
        currentTask++;

        notifyAll(); // Уведомляем интегратор, что задание готово
    }

    public synchronized void getTask() {
        while (!isReady && currentTask < tasksCount) {
            try {
                wait(); // Ждем, пока появится новое задание
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized void taskProcessed() {
        isReady = false;
        notifyAll(); // Уведомляем генератор, что задание обработано
    }

    public synchronized boolean isCompleted() {
        return currentTask >= tasksCount && !isReady;
    }
}
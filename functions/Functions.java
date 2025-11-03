package functions;

import functions.meta.*;

public class Functions {
    // Приватный конструктор для запрета создания объектов
    private Functions() {
        throw new AssertionError("Нельзя создать объект класса Functions");
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
    public static double integrate(Function function, double leftX, double rightX, double step)
            throws IllegalArgumentException {

        // Проверка границ области интегрирования
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Интервал интегрирования [" + leftX + ", " + rightX + "] " +
                            "выходит за область определения функции [" +
                            function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]"
            );
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double integral = 0.0;
        double currentX = leftX;

        // Проходим по всем участкам
        while (currentX < rightX) {
            double nextX = Math.min(currentX + step, rightX);
            double segmentStep = nextX - currentX;

            double y1 = function.getFunctionValue(currentX);
            double y2 = function.getFunctionValue(nextX);

            // Если какое-то из значений не определено, интеграл тоже не определен
            if (Double.isNaN(y1) || Double.isNaN(y2)) {
                return Double.NaN;
            }

            // Площадь трапеции
            integral += (y1 + y2) * segmentStep / 2.0;
            currentX = nextX;
        }

        return integral;
    }
}
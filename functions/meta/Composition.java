package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function f1; // внешняя функция
    private Function f2; // внутренняя функция

    public Composition(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        return f2.getLeftDomainBorder(); // область определения композиции = области определения внутренней функции
    }

    public double getRightDomainBorder() {
        return f2.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        // Вычисляем внутреннюю функцию
        double innerValue = f2.getFunctionValue(x);

        // Проверяем, определено ли значение внутренней функции
        if (Double.isNaN(innerValue)) {
            return Double.NaN;
        }

        // Проверяем, попадает ли результат в область определения внешней функции
        if (innerValue < f1.getLeftDomainBorder() || innerValue > f1.getRightDomainBorder()) {
            return Double.NaN;
        }

        // Вычисляем внешнюю функцию от результата внутренней
        return f1.getFunctionValue(innerValue);
    }
}
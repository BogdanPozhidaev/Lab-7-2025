package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f;
    private double power;

    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double value = f.getFunctionValue(x);
        // Проверка на возможность возведения в степень
        if (value < 0 && power != (int)power) {
            return Double.NaN; // Отрицательное число в дробной степени
        }
        return Math.pow(value, power);
    }
}
package functions;

public interface Function {
    double getLeftDomainBorder();

    // Получение правой границы области определения
    double getRightDomainBorder();

    // Получение значения функции в точке
    double getFunctionValue(double x);
}

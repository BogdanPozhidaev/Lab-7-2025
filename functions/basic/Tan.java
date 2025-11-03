package functions.basic;

public class Tan extends TrigonometricFunction {
    public double getFunctionValue(double x) {
        // Тангенс не определен в точках, где косинус равен 0
        double cosX = Math.cos(x);
        if (Math.abs(cosX) < 1e-10) {
            return Double.NaN;
        }
        return Math.tan(x);
    }
}
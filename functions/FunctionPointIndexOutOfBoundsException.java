// FunctionPointIndexOutOfBoundsException.java
package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException() {
        super();
    }

    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }

    // Конструктор с номером индекса для удобства
    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Индекс выходит за границы: " + index);
    }
}
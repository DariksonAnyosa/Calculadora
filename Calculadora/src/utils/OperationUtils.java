package utils;

public class OperationUtils {
    public static double performOperation(double num1, double num2, char operator) {
        double result = 0;
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    throw new ArithmeticException("No se puede dividir por cero");
                }
                break;
            default:
                throw new UnsupportedOperationException("Operador no soportado");
        }
        return result;
    }
}

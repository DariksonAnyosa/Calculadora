import utils.ThemeUtils;
import utils.OperationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScientificCalculatorGUI extends JFrame {
    private JTextField displayNumbers;
    private JTextArea displayOperations;
    private StringBuilder currentOperation;
    private double num1, num2, result;
    private char operator;
    private boolean isDarkMode = false;
    private boolean isFirstNumber = true;

    public ScientificCalculatorGUI() {
        setTitle("Calculadora Científica");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        displayNumbers = new JTextField();
        displayNumbers.setEditable(false);
        displayNumbers.setFont(new Font("Arial", Font.PLAIN, 24));
        displayNumbers.setHorizontalAlignment(JTextField.RIGHT);

        displayOperations = new JTextArea();
        displayOperations.setEditable(false);
        displayOperations.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayOperations);
        scrollPane.setPreferredSize(new Dimension(400, 100));

        JPanel panelNumbers = new JPanel(new GridLayout(5, 4, 5, 5));
        String[] numberButtons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String text : numberButtons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(new ButtonClickListener());
            panelNumbers.add(button);
        }

        JPanel panelFunctions = new JPanel(new GridLayout(6, 4, 5, 5));

        String[] functionButtons = {
                "C", "sin", "cos", "tan",
                "log", "ln", "sqrt", "x^2",
                "Dark"
        };

        for (String text : functionButtons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(new ButtonClickListener());
            panelFunctions.add(button);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(displayNumbers, BorderLayout.NORTH);
        mainPanel.add(panelNumbers, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        JPanel functionsPanel = new JPanel(new BorderLayout());
        functionsPanel.add(panelFunctions, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(functionsPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void calculate() {
        try {
            String operation = currentOperation.toString().trim();

            // Encontrar el índice del operador
            int operatorIndex = findOperatorIndex(operation);

            if (operatorIndex == -1) {
                // No se encontró un operador válido
                throw new NumberFormatException();
            }

            // Obtener los números y el operador
            num2 = Double.parseDouble(operation.substring(operatorIndex + 1).trim());
            operator = operation.charAt(operatorIndex);

            if (isFirstNumber) {
                // Si es la primera operación
                num1 = Double.parseDouble(operation.substring(0, operatorIndex).trim());
                isFirstNumber = false;
            } else {
                // Si no es la primera operación, usar el resultado anterior como num1
                num1 = result;
            }

            // Realizar la operación
            result = OperationUtils.performOperation(num1, num2, operator);

            // Construir la cadena de operación completa para el historial
            String fullOperation = num1 + " " + operator + " " + num2 + " = " + result;

            // Actualizar historial y mostrar resultado
            displayOperations.append(fullOperation + "\n");
            displayNumbers.setText(String.format("%.2f", result));

            // Limpiar la operación actual después de calcular
            currentOperation.setLength(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada no válida", "Error", JOptionPane.ERROR_MESSAGE);
            currentOperation.setLength(0); // Limpiar la operación actual en caso de error
        }
    }

    private int findOperatorIndex(String operation) {
        char[] operators = {'+', '-', '*', '/'};
        int operatorIndex = -1;
        for (char op : operators) {
            operatorIndex = operation.lastIndexOf(op);
            if (operatorIndex != -1) {
                break;
            }
        }
        return operatorIndex;
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            try {
                switch (command) {
                    case "C":
                        currentOperation.setLength(0);
                        displayNumbers.setText("");
                        isFirstNumber = true; // Reiniciar el estado de cálculo
                        break;
                    case "=":
                        if (currentOperation.length() > 0) {
                            calculate();
                        }
                        break;
                    case "sin":
                    case "cos":
                    case "tan":
                    case "log":
                    case "ln":
                    case "sqrt":
                    case "x^2":
                        handleUnaryOperation(command);
                        break;
                    case "Dark":
                        toggleDarkMode();
                        break;
                    default:
                        handleNumericInput(command);
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ScientificCalculatorGUI.this, "Entrada no válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleUnaryOperation(String operation) {
        if (!displayNumbers.getText().isEmpty()) {
            num1 = Double.parseDouble(displayNumbers.getText());
            switch (operation) {
                case "sin":
                    result = Math.sin(Math.toRadians(num1));
                    break;
                case "cos":
                    result = Math.cos(Math.toRadians(num1));
                    break;
                case "tan":
                    result = Math.tan(Math.toRadians(num1));
                    break;
                case "log":
                    result = Math.log10(num1);
                    break;
                case "ln":
                    result = Math.log(num1);
                    break;
                case "sqrt":
                    result = Math.sqrt(num1);
                    break;
                case "x^2":
                    result = Math.pow(num1, 2);
                    break;
            }
            displayNumbers.setText(String.format("%.2f", result));
            displayOperations.append(operation + "(" + num1 + ") = " + result + "\n");
            currentOperation.setLength(0); // Limpiar la operación actual después de una operación unaria
        }
    }

    private void handleNumericInput(String input) {
        if (currentOperation == null) {
            currentOperation = new StringBuilder(); // Inicializar si es nulo
        }

        if (Character.isDigit(input.charAt(0)) || input.equals(".")) {
            currentOperation.append(input);
            updateDisplay();
        } else {
            if (!currentOperation.toString().isEmpty()) {
                currentOperation.append(" ").append(input).append(" ");
                updateDisplay();
            }
        }
    }

    private void updateDisplay() {
        if (currentOperation != null) {
            displayNumbers.setText(currentOperation.toString());
        }
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            ThemeUtils.applyDarkTheme(ScientificCalculatorGUI.this);
        } else {
            ThemeUtils.applyLightTheme(ScientificCalculatorGUI.this);
        }
    }

    public static void main(String[] args) {
        new ScientificCalculatorGUI();
    }
}

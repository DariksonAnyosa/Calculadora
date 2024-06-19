import utils.OperationUtils;
import utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI extends JFrame {
    private JTextField display;
    private JTextArea history;
    private StringBuilder currentOperation;
    private double num1, num2, result;
    private char operator;
    private boolean isDarkMode = false;

    public CalculatorGUI() {
        setTitle("Calculadora Única");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);

        history = new JTextArea();
        history.setEditable(false);
        history.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(history);
        scrollPane.setPreferredSize(new Dimension(400, 100));

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5)); // GridLayout para los botones

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "Hist", "Dark", "Mod"
        };

        currentOperation = new StringBuilder();

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(display, BorderLayout.NORTH);
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.SOUTH);

        add(contentPanel); // Añadir el panel principal al JFrame

        setVisible(true);
    }

    private void updateDisplay() {
        display.setText(currentOperation.toString());
    }

    private void calculate() {
        try {
            // Obtener el segundo número y el operador
            String operation = currentOperation.toString().trim();
            int operatorIndex = findOperatorIndex(operation);

            if (operatorIndex == -1) {
                JOptionPane.showMessageDialog(this, "Entrada no válida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            num1 = Double.parseDouble(operation.substring(0, operatorIndex).trim());
            operator = operation.charAt(operatorIndex);
            num2 = Double.parseDouble(operation.substring(operatorIndex + 1).trim());

            // Realizar la operación
            result = OperationUtils.performOperation(num1, num2, operator);

            // Construir la cadena de operación completa para el historial
            String fullOperation = num1 + " " + operator + " " + num2 + " = " + result;

            // Actualizar historial y mostrar resultado
            history.append(fullOperation + "\n");
            display.setText(String.valueOf(result));

            // Limpiar la operación actual después de calcular
            currentOperation.setLength(0);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
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

            switch (command) {
                case "C":
                    currentOperation.setLength(0);
                    updateDisplay();
                    display.setText(""); // Limpiar el display al presionar "C"
                    break;
                case "=":
                    calculate();
                    break;
                case "Hist":
                    JOptionPane.showMessageDialog(null, history.getText(), "Historial", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "Dark":
                    isDarkMode = !isDarkMode;
                    if (isDarkMode) {
                        ThemeUtils.applyDarkTheme(CalculatorGUI.this);
                    } else {
                        ThemeUtils.applyLightTheme(CalculatorGUI.this);
                    }
                    break;
                case "Mod":
                    new ScientificCalculatorGUI();
                    break;
                default:
                    if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                        currentOperation.append(command);
                        updateDisplay();
                    } else {
                        if (!currentOperation.toString().isEmpty()) {
                            currentOperation.append(" ").append(command).append(" ");
                            updateDisplay();
                        }
                    }
                    break;
            }
        }
    }

}

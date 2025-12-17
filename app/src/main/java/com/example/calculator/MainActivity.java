package com.example.calculator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private EditText expressionInput;
    private TextView resultDisplay;
    
    // Calculator State
    private String currentExpression = "";
    private String lastResult = "0";
    private boolean isResultDisplayed = false;
    private boolean hasError = false;
    
    // Number formatting
    private DecimalFormat decimalFormat;
    private MathContext mathContext;
    
    // Animation
    private Handler animationHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize components
        initializeViews();
        setupWindowInsets();
        setupNumberFormatting();
        setupAnimationHandler();
        setupButtonListeners();
        setupExpressionWatcher();
        
        // Initialize display
        updateDisplay();
    }
    
    private void initializeViews() {
        expressionInput = findViewById(R.id.expression_input);
        resultDisplay = findViewById(R.id.result_display);
        
        // Disable keyboard input for expression field
        if (expressionInput != null) {
            expressionInput.setShowSoftInputOnFocus(false);
            expressionInput.setFocusable(false);
            expressionInput.setClickable(false);
        }
    }
    
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void setupNumberFormatting() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormat = new DecimalFormat("#,##0.##########", symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        mathContext = new MathContext(15, RoundingMode.HALF_UP);
    }
    
    private void setupAnimationHandler() {
        animationHandler = new Handler(Looper.getMainLooper());
    }
    
    private void setupButtonListeners() {
        // Number buttons
        setButtonListener(R.id.btn_0);
        setButtonListener(R.id.btn_1);
        setButtonListener(R.id.btn_2);
        setButtonListener(R.id.btn_3);
        setButtonListener(R.id.btn_4);
        setButtonListener(R.id.btn_5);
        setButtonListener(R.id.btn_6);
        setButtonListener(R.id.btn_7);
        setButtonListener(R.id.btn_8);
        setButtonListener(R.id.btn_9);
        
        // Operator buttons
        setButtonListener(R.id.btn_add);
        setButtonListener(R.id.btn_subtract);
        setButtonListener(R.id.btn_multiply);
        setButtonListener(R.id.btn_divide);
        setButtonListener(R.id.btn_percentage);
        
        // Function buttons
        setButtonListener(R.id.btn_sqrt);
        setButtonListener(R.id.btn_power);
        setButtonListener(R.id.btn_decimal);
        setButtonListener(R.id.btn_parentheses);
        
        // Control buttons
        setButtonListener(R.id.btn_clear);
        setButtonListener(R.id.btn_delete);
        setButtonListener(R.id.btn_equals);
    }
    
    private void setButtonListener(int buttonId) {
        MaterialButton button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(this);
        }
    }
    
    private void setupExpressionWatcher() {
        if (expressionInput != null) {
            expressionInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    currentExpression = s.toString();
                    if (!hasError && !currentExpression.isEmpty()) {
                        calculatePreview();
                    }
                }
                
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        // Add button press animation
        animateButtonPress((MaterialButton) v);
        
        // Handle button clicks
        if (id == R.id.btn_clear) {
            handleClear();
        } else if (id == R.id.btn_delete) {
            handleDelete();
        } else if (id == R.id.btn_equals) {
            handleEquals();
        } else if (id == R.id.btn_0 || id == R.id.btn_1 || id == R.id.btn_2 || 
                   id == R.id.btn_3 || id == R.id.btn_4 || id == R.id.btn_5 || 
                   id == R.id.btn_6 || id == R.id.btn_7 || id == R.id.btn_8 || 
                   id == R.id.btn_9) {
            handleNumber(getButtonText(id));
        } else if (id == R.id.btn_decimal) {
            handleDecimal();
        } else if (id == R.id.btn_add || id == R.id.btn_subtract || 
                   id == R.id.btn_multiply || id == R.id.btn_divide) {
            handleBasicOperator(getOperatorSymbol(id));
        } else if (id == R.id.btn_percentage) {
            handlePercentage();
        } else if (id == R.id.btn_sqrt) {
            handleSquareRoot();
        } else if (id == R.id.btn_power) {
            handlePower();
        } else if (id == R.id.btn_parentheses) {
            handleParentheses();
        }
        
        updateDisplay();
    }
    
    private void animateButtonPress(MaterialButton button) {
        // Scale animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1.0f, 0.95f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1.0f, 0.95f, 1.0f);
        
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        
        scaleX.start();
        scaleY.start();
        
        // Elevation animation
        ValueAnimator elevationAnimator = ValueAnimator.ofFloat(
            button.getElevation(), 
            button.getElevation() + 2f, 
            button.getElevation()
        );
        elevationAnimator.setDuration(150);
        elevationAnimator.addUpdateListener(animation -> 
            button.setElevation((Float) animation.getAnimatedValue()));
        elevationAnimator.start();
    }
    
    private String getButtonText(int buttonId) {
        if (buttonId == R.id.btn_0) return "0";
        if (buttonId == R.id.btn_1) return "1";
        if (buttonId == R.id.btn_2) return "2";
        if (buttonId == R.id.btn_3) return "3";
        if (buttonId == R.id.btn_4) return "4";
        if (buttonId == R.id.btn_5) return "5";
        if (buttonId == R.id.btn_6) return "6";
        if (buttonId == R.id.btn_7) return "7";
        if (buttonId == R.id.btn_8) return "8";
        if (buttonId == R.id.btn_9) return "9";
        return "";
    }
    
    private String getOperatorSymbol(int buttonId) {
        if (buttonId == R.id.btn_add) return "+";
        if (buttonId == R.id.btn_subtract) return "-";
        if (buttonId == R.id.btn_multiply) return "×";
        if (buttonId == R.id.btn_divide) return "÷";
        return "";
    }
    
    private void handleClear() {
        currentExpression = "";
        lastResult = "0";
        isResultDisplayed = false;
        hasError = false;
        animateResultChange();
    }
    
    private void handleDelete() {
        if (hasError) {
            handleClear();
            return;
        }
        
        if (isResultDisplayed) {
            currentExpression = lastResult;
            isResultDisplayed = false;
        }
        
        if (!currentExpression.isEmpty()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
        }
    }
    
    private void handleEquals() {
        if (currentExpression.isEmpty()) return;
        
        try {
            String result = evaluateExpression(currentExpression);
            lastResult = result;
            isResultDisplayed = true;
            hasError = false;
            animateResultChange();
        } catch (Exception e) {
            showError("Invalid expression");
        }
    }
    
    private void handleNumber(String number) {
        if (hasError) {
            handleClear();
        }
        
        if (isResultDisplayed) {
            currentExpression = "";
            isResultDisplayed = false;
        }
        
        currentExpression += number;
    }
    
    private void handleDecimal() {
        if (hasError) {
            handleClear();
        }
        
        if (isResultDisplayed) {
            currentExpression = "0";
            isResultDisplayed = false;
        }
        
        // Check if current number already has decimal
        String[] parts = currentExpression.split("[+\\-×÷]");
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            if (!lastPart.contains(".")) {
                if (lastPart.isEmpty() || isOperator(lastPart.charAt(lastPart.length() - 1))) {
                    currentExpression += "0.";
                } else {
                    currentExpression += ".";
                }
            }
        }
    }
    
    private void handleBasicOperator(String operator) {
        if (hasError) return;
        
        if (isResultDisplayed) {
            currentExpression = lastResult;
            isResultDisplayed = false;
        }
        
        if (currentExpression.isEmpty()) {
            if (operator.equals("-")) {
                currentExpression = "-";
            }
            return;
        }
        
        // Replace last operator if expression ends with operator
        char lastChar = currentExpression.charAt(currentExpression.length() - 1);
        if (isOperator(lastChar)) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
        }
        
        currentExpression += operator;
    }
    
    private void handlePercentage() {
        if (hasError || currentExpression.isEmpty()) return;
        
        try {
            String result = evaluateExpression(currentExpression);
            double value = Double.parseDouble(result);
            value = value / 100.0;
            currentExpression = formatNumber(value);
            isResultDisplayed = true;
        } catch (Exception e) {
            showError("Invalid expression");
        }
    }
    
    private void handleSquareRoot() {
        if (hasError) return;
        
        if (currentExpression.isEmpty()) {
            currentExpression = "√(";
        } else if (isResultDisplayed) {
            try {
                double value = Double.parseDouble(lastResult);
                if (value < 0) {
                    showError(getString(R.string.error_sqrt_negative));
                    return;
                }
                double result = Math.sqrt(value);
                currentExpression = formatNumber(result);
                lastResult = currentExpression;
                isResultDisplayed = true;
            } catch (Exception e) {
                showError(getString(R.string.error_invalid_operation));
            }
        } else {
            currentExpression += "√(";
        }
    }
    
    private void handlePower() {
        if (hasError) return;
        
        if (isResultDisplayed) {
            currentExpression = lastResult;
            isResultDisplayed = false;
        }
        
        if (!currentExpression.isEmpty()) {
            currentExpression += "²";
        }
    }
    
    private void handleParentheses() {
        if (hasError) {
            handleClear();
        }
        
        if (isResultDisplayed) {
            currentExpression = "";
            isResultDisplayed = false;
        }
        
        // Smart parentheses handling
        int openCount = countChar(currentExpression, '(');
        int closeCount = countChar(currentExpression, ')');
        
        if (currentExpression.isEmpty() || 
            isOperator(currentExpression.charAt(currentExpression.length() - 1)) ||
            currentExpression.endsWith("(")) {
            currentExpression += "(";
        } else if (openCount > closeCount) {
            currentExpression += ")";
        } else {
            currentExpression += "×(";
        }
    }
    
    private void calculatePreview() {
        if (currentExpression.isEmpty() || isResultDisplayed) return;
        
        try {
            // Only calculate if expression seems complete
            if (!endsWithOperator(currentExpression)) {
                String result = evaluateExpression(currentExpression);
                // Don't update if result is same as expression (single number)
                if (!result.equals(currentExpression)) {
                    lastResult = result;
                }
            }
        } catch (Exception e) {
            // Ignore preview calculation errors
        }
    }
    
    private String evaluateExpression(String expression) throws Exception {
        if (expression.isEmpty()) return "0";
        
        // Handle special functions
        expression = preprocessExpression(expression);
        
        // Convert to postfix notation and evaluate
        List<String> tokens = tokenize(expression);
        List<String> postfix = infixToPostfix(tokens);
        double result = evaluatePostfix(postfix);
        
        // Check for special values
        if (Double.isNaN(result)) {
            throw new Exception(getString(R.string.error_not_a_number));
        }
        if (Double.isInfinite(result)) {
            throw new Exception(getString(R.string.error_infinity));
        }
        
        return formatNumber(result);
    }
    
    private String preprocessExpression(String expression) {
        // Replace display symbols with calculation symbols
        expression = expression.replace("×", "*");
        expression = expression.replace("÷", "/");
        
        // Handle square root
        expression = expression.replace("√", "sqrt");
        
        // Handle power (x²)
        expression = expression.replace("²", "^2");
        
        // Handle implicit multiplication
        expression = addImplicitMultiplication(expression);
        
        return expression;
    }
    
    private String addImplicitMultiplication(String expression) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
            result.append(current);
            
            if (i < expression.length() - 1) {
                char next = expression.charAt(i + 1);
                
                // Add multiplication between number and opening parenthesis
                if (Character.isDigit(current) && next == '(') {
                    result.append("*");
                }
                // Add multiplication between closing and opening parenthesis
                else if (current == ')' && next == '(') {
                    result.append("*");
                }
                // Add multiplication between closing parenthesis and number
                else if (current == ')' && Character.isDigit(next)) {
                    result.append("*");
                }
            }
        }
        
        return result.toString();
    }
    
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                currentToken.append(c);
            } else if (c == 's' && expression.substring(i).startsWith("sqrt")) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add("sqrt");
                i += 3; // Skip "qrt"
            } else {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }
        
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        
        return tokens;
    }
    
    private List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (token.equals("sqrt")) {
                operators.push(token);
            } else if (isOperator(token.charAt(0))) {
                while (!operators.isEmpty() && 
                       !operators.peek().equals("(") &&
                       getPrecedence(operators.peek()) >= getPrecedence(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Remove the "("
                }
                if (!operators.isEmpty() && operators.peek().equals("sqrt")) {
                    output.add(operators.pop());
                }
            }
        }
        
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        
        return output;
    }
    
    private double evaluatePostfix(List<String> postfix) throws Exception {
        Stack<Double> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("sqrt")) {
                if (stack.isEmpty()) throw new Exception("Invalid expression");
                double operand = stack.pop();
                if (operand < 0) throw new Exception(getString(R.string.error_sqrt_negative));
                stack.push(Math.sqrt(operand));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) throw new Exception("Invalid expression");
                double b = stack.pop();
                double a = stack.pop();
                
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) throw new Exception(getString(R.string.error_division_by_zero));
                        stack.push(a / b);
                        break;
                    case "^":
                        stack.push(Math.pow(a, b));
                        break;
                    default:
                        throw new Exception("Unknown operator: " + token);
                }
            }
        }
        
        if (stack.size() != 1) {
            throw new Exception("Invalid expression");
        }
        
        return stack.pop();
    }
    
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '×' || c == '÷';
    }
    
    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            case "sqrt":
                return 4;
            default:
                return 0;
        }
    }
    
    private boolean endsWithOperator(String expression) {
        if (expression.isEmpty()) return false;
        char lastChar = expression.charAt(expression.length() - 1);
        return isOperator(lastChar);
    }
    
    private int countChar(String str, char c) {
        int count = 0;
        for (char ch : str.toCharArray()) {
            if (ch == c) count++;
        }
        return count;
    }
    
    private String formatNumber(double number) {
        // Handle very large or very small numbers
        if (Math.abs(number) >= 1e10 || (Math.abs(number) < 1e-6 && number != 0)) {
            return String.format("%.6E", number);
        }
        
        // Use BigDecimal for precise formatting
        BigDecimal bd = new BigDecimal(number, mathContext);
        bd = bd.stripTrailingZeros();
        
        if (bd.scale() <= 0) {
            return bd.toBigInteger().toString();
        } else {
            return bd.toPlainString();
        }
    }
    
    private void updateDisplay() {
        if (expressionInput != null) {
            expressionInput.setText(currentExpression);
        }
        
        if (resultDisplay != null) {
            if (hasError) {
                resultDisplay.setText("Error");
                resultDisplay.setTextColor(getColor(R.color.md_theme_light_error));
            } else if (isResultDisplayed) {
                resultDisplay.setText(lastResult);
                resultDisplay.setTextColor(getColor(R.color.md_theme_light_primary));
            } else {
                resultDisplay.setText(lastResult);
                resultDisplay.setTextColor(getColor(R.color.md_theme_light_onSurface));
            }
        }
    }
    
    private void animateResultChange() {
        if (resultDisplay != null) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(resultDisplay, "alpha", 1.0f, 0.3f);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(resultDisplay, "alpha", 0.3f, 1.0f);
            
            fadeOut.setDuration(100);
            fadeIn.setDuration(100);
            
            fadeOut.addUpdateListener(animation -> {
                if (animation.getAnimatedFraction() == 1.0f) {
                    fadeIn.start();
                }
            });
            
            fadeOut.start();
        }
    }
    
    private void showError(String message) {
        hasError = true;
        lastResult = "Error";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        // Clear error after 2 seconds
        animationHandler.postDelayed(() -> {
            if (hasError) {
                handleClear();
                updateDisplay();
            }
        }, 2000);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animationHandler != null) {
            animationHandler.removeCallbacksAndMessages(null);
        }
    }
}
package pervasivecomputing.example.mycalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {

    TextView calculation, result;
    String calc = "", res = "", value = "", current_operator = "", prev_ans = "";
    Double Result = 0.0, Value = 0.0, temp = 0.0;
    Boolean dot_present = false, number_allow = true, root_present = false;
    NumberFormat format, longformat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculation = findViewById(R.id.calculation);
        calculation.setMovementMethod(new ScrollingMovementMethod());
        result = findViewById(R.id.result);

        // set the answer up to four decimal
        format = new DecimalFormat("#.####");
        // reformat answer if it's long
        longformat = new DecimalFormat("0.#E0");
    }
    public void onClickNumber(View v) {
        Button button = (Button) v;
        if (number_allow) {
            calc += button.getText();
            value += button.getText();
            Value = Double.parseDouble(value);

            if (root_present) {
                Value = Math.sqrt(Value);
            }
            switch (current_operator) {
                case "":
                    temp = Result + Value;
                    break;

                case "+":
                    temp = Result + Value;
                    break;

                case "-":
                    temp = Result - Value;
                    break;

                case "*":
                    temp = Result * Value;
                    break;

                case "/":
                    try {
                        temp = Result / Value;
                    } catch (Exception e) {
                        res = e.getMessage();
                    }
                    break;

            }
            res = format.format(temp).toString();
            updateCalculation();
        }
    }
    public void onClickOperator(View v) {
        Button button = (Button) v;
        // check if res is empty, because if it's empty I don't need to do calculations
        if (res != "") {
            // check if last char is operator
            if (current_operator != "") {
                char c = getTheChar(calc, 2);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    calc = calc.substring(0, calc.length() - 3);
                }
            }
            calc = calc + "\n" + button.getText() + " ";
            value = "";
            Result = temp;
            current_operator = button.getText().toString();
            updateCalculation();
            dot_present = false;
            number_allow = true;
            root_present = false;
        }

    }
    public void onClickModulo(View view) {
        if (res != "" && getTheChar(calc, 1) != ' ') {
            calc += "% ";
            switch (current_operator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * Value) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * Value) / 100);
                    break;
                case "*":
                    temp = Result * (Value / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (Value / 100);
                    } catch (Exception e) {
                        res = e.getMessage();
                    }
                    break;
            }
            res = format.format(temp).toString();
            if (res.length() > 9) {
                res = longformat.format(temp).toString();
            }
            Result = temp;
            show();

        }
    }
    public void onClickDelete(View view) {
        if (root_present) {
            removeRoot();
            return;
        }
        // check if res is empty, because if it's empty I don't have something to delete
        if (res != "") {
            // I want to delete only numbers, root and dot. Not operators
            if (getTheChar(calc, 1) != ' ') {
                if (value.length() < 2 && current_operator != "") {
                    value = "";
                    temp = Result;
                    res = format.format(Result).toString();
                    calc = removeChar(calc, 1);
                    updateCalculation();
                }
                else {
                    switch (current_operator) {
                        case "":
                            if (calc.length() < 2) {
                                clear();
                            }
                            else {
                                if (getTheChar(calc, 1) == '.') {
                                    dot_present = false;
                                }
                                value = removeChar(value, 1);
                                Value = Double.parseDouble(value);
                                temp = Value;
                                calc = value;
                                res = value;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            temp = Result + Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            temp = Result - Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "*":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            temp = Result * Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (getTheChar(calc, 1) == '.') {
                                    dot_present = false;
                                }
                                value = removeChar(value, 1);
                                if (value.length() == 1 && value == ".") {
                                    Value = Double.parseDouble(value);
                                }
                                Value = Double.parseDouble(value);
                                temp = Result / Value;
                                res = format.format(temp).toString();
                                calc = removeChar(calc, 1);
                            } catch (Exception e) {
                                res = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }
    public void onClickClear(View v) {
        clear();
    }
    public void onClickDot(View view) {
        //check if dot is present or not
        if (!dot_present) {
            if (value.length() == 0) {
                value = "0.";
                calc += "0.";
                res = "0.";
                dot_present = true;
                updateCalculation();
            } else {
                value += ".";
                calc += ".";
                res += ".";
                dot_present = true;
                updateCalculation();
            }
        }

    }
    public void onClickRoot(View view) {
        Button root = (Button) view;
        //check if root is present or not
        if (res == "" && Result == 0 && !root_present) {
            calc = root.getText().toString();
            root_present = true;
            updateCalculation();
        } else if (getTheChar(calc, 1) == ' ' && current_operator != "" && !root_present) {
            calc += root.getText().toString();
            root_present = true;
            updateCalculation();
        }
    }
    public void onClickEqual(View view) {
        if (res != "" && res != prev_ans) {
            calc += "\n= " + res + "\n----------\n" + res + " ";
            value = "";
            Value = 0.0;
            Result = temp;
            prev_ans = res;
            updateCalculation();
            dot_present = true;
            number_allow = false;
            root_present = true;
        }
    }
    public void updateCalculation() {
        calculation.setText(calc);
        result.setText(res);
    }
    public String removeChar(String str, int i) {
        char c = str.charAt(str.length() - i);
        //check if dot is deleted or not
        if (c == '.' && !dot_present) {
            dot_present = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }
    public void show() {
        if (res != "" && res != prev_ans) {
            calc += "\n= " + res + "\n----------\n" + res + " ";
            value = "";
            Value = 0.0;
            Result = temp;
            prev_ans = res;
            updateCalculation();
            dot_present = true;
            number_allow = false;
        }
    }
    public void clear() {
        calc = "";
        res = "";
        current_operator = "";
        value = "";
        prev_ans = "";
        Result = 0.0;
        Value = 0.0;
        temp = 0.0;
        updateCalculation();
        dot_present = false;
        number_allow = true;
        root_present = false;
    }
    private char getTheChar(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }
    public void removeRoot() {
        if (getTheChar(calc, 1) != ' ') {
            if (String.valueOf(getTheChar(calc, 1)).equals("\u221A")) {
                calc = removeChar(calc, 1);
                root_present = false;
                updateCalculation();
            }
            if (res != "") {
                if (value.length() < 2 && current_operator != "") {
                    value = "";
                    Value = Result;
                    temp = Result;
                    res = format.format(Result).toString();
                    calc = removeChar(calc, 1);
                    updateCalculation();
                } else {
                    switch (current_operator) {
                        case "":
                            if (calc.length() <= 2) {
                                clear();
                            } else {
                                if (getTheChar(calc, 1) == '.') {
                                    dot_present = false;
                                }
                                value = removeChar(value, 1);
                                Value = Double.parseDouble(value);
                                Value = Math.sqrt(Value);
                                temp = Value;
                                res = format.format(temp).toString();
                                calc = "\u221A" + value;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            Value = Math.sqrt(Value);
                            temp = Result + Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            Value = Math.sqrt(Value);
                            temp = Result - Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "*":
                            if (getTheChar(calc, 1) == '.') {
                                dot_present = false;
                            }
                            value = removeChar(value, 1);
                            if (value.length() == 1 && value == ".") {
                                Value = Double.parseDouble(value);
                            }
                            Value = Double.parseDouble(value);
                            Value = Math.sqrt(Value);
                            temp = Result * Value;
                            res = format.format(temp).toString();
                            calc = removeChar(calc, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (getTheChar(calc, 1) == '.') {
                                    dot_present = false;
                                }
                                value = removeChar(value, 1);
                                if (value.length() == 1 && value == ".") {
                                    Value = Double.parseDouble(value);
                                }
                                Value = Double.parseDouble(value);
                                Value = Math.sqrt(Value);
                                temp = Result + Value;
                                res = format.format(temp).toString();
                                calc = removeChar(calc, 1);
                            } catch (Exception e) {
                                res = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }
}
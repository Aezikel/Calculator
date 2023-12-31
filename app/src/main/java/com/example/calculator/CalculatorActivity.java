package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.widget.ListView;
import com.example.calculator.databinding.ActivityCalculatorBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.Arrays;
import java.util.Objects;


public class CalculatorActivity extends AppCompatActivity {
     ActivityCalculatorBinding binding;
     BaseInputConnection textFieldInputConnection;
     boolean canDecimal;
     boolean canAppendZeroZero;
     boolean clickedEqualitySign;

     int cursorIndex;
     int findForwardAdd, findForwardSubtract, findForwardMultiply, findForwardDivide;
     int findBackwardAdd, findBackwardSubtract, findBackwardMultiply, findBackwardDivide;
     int findForward , findBackward;

     StringBuilder utilitySBuilder;
     String result;
     String ErrorMessage;

     int themeMode;
     SharedPreferences preferences;
     int checkedItemPosition;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode =  preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        utilitySBuilder = new StringBuilder();
        textFieldInputConnection = new BaseInputConnection(binding.userInputEditText, true); // fake delete event
        final int ZERO = 0;
        final int ONE = 1;
        final int TWO = 2;
        final int THREE = 3;
        final int FOUR = 4;
        final int FIVE = 5;
        final int SIX = 6;
        final int SEVEN = 7;
        final int EIGHT = 8;
        final int NINE = 9;
        final String decimal_point = ".";
        final String ADD = "+";
        final String SUBTRACT = "-";
        final String MULTIPLY = "×";
        final String DIVIDE = "÷";
        final String PERCENT = "%";

        ErrorMessage = "Expression error";

        canDecimal = true;
        canAppendZeroZero = true;
        clickedEqualitySign = false;

        cursorIndex = getCursorPosition();

        binding.toolbarCalculatorActivity.getMenu().findItem(R.id.choose_theme_menu_item).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(CalculatorActivity.this)
                        .setTitle(getString(R.string.choosePtheme))
                        .setSingleChoiceItems(getResources().getStringArray(R.array.theme_dialog_array), getCheckedItemPosition(), null)
                        .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ListView lw = ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView();
                                int checkedItem = lw.getCheckedItemPosition();

                                switch (checkedItem) {
                                    case 0:
                                        themeMode = AppCompatDelegate.MODE_NIGHT_YES;
                                        break;
                                    case 1:
                                        themeMode = AppCompatDelegate.MODE_NIGHT_NO;
                                }

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("ThemeMode", themeMode);
                                editor.apply();

                                applyThemeMode(themeMode);
                            }
                        });

                    materialAlertDialogBuilder.create().show();
                return false;
            }
        });

        log();
        Log.d("CursorIndex",String.valueOf(cursorIndex) );

        binding.userInputEditText.setShowSoftInputOnFocus(false);
        binding.userInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.userInputEditText.length() == 0 ){
                    binding.resultTextviewCalculatorActivity.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText()) && binding.userInputEditText.length() == ONE){// request focus once
                    binding.userInputEditText.requestFocus();
                    Log.d("FocusRequested", "true");
                }
                log();
                evaluateAndSetExpression(binding.userInputEditText.getText().toString());
            }
        });

        binding.userInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCursorIndex();
                log();
                Log.d("LastIndex",String.valueOf(binding.userInputEditText.length() - 1));

                //  check forward for an operator
                //  return index of that operator
                findForwardAdd = Objects.requireNonNull(binding.userInputEditText.getText()).toString().indexOf(ADD  , getCursorPosition());
                findForwardSubtract = binding.userInputEditText.getText().toString().indexOf(SUBTRACT  , getCursorPosition());
                findForwardMultiply = binding.userInputEditText.getText().toString().indexOf(MULTIPLY  , getCursorPosition());
                findForwardDivide = binding.userInputEditText.getText().toString().indexOf(DIVIDE  , getCursorPosition());

                //  check backwards for an operator
                //  return index of that operator
                findBackwardAdd = binding.userInputEditText.getText().toString().lastIndexOf(ADD, cursorIndex);
                findBackwardSubtract = binding.userInputEditText.getText().toString().lastIndexOf(SUBTRACT, cursorIndex);
                findBackwardMultiply = binding.userInputEditText.getText().toString().lastIndexOf(MULTIPLY, cursorIndex);
                findBackwardDivide = binding.userInputEditText.getText().toString().lastIndexOf(DIVIDE, cursorIndex);

                // store forward and backward indexes in their respective arrays
                int [] findBackwardsArray = {findBackwardAdd, findBackwardSubtract, findBackwardMultiply, findBackwardDivide};
                int [] findForwardsArray = {findForwardAdd, findForwardSubtract, findForwardMultiply, findForwardDivide};

                findBackward = Arrays.stream(findBackwardsArray).summaryStatistics().getMax();// get Max backward index
                findForward = Arrays.stream(findForwardsArray).filter(i -> i >= 0).min().orElse(-1); // get MIN Positive forward index

                if (findForward == -1){
                    findForward = binding.userInputEditText.length();
                }
                if (findBackward == -1){
                    findBackward = 0;
                }
                String extractedValue = binding.userInputEditText.getText().toString().substring(findBackward, findForward);

                Log.d("foundForward" , String.valueOf(findForward));
                Log.d("foundBackward" , String.valueOf(findBackward));
                Log.d("Extracted Value", extractedValue);
                canDecimal = !extractedValue.contains(".");
            }
        });

        binding.zeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (binding.userInputEditText.length() == ONE
                            && binding.userInputEditText.getText().toString().charAt(ZERO) == '0') {
                                return;
                    }
                    if (checkOperator()){
                        insertOrAppend(String.valueOf(ZERO));
                        canAppendZeroZero = false;
                        return;
                    }
                    if (!canAppendZeroZero){
                        return;
                    }
                }
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(ZERO));
            }
        });
        binding.doubleZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (binding.userInputEditText.length() == ONE
                            && binding.userInputEditText.getText().toString().charAt(ZERO) == '0') {
                            return; // end code if true
                    }

                    if (checkOperator()){
                        insertOrAppend(String.valueOf(ZERO));
                        canAppendZeroZero = false;
                        return;
                    }

                    if (!canAppendZeroZero){
                        return;
                    }
                    insertOrAppend(String.valueOf(ZERO));

                }
                // continue code
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(ZERO));

            }
        });

        binding.oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();// replace initial zero with one
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(ONE));
                canAppendZeroZero = true;
            }
        });

        binding.twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(TWO));
                canAppendZeroZero = true;
            }
        });

        binding.threeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(THREE));
                canAppendZeroZero = true;
            }
        });

        binding.fourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(FOUR));
                canAppendZeroZero = true;
            }
        });

        binding.fiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(FIVE));
                canAppendZeroZero = true;
            }
        });

        binding.sixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(SIX));
                canAppendZeroZero = true;
            }
        });

        binding.sevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(SEVEN));
                canAppendZeroZero = true;
            }
        });

        binding.eightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(EIGHT));
                canAppendZeroZero = true;
            }
        });

        binding.nineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initZero();
                numberModifyAfterEqualityClicked();
                insertOrAppend(String.valueOf(NINE));
                canAppendZeroZero = true;
            }
        });

        binding.decimalPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canAppendZeroZero = true;
                if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
                    if (!canDecimal){
                        return;
                    }else
                        if(checkOperator() || getCursorPosition() == 0 || clickedEqualitySign){
                            numberModifyAfterEqualityClicked();
                            insertOrAppend(String.valueOf(ZERO));
                    }

                }
                else {
                    insertOrAppend(String.valueOf(ZERO));
                }
                insertOrAppend(decimal_point);
                canDecimal = false;
            }
        });


        /* To avoid confusion
         * The operator button logic has been implemented below
        **/

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                operatorModifyAfterEqualityClicked();
                replaceOperator(ADD);
            }
        });

        binding.subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.userInputEditText.getText()) && indexZeroIsMinus()){
                    return;
                }

                if(checkForOperatorExceptSubtract()){
                    insertOrAppend(SUBTRACT);
                    return;
                }
                operatorModifyAfterEqualityClicked();
                replaceOperator(SUBTRACT);
            }
        });

        binding.multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                operatorModifyAfterEqualityClicked();
                replaceOperator(MULTIPLY);
            }
        });

        binding.divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                operatorModifyAfterEqualityClicked();
                replaceOperator(DIVIDE);
            }
        });

        binding.percentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmptyOrSubtract()){
                    return;
                }
                operatorModifyAfterEqualityClicked();
                replaceOperator(PERCENT);
            }
        });

        binding.equalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.equals(binding.resultTextviewCalculatorActivity.getText().toString(), ErrorMessage)
                        || !TextUtils.isEmpty(binding.userInputEditText.getText())){
                            binding.userInputEditText.setTextSize(32);
                            binding.resultTextviewCalculatorActivity.setTextSize(40);
                            binding.userInputEditText.clearFocus();
                            clickedEqualitySign = true;

                }
                canDecimal = true;
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.userInputEditText.getText())){
                    return;
                }
                binding.userInputEditText.getText().clear();
                binding.resultTextviewCalculatorActivity.setText("");

                if (clickedEqualitySign){
                    resetTextSizes();
                    clickedEqualitySign = false;
                }
                canDecimal = true;
            }
        });

        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userInputEditText.isFocused()){
                    if (getCursorPosition() == 0){
                        return;
                    }
                    if (TextUtils.equals(String.valueOf(Objects.requireNonNull(binding.userInputEditText.getText()).charAt(getCursorPosition() - 1)) , decimal_point)){
                        canDecimal = true;
                    }

                } else if (clickedEqualitySign){
                    resetTextSizes();
                    binding.userInputEditText.requestFocus(binding.userInputEditText.length());
                }
                textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
    }

    // checks
    protected void initZero(){
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                && binding.userInputEditText.length() == 1
                && binding.userInputEditText.getText().toString().charAt(0) == '0'){
                    binding.userInputEditText.getText().clear();
        }
    }

    protected void replaceOperator(String operator){
        Log.d("Operator found", String.valueOf(checkOperator()));
        if (checkOperator()){
            Objects.requireNonNull(binding.userInputEditText.getText()).delete(cursorIndex, getCursorPosition());
        }
        insertOrAppend(operator);
        canDecimal = true;
    }

    protected boolean checkOperator() {
        // check if the cursor index is an operator
        setCursorIndex();
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
            return binding.userInputEditText.getText().charAt(cursorIndex) == '+'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '-'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '÷'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '×';
        }
        return false;
    }

    protected boolean checkForOperatorExceptSubtract(){
        setCursorIndex();
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())) {
            return binding.userInputEditText.getText().charAt(cursorIndex) == '+'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '÷'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '×'
                    || binding.userInputEditText.getText().charAt(cursorIndex) == '%';
        }
        return false;
    }

    protected boolean checkForEmptyOrSubtract(){
        return TextUtils.isEmpty(binding.userInputEditText.getText())
                || (indexZeroIsMinus())
                || getCursorPosition() == 0;
    }

    protected int getCursorPosition(){
        return binding.userInputEditText.getSelectionStart();

    }

    protected boolean isCursorPositionEqualsLength(){
        return getCursorPosition() == binding.userInputEditText.length();
    }

    protected boolean indexZeroIsMinus(){
        return (getCursorPosition() == 0 || getCursorPosition() == 1) && Objects.requireNonNull(binding.userInputEditText.getText()).charAt(0) == '-';
    }

    protected void log(){
        Log.d("cursorAt", String.valueOf(getCursorPosition()));
        Log.d("length", String.valueOf(binding.userInputEditText.length()));
    }

    protected void insertOrAppend(String input){
        if (!TextUtils.isEmpty(binding.userInputEditText.getText())
                && !isCursorPositionEqualsLength()){
            binding.userInputEditText.getText().insert(getCursorPosition(), input);
        }

        else {
            binding.userInputEditText.append(input);
        }
    }

    protected void setCursorIndex(){
        if (getCursorPosition() > 0 ){
            cursorIndex = getCursorPosition() - 1;
        } else
            cursorIndex = 0;
    }

    protected String checkExpression(String expression){
        utilitySBuilder.setLength(0); // clear utility String builder
        utilitySBuilder.append(expression);


        if (expression.endsWith("×-") || expression.endsWith("÷-")){
            utilitySBuilder.delete(utilitySBuilder.length() - 2 , utilitySBuilder.length() - 1);
        }

        if (expression.endsWith("×") || expression.endsWith("÷") || expression.endsWith(".")){
            utilitySBuilder.deleteCharAt(utilitySBuilder.length() - 1);
        }

        return  utilitySBuilder.toString();
    }

    protected void evaluateAndSetExpression(String expression){
        if (!expression.isEmpty() && !indexZeroIsMinus()){

            Expression esp = new Expression(checkExpression(expression));
            if (esp.checkSyntax()){
                result = String.valueOf(esp.calculate());
                if (result.endsWith(".0")){
                    result = result.replace(".0", "");
                }
            }
            else {
                result = ErrorMessage;
            }
            binding.resultTextviewCalculatorActivity.setText(result);
        }
    }

    protected void operatorModifyAfterEqualityClicked(){
        if (clickedEqualitySign){
            resetTextSizes();
            binding.userInputEditText.setText(binding.resultTextviewCalculatorActivity.getText().toString());
            binding.userInputEditText.requestFocus(binding.userInputEditText.length());
            binding.resultTextviewCalculatorActivity.setText("");
            clickedEqualitySign = false;
        }
    }

    protected void numberModifyAfterEqualityClicked(){
        if (clickedEqualitySign){
            resetTextSizes();
            binding.userInputEditText.setText("");
            binding.resultTextviewCalculatorActivity.setText("");
            clickedEqualitySign = false;
        }
    }

    protected void resetTextSizes(){
        binding.userInputEditText.setTextSize(40);
        binding.resultTextviewCalculatorActivity.setTextSize(32);
    }

    protected void applyThemeMode(int themeMode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

    protected int getCheckedItemPosition(){
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES){
            checkedItemPosition = 0;
        } else if (themeMode == AppCompatDelegate.MODE_NIGHT_NO) {
            checkedItemPosition = 1;
        }
        return  checkedItemPosition;
    }
}
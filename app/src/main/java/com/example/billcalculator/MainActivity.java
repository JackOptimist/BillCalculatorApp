package com.example.billcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText beforeUnitUsed, lastAfterUsed;
    Button btnCalc,btnClear;
    TextView totalCostView, level1Used, level2Used, level3Used, level1Cost, level2Cost, level3Cost, total1, total2, total3, totalUsed, total;
    TextView totalRebate, totalBillCost;

    private DecimalFormat decimalFormat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decimalFormat = new DecimalFormat("#.00");

        beforeUnitUsed = findViewById(R.id.beforeUsed);
        lastAfterUsed = findViewById(R.id.lastUsed);

        btnCalc = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);

        totalCostView = findViewById(R.id.totalCostView);

        level1Used = findViewById(R.id.level1Used);
        level2Used = findViewById(R.id.level2Used);
        level3Used = findViewById(R.id.level3Used);

        level1Cost = findViewById(R.id.level1Cost);
        level2Cost = findViewById(R.id.level2Cost);
        level3Cost = findViewById(R.id.level3Cost);
        total1 = findViewById(R.id.total1);
        total2 = findViewById(R.id.total2);
        total3 = findViewById(R.id.total3);

        totalUsed = findViewById(R.id.totalUsed);
        total = findViewById(R.id.total);

        totalRebate = findViewById(R.id.totalRebate);
        totalBillCost = findViewById(R.id.totalCost);

        btnCalc.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCalculate) {
            try {
                String variable = beforeUnitUsed.getText().toString();
                String variable2 = lastAfterUsed.getText().toString();

                int unit1 = Integer.parseInt(variable);
                int unit2 = Integer.parseInt(variable2);
                int unit = unit2 - unit1;


                boolean haveRebate = unit > 100;
                double rebate = 0.05;

                int level1 = 80;
                int level2 = 150;

                double rate1 = 22.03;
                double rate2 = 29.06;
                double rate3 = 36.32;

                double charges = calculateCharges(unit, rate1, rate2, rate3);
                double finalCost = calculateFinalCost(charges, rebate, haveRebate);
                double finalRebate = setRebate(unit);


                if (unit <= level1) {
                    level1Used.setText(unit + " kWh");
                    level1Cost.setText(rate1 + " tg/kWh");
                    total1.setText(finalCost + "тг");
                }
                else if (unit >= level1 && unit <= level2)
                {
                    level2Used.setText(unit + " kWh");
                    level2Cost.setText(rate2 + " tg/kWh");
                    total2.setText(finalCost + " тг");
                }
                else
                {
                    level3Used.setText(unit + " kWh");
                    level3Cost.setText(rate3 + " tg/kWh");
                    total3.setText(finalCost + " тг");
                }

                totalCostView.setText(decimalFormat.format(finalCost) + " тг");

                totalUsed.setText(unit + "kWh");
                total.setText(decimalFormat.format((finalCost)) + " тг");

                totalRebate.setText("Жеңілдік: " + finalRebate + "%");
                totalBillCost.setText("Толық бағасы: " + decimalFormat.format(finalCost));

            }
            catch (NumberFormatException nfe) {
                showToast("Дұрыс сан жазыңыз");
            }
            catch (Exception exp) {
                showToast("Түсінбеушілің");
                Log.d("Қателік", Objects.requireNonNull(exp.getMessage()));
            }
        }
        else if (v.getId() == R.id.btnClear) {
            //todo
            beforeUnitUsed.setText("");
            lastAfterUsed.setText("");
            Toast.makeText(this, "Тазаланды", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateCharges(int unit, double rate1, double rate2, double rate3) {

        double charges;

        if (unit <= 80) {
            charges = unit * rate1;
        }
        else if (unit >= 80 && unit <= 150) {
            charges = unit * rate2;
        }
        else
            charges = unit * rate3;

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String roundedChargesStr = decimalFormat.format(charges);
        return Double.parseDouble(roundedChargesStr);
    }

    private double calculateFinalCost(double charges, double rebate, boolean haveRebate) {

        double finalCost = charges;

        if (haveRebate) {
            finalCost = charges - (charges * rebate);
        }

        return Double.parseDouble(decimalFormat.format(finalCost));
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private double setRebate(int unit)
    {
        if (unit > 100)
            return 5;
        return 0;
    }
}

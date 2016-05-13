package com.example.jumana.myweatherbuddy.themes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jumana.myweatherbuddy.MainActivity;
import com.example.jumana.myweatherbuddy.Params;
import com.example.jumana.myweatherbuddy.R;
import com.example.jumana.myweatherbuddy.data.model.Main;

public class MainTheme extends Activity implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    MainActivity main = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        Utils.onActivityCreateSetTheme(this, Params.MyPREFERENCES, MainTheme.this);
    }

    @Override
    public void onClick(View v) {
        sharedpreferences = getSharedPreferences(Params.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        switch (v.getId()) {
            case R.id.button1:
                this.setTheme(R.style.FirstTheme);
                editor.putString("theme", "red");
                editor.apply();
                Utils.changeToTheme(this, Utils.THEME_RED, MainActivity.mcontext);

                break;
            case R.id.button2:
                this.setTheme(R.style.SecondTheme);
                editor.putString("theme", "green");
                editor.apply();
                Utils.changeToTheme(this, Utils.THEME_GREEN, MainActivity.mcontext);

                break;
            case R.id.button3:
                this.setTheme(R.style.ThirdTheme);
                editor.putString("theme", "blue");
                editor.apply();
                Utils.changeToTheme(this, Utils.THEME_BLUE, MainActivity.mcontext);

                break;
            case R.id.button4:
                this.setTheme(R.style.FourthTheme);
                editor.putString("theme", "vintage");
                editor.apply();
                Utils.changeToTheme(this, Utils.THEME_VINTAGE, MainActivity.mcontext);

                break;
            case R.id.button5:
                this.setTheme(R.style.FifthTheme);
                editor.putString("theme", "totoro");
                editor.apply();
                Utils.changeToTheme(this, Utils.THEME_TOTORO, MainActivity.mcontext);

                break;
        }

    }
}


package com.example.jumana.myweatherbuddy.themes;

/**
 * Created by Utilisateur on 11/05/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.jumana.myweatherbuddy.MainActivity;
import com.example.jumana.myweatherbuddy.Params;
import com.example.jumana.myweatherbuddy.R;

public class Utils {
    private static int sTheme;
    public final static int THEME_RED = 0;
    public final static int THEME_GREEN = 1;
    public final static int THEME_BLUE = 2;
    public final static int THEME_VINTAGE = 3;
    public final static int THEME_TOTORO = 4;

    public static void changeToTheme(Activity activity, int theme, Context context) {
        sTheme = theme;
        activity.finish();
        onActivityCreateSetTheme(activity, Params.MyPREFERENCES, context);
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    public static void onActivityCreateSetTheme(Activity activity, String prefs, Context context) {
        switch (sTheme) {
            default:
            case THEME_RED:
                activity.setTheme(R.style.FirstTheme);
                break;
            case THEME_GREEN:
                activity.setTheme(R.style.SecondTheme);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.ThirdTheme);
                break;
            case THEME_VINTAGE:
                activity.setTheme(R.style.FourthTheme);
                break;
            case THEME_TOTORO:
                activity.setTheme(R.style.FifthTheme);
                break;
        }

        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(prefs, context.MODE_PRIVATE);
        String theme = sharedpreferences.getString("theme", "error : no theme");
        ImageView bg = (ImageView) MainActivity.mActivity.findViewById(R.id.Fullbackground);
        ImageView imageview = (ImageView) MainActivity.mActivity.findViewById(R.id.background);

        String uri = "";
        String uri2 = "";

        switch (theme) {
            default:
            case "red":
                activity.setTheme(R.style.FirstTheme);
                uri = "@drawable/starwars";
                uri2 = "@drawable/starwarsbg";
                break;
            case "green":
                activity.setTheme(R.style.SecondTheme);
                uri = "@drawable/pokemon";
                uri2 = "@drawable/pokemonbg";
                break;
            case "blue":
                activity.setTheme(R.style.ThirdTheme);
                uri = "@drawable/mario";
                uri2 = "@drawable/mariobg";
                break;
            case "vintage":
                activity.setTheme(R.style.FourthTheme);
                uri = "@drawable/vintage1";
                uri2 = "@drawable/vintagebg";
                break;
            case "totoro":
                activity.setTheme(R.style.FifthTheme);
                uri = "@drawable/totoro";
                uri2 = "@drawable/totorobg";
        }

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        int bgResource = context.getResources().getIdentifier(uri2, null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        Drawable bgRes = context.getResources().getDrawable(bgResource);
        imageview.setImageDrawable(res);
        bg.setImageDrawable(bgRes);
    }
}


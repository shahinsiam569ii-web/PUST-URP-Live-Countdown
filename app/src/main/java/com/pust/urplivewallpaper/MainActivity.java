package com.pust.urplivewallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, ExamWallpaperService.class)
            );
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            startActivity(intent);
        }
    }
}

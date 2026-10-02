package com.pust.urplivewallpaper;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ExamWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new CountdownEngine();
    }

    private class CountdownEngine extends Engine {

        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean visible = true;

        private final Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                drawWallpaper();
                if (visible) {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        @Override
        public void onVisibilityChanged(boolean isVisible) {
            visible = isVisible;

            if (isVisible) {
                drawWallpaper();
                handler.removeCallbacks(drawRunnable);
                handler.post(drawRunnable);
            } else {
                handler.removeCallbacks(drawRunnable);
            }
        }

        @Override
        public void onSurfaceChanged(
                SurfaceHolder holder,
                int format,
                int width,
                int height) {
            drawWallpaper();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            visible = false;
            handler.removeCallbacks(drawRunnable);
            super.onSurfaceDestroyed(holder);
        }

        private void drawWallpaper() {

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();

                if (canvas == null) return;

                int width = canvas.getWidth();
                int height = canvas.getHeight();

                // Background
                canvas.drawColor(android.graphics.Color.rgb(8, 24, 35));

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTypeface(Typeface.create("sans", Typeface.NORMAL));

                // Header
                paint.setColor(android.graphics.Color.rgb(80, 180, 220));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(Typeface.create("sans", Typeface.BOLD));
                paint.setTextSize(width * 0.045f);

                canvas.drawText(
                        "PUST • URP",
                        width / 2f,
                        height * 0.12f,
                        paint
                );

                paint.setColor(android.graphics.Color.WHITE);
                paint.setTextSize(width * 0.032f);

                canvas.drawText(
                        "2nd Year 2nd Semester",
                        width / 2f,
                        height * 0.17f,
                        paint
                );

                Exam exam = getNextExam();

                if (exam == null) {
                    paint.setTextSize(width * 0.06f);
                    canvas.drawText(
                            "ALL EXAMS COMPLETED",
                            width / 2f,
                            height * 0.50f,
                            paint
                    );
                    return;
                }

                // Next exam label
                paint.setColor(android.graphics.Color.rgb(150, 210, 230));
                paint.setTextSize(width * 0.030f);
                paint.setTypeface(Typeface.create("sans", Typeface.NORMAL));

                canvas.drawText(
                        "NEXT EXAMINATION",
                        width / 2f,
                        height * 0.28f,
                        paint
                );

                // Course title
                paint.setColor(android.graphics.Color.WHITE);
                paint.setTypeface(Typeface.create("sans", Typeface.BOLD));
                paint.setTextSize(width * 0.050f);

                canvas.drawText(
                        exam.title,
                        width / 2f,
                        height * 0.35f,
                        paint
                );

                // Course code
                paint.setTextSize(width * 0.028f);
                paint.setTypeface(Typeface.create("sans", Typeface.NORMAL));

                canvas.drawText(
                        exam.code,
                        width / 2f,
                        height * 0.40f,
                        paint
                );

                // Countdown
                long remaining = exam.time - System.currentTimeMillis();

                if (remaining < 0) remaining = 0;

                long totalSeconds = remaining / 1000;

                long days = totalSeconds / 86400;
                long hours = (totalSeconds % 86400) / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                String countdown = String.format(
                        Locale.getDefault(),
                        "%02d : %02d : %02d : %02d",
                        days,
                        hours,
                        minutes,
                        seconds
                );

                paint.setColor(android.graphics.Color.rgb(80, 210, 240));
                paint.setTypeface(Typeface.create("sans", Typeface.BOLD));
                paint.setTextSize(width * 0.075f);

                canvas.drawText(
                        countdown,
                        width / 2f,
                        height * 0.54f,
                        paint
                );

                // Labels
                paint.setColor(android.graphics.Color.LTGRAY);
                paint.setTypeface(Typeface.create("sans", Typeface.NORMAL));
                paint.setTextSize(width * 0.022f);

                canvas.drawText(
                        "DAYS          HOURS        MINUTES      SECONDS",
                        width / 2f,
                        height * 0.59f,
                        paint
                );

                // Exam date
                paint.setColor(android.graphics.Color.WHITE);
                paint.setTextSize(width * 0.028f);

                canvas.drawText(
                        exam.dateText,
                        width / 2f,
                        height * 0.67f,
                        paint
                );

                // Venue / time
                paint.setColor(android.graphics.Color.LTGRAY);
                paint.setTextSize(width * 0.022f);

                canvas.drawText(
                        "10:00 AM – 01:00 PM • Exam Hall, Dept. of URP",
                        width / 2f,
                        height * 0.72f,
                        paint
                );

                // Footer
                paint.setColor(android.graphics.Color.rgb(120, 160, 175));
                paint.setTextSize(width * 0.020f);

                canvas.drawText(
                        "Pabna University of Science & Technology",
                        width / 2f,
                        height * 0.90f,
                        paint
                );

            } finally {

                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        private Exam getNextExam() {

            TimeZone.setDefault(
                    TimeZone.getTimeZone("Asia/Dhaka")
            );

            Exam[] exams = new Exam[] {

                    new Exam(
                            "Statistics for Planners-II",
                            "URP 2203",
                            "10 October 2026 • Saturday",
                            makeDate(2026, 10, 10, 10, 0)
                    ),

                    new Exam(
                            "Research Methodology",
                            "URP 2205",
                            "14 October 2026 • Wednesday",
                            makeDate(2026, 10, 14, 10, 0)
                    ),

                    new Exam(
                            "Elements of Civil Engineering Structures",
                            "CE 2251",
                            "28 October 2026 • Wednesday",
                            makeDate(2026, 10, 28, 10, 0)
                    ),

                    new Exam(
                            "Urban Planning Techniques",
                            "URP 2201",
                            "1 November 2026 • Sunday",
                            makeDate(2026, 11, 1, 10, 0)
                    ),

                    new Exam(
                            "Programming Techniques for Planners",
                            "CSE 2253",
                            "7 November 2026 • Saturday",
                            makeDate(2026, 11, 7, 10, 0)
                    ),

                    new Exam(
                            "Viva-Voce",
                            "URP 2nd Year 2nd Semester",
                            "9 November 2026 • Monday",
                            makeDate(2026, 11, 9, 10, 0)
                    )
            };

            long now = System.currentTimeMillis();

            for (Exam exam : exams) {
                if (exam.time > now) {
                    return exam;
                }
            }

            return null;
        }

        private long makeDate(
                int year,
                int month,
                int day,
                int hour,
                int minute) {

            java.util.Calendar calendar =
                    java.util.Calendar.getInstance(
                            TimeZone.getTimeZone("Asia/Dhaka")
                    );

            calendar.set(
                    year,
                    month - 1,
                    day,
                    hour,
                    minute,
                    0
            );

            calendar.set(
                    java.util.Calendar.MILLISECOND,
                    0
            );

            return calendar.getTimeInMillis();
        }
    }

    private static class Exam {

        String title;
        String code;
        String dateText;
        long time;

        Exam(
                String title,
                String code,
                String dateText,
                long time) {

            this.title = title;
            this.code = code;
            this.dateText = dateText;
            this.time = time;
        }
    }
}

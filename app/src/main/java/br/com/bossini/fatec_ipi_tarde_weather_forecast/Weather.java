package br.com.bossini.fatec_ipi_tarde_weather_forecast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Weather {
    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather (long timeStamp, double minTemp,
                        double maxTemp, double humidity,
                    String description, String iconName){

        NumberFormat nf =
                NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        this.dayOfWeek = convertTimeStampToDay(timeStamp);
        this.minTemp = nf.format(minTemp);
        this.maxTemp = nf.format(maxTemp);
        this.humidity =
                NumberFormat.
                        getPercentInstance().
                        format(humidity / 100.);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/"
                    + iconName + ".png";
    }

    private static String
            convertTimeStampToDay (long timeStamp){

        Calendar agora = Calendar.getInstance();
        agora.setTimeInMillis(timeStamp * 1000);
        TimeZone fusoHorario = TimeZone.getDefault();
        agora.add(Calendar.MILLISECOND,
                fusoHorario.getOffset(agora.getTimeInMillis()));
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEEE");
        return sdf.format(agora.getTime());
    }
}

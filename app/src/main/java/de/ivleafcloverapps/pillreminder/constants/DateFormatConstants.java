package de.ivleafcloverapps.pillreminder.constants;

import java.text.SimpleDateFormat;

/**
 * Created by Christian on 28.05.2017.
 *
 * DateFormatter Constants for the app, at the moment only the german default format is supported
 */
public class DateFormatConstants {
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
}

package io.emaster.smashretrochat.helper;

import android.content.Context;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elezermaster on 25/09/2017.
 */

public class GetDateTime {
    public static  String getCurrentDate(){
        Date date = new Date();
        Date newDate = new Date(date.getTime());// + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringdate = dt.format(newDate);
        return  stringdate;
    }

    public static  String getDateFromStamp(String stamp){
       // String x = "1086073200000";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        long milliSeconds= Long.parseLong(stamp);
        //System.out.println(milliSeconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        //System.out.println(formatter.format(calendar.getTime()));
        return  formatter.format(calendar.getTime());
    }

//    public static Timestamp convertStringToTimestamp(String str_date) {
//        try {
//            DateFormat formatter;
//            formatter = new SimpleDateFormat("dd/MM/yyyy");
//            // you can change format of date
//            Date date = formatter.parse(str_date);
//            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
//
//            return timeStampDate;
//        } catch (ParseException e) {
//            System.out.println("Exception :" + e);
//            return null;
//        }
//    }

    public static String getCurrentDateTime(){
        Date date = new Date();
        Date newDate = new Date(date.getTime());// + (604800000L * 2) + (24 * 60 * 60));
        //SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String stringdate = sdf.format(newDate);
        return  stringdate;
    }

    public static String friendlyTimeDiff(String timeFromSerever) {
        long timeMilliseconds = 0;
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");//= new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date d = f.parse(timeFromSerever);
            timeMilliseconds = d.getTime();

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(date);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
            timeStamp = sdf.format(date);
            timeMilliseconds = timeMilliseconds - Long.valueOf(timeStamp).longValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //method 2 - via Date

        //long timenow = new Timestamp(date.getTime());





        //timenow.getTime();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
        //System.out.println(timestamp.getTime());

        long diffSeconds = timeMilliseconds / 1000;
        long diffMinutes = timeMilliseconds / (60 * 1000);
        long diffHours = timeMilliseconds / (60 * 60 * 1000);
        long diffDays = timeMilliseconds / (60 * 60 * 1000 * 24);
        long diffWeeks = timeMilliseconds / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = (long) (timeMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = timeMilliseconds / ((long)60 * 60 * 1000 * 24 * 365);

        if (diffSeconds < 1) {
            return "less than a second";
        } else if (diffMinutes < 1) {
            return diffSeconds + " seconds";
        } else if (diffHours < 1) {
            return diffMinutes + " minutes";
        } else if (diffDays < 1) {
            return diffHours + " hours";
        } else if (diffWeeks < 1) {
            return diffDays + " days";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks";
        } else if (diffYears < 1) {
            return diffMonths + " months";
        } else {
            return diffYears + " years";
        }
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        //getCurrentTime(ctx);
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getTimeAgo(String timeStamp, Context ctx) {
       /// java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(timeStamp);//"2015-07-24T09:45:44.000Z");
       /// long time = ts2.getTime();
        //long time =timeStamp.getTime();
        //long time=new DateTime(timeStamp).toDate().getTime();
        long time =0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        //String dateAsString = sdf.format (milliSeconds);
        try {
            Date date = sdf.parse(timeStamp);//.parse(timeStamp);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        //getCurrentTime(ctx);
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


}

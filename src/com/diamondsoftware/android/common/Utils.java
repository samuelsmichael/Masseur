package com.diamondsoftware.android.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.ImageView;

 
public class Utils {
	/*
	 * Makes the first letter of each word captialized, and all other letters lower-case
	 */
	public static String scrubResortName(String resortName) {
		return resortName.replace("ln", "lane").replace("Ln", "Lane").replace("LN", "LANE");
		
	}
	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	
	public static boolean canHandleIntent(Context context, Intent intent){
	    PackageManager packageManager = context.getPackageManager();
	    List activities = packageManager.queryIntentActivities(
	        intent, 
	        PackageManager.MATCH_DEFAULT_ONLY);
	    return activities.size() > 0;
	}	
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
	public static GregorianCalendar toGregorianCalendarFromYYYYdashMMdashDDTHHcolonMMcolonSS(String s) {
		return new GregorianCalendar(
			  Integer.valueOf(s.substring(0, 3)), 
			  Integer.valueOf(s.substring(5, 6)),
			  Integer.valueOf(s.substring(8,2)),
			  Integer.valueOf(s.substring(11, 2)),
			  Integer.valueOf(s.substring(14, 2)),
			  Integer.valueOf(s.substring(17, 2)));
	}
	public static GregorianCalendar toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(String s) {
		if (s==null || s.equalsIgnoreCase("null")) {
			s="0001-01-01T00:00:00"; // the system was originally written to return null's like this
		}
		int yyyy=Integer.valueOf(s.substring(0, 4));
		int mm=Integer.valueOf(s.substring(5, 7));
		int dd=Integer.valueOf(s.substring(8,10));
		int hh=Integer.valueOf(s.substring(11,13));
		int min=Integer.valueOf(s.substring(14,16));
		int sec=Integer.valueOf(s.substring(17));
		return new GregorianCalendar(
			  yyyy, 
			  mm-1,
			  dd,hh,min,sec);
	}
	public static final DateFormat mDateFormatYYYYMMDD=new SimpleDateFormat(
			"yyyy.mm.dd");
	public static final DateFormat mLocaleDateFormat=new SimpleDateFormat("MM/dd/yyyy",Locale.US);
	public static final DateFormat mDateFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	public static String toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(GregorianCalendar gc) {
		
		String retValue=mDateFormat.format(gc.getTime());
		return retValue;
	}
	public static boolean isNullDate(GregorianCalendar gc) {
		if (gc==null) {
			return true;
		}
		String str=toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(gc);
		return 
				str.equals("0001-01-01 00:00:00") ||
				str.equals("1900-01-01 00:00:00");
	}
	public static java.util.Calendar toDateFromMMdashDDdashYYYY(String s) throws ParseException {
		DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat df3 = new SimpleDateFormat("MM.dd.yyyy");
		Date parsed=null;
		try {
			parsed = df1.parse(s);
		} catch (Exception e) {
			try {
				parsed=df2.parse(s);
			} catch (Exception e2) {
				parsed=df3.parse(s);
			}
		}
		java.util.Calendar newCalendar = GregorianCalendar.getInstance();
		newCalendar.setTime(parsed);
		return newCalendar;
	}
    public static void setBlackAndWhite(ImageView iv){

        float brightness = (float)(255-255);
        
        float[] colorMatrix = { 
                0.33f, 0.33f, 0.33f, 0, brightness, //red
                0.33f, 0.33f, 0.33f, 0, brightness, //green
                0.33f, 0.33f, 0.33f, 0, brightness, //blue
                0, 0, 0, 1, 0    //alpha    
              };
        
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
           iv.setColorFilter(colorFilter);
           
       }
    public static class Confirmer extends DialogFragment {
		private String mTitle;
		private String mMessage;
		private Activity mActivity;
		private ConfirmerClient mClient;
    	private ArrayList<Object> mOtherData;
    	
    	@SuppressWarnings("unused")
		private Confirmer() {
    		super();
    	}
		public Confirmer(String title, String message,
				Activity activity, ConfirmerClient client, ArrayList<Object> otherData) {
			super();
			mTitle = title;
			mMessage = message;
			mActivity = activity;
			mClient=client;
			mOtherData=otherData;
		}

		
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(mTitle)
            			.setMessage(mMessage)
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mClient.heresYourAnswer(false, mOtherData);
										dismiss();
									}
								})
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mClient.heresYourAnswer(true, mOtherData);
										dismiss();
									}
								});
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
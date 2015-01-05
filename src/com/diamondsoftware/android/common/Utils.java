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

import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

 
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
    	private Uri mOptionalURLOfImageToDisplay;
    	
    	
    	@SuppressWarnings("unused")
		private Confirmer() {
    		super();	
    	}
		public Confirmer(String title, String message,
				Activity activity, ConfirmerClient client, ArrayList<Object> otherData, Uri optionalURLOfImageToDisplay) {
			super();
			mTitle = title;
			mMessage = message;
			mActivity = activity;
			mClient=client;
			mOtherData=otherData;
			mOptionalURLOfImageToDisplay=optionalURLOfImageToDisplay;
		}

		
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	ImageView image=null;
        	if(mOptionalURLOfImageToDisplay!=null) {
        		image = new ImageView(mActivity);
        		image.setImageURI(mOptionalURLOfImageToDisplay);
        	}
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            
            if(image!=null) {
            	/*  This doesn't do squat
            	Display display = getActivity().getWindowManager().getDefaultDisplay();
            	int width = display.getWidth(); // ((display.getWidth()*20)/100)
            	int height = display.getHeight();// ((display.getHeight()*30)/100)
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width-50,height-50);
                image.setLayoutParams(lp);
                */
//            	image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            	builder.setView(image);
            }
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
            AlertDialog d= builder.create();
            return d;
        }
    }
    public static class Confirmer2 extends DialogFragment {
		private String mTitle;
		private String mMessage;
		private Activity mActivity;
		private ConfirmerClient mClient;
    	private ArrayList<Object> mOtherData;
    	private Uri mOptionalURLOfImageToDisplay;
    	private String mButtonYesVerbiage;
    	private String mButtonNoVerbiage;
		public Confirmer2(String title, String message,
				Activity activity, ConfirmerClient client, ArrayList<Object> otherData, Uri optionalURLOfImageToDisplay, String yes, String no) {
			super();
			mTitle = title;
			mMessage = message;
			mActivity = activity;
			mClient=client;
			mOtherData=otherData;
			mOptionalURLOfImageToDisplay=optionalURLOfImageToDisplay;
			mButtonYesVerbiage=yes;
			mButtonNoVerbiage=no;
		}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            int style = DialogFragment.STYLE_NORMAL, theme = android.R.style.Theme_Holo_Light_Dialog;
            theme = android.R.style.Theme_Holo_Light_Dialog;
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            getDialog().setTitle(mTitle);

            View v = inflater.inflate(com.diamondsoftware.android.masseur.R.layout.fragment_dialog, container, false);
            View tv = v.findViewById(com.diamondsoftware.android.masseur.R.id.text);
            ((TextView)tv).setText(mMessage);

            // Watch for button clicks.
            Button button = (Button)v.findViewById(com.diamondsoftware.android.masseur.R.id.btnDialogYes);
            button.setText(mButtonYesVerbiage);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					mClient.heresYourAnswer(true, mOtherData);
					dismiss();
                }
            });
            Button button2 = (Button)v.findViewById(com.diamondsoftware.android.masseur.R.id.btnDialogNo);
            button2.setText(mButtonNoVerbiage);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					mClient.heresYourAnswer(false, mOtherData);
					dismiss();
                }
            });

        	ImageView image=null;
        	if(mOptionalURLOfImageToDisplay!=null) {
        		image = (ImageView)v.findViewById(R.id.ivDialog);
        		image.setImageURI(mOptionalURLOfImageToDisplay);
        	}
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            
            return v;
        }
    }
}
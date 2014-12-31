package com.diamondsoftware.android.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;
import android.util.Log;
	
public class HttpFileUpload extends AsyncTask<HttpFileUploadParameters, Void, Object> {
	private HttpFileUploadParameters mParameters;
	private String lineEnd = "\r\n";
	private String twoHyphens = "--";
	private String boundary = "*****";
	private String Tag="fSnd";
	
	@Override
	protected Exception doInBackground(HttpFileUploadParameters... params) {
		mParameters=params[0];
		Exception e=null;
		
		try {
		
			URL connectURL= new URL(mParameters.mRemoteURL);
	        // Open a HTTP connection to the URL
	        HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();
	
	        // Allow Inputs
	        conn.setDoInput(true);
	
	        // Allow Outputs
	        conn.setDoOutput(true);
	
	        // Don't use a cached copy.
	        conn.setUseCaches(false);
	
	        // Use a post method.
	        conn.setRequestMethod("POST");
	
	        conn.setRequestProperty("Connection", "Keep-Alive");
	
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
	        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	
	        
	        
	        
	        if(mParameters.mParameters!=null) {
	        	int whichPrivatePicture=0;
				for(NameValuePair nvp: mParameters.mParameters) {
					writeAField(nvp.getName(),nvp.getValue(),dos);
					if(nvp.getName().equals("pnbr")) {
						whichPrivatePicture=Integer.valueOf(nvp.getValue());
					}
				}
                dos.writeBytes(twoHyphens + boundary + lineEnd);   
                if(whichPrivatePicture==0) {
                	dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "MesseurUserId_"+this.mParameters.mUserId+"_XMainPicture_" + String.valueOf(new Date().getTime()) +"\"" + lineEnd);
                } else {
                	dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "MesseurUserId_"+this.mParameters.mUserId+"_XPrivatePicture_" +String.valueOf(whichPrivatePicture)+"_"+ String.valueOf(new Date().getTime()) +"\"" + lineEnd);
                }
                dos.writeBytes(lineEnd);

	        } else {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
               	dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + "MesseurUserId_"+mParameters.mUserId+"_MainPicture" +"\"" + lineEnd);
                dos.writeBytes(lineEnd);
	        }
	        // create a buffer of maximum size
	        int bytesAvailable = mParameters.mInputStream.available();
	            
	        int maxBufferSize = 1024;
	        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        byte[ ] buffer = new byte[bufferSize];
	
	        // read file and write it into form...
	        int bytesRead = mParameters.mInputStream.read(buffer, 0, bufferSize);
	
	        while (bytesRead > 0)
	        {
	                dos.write(buffer, 0, bufferSize);
	                bytesAvailable = mParameters.mInputStream.available();
	                bufferSize = Math.min(bytesAvailable,maxBufferSize);
	                bytesRead = mParameters.mInputStream.read(buffer, 0,bufferSize);
	        }
	        dos.writeBytes(lineEnd);
	        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	
	        // close streams
	        mParameters.mInputStream.close();
	            
	        dos.flush();
	             
	        InputStream is = conn.getInputStream();
	            
	        // retrieve the response from server
	        int ch;
	
	        StringBuffer b =new StringBuffer();
	        while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
	        String s=b.toString();
	        
	        dos.close();
	
	        mParameters.mCanShowAlert.heresTheResponse(s,(ArrayList<NameValuePair>) mParameters.mParameters);
		} catch (MalformedURLException mex) {
			e=mex;
		} catch (IOException ioex) {
			e=ioex;
		}
		return e;
	}
	protected void onPostExecute(Object result) {
		if(mParameters.mProgressDialog!=null) {
			mParameters.mProgressDialog.dismiss();
		}
		if(result!=null && result instanceof Exception) {
			mParameters.mCanShowAlert.alert(result.toString());
		}
	}
	private void writeAField(String name, String value, DataOutputStream dos) throws IOException {
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\""+name+"\""+ lineEnd);
        dos.writeBytes(lineEnd);
        dos.writeBytes(value);
        dos.writeBytes(lineEnd);
	}

/* original
	private 
        URL connectURL;
        String responseString;
        String Title;
        String Description;
        byte[ ] dataToServer;
        FileInputStream fileInputStream = null;


		
        HttpFileUpload(String urlString, String vTitle, String vDesc){
                try{
                        connectURL = new URL(urlString);
                        Title= vTitle;
                        Description = vDesc;
                }catch(Exception ex){
                    Log.i("HttpFileUpload","URL Malformatted");
                }
        }
	
        void Send_Now(FileInputStream fStream){
                fileInputStream = fStream;
                Sending();
        }
	
        void Sending(){
                String iFileName = "ovicam_temp_vid.mp4";
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                String Tag="fSnd";
                try
                {
                        Log.e(Tag,"Starting Http File Sending to URL");
	
                        // Open a HTTP connection to the URL
                        HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();
	
                        // Allow Inputs
                        conn.setDoInput(true);
	
                        // Allow Outputs
                        conn.setDoOutput(true);
	
                        // Don't use a cached copy.
                        conn.setUseCaches(false);
	
                        // Use a post method.
                        conn.setRequestMethod("POST");
	
                        conn.setRequestProperty("Connection", "Keep-Alive");
	
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(Title);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
	                        
                        dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(Description);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
	                        
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
                        dos.writeBytes(lineEnd);
	
                        Log.e(Tag,"Headers are written");
	
                        // create a buffer of maximum size
                        int bytesAvailable = fileInputStream.available();
	                        
                        int maxBufferSize = 1024;
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        byte[ ] buffer = new byte[bufferSize];
	
                        // read file and write it into form...
                        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
                        while (bytesRead > 0)
                        {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
                        }
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	
                        // close streams
                        fileInputStream.close();
	                        
                        dos.flush();
	                        
                        Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));
	                         
                        InputStream is = conn.getInputStream();
	                        
                        // retrieve the response from server
                        int ch;
	
                        StringBuffer b =new StringBuffer();
                        while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                        String s=b.toString();
                        Log.i("Response",s);
                        dos.close();
                }
                catch (MalformedURLException ex)
                {
                        Log.e(Tag, "URL error: " + ex.getMessage(), ex);
                }
	
                catch (IOException ioe)
                {
                        Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
                }
        }
*/
}
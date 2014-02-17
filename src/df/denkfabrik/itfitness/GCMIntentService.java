package df.denkfabrik.itfitness;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gcm.GCMBaseIntentService;
import static df.denkfabrik.itfitness.CommonUtilities.displayMessage;
public class GCMIntentService extends GCMBaseIntentService{
	
	private static final String SENDER_ID="590463838489";
	public static int count=0;
	
	
	//default constructor
    public GCMIntentService() {
    	super(SENDER_ID);
        // TODO Auto-generated constructor stub
    }
	
	@Override
    protected void onError(Context arg0, String arg1) {
        Log.e("Registration", "Got an error!");
        Log.e("Registration", arg0.toString() + arg1.toString());
    }
 
    @Override
    protected void onMessage(Context context, Intent intent) {
    	count++;
    	
        String message = intent.getExtras().getString("price");
         
        displayMessage(context, message, ""+count);
        // notifies user
        generateNotification(context, message);
        
    }
    
    
 
    @Override
    protected void onRegistered(Context arg0, String arg1) {
    	String possibleEmail="";
    	 System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");   
        Log.i("Registration", "Just registered!");
        Log.i("Registration", arg1.toString());
        String regId=arg1.toString();
        HttpClient httpclient=new DefaultHttpClient();
     // Add your data
    	
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
        	
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                
                
            }
        }

        try {
        	HttpPost httppost = new HttpPost("http://itfitness-gcm.denkfabrik-entwicklung.de/web/gcm/register/");
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        	nameValuePairs.add(new BasicNameValuePair("regId", regId));
        	if(possibleEmail !=""){
        		nameValuePairs.add(new BasicNameValuePair("email", possibleEmail));
        	}
        	
        	HttpEntity postEntity = new UrlEncodedFormEntity(nameValuePairs);
        	httppost.setEntity(postEntity);
        	

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            Log.d("result",result);
            
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        
    }
    
    @Override
    protected void onUnregistered(Context arg0, String arg1) {
    }
    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        notification.number = count;
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
 
    }
    public void clearNotification(Context mContext) {
	    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	    notificationManager.cancelAll();
	}
    
}
package df.idgbusiness.itfitness;


import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    //  
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "Message";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "df.denkfabrik.itfitness.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
    static final String EXTRA_COUNT= "count";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message, String count) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_COUNT, count);
        context.sendBroadcast(intent);
    }
}
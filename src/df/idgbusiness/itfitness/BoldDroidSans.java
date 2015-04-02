package df.idgbusiness.itfitness;

import android.content.Context;
import android.graphics.Typeface;

public class BoldDroidSans {
 
	private Context context;
	private static BoldDroidSans instance;
 
	public BoldDroidSans(Context context) {
		this.context = context;
	}
 
	public static BoldDroidSans getInstance(Context context) {
		synchronized (BoldDroidSans.class) {
			if (instance == null)
				instance = new BoldDroidSans(context);
			return instance;
		}
	}
 
	public Typeface getTypeFace() {
		return Typeface.createFromAsset(context.getResources().getAssets(),
				"DroidSans-Bold.ttf");
	}
}
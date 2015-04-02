package df.idgbusiness.itfitness;

import android.content.Context;
import android.graphics.Typeface;

public class DroidSans {
 
	private Context context;
	private static DroidSans instance;
 
	public DroidSans(Context context) {
		this.context = context;
	}
 
	public static DroidSans getInstance(Context context) {
		synchronized (DroidSans.class) {
			if (instance == null)
				instance = new DroidSans(context);
			return instance;
		}
	}
 
	public Typeface getTypeFace() {
		return Typeface.createFromAsset(context.getResources().getAssets(),
				"DroidSans.ttf");
	}
}
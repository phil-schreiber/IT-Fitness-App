package df.idgbusiness.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class NativelyCustomTextView extends TextView {
 
	public NativelyCustomTextView(Context context) {
		super(context);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public NativelyCustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public NativelyCustomTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
}
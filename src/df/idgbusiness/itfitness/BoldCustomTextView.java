package df.idgbusiness.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class BoldCustomTextView extends TextView {
 
	public BoldCustomTextView(Context context) {
		super(context);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
	public BoldCustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
	public BoldCustomTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
}
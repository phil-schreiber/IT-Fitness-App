package df.denkfabrik.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;



public class BoldButtonView extends Button {
 
	public BoldButtonView(Context context) {
		super(context);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
	public BoldButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
	public BoldButtonView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(BoldDroidSans.getInstance(context).getTypeFace());
	}
 
}
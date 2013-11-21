package df.denkfabrik.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;



public class CustomButtonView extends Button {
 
	public CustomButtonView(Context context) {
		super(context);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomButtonView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
}
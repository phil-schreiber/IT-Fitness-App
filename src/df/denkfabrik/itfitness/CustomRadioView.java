package df.denkfabrik.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;




public class CustomRadioView extends RadioButton {
 
	public CustomRadioView (Context context) {
		super(context);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomRadioView (Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomRadioView (Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
}
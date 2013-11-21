package df.denkfabrik.itfitness;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;




public class CustomCheckboxView extends CheckBox {
 
	public CustomCheckboxView (Context context) {
		super(context);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomCheckboxView (Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
	public CustomCheckboxView (Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(DroidSans.getInstance(context).getTypeFace());
	}
 
}
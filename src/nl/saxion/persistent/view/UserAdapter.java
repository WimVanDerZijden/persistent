package nl.saxion.persistent.view;

import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.R.id;
import nl.saxion.persistent.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User>{
	private List<User> values;
	private Context context;

	public UserAdapter(Context context, List<User> values){
		super(context, R.layout.user_list_row, values);
		this.values = values;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.user_list_row, parent, false);
	    User user = values.get(position);
	    TextView textView = (TextView) rowView.findViewById(R.id.user_name_field);
	    textView.setText(user.getName());
	    Bitmap photo = user.getPhoto();
	    if (photo != null) {
	    	ImageView profilePic = (ImageView) rowView.findViewById(R.id.image_profile);
	    	profilePic.setImageBitmap(photo);
	    }
	    if (user.isThales()) {
	    	ImageView thalesPic = (ImageView) rowView.findViewById(R.id.image_is_thales);
	    	thalesPic.setImageDrawable(context.getResources().getDrawable(R.drawable.thales));
	    }
	    //Drawable drawable = new BitmapDrawable(context.getResources(), photo);
	    //drawable.setBounds(new Rect(0, 0, 200, 200));
	    //textView.setCompoundDrawables(drawable, null, null, null);
	    
	    return rowView;
	}
}

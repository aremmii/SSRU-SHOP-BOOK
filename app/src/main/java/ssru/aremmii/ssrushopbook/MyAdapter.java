package ssru.aremmii.ssrushopbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MyAdapter extends BaseAdapter{

    //Explicit
    private Context context;
    private String[] nameStrings, priceStrings, coverStrings;

    public MyAdapter(Context context,
                     String[] nameStrings,
                     String[] priceStrings,
                     String[] coverStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.priceStrings = priceStrings;
        this.coverStrings = coverStrings;
    }

    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.my_listview, viewGroup, false);

        TextView nameTextView = (TextView) view1.findViewById(R.id.textView10);
        nameTextView.setText(nameStrings[i]);

        TextView priceTextView = (TextView) view1.findViewById(R.id.textView11);
        priceTextView.setText(priceStrings[i] + " THB.");

        ImageView imageView = (ImageView) view1.findViewById(R.id.imageView2);
        Picasso.with(context).load(coverStrings[i]).resize(120,150).into(imageView);


        return view1;
    }
}   // Main Class

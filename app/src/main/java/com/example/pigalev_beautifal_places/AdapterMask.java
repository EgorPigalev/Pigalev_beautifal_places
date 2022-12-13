package com.example.pigalev_beautifal_places;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterMask extends BaseAdapter {

    private Context mContext;

    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    List<Mask> maskList;

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return maskList.get(i).getID();
    }

    private Bitmap getUserImage(String encodedImg) // Преобразование из String в Bitmap
    {

        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else
            return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext,R.layout.item_mask,null);

        TextView Name = v.findViewById(R.id.namePlace);
        ImageView Image = v.findViewById(R.id.picture);

        Mask mask = maskList.get(position);
        Name.setText(mask.getName());

        if(mask.getImage().toString().equals("null"))
        {
            Image.setImageResource(R.drawable.absence);
        }
        else
        {
            Image.setImageBitmap(getUserImage(mask.getImage()));
        }
        return v;
    }

}
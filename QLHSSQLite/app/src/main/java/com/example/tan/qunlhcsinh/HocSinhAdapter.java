package com.example.tan.qunlhcsinh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class HocSinhAdapter extends  ArrayAdapter{
    Context context;
    int layoutID;
    ArrayList<HocSinh> arrayList;

    public HocSinhAdapter(Context context, int layoutID, ArrayList<HocSinh> arrayList) {
        super(context, layoutID, arrayList);
        this.context = context;
        this.layoutID = layoutID;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutID, parent, false);

        TextView txt1=(TextView) view.findViewById(R.id.textView2);
        txt1.setText(arrayList.get(position).getHoTen());

        TextView txt2=(TextView) view.findViewById(R.id.textView3);
        txt2.setText(arrayList.get(position).getLop());

        ImageView img=(ImageView) view.findViewById(R.id.imageView);
        if (arrayList.get(position).isGioiTinh())
            img.setImageResource(R.drawable.boy);
        else
            img.setImageResource(R.drawable.girl);


        Button btnPlace=(Button) view.findViewById(R.id.btnPlace);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(parent.getContext(),MapsActivity.class);
                intent.putExtra("Student",arrayList.get(position));
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }
}

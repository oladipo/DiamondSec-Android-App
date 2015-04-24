package com.synkron.diamondsec.adapters;

import java.util.ArrayList;

import com.synkron.diamondsec.R;
import com.synkron.diamondsec.SideMenuModel;
import com.synkron.diamondsec.R.id;
import com.synkron.diamondsec.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SideMenuAdapter extends ArrayAdapter<SideMenuModel>{
	private final Context context;
	private ArrayList<SideMenuModel> modelArrayList;
	
	public SideMenuAdapter(Context context, ArrayList<SideMenuModel> modelArrayList) {
		super(context, R.layout.menu_list_item, modelArrayList);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.modelArrayList = modelArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//2. Get RowView from inflater
		View rowView = null;
		
		rowView = inflater.inflate(R.layout.menu_list_item, parent, false);
		
		//3. get icon and title from row view
		ImageView imageView = (ImageView) rowView.findViewById(R.id.item_icon);
		TextView textView = (TextView) rowView.findViewById(R.id.text1);
		
		//4. set text for text view and icon resource for imageview
		imageView.setImageResource(modelArrayList.get(position).getIcon());
		textView.setText(modelArrayList.get(position).getTitle());
		
		
		return rowView;
		
	}
}

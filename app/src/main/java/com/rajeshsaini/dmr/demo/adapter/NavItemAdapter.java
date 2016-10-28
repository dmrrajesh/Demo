package com.rajeshsaini.dmr.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajeshsaini.dmr.demo.Admin;
import com.rajeshsaini.dmr.demo.Profile;
import com.rajeshsaini.dmr.demo.ProfileChild;
import com.rajeshsaini.dmr.demo.R;
import com.rajeshsaini.dmr.demo.models.NavItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 3/9/2016.
 */

public class NavItemAdapter extends ArrayAdapter<NavItem>{
    List<NavItem> navItems;
    int resource;
    Context context;
    DisplayMetrics metrics;
    int navWidth;
    public NavItemAdapter(Context context, int resource, List<NavItem> navItems) {
        super(context, resource, navItems);
        this.context=context;
        this.resource=resource;
        this.navItems=navItems;
    }
    public NavItemAdapter(Context context, int resource, List<NavItem> navItems, int navWidth) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
        this.navItems = navItems;
        this.navWidth =navWidth;
    }
    public NavItemAdapter(Context context, int resource, List<NavItem> navItems, DisplayMetrics metrics) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
        this.navItems = navItems;
        this.metrics = metrics;
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view=View.inflate(context,resource,null);
        final NavItem navItem=navItems.get(position);
        ImageView navIcon=(ImageView)view.findViewById(R.id.drawer_menu_item_icon);
        TextView navTitle=(TextView)view.findViewById(R.id.drawer_menu_item_title);
        TextView navMobile=(TextView)view.findViewById(R.id.drawer_menu_item_mobile);
        String image=Admin.getImagePath(navItem.getImage());
        Picasso.with(context).load(image)
                .error(R.mipmap.ic_account_box_white_48dp)
                .placeholder(R.mipmap.ic_account_box_white_48dp)
                .into(navIcon);
        navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProfileChild.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        navTitle.setText(navItem.getNavTitle());
        navMobile.setText(navItem.getMobile());
        return view;
    }
}

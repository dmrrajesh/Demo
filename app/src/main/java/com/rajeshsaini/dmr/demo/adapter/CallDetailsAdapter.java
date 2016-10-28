package com.rajeshsaini.dmr.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.rajeshsaini.dmr.demo.Admin;
import com.rajeshsaini.dmr.demo.R;
import com.rajeshsaini.dmr.demo.models.CallDetails;
import java.util.List;
/**
 * Created by DMRSAINI on 3/15/2016.
 */
public class CallDetailsAdapter extends ArrayAdapter<CallDetails>{
    private List<CallDetails> callDetailsList;
    private int resource;
    private Context context;

    public CallDetailsAdapter(Context context, int resource, List<CallDetails> callDetailsList) {
        super(context, resource,callDetailsList);
        this.context=context;
        this.resource=resource;
        this.callDetailsList = callDetailsList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context,resource,null);
        CallDetails details=callDetailsList.get(position);
        TextView mobile=(TextView)view.findViewById(R.id.call_details_mobile);
        TextView date=(TextView)view.findViewById(R.id.call_details_date);
        TextView time=(TextView)view.findViewById(R.id.call_details_time);
        TextView duration=(TextView)view.findViewById(R.id.call_details_duration);
        ImageView type=(ImageView)view.findViewById(R.id.call_details_type);
        mobile.setText(details.getMobile());
        date.setText(details.getDate());
        time.setText(details.getTime());
        duration.setText(details.getDuration()+" Sec");
        if(Admin.CALL_MISS.equalsIgnoreCase(details.getType().trim())){
            type.setImageResource(android.R.drawable.sym_call_missed);
        }else if(Admin.CALL_OUTGOING.equalsIgnoreCase(details.getType().trim())){
            type.setImageResource(android.R.drawable.sym_call_outgoing);
        }else if(Admin.CALL_INCOMING.equalsIgnoreCase(details.getType().trim())){
            type.setImageResource(android.R.drawable.sym_call_incoming);
        }
        return view;
    }
}

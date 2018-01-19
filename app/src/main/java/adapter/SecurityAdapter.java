package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomanav.wahed.R;

import java.util.List;

import model_classes.Document;
import model_classes.Security;
import services.Application_Constants;
import services.Utility;

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.CustomViewHolder> {
    private List<Security> securityList;
    private Context mContext;
    private String ischild="";
    private PopupWindow feedMenuPW;
    private View popupView;
    private LayoutInflater inflater;
    private String siteUserID;
    private Utility utility;
    private String strTokLike = Application_Constants.Main_URL+"xAction=tokeLike";
    private String strTokDelete = Application_Constants.Main_URL+"xAction=tokDelete";
    private String strTokShare = Application_Constants.Main_URL+"xAction=tokShare";

    public SecurityAdapter(Context context, List<Security> securityList) {
        this.securityList = securityList;
        this.mContext = context;

        // popup menu
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_row_new, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);*/


        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_security_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Security security = securityList.get(i);



        //Setting text view title


        if(!TextUtils.isEmpty(security.getState()))
        {
            customViewHolder.tv_state.setText(security.getState());
        }
        else
        {
            customViewHolder.tv_state.setText("-");
        }

        if(!TextUtils.isEmpty(security.getIp_address()))
        {
            customViewHolder.tv_ip_address.setText(security.getIp_address());
        }
        else
        {
            customViewHolder.tv_ip_address.setText("-");
        }
        if(!TextUtils.isEmpty(security.getCountry()))
        {
            customViewHolder.tv_country.setText(security.getCountry());
        }
        else
        {
            customViewHolder.tv_country.setText("-");
        }



        customViewHolder.tv_date.setText(security.getDate());
        customViewHolder.tv_time.setText(security.getTime());


        customViewHolder.tv_ip_address.setTag(customViewHolder);
        customViewHolder.tv_state.setTag(customViewHolder);
        customViewHolder.tv_country.setTag(customViewHolder);
        customViewHolder.tv_date.setTag(customViewHolder);
        customViewHolder.tv_time.setTag(customViewHolder);
        customViewHolder.lay_parent.setTag(customViewHolder);




        customViewHolder.lay_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                Security security = securityList.get(position);

            }
        });



        View.OnClickListener clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getPosition();

            }
        };
    }


    @Override
    public int getItemCount()
    {
        int list_size = securityList.size();

        return (null != securityList ? securityList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder
    {

         TextView tv_ip_address,tv_state,tv_country,tv_date,tv_time;
        LinearLayout lay_parent;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_ip_address = (TextView) view.findViewById(R.id.tv_ip_address);
            this.tv_state = (TextView) view.findViewById(R.id.tv_state);
            this.tv_country = (TextView) view.findViewById(R.id.tv_country);
            this.tv_date = (TextView) view.findViewById(R.id.tv_date);
            this.tv_time = (TextView) view.findViewById(R.id.tv_time);
            this.lay_parent = (LinearLayout) view.findViewById(R.id.lay_parent);


        }
    }

}
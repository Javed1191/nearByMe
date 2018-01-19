package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infomanav.wahed.MainActivity;
import com.infomanav.wahed.R;
import com.infomanav.wahed.TransactionDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model_classes.Transaction;
import services.Application_Constants;
import services.Shared_Preferences_Class;
import services.Utility;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.CustomViewHolder> {
    private List<Transaction> feedList;
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

    public TransactionAdapter(Context context, List<Transaction> feedList) {
        this.feedList = feedList;
        this.mContext = context;

        // popup menu
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_row_new, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);*/


        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_transaction_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Transaction transaction = feedList.get(i);


        if(transaction.is_valid())
        {
            customViewHolder.tv_price.setTextColor(Color.parseColor("#29c065"));
        }
        else
        {
            customViewHolder.tv_price.setTextColor(Color.parseColor("#dc2f2f"));
        }

        //Setting text view title
       customViewHolder.tv_title.setText(transaction.getTitle());
        customViewHolder.tv_date.setText(transaction.getDate());
        customViewHolder.tv_price.setText(transaction.getPrice());


        customViewHolder.tv_title.setTag(customViewHolder);
        customViewHolder.tv_date.setTag(customViewHolder);
        customViewHolder.tv_price.setTag(customViewHolder);
        customViewHolder.img_arrow.setTag(customViewHolder);
        customViewHolder.lay_parent.setTag(customViewHolder);




        customViewHolder.lay_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                Transaction feeds = feedList.get(position);

                Intent intent = new Intent(mContext, TransactionDetailsActivity.class);
                intent.putExtra("title",feeds.getTitle());
                intent.putExtra("amount",feeds.getPrice());
                intent.putExtra("date",feeds.getDate());
                mContext.startActivity(intent);

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
        int list_size = feedList.size();

        return (null != feedList ? feedList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder
    {

         TextView tv_title,tv_date,tv_price;
        RelativeLayout lay_parent;
        ImageView img_arrow;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_title = (TextView) view.findViewById(R.id.tv_title);
            this.tv_date = (TextView) view.findViewById(R.id.tv_date);
            this.tv_price = (TextView) view.findViewById(R.id.tv_price);
            this.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            this.lay_parent = (RelativeLayout) view.findViewById(R.id.lay_parent);


        }
    }

}
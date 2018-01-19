package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomanav.wahed.PositionDetailsActivity;
import com.infomanav.wahed.R;

import java.util.List;

import model_classes.Category;
import services.Application_Constants;
import services.Utility;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {
    private List<Category> feedList;
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
    public DisplayMetrics displayMetrics;
    View rootView;
    public CategoryAdapter(Context context, List<Category> feedList) {
        this.feedList = feedList;
        this.mContext = context;
        displayMetrics = mContext.getResources().getDisplayMetrics();
        // popup menu
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_row_new, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);*/


        rootView = LayoutInflater.from(mContext).inflate(R.layout.list_category_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Category category = feedList.get(i);



       /* //Setting text view title
       customViewHolder.tv_title.setText(document.getTitle());
        customViewHolder.tv_.setText(document.getDate());
        customViewHolder.tv_price.setText(document.getCountry());*/

        final Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);


        int width  = rootView.getMeasuredWidth();
        int height = rootView.getMeasuredHeight();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, (displayMetrics.widthPixels / 2)+300);

        if(category.is_open())
        {
           // customViewHolder.lay_child.setVisibility(View.VISIBLE);
           // customViewHolder.lay_child.startAnimation(slideUp);
            customViewHolder.lay_child.setVisibility(View.VISIBLE);
        }
        else
        {
            customViewHolder.lay_child.setVisibility(View.GONE);
        }
        customViewHolder.lay_main_parent.setBackgroundColor(Color.parseColor(category.getColor()));

        customViewHolder.tv_security_name.setText(category.getSecuritiesName());
        customViewHolder.tv_allocation.setText(category.getAllocation()+" %");
        customViewHolder.tv_desc.setText(category.getSecurityDescription());
        customViewHolder.tv_running_perc.setText(category.getApiPrice());

        if(category.getApistatus().equalsIgnoreCase("up"))
        {
            customViewHolder.tv_share_change_perc.setTextColor(Color.parseColor("#29c065"));
            customViewHolder.img_arrow.setImageResource(R.drawable.icon_up);

        }
        else
        {
            customViewHolder.tv_share_change_perc.setTextColor(Color.parseColor("#ff0000"));
            customViewHolder.img_arrow.setImageResource(R.drawable.icon_down);
        }

        if(category.getApichange().equalsIgnoreCase("0.00"))
        {
            customViewHolder.tv_share_change_perc.setTextColor(Color.parseColor("#000000"));
            customViewHolder.tv_share_change_perc.setText("("+category.getApichange()+")");
            customViewHolder.img_arrow.setVisibility(View.GONE);

        }
        else
        {
            customViewHolder.tv_share_change_perc.setText("("+category.getApichange()+")");
            customViewHolder.img_arrow.setVisibility(View.VISIBLE);
        }

        customViewHolder.tv_security_name.setTag(customViewHolder);
        customViewHolder.tv_allocation.setTag(customViewHolder);
        customViewHolder.img_arrow.setTag(customViewHolder);
        customViewHolder.lay_parent.setTag(customViewHolder);
        customViewHolder.lay_child.setTag(customViewHolder);
        customViewHolder.tv_desc.setTag(customViewHolder);
        customViewHolder.tv_running_perc.setTag(customViewHolder);
        customViewHolder.tv_share_change_perc.setTag(customViewHolder);
        customViewHolder.lay_main_parent.setTag(customViewHolder);


        customViewHolder.lay_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                Category feeds = feedList.get(position);

                if(feeds.is_open())
                {
                    feeds.setIs_open(false);
                    notifyDataSetChanged();
                }
                else
                {
                    feeds.setIs_open(true);
                    notifyDataSetChanged();
                }

            }
        });

        customViewHolder.lay_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                Category feeds = feedList.get(position);
                Intent intent = new Intent(mContext, PositionDetailsActivity.class);
                intent.putExtra("title",feeds.getSecurityDescription());
                intent.putExtra("model_fk_id",feeds.getModelsFkId());
                intent.putExtra("account_id",feeds.getAccountID());
                intent.putExtra("position_id",feeds.getPositionID());
                intent.putExtra("api_change",feeds.getApichange());
                intent.putExtra("api_price",feeds.getApiPrice());
                intent.putExtra("api_status",feeds.getApistatus());
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

         TextView tv_security_name,tv_allocation,tv_desc,tv_running_perc,tv_share_change_perc;
        RelativeLayout lay_child;
        ImageView img_arrow;
        LinearLayout lay_parent,lay_main_parent;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_security_name = (TextView) view.findViewById(R.id.tv_security_name);
            this.tv_allocation = (TextView) view.findViewById(R.id.tv_allocation);
            this.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            this.tv_running_perc = (TextView) view.findViewById(R.id.tv_running_perc);
            this.tv_share_change_perc = (TextView) view.findViewById(R.id.tv_share_change_perc);
            this.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            this.lay_parent = (LinearLayout) view.findViewById(R.id.lay_parent);
            this.lay_child = (RelativeLayout) view.findViewById(R.id.lay_child);
            this.lay_main_parent = (LinearLayout) view.findViewById(R.id.lay_main_parent);



        }
    }

}
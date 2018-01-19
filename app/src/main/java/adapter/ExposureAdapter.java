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
import model_classes.Exposure;
import services.Application_Constants;
import services.Utility;

public class ExposureAdapter extends RecyclerView.Adapter<ExposureAdapter.CustomViewHolder> {
    private List<Exposure> feedList;
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
    public ExposureAdapter(Context context, List<Exposure> feedList) {
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


        rootView = LayoutInflater.from(mContext).inflate(R.layout.list_exposure_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Exposure category = feedList.get(i);


        customViewHolder.tv_title.setText(category.getSector_name());
        customViewHolder.tv_value.setText(category.getSector_percentage());

        customViewHolder.tv_title.setTag(customViewHolder);
        customViewHolder.tv_value.setTag(customViewHolder);

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

         TextView tv_title,tv_value;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_title = (TextView) view.findViewById(R.id.tv_title);
            this.tv_value = (TextView) view.findViewById(R.id.tv_value);
        }
    }

}
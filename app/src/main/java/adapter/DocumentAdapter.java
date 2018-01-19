package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infomanav.wahed.R;

import java.util.List;

import model_classes.Document;
import model_classes.Transaction;
import services.Application_Constants;
import services.Utility;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.CustomViewHolder> {
    private List<Document> feedList;
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

    public DocumentAdapter(Context context, List<Document> feedList) {
        this.feedList = feedList;
        this.mContext = context;

        // popup menu
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_row_new, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);*/


        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_document_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Document document = feedList.get(i);



        //Setting text view title
       customViewHolder.tv_title.setText(document.getTitle());
        customViewHolder.tv_des.setText(document.getDescription());


        customViewHolder.tv_title.setTag(customViewHolder);
        customViewHolder.tv_des.setTag(customViewHolder);
        customViewHolder.img_download.setTag(customViewHolder);
        customViewHolder.img_pdf.setTag(customViewHolder);
        customViewHolder.lay_parent.setTag(customViewHolder);




        customViewHolder.lay_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();
                Document document = feedList.get(position);

                String pdf = document.getLink();


                try
                {
                    if (!pdf.startsWith("http://") && !pdf.startsWith("https://"))
                    {
                        pdf = "http://" + pdf;
                    }

                    if(!pdf.isEmpty())
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                        mContext.startActivity(browserIntent);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

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

         TextView tv_title,tv_des;
        RelativeLayout lay_parent;
        ImageView img_download,img_pdf;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_title = (TextView) view.findViewById(R.id.tv_title);
            this.tv_des = (TextView) view.findViewById(R.id.tv_des);
            this.img_download = (ImageView) view.findViewById(R.id.img_download);
            this.img_pdf = (ImageView) view.findViewById(R.id.img_pdf);
            this.lay_parent = (RelativeLayout) view.findViewById(R.id.lay_parent);


        }
    }

}
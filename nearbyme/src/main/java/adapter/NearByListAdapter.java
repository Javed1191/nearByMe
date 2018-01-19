package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nearbyme.R;

import java.util.List;

import classes.Place;
import utils.GetCurrentLocation;

public class NearByListAdapter extends RecyclerView.Adapter<NearByListAdapter.CustomViewHolder> {
    private List<Place> ContactsList;
    private Context mContext;
    GetCurrentLocation currentLocation;

    public NearByListAdapter(Context context, List<Place> ContactsList) {
        this.ContactsList = ContactsList;
        this.mContext = context;
        currentLocation = new GetCurrentLocation(mContext);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_row_new, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);*/


        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_near_by_me_view_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        final CustomViewHolder viewHolder = new CustomViewHolder(rootView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Place place = ContactsList.get(i);
        // position = i;

        //Download image using picasso library
       /* Picasso.with(mContext).load(Contacts.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);*/


        //Setting text view title
        customViewHolder.tv_address.setText(place.getAddress_1()+" "+ place.getAddress_2()+" "+ place.getAddress_3()+" "+ place.getArea());

        String formato = String.format("%.2f",Double.parseDouble(place.getDistance()));
        customViewHolder.tv_distance.setText(formato+" Km");


        customViewHolder.tv_address.setTag(customViewHolder);
        customViewHolder.tv_distance.setTag(customViewHolder);
        customViewHolder.lay_direction.setTag(customViewHolder);


        customViewHolder.lay_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomViewHolder holder = (CustomViewHolder) v.getTag();
                int position = holder.getPosition();

                Place apps = ContactsList.get(position);


/*                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);*/
                String strStartLat="",strStartLong="",strDesLat="",strDesLong="";
                strDesLat = apps.getLat();
                strDesLong = apps.getLog();

                strStartLat = String.valueOf(currentLocation.getLatitude());
                strStartLong = String.valueOf(currentLocation.getLongitude());


                String strUrl = "http://maps.google.com/maps?saddr="+strStartLat+","+strStartLong+"&daddr="+strDesLat+","+strDesLong;

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(strUrl));
                mContext.startActivity(intent);



                //  Toast.makeText(mContext, Products.getProductTitle()+"="+ position, Toast.LENGTH_SHORT).show();
                  /*  Toast.makeText(mContext, Products.getEmployeeName()+"="+ position, Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(mContext,ScoreDetailsActivity.class);
                    intent.putExtra("empName",Products.getEmployeeName());
                    intent.putExtra("empCode",Products.getEmployeeCode());
                    intent.putExtra("empScoreId",Products.getScoreId());
                    mContext.startActivity(intent);*/


            }
        });
    }


    @Override
    public int getItemCount()
    {
        int list_size = ContactsList.size();

        return (null != ContactsList ? ContactsList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder
    {

         TextView tv_address,tv_distance;
        LinearLayout lay_direction;


        public CustomViewHolder(View view)
        {
            super(view);
            this.tv_address = (TextView) view.findViewById(R.id.tv_address);
            this.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            this.lay_direction = (LinearLayout) view.findViewById(R.id.lay_direction);
        }
    }




}
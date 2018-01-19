package com.infomanav.wahed;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.CategoryAdapter;
import adapter.HomeFragmentTabAdapter;
import adapter.PositionDetailsTabAdapter;
import model_classes.Category;
import services.Application_Constants;
import services.JWTToken;
import services.ServiceHandler;
import services.Utility;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class PositionDetailsActivity extends AppCompatActivity
{

    private TextView tv_action_title,tv_title,tv_running_perc,tv_share_change_perc,tv_symbol,
            tv_allocation,tv_quantity,tv_cost_basis_price,tv_unrealized_gains_loss,tv_position_value;
    private String strPositionDetailsUrl = Application_Constants.Main_URL+"positionDetails";
    private Utility utility;
    private JWTToken jwtToken;
    private  Toolbar toolbar;
    private String title="",model_fk_id="",account_id="",position_id="",api_change="",
            api_price,api_status;
    private ImageView img_arrow;
    private TabLayout tabLayout;
    public DisplayMetrics displayMetrics;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_details);
        // Initilization
       init();

        Intent intent = getIntent();

        if(intent.getExtras()!=null)
        {
            title = intent.getStringExtra("title");
            model_fk_id = intent.getStringExtra("model_fk_id");
            account_id = intent.getStringExtra("account_id");
            position_id = intent.getStringExtra("position_id");

            api_change = intent.getStringExtra("api_change");
            api_price = intent.getStringExtra("api_price");
            api_status = intent.getStringExtra("api_status");

            if (!TextUtils.isEmpty(title)) {
                tv_action_title.setText(title);
                tv_title.setText(title);
            }


            if(!TextUtils.isEmpty(api_price))
            {
                tv_running_perc.setText(api_price);
            }

            if(api_status.equalsIgnoreCase("up"))
            {
                tv_share_change_perc.setTextColor(Color.parseColor("#29c065"));
                img_arrow.setImageResource(R.drawable.icon_up);

            }
            else
            {
                tv_share_change_perc.setTextColor(Color.parseColor("#ff0000"));
                img_arrow.setImageResource(R.drawable.icon_down);
            }


            if(api_change.equalsIgnoreCase("0.00"))
            {
               tv_share_change_perc.setTextColor(Color.parseColor("#000000"));
                tv_share_change_perc.setText("("+api_change+")");
                img_arrow.setVisibility(View.GONE);

            }
            else
            {
                tv_share_change_perc.setText("("+api_change+")");
                img_arrow.setVisibility(View.VISIBLE);
            }


            getPositionDetails(account_id,model_fk_id,position_id);
        }
    }

    private void init()
    {
        displayMetrics = getResources().getDisplayMetrics();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_action_title = (TextView) toolbar.findViewById(R.id.tv_action_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_action_title.setText("Transaction Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("menu","home");
                startActivity(intent);
            }
        });
        // status bar color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        utility = new Utility(getApplicationContext());
        jwtToken = new JWTToken(getApplicationContext());

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_running_perc = (TextView) findViewById(R.id.tv_running_perc);
        tv_share_change_perc = (TextView) findViewById(R.id.tv_share_change_perc);
        tv_symbol = (TextView) findViewById(R.id.tv_symbol);
        tv_allocation = (TextView) findViewById(R.id.tv_allocation);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);
        tv_cost_basis_price = (TextView) findViewById(R.id.tv_cost_basis_price);
        tv_unrealized_gains_loss = (TextView) findViewById(R.id.tv_unrealized_gains_loss);
        tv_position_value = (TextView) findViewById(R.id.tv_position_value);
        img_arrow = (ImageView) findViewById(R.id.img_arrow);



        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("HIGHLIGHTS"));
        tabLayout.addTab(tabLayout.newTab().setText("PERFORMANCE"));
        tabLayout.addTab(tabLayout.newTab().setText("EXPOSURE"));
        tabLayout.addTab(tabLayout.newTab().setText("SHARIAH BOARD"));

        // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager = (ViewPager) findViewById(R.id.pager);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayMetrics.widthPixels);
        viewPager.setLayoutParams(layoutParams);
        fm = getSupportFragmentManager();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // .resize(displayMetrics.widthPixels / 3,displayMetrics.widthPixels / 3)

        changeTabsFont();

    }




    private void getPositionDetails(String accountID, String modelsFkId, String positionID)
    {

        if(utility.checkInternet())
        {
            //final ArrayList<Department> feedsArrayList = new ArrayList<Department>();
            String strJwtToken = jwtToken.getJWTToken();

            final Map<String, String> params = new HashMap<String, String>();
            params.put("jwtToken", strJwtToken);
            params.put("accountID", accountID);
            params.put("modelsFkId", modelsFkId);
            params.put("positionID", positionID);
            ServiceHandler serviceHandler = new ServiceHandler(PositionDetailsActivity.this);


            serviceHandler.registerUser(params, strPositionDetailsUrl, new ServiceHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    System.out.println(strPositionDetailsUrl+" Json responce" + result);

                    //Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
                    String str_json = result;
                    String str_status, str_msg,server_jwt_token;
                    String  highlights="",AccountNumber="",SecuritiesName="",Symbol="",Allocation="",
                            Quantity="",CostBasisPrice="",MarketPrice="",InvestementValue="",
                            PosValuePerSec="",UpdatedDate="",UpdatedDateUnix="",SecurityDescription="",
                            UnrealizedG_LPerSecurity="",accountID="",UnrealizedG="",positionID="";
                    final ArrayList<Category> categoryArrayList = new ArrayList<Category>();
                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                    ArrayList<Integer> colors = new ArrayList<Integer>();
                    try {
                        if (str_json != null)
                        {
                            JSONObject jobject = new JSONObject(str_json);
                            str_status = jobject.getString("status");
                            str_msg = jobject.getString("msg");
                            server_jwt_token  = jobject.getString("server_jwt_token");
                            if (str_status.equalsIgnoreCase("success"))
                            {
                                if(jwtToken.decryptJWTToken(server_jwt_token))
                                {
                                    JSONArray jArray = new JSONArray();
                                    jArray = jobject.getJSONArray("user_data");


                                    for (int i = 0; i < jArray.length(); i++)
                                    {
                                        JSONObject Obj = jArray.getJSONObject(i);
                                        // A category variables
                                        positionID  = Obj.getString("positionID");
                                        AccountNumber = Obj.getString("AccountNumber");
                                        SecuritiesName = Obj.getString("SecuritiesName");
                                        Symbol = Obj.getString("Symbol");
                                        Quantity = Obj.getString("Quantity");
                                        CostBasisPrice = Obj.getString("CostBasisPrice");
                                        MarketPrice = Obj.getString("MarketPrice");
                                        InvestementValue = Obj.getString("InvestementValue");
                                        UnrealizedG = Obj.getString("UnrealizedG");
                                        PosValuePerSec = Obj.getString("PosValuePerSec");
                                        UpdatedDate = Obj.getString("UpdatedDate");
                                        UpdatedDateUnix = Obj.getString("UpdatedDateUnix");
                                        SecurityDescription = Obj.getString("SecurityDescription");
                                        Allocation = Obj.getString("Allocation");
                                        highlights = Obj.getString("highlights");

                                    }

                                    if(!TextUtils.isEmpty(Quantity)&&!Quantity.equalsIgnoreCase("null"))
                                    {
                                        Quantity = String.format("%.2f", Double.valueOf(Quantity));
                                    }
                                    if(!TextUtils.isEmpty(CostBasisPrice)&&!CostBasisPrice.equalsIgnoreCase("null"))
                                    {
                                        CostBasisPrice = String.format("%.2f", Double.valueOf(CostBasisPrice));
                                    }
                                    if(!TextUtils.isEmpty(Allocation)&&!Allocation.equalsIgnoreCase("null"))
                                    {
                                        Allocation = String.format("%.2f", Double.valueOf(Allocation));
                                    }
                                    if(!TextUtils.isEmpty(UnrealizedG)&&!UnrealizedG.equalsIgnoreCase("null"))
                                    {
                                        UnrealizedG = String.format("%.2f", Double.valueOf(UnrealizedG));
                                    }
                                    if(!TextUtils.isEmpty(PosValuePerSec)&&!PosValuePerSec.equalsIgnoreCase("null"))
                                    {
                                        PosValuePerSec = String.format("%.2f", Double.valueOf(PosValuePerSec));
                                    }

                                    tv_symbol.setText(Symbol);
                                    tv_allocation.setText(Allocation);
                                    tv_quantity.setText(Quantity);
                                    tv_cost_basis_price.setText(CostBasisPrice);
                                    tv_unrealized_gains_loss.setText(UnrealizedG);
                                    tv_position_value.setText(PosValuePerSec);

                                    final PositionDetailsTabAdapter adapter = new PositionDetailsTabAdapter(fm, tabLayout.getTabCount(),highlights,Symbol);
                                    viewPager.setAdapter(adapter);
                                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


                                }
                                else
                                {
                                    Log.i("JWT","This Is Not Valid");
                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "This is server issue", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void changeTabsFont() {
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/CenturyGothic_regular.ttf");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);

                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(type);
                    ((TextView) tabViewChild).setTextSize(11);



                }
            }
        }
    }

}

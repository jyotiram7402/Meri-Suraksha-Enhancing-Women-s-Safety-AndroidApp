package com.example.merisuraksha.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merisuraksha.Adapter.IconAdapter;
import com.example.merisuraksha.Domain.IconDomain;
import com.example.merisuraksha.R;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CardView instruction, testing,chatbot,news;
    String prevStarted = "yesChoosen";

    RecyclerView recyclerViewIcon;
    IconAdapter iconAdapter;
    private static final String PREF_DIALOG_SHOWN = "dialog_shown";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(PREF_DIALOG_SHOWN, false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_DIALOG_SHOWN, true);
            editor.apply();

            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);

            Button btn_okay = mView.findViewById(R.id.btn_okay);
            TextView headingText = mView.findViewById(R.id.headings);
            headingText.setVisibility(View.INVISIBLE);
            TextView textView = mView.findViewById(R.id.textFormodal);
            textView.setText("Hidden cameras which emit IR rays can simply be detected by using night vision of your phone camera.But these days very high tech hidden cameras are present in the market which do not even emit IR rays, so our magnetometer sensor simulation helps you detect those spy cameras very easily..");
            alert.setView(mView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewIcon = findViewById(R.id.recyclerviewicon);
        ArrayList<IconDomain> iconDomainArrayList = new ArrayList<>();
        iconDomainArrayList.add(new IconDomain("Camera Detector","cctv"));
        iconDomainArrayList.add(new IconDomain("Help","helpline"));
        iconDomainArrayList.add(new IconDomain("Live location","location"));
        iconDomainArrayList.add(new IconDomain("Emergency SOS","emergency"));
        iconDomainArrayList.add(new IconDomain("Siren","alarm"));
        iconDomainArrayList.add(new IconDomain("News","newspaper"));
        iconDomainArrayList.add(new IconDomain("Chatbot","conversation"));
        iconAdapter = new IconAdapter(iconDomainArrayList);
        recyclerViewIcon.setLayoutManager(new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false));
        recyclerViewIcon.setAdapter(iconAdapter);
    }
}

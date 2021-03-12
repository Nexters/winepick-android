package kr.co.nexters.winepick.ui.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import kr.co.nexters.winepick.R;

public class DetailActivity extends AppCompatActivity {

    ImageView floatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        floatingBtn = (ImageView) findViewById(R.id.floating_eclipse);
        floatingBtn.bringToFront();
    }
}
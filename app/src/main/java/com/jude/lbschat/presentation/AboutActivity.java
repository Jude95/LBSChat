package com.jude.lbschat.presentation;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.lbschat.R;
import com.jude.utils.JUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
public class AboutActivity extends BeamBaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        version.setText(getString(R.string.app_name)+" "+JUtils.getAppVersionName());
    }
}

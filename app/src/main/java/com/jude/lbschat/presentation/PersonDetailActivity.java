package com.jude.lbschat.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.lbschat.R;
import com.jude.lbschat.data.AccountModel;
import com.jude.lbschat.domain.entities.PersonBrief;
import com.jude.utils.JUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.caoyue.util.time.Time;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
@RequiresPresenter(PersonDetailPresenter.class)
public class PersonDetailActivity extends BeamDataActivity<PersonDetailPresenter, PersonBrief> {

    @Bind(R.id.img_avatar)
    ImageView imgAvatar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_intro)
    TextView tvIntro;
    @Bind(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
    }

    @Override
    public void setData(PersonBrief data) {
        Glide.with(this)
                .load(data.getAvatar())
                .into(imgAvatar);
        collapsingToolbarLayout.setTitle(data.getName());
        tvGender.setText(data.getGender()==0?"男":"女");
        tvAddress.setText(data.getAddress());
        tvIntro.setText(data.getIntro());
        Time time1 = new Time(data.getBirth());
        Time time2 = new Time(System.currentTimeMillis()/1000);
        tvAge.setText(time2.getYear()-time1.getYear()+"岁");

        JUtils.Log("A"+data.getId()+" B"+AccountModel.getInstance().getCurrentAccount().getId());
        if (data.getId() == AccountModel.getInstance().getCurrentAccount().getId()){
            floatingActionButton.setImageResource(R.drawable.edit);
            RxView.clicks(floatingActionButton).subscribe(aVoid -> {
                Intent i = new Intent(this, EditActivity.class);
                startActivity(i);
            });
        }
    }
}

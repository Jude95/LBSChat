package com.jude.lbschat.presentation;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.lbschat.R;
import com.jude.lbschat.domain.entities.PersonBrief;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.caoyue.util.time.Time;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
@RequiresPresenter(EditPresenter.class)
public class EditActivity extends BeamDataActivity<EditPresenter, PersonBrief> implements DatePickerDialog.OnDateSetListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_avatar)
    ImageView imgAvatar;
    @Bind(R.id.male)
    RadioButton male;
    @Bind(R.id.female)
    RadioButton female;
    @Bind(R.id.tv_birth)
    TextView tvBirth;
    @Bind(R.id.et_name)
    AppCompatEditText etName;
    @Bind(R.id.et_intro)
    AppCompatEditText etIntro;
    @Bind(R.id.radio_gender)
    RadioGroup radioGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        imgAvatar.setOnClickListener(v->{
            new MaterialDialog.Builder(this)
                    .title("选择图片来源")
                    .items(new String[]{"拍照", "相册", "网络"})
                    .itemsCallback((materialDialog, view, i, charSequence) -> getPresenter().editLogo(i)).show();
        });

        RxTextView.textChanges(etName)
                .skip(1)
                .subscribe(charSequence -> {
            getPresenter().getData().setName(charSequence.toString());
        });

        RxTextView.textChanges(etIntro)
                .skip(1)
                .subscribe(charSequence -> {
            getPresenter().getData().setIntro(charSequence.toString());
        });

        RxRadioGroup.checkedChanges(radioGender).subscribe(integer -> {
            switch (integer){
                case R.id.male:
                    getPresenter().getData().setGender(0);
                    break;
                case R.id.female:
                    getPresenter().getData().setGender(1);
                    break;
            }
        });

        RxView.clicks(tvBirth).subscribe(aVoid -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    EditActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "选择生日");
        });
    }

    @Override
    public void setData(PersonBrief data) {
        super.setData(data);
        if (!TextUtils.isEmpty(data.getAvatar()))
        Glide.with(this)
                .load(data.getAvatar())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(imgAvatar);
        if (data.getGender() == 0) male.setChecked(true);
        else female.setChecked(true);
        tvBirth.setText(new Time(data.getBirth()).toString("yyyy年MM月dd日"));
        etName.setText(data.getName());
        etIntro.setText(data.getIntro());

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        getPresenter().getData().setBirth(new Time(year,monthOfYear,dayOfMonth,0,0,0).getTimeStamp());
        getPresenter().refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem add = menu.add("提交");
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        add.setOnMenuItemClickListener(item -> {
            getPresenter().upload();
            return true;
        });
        return true;
    }
}

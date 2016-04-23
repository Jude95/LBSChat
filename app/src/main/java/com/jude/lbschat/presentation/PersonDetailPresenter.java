package com.jude.lbschat.presentation;

import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.lbschat.data.AccountModel;
import com.jude.lbschat.domain.entities.PersonBrief;

/**
 * Created by Mr.Jude on 2016/4/23.
 */
public class PersonDetailPresenter extends BeamDataActivityPresenter<PersonDetailActivity,PersonBrief> {

    @Override
    protected void onResume() {
        super.onResume();
        if (getData().getId() == AccountModel.getInstance().getCurrentAccount().getId()){
            setData(AccountModel.getInstance().getCurrentAccount());
        }
    }
}

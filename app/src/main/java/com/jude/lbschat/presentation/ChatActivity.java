package com.jude.lbschat.presentation;

import android.os.Bundle;

import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.lbschat.R;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by zhuchenxi on 15/7/21.
 */
public class ChatActivity extends BeamBaseActivity {
    String id;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        id = getIntent().getData().getQueryParameter("targetId");
        title = getIntent().getData().getQueryParameter("title");
        ConversationFragment fragment =  (ConversationFragment)getSupportFragmentManager().findFragmentById(R.id.conversation);
        getSupportActionBar().setTitle(title);
        if (fragment !=null) fragment.setUri(getIntent().getData());
        else{
            finish();
        }
    }
}

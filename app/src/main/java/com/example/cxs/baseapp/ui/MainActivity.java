package com.example.cxs.baseapp.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.cxs.baseapp.R;
import com.example.cxs.baseapp.ui.base.BaseActivity;
import com.example.cxs.baseapp.ui.base.BaseFragment;

public class MainActivity extends BaseActivity {

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startFragment(new NovelFragment());
    }

    private void startFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.fragmentTag);
        transaction.addToBackStack(fragment.fragmentTag);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}

package com.qgyyzs.globalcosmetics.nim.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.main.model.MainTab;
import com.qgyyzs.globalcosmetics.uikit.common.fragment.TabFragment;


public abstract class MainTabFragment extends TabFragment {

    private boolean loaded = false;

    private MainTab tabData;

    protected abstract void onInit();

    protected boolean inited() {
        return loaded;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_tab_fragment_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void attachTabData(MainTab tabData) {
        this.tabData = tabData;
    }

    @Override
    public void onCurrent() {
        super.onCurrent();

        if (!loaded && loadRealLayout()) {
            loaded = true;
            onInit();
        }
    }

    private boolean loadRealLayout() {
        ViewGroup root = (ViewGroup) getView();
        if (root != null) {
            root.removeAllViewsInLayout();
            View.inflate(root.getContext(), tabData.layoutId, root);
        }
        return root != null;
    }
}

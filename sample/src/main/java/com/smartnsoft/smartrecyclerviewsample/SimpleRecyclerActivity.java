package com.smartnsoft.smartrecyclerviewsample;

import com.smartnsoft.smartrecyclerview.adapter.SmartRecyclerAdapter;

/**
 * An activity containing a simple way to use a RecyclerView with a SmartRecyclerAdapter
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */
public class SimpleRecyclerActivity
        extends AbstractRecyclerActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.simple_recycler_activity;
    }

    @Override
    protected SmartRecyclerAdapter createRecyclerAdapter() {
        return new SmartRecyclerAdapter(this, true);
    }

}
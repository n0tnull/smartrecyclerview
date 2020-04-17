package com.smartnsoft.smartrecyclerviewsample;

import androidx.annotation.NonNull;

import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerviewsample.bo.Person;
import com.smartnsoft.smartrecyclerviewsample.wrappers.PersonWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity using a SmartRecyclerAdapter and Wrappers with Android Databind
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */
public final class DataBindedRecyclerActivity
        extends SimpleRecyclerActivity {

    @NonNull
    @Override
    protected List<? extends SmartRecyclerViewWrapper<?>> createWrappers() {
        final List<SmartRecyclerViewWrapper<?>> wrappers = new ArrayList<>();
        final String[] personNames = getResources().getStringArray(R.array.personNames);
        for (final String personName : personNames) {
            wrappers.add(new PersonWrapper(new Person(personName)));
        }
        return wrappers;
    }
}

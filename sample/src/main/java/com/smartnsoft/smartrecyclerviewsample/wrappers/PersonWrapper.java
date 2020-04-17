package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerviewsample.R;
import com.smartnsoft.smartrecyclerviewsample.bo.Person;

import org.jetbrains.annotations.NotNull;

/**
 * @author Adrien Vitti
 * @since 2018.01.24
 */

public final class PersonWrapper
        extends AbstractDataBindedViewWrapper<Person> {

    public PersonWrapper(Person businessObject) {
        super(businessObject, R.layout.person_item);
    }

    @Override
    protected SmartRecyclerAttributes<Person> extractNewViewAttributes(@NotNull Context context, @NotNull View view, Person person) {
        return new PersonAttributes(view);
    }

}

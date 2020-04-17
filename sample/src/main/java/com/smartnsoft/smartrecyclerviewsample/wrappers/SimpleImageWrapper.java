package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerviewsample.R;

import org.jetbrains.annotations.NotNull;

/**
 * A RecyclerViewWrapper with an DrawableRes integer as business object
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

public final class SimpleImageWrapper
        extends SmartRecyclerViewWrapper<Integer> {

    public SimpleImageWrapper(@DrawableRes Integer businessObject) {
        super(businessObject, R.layout.simple_image_item);
    }

    @Override
    protected SmartRecyclerAttributes<Integer> extractNewViewAttributes(@NotNull Context context, @NotNull View view, @DrawableRes Integer businessObject) {
        return new SimpleImageAttributes(view);
    }

}

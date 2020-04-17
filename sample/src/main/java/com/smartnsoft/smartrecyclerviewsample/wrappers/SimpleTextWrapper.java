package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerviewsample.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A RecyclerViewWrapper with a simple String as business object
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

public final class SimpleTextWrapper
        extends SmartRecyclerViewWrapper<String> {

    public SimpleTextWrapper(String businessObject) {
        super(businessObject, R.layout.simple_text_item);
    }

    @Nullable
    @Override
    protected SmartRecyclerAttributes<String> extractNewViewAttributes(@NotNull Context context, @NotNull View view, String s) {
        return new SimpleTextAttributes(view);
    }

}

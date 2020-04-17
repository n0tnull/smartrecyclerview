package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerviewsample.R;

import org.jetbrains.annotations.NotNull;

/**
 * A RecyclerViewWrapper with a simple String as business object
 *
 * @author Adrien Vitti
 * @since 2019.07.15
 */

public final class ReorderTextWrapper
        extends SmartRecyclerViewWrapper<String> {

    public ReorderTextWrapper(String businessObject) {
        super(businessObject, R.layout.reorder_text_item);
    }

    @Override
    protected SmartRecyclerAttributes<String> extractNewViewAttributes(@NotNull Context context, @NotNull View view, String businessObject) {
        return new ReorderTextAttributes(view);
    }

}

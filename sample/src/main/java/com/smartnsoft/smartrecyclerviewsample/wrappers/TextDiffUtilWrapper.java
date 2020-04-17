package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.View;

import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes;
import com.smartnsoft.smartrecyclerview.wrapper.SmartDiffUtil;
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;
import com.smartnsoft.smartrecyclerviewsample.R;

import org.jetbrains.annotations.NotNull;

/**
 * A RecyclerViewWrapper with a simple String as business object
 * which can be used with DiffUtil
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */

public final class TextDiffUtilWrapper
        extends SmartRecyclerViewWrapper<String>
        implements SmartDiffUtil {

    public TextDiffUtilWrapper(String businessObject) {
        super(businessObject, R.layout.simple_text_item);
    }

    @Override
    protected SmartRecyclerAttributes<String> extractNewViewAttributes(@NotNull Context context, @NotNull View view, String businessObject) {
        return new SimpleTextAttributes(view);
    }

    @Override
    public long getDiffUtilHashCode() {
        return getId();
    }

}

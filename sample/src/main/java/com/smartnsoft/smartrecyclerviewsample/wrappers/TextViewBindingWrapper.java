package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n0tnull.smartrecyclerview.viewbinding.attributes.SmartViewBindingRecyclerAttributes;
import com.n0tnull.smartrecyclerview.viewbinding.wrapper.SmartViewBindingRecyclerWrapper;
import com.smartnsoft.smartrecyclerviewsample.databinding.SimpleTextItemBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A RecyclerViewWrapper with a simple String as business object
 *
 * @author Adrien Vitti
 * @since 2018.01.22
 */

public final class TextViewBindingWrapper
        extends SmartViewBindingRecyclerWrapper<String, SimpleTextItemBinding> {

    final static class TextViewBindingAttributes
            extends SmartViewBindingRecyclerAttributes<String, SimpleTextItemBinding> {

        public TextViewBindingAttributes(@NotNull SimpleTextItemBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void onBusinessObjectUpdated(String businessObject, boolean isSelected) {
            super.onBusinessObjectUpdated(businessObject, isSelected);
            getViewBinding().textView.setText(businessObject);
        }

    }

    public TextViewBindingWrapper(String businessObject) {
        super(businessObject);
    }

    @NotNull
    @Override
    public SimpleTextItemBinding createViewBinding(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup parentView) {
        return SimpleTextItemBinding.inflate(layoutInflater, parentView, false);
    }

    @NotNull
    @Override
    protected SmartViewBindingRecyclerAttributes<String, SimpleTextItemBinding> extractNewViewAttributes(@NotNull Context context, @NotNull View view, String s) {
        return new TextViewBindingAttributes(SimpleTextItemBinding.bind(view));
    }

}

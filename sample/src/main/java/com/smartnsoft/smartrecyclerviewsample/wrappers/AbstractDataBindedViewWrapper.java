package com.smartnsoft.smartrecyclerviewsample.wrappers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper;

import org.jetbrains.annotations.NotNull;

/**
 * A Wrapper which should be used with Android Databinding
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */

abstract class AbstractDataBindedViewWrapper<T>
        extends SmartRecyclerViewWrapper<T> {

    @LayoutRes
    private int layoutResourceId;

    AbstractDataBindedViewWrapper(T businessObject, int layoutResourceId) {
        super(businessObject, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
    }

    public View getNewView(@NotNull ViewGroup parent, @NotNull Context context) {
        final ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResourceId, parent, false);
        if (viewDataBinding == null) {
            throw new IllegalArgumentException("The given layout is not usable with data binding");
        }
        final View view = viewDataBinding.getRoot();
        view.setTag(extractNewViewAttributes(context, view, this.getBusinessObject()));
        return view;
    }

}

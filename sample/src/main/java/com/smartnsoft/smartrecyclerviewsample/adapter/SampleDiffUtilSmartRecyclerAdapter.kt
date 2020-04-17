package com.smartnsoft.smartrecyclerviewsample.adapter

import android.content.Context

import com.smartnsoft.smartrecyclerview.adapter.DiffUtilSmartRecyclerAdapter
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper

/**
 * A simple implementation of the DiffUtilSmartRecyclerAdapter
 *
 * @author Adrien Vitti
 * @since 2018.01.24
 */

class SampleDiffUtilSmartRecyclerAdapter(context: Context)
    : DiffUtilSmartRecyclerAdapter(context) {

    class ListDiffUtilCallback(oldWrappers: List<SmartRecyclerViewWrapper<*>>, newWrappers: List<SmartRecyclerViewWrapper<*>>)
        : DiffUtilSmartRecyclerAdapter.SmartDiffUtilCallback(oldWrappers, newWrappers) {

        override fun getChangePayloadCustom(oldItemPosition: Int, newItemPosition: Int, oldBusinessObject: Any?, newBusinessObject: Any?): Any? =
                SmartDiffUtilCallback.ITEM_CHANGED_PAYLOAD

    }

    override fun <T : SmartDiffUtilCallback> getDiffUtilCallback(oldWrappers: List<SmartRecyclerViewWrapper<*>>, newWrappers: List<SmartRecyclerViewWrapper<*>>): T =
            ListDiffUtilCallback(oldWrappers, newWrappers) as T

    override fun onBindViewHolderCustom(holder: SmartRecyclerAttributes<*>?, position: Int, payload: Any?): Boolean =
            false

}

// The MIT License (MIT)
//
// Copyright (c) 2017
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
package com.smartnsoft.smartrecyclerview.adapter

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.smartnsoft.smartrecyclerview.attributes.SmartRecyclerAttributes
import com.smartnsoft.smartrecyclerview.wrapper.SmartRecyclerViewWrapper
import java.util.*

/**
 * A [RecyclerView] adapter, which works closely with the [SmartRecyclerViewWrapper].
 *
 * @author Jocelyn Girard, Ludovic Roland, Adrien Vitti
 * @since 2014.04.16
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class SmartRecyclerAdapter
@JvmOverloads constructor(
        protected val context: Context,
        private val shouldNotifyBeCalled: Boolean = false
) : RecyclerView.Adapter<SmartRecyclerAttributes<*>>() {

    /**
     * Update behaviour that can be used:
     *  * [.NONE]
     *  * [.REMOVE_OLD_DATA_AT_ONCE]
     *  * [.REMOVE_OLD_DATA_ONE_BY_ONE]
     *  * [.IGNORE_NEW_DUPLICATES]
     *  * [.REMOVE_OLD_DUPLICATES]
     */
    enum class UpdateType {
        /**
         * Does nothing
         */
        NONE,
        /**
         * Removes every items in one batch
         */
        REMOVE_OLD_DATA_AT_ONCE,
        /**
         * Removes items one by one to benefit from single deletion animation
         */
        REMOVE_OLD_DATA_ONE_BY_ONE,
        /**
         * Removes duplicates in the new list before adding them
         */
        IGNORE_NEW_DUPLICATES,
        /**
         * Removes duplicates in the current list before adding new ones
         */
        REMOVE_OLD_DUPLICATES,
        /**
         * Replaces duplicates in place before adding new ones
         */
        REPLACE_DUPLICATES
    }

    /**
     * Comparison behaviour that can be used:
     *  * [.CLASSIC]
     *  * [.BUSINESS_OBJECT_TYPE]
     *  * [.WRAPPER_TYPE]
     *  * [.BUSINESS_OBJECT_AND_WRAPPER_TYPE]
     */
    enum class ComparisonType {
        /**
         * Classic comparison of id
         */
        CLASSIC,
        /**
         * Compare ids and business object type
         */
        BUSINESS_OBJECT_TYPE,
        /**
         * Compare ids and wrapper type
         */
        WRAPPER_TYPE,
        /**
         * Compare ids, business object type and wrapper type
         */
        BUSINESS_OBJECT_AND_WRAPPER_TYPE
    }

    protected var wrappers: MutableList<SmartRecyclerViewWrapper<*>> = mutableListOf()

    var intentFilterCategory: String = ""

    private val viewTypeAttributesDictionary = SparseArray<SmartRecyclerViewWrapper<*>>()

    private var selectedPositionItem = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SmartRecyclerAttributes<*> {
        val wrapper = viewTypeAttributesDictionary[viewType]
        val view = wrapper.getNewView(viewGroup, context)
        val viewAttributes = wrapper.getViewAttributes(view)
        viewAttributes.intentFilterCategory = intentFilterCategory
        return viewAttributes
    }

    override fun onBindViewHolder(smartRecyclerAttributes: SmartRecyclerAttributes<*>, position: Int) {
        wrappers[position].businessObject?.also { businessObject ->
            smartRecyclerAttributes.uncheckedUpdate(businessObject, selectedPositionItem == position)
        }
    }

    /**
     * Allows you to get the unique identifier of an item in the adapter
     *
     * @param position The position of the item
     * @return The unique identifier of the item
     */
    override fun getItemId(position: Int): Long = wrappers[position].getId()

    /**
     * @return the numbers of item in the adapter
     */
    override fun getItemCount(): Int = wrappers.size

    override fun getItemViewType(position: Int): Int = wrappers[position].getType()

    /**
     * Initializes the wrapper list in the adapter and call notifyDataSetChanged
     * Should be called only the first time to avoid flick.
     * It MUST be used on the UI thread.
     *
     * @param wrappers The list of wrappers to use in the adapter
     */
    open fun setNewWrapperList(wrappers: List<SmartRecyclerViewWrapper<*>>) {
        this.wrappers.apply {
            clear()
            addAll(wrappers)
        }
        for (wrapper in wrappers) {
            addWrapperTypeToDictionary(wrapper)
        }
        if (shouldNotifyBeCalled) {
            notifyDataSetChanged()
        }
    }

    /**
     * It MUST be used on the UI thread.
     *
     * @param position The position which will be selected in the list
     */
    @UiThread
    fun setSelectedPositionItem(position: Int) {
        val lastSelectedPositionItem = selectedPositionItem
        selectedPositionItem = position
        notifyItemChanged(position)
        notifyItemChanged(lastSelectedPositionItem)
    }

    fun getSpanSizeForPosition(position: Int): Int = wrappers.getOrNull(position)?.spanSize ?: 1

    /**
     * Removes only a single item in the adapter and call notifyItemRemoved
     * It MUST be used on the UI thread.
     *
     * @param position the position of the item to remove
     */
    @UiThread
    fun removeItem(position: Int) {
        wrappers.removeAt(position)
        if (shouldNotifyBeCalled) {
            notifyItemRemoved(position)
        }
    }

    /**
     * Removes every wrapper in the adapter and call notifyItemRangeRemoved
     * It MUST be used on the UI thread.
     */
    @UiThread
    fun removeAll() {
        val initialSize = wrappers.size
        wrappers.clear()
        if (shouldNotifyBeCalled) {
            notifyItemRangeRemoved(0, initialSize)
        }
    }

    /**
     * Adds an item to the adapter and call notifyItemInserted
     * It MUST be used on the UI thread.
     *
     * @param item the wrapper you want to add to the adapter
     */
    @UiThread
    fun addItem(item: SmartRecyclerViewWrapper<*>) {
        addItem(wrappers.size, item)
    }

    /**
     * Adds an item to the adapter at a specified position
     * and call notifyItemInserted
     * It MUST be used on the UI thread.
     *
     * @param position The index where we want to add the wrapper
     * @param item     the wrapper you want to add to the adapter
     */
    @UiThread
    fun addItem(position: Int, item: SmartRecyclerViewWrapper<*>) {
        wrappers.add(position, item)
        addWrapperTypeToDictionary(item)
        if (shouldNotifyBeCalled) {
            notifyItemInserted(position)
        }
    }

    /**
     * Adds a list of wrapper to the adapter without any verification and call notifyItemRangeInserted
     * It MUST be used on the UI thread.
     *
     * @param wrappersToAdd the list of wrappers to add
     */
    @UiThread
    fun addAll(wrappersToAdd: List<SmartRecyclerViewWrapper<*>>?) =
            addAll(wrappers.size, wrappersToAdd)

    /**
     * Adds a list of wrapper to the adapter at the specified position
     * without any verification and call notifyItemRangeInserted
     * It MUST be used on the UI thread.
     *
     * @param position      The index where we want to add the list
     * @param wrappersToAdd the list of wrappers to add
     */
    @UiThread
    fun addAll(position: Int, wrappersToAdd: List<SmartRecyclerViewWrapper<*>>?) {
        wrappersToAdd
                ?.takeIf { it.isNotEmpty() }
                ?.also { newWrappers ->
                    if (position >= 0 && position <= wrappers.size) {
                        for (item in newWrappers) {
                            addWrapperTypeToDictionary(item)
                        }
                        wrappers.addAll(position, newWrappers)
                        if (shouldNotifyBeCalled) {
                            notifyItemRangeInserted(position, newWrappers.size)
                        }
                    }
                }
    }

    /**
     * If the adapter has already been set, use this method to update data.
     * It MUST be used on the UI thread.
     *
     * @param newWrappers The list of wrappers you want to use for the update
     */
    @UiThread
    fun updateWrappers(newWrappers: List<SmartRecyclerViewWrapper<*>>?) =
            addAll(newWrappers)

    @UiThread
    fun updateWrappers(newWrappers: List<SmartRecyclerViewWrapper<*>>?, removeType: UpdateType?) =
            updateWrappers(newWrappers, removeType, ComparisonType.CLASSIC)

    /**
     * If the adapter has already been set, use this method to update data.
     * It MUST be used on the UI thread.
     *
     * @param newWrappers    The list of wrappers you want to use for the update
     * @param removeType     One of [UpdateType.NONE], [UpdateType.REMOVE_OLD_DATA_AT_ONCE], [UpdateType.REMOVE_OLD_DATA_ONE_BY_ONE],
     * [UpdateType.IGNORE_NEW_DUPLICATES], or [UpdateType.REMOVE_OLD_DUPLICATES].
     * @param comparisonType One of [ComparisonType.CLASSIC], [ComparisonType.BUSINESS_OBJECT_TYPE], [ComparisonType.WRAPPER_TYPE]
     * or [ComparisonType.BUSINESS_OBJECT_AND_WRAPPER_TYPE].
     */
    @UiThread
    fun updateWrappers(newWrappers: List<SmartRecyclerViewWrapper<*>>?,
                       removeType: UpdateType?,
                       comparisonType: ComparisonType?) {
        newWrappers?.toMutableList()?.also { newWrappersList ->
            when (removeType) {
                UpdateType.REMOVE_OLD_DATA_AT_ONCE -> removeAll()
                UpdateType.REMOVE_OLD_DATA_ONE_BY_ONE -> {
                    val oldWrapperToRemove: MutableList<SmartRecyclerViewWrapper<*>> = ArrayList()
                    oldWrapperToRemove.addAll(wrappers)
                    for (smartRecyclerViewWrapper in oldWrapperToRemove) {
                        removeItem(getItemPosition(smartRecyclerViewWrapper.getId(),
                                smartRecyclerViewWrapper.businessObject?.javaClass,
                                smartRecyclerViewWrapper.javaClass, comparisonType))
                    }
                }
                UpdateType.IGNORE_NEW_DUPLICATES -> {
                    val wrapperToRemove: MutableList<SmartRecyclerViewWrapper<*>> = ArrayList()
                    for (item in newWrappersList) {
                        if (contains(item.getId(), item.businessObject?.javaClass, item.javaClass, comparisonType)) {
                            wrapperToRemove.add(item)
                        }
                    }
                    newWrappersList.removeAll(wrapperToRemove)
                }
                UpdateType.REMOVE_OLD_DUPLICATES -> for (wrapper in newWrappersList) {
                    val businessObjectId = wrapper.getId()
                    val businessObjectType: Class<*>? = wrapper.businessObject?.javaClass
                    val wrapperType: Class<out SmartRecyclerViewWrapper<*>> = wrapper.javaClass
                    if (contains(businessObjectId, businessObjectType, wrapperType, comparisonType)) {
                        removeItem(getItemPosition(businessObjectId, businessObjectType, wrapperType, comparisonType))
                    }
                }
                UpdateType.REPLACE_DUPLICATES -> {
                    val replacedWrappersToRemove: MutableList<SmartRecyclerViewWrapper<*>> = ArrayList()
                    for (wrapper in newWrappersList) {
                        val businessObjectId = wrapper.getId()
                        val businessObjectType: Class<*>? = wrapper.businessObject?.javaClass
                        val wrapperType: Class<out SmartRecyclerViewWrapper<*>> = wrapper.javaClass
                        if (contains(businessObjectId, businessObjectType, wrapperType, comparisonType)) {
                            val position = getItemPosition(businessObjectId, businessObjectType, wrapperType, comparisonType)
                            set(position, wrapper)
                            replacedWrappersToRemove.add(wrapper)
                        }
                    }
                    newWrappersList.removeAll(replacedWrappersToRemove)
                }
                UpdateType.NONE -> {
                }
                else -> {
                }
            }
            addAll(newWrappersList)
        }
    }

    /**
     * Allows you to know if an adapter contains a specific object.
     * The identifier must be unique, for example the object's hashcode.
     *
     * @param businessObjectID The unique identifier of an object
     * @return true if the adapter as an item with the same identifier, false otherwise
     */
    operator fun contains(businessObjectID: Long): Boolean {
        return wrappers
                .takeIf { businessObjectID != -1L }
                ?.any { it.getId() == businessObjectID }
                ?: false
    }

    /**
     * Allows you to know if an adapter contains a specific object.
     * The identifier must be unique, for example the object's hashcode.
     *
     * @param businessObjectID   The unique identifier of an object
     * @param businessObjectType The business object type
     * @param wrapperType        The wrapper type
     * @return true if the adapter as an item with the same identifier and other parameters depending on comparisonType, false otherwise
     */
    fun contains(businessObjectID: Long, businessObjectType: Class<*>?, wrapperType: Class<*>,
                 comparisonType: ComparisonType?): Boolean {
        return wrappers
                .takeIf { businessObjectID != -1L }
                ?.any {
                    val businessObject = it.businessObject
                    when (comparisonType) {
                        ComparisonType.CLASSIC -> businessObjectID == it.getId()
                        ComparisonType.BUSINESS_OBJECT_TYPE -> businessObjectID == it.getId() && businessObjectType == businessObject?.javaClass
                        ComparisonType.WRAPPER_TYPE -> businessObjectID == it.getId() && wrapperType == it.javaClass
                        ComparisonType.BUSINESS_OBJECT_AND_WRAPPER_TYPE -> businessObjectID == it.getId() && businessObjectType == businessObject?.javaClass && wrapperType == it.javaClass
                        else -> businessObjectID == it.getId()
                    }
                } ?: false
    }

    /**
     * Allows you to recover the business object used by the adapter.
     * It should be used with caution as the type return is a list of Object and not a list using the correct type of object.
     *
     * @return the list of Business Object which are used by wrappers
     */
    val itemList: List<*>
        get() = wrappers.map { it.businessObject }

    /**
     * Allows you to get a specific wrapper via its unique identifier.
     *
     * @param businessObjectID The unique identifier of the object
     * @return the object if found, null otherwise
     */
    fun getItemWrapper(businessObjectID: Long): SmartRecyclerViewWrapper<*>? {
        return wrappers
                .takeIf { businessObjectID != -1L }
                ?.firstOrNull { businessObjectID == it.getId() }
    }

    /**
     * Allows you to get a specific wrapper via its unique identifier.
     *
     * @param businessObjectID   The unique identifier of the object
     * @param businessObjectType The business object type
     * @param wrapperType        The wrapper type
     * @return the object if found, null otherwise
     */
    fun getItemWrapper(businessObjectID: Long, businessObjectType: Class<*>,
                       wrapperType: Class<*>, comparisonType: ComparisonType?): SmartRecyclerViewWrapper<*>? {
        if (wrappers.isNotEmpty() && businessObjectID != -1L) {
            for (index in wrappers.indices) {
                val areEqual: Boolean
                val wrapper = wrappers[index]
                val businessObject = wrapper.businessObject
                areEqual = when (comparisonType) {
                    ComparisonType.CLASSIC -> businessObjectID == getItemId(index)
                    ComparisonType.BUSINESS_OBJECT_TYPE -> businessObjectID == getItemId(
                            index) && businessObjectType == businessObject?.javaClass
                    ComparisonType.WRAPPER_TYPE -> businessObjectID == getItemId(index) && wrapperType == wrapper.javaClass
                    ComparisonType.BUSINESS_OBJECT_AND_WRAPPER_TYPE -> businessObjectID == getItemId(
                            index) && businessObjectType == businessObject?.javaClass && wrapperType == wrapper.javaClass
                    else -> businessObjectID == getItemId(index)
                }
                if (areEqual) {
                    return wrappers[index]
                }
            }
        }
        return null
    }

    /**
     * Allows you to get a specific object position via its unique identifier.
     *
     * @param businessObjectID The unique identifier of the object
     * @return the object position if found, -1 otherwise
     */
    fun getItemPosition(businessObjectID: Long): Int {
        return wrappers
                .takeIf { businessObjectID != -1L }
                ?.indexOfFirst {
                    it.getId() == businessObjectID
                } ?: -1
    }

    /**
     * Allows you to get a specific object position via its unique identifier.
     *
     * @param businessObjectID   The unique identifier of the object
     * @param businessObjectType The business object type
     * @param wrapperType        The wrapper type
     * @return the object position if found, -1 otherwise
     */
    fun getItemPosition(businessObjectID: Long, businessObjectType: Class<*>?, wrapperType: Class<*>,
                        comparisonType: ComparisonType?): Int {
        if (wrappers.isNotEmpty() && businessObjectID != -1L) {
            for (index in wrappers.indices) {
                val areEqual: Boolean
                val wrapper = wrappers[index]
                val businessObject = wrapper.businessObject
                areEqual = when (comparisonType) {
                    ComparisonType.CLASSIC -> businessObjectID == getItemId(index)
                    ComparisonType.BUSINESS_OBJECT_TYPE -> businessObjectID == getItemId(
                            index) && businessObjectType == businessObject?.javaClass
                    ComparisonType.WRAPPER_TYPE -> businessObjectID == getItemId(index) && wrapperType == wrapper.javaClass
                    ComparisonType.BUSINESS_OBJECT_AND_WRAPPER_TYPE -> businessObjectID == getItemId(
                            index) && businessObjectType == businessObject?.javaClass && wrapperType == wrapper.javaClass
                    else -> businessObjectID == getItemId(index)
                }
                if (areEqual) {
                    return index
                }
            }
        }
        return -1
    }

    /**
     * Replaces the element at the specified location in this List with the specified object. This operation does not change the size of the List.
     *
     * @param position the index at which to put the specified object.
     * @param item     the object to insert.
     * @return Returns the previous element at the index.
     */
    operator fun set(position: Int, item: SmartRecyclerViewWrapper<*>): SmartRecyclerViewWrapper<*>? {
        if (wrappers.isNotEmpty() && position >= 0 && position <= wrappers.size) {
            addWrapperTypeToDictionary(item)
            val wrapper = wrappers.set(position, item)
            if (shouldNotifyBeCalled) {
                notifyItemChanged(position)
            }
            return wrapper
        }
        return null
    }

    /**
     * Allows you to move a wrapper present at a specific position to a new given position.
     * If at least one of the parameters is out of bounds for the current list of wrapper, this method won't do anything.
     *
     * @param fromIndex The initial position of the wrapper to move
     * @param toIndex   The final position of the wrapper to move
     */
    fun moveWrapper(fromIndex: Int, toIndex: Int) {
        if (fromIndex >= 0 && fromIndex < wrappers.size && toIndex >= 0 && toIndex < wrappers.size) {
            wrappers.getOrNull(fromIndex)?.also { wrapperToMove ->
                wrappers.removeAt(fromIndex)
                wrappers.add(toIndex, wrapperToMove)
                if (shouldNotifyBeCalled) {
                    notifyItemMoved(fromIndex, toIndex)
                }
            }
        }
    }

    private fun addWrapperTypeToDictionary(wrapper: SmartRecyclerViewWrapper<*>) {
        val wrapperType = wrapper.getType()
        if (viewTypeAttributesDictionary[wrapperType] == null) {
            viewTypeAttributesDictionary.append(wrapperType, wrapper)
        }
    }

    companion object {
        /**
         * Allows you to know if the given wrapper list contains a specific id.
         * The identifier must be unique, for example the object's hashcode.
         *
         * @param wrappers         The list of wrappers to look into
         * @param businessObjectID The unique identifier of an object
         * @return true if the adapter as an item with the same identifier, false otherwise
         */
        @JvmStatic
        fun isObjectIDContainedInList(wrappers: List<SmartRecyclerViewWrapper<*>>?,
                                      businessObjectID: Long): Boolean {
            return wrappers
                    ?.takeIf { businessObjectID != -1L }
                    ?.any { businessObjectID == it.getId() }
                    ?: false
        }
    }

}
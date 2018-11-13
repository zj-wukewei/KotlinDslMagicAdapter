package com.wkw.magicadapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author GoGo on 2018-11-07.
 */
class MagicAdapter(builder: Builder) : RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>() {

    private var datas: MutableList<Any> = ArrayList()

    internal val items: MutableList<MagicItem<Any>> = builder.items

    private val positionToTypeMap = SparseIntArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            items[viewType].layoutId(),
            parent,
            false
        )
        return BindingViewHolder(viewDataBinding!!)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, postion: Int) {
        val data = datas[postion]
        val magicItem = items[positionToTypeMap.get(postion)]

        holder.binding.setVariable(BR.item, data)

        val handlers = magicItem.handlers()
        for ((id, handle) in handlers) {
            holder.binding.setVariable(id, handle)
        }

        val itemIds = magicItem.itemIds()
        for ((idGet, setter) in itemIds) {
            val itemVariable = idGet(data)
            holder.binding.setVariable(itemVariable, setter(data))
        }

        holder.binding.executePendingBindings()
    }


    override fun getItemCount() = datas.size


    override fun getItemViewType(position: Int): Int {
        return positionToItemsIndex(position)
    }

    private fun positionToItemsIndex(position: Int): Int {
        if (items.isEmpty()) {
            throw RuntimeException("item must add")
        }

        if (items.size == 1) {
            return 0
        }

        items.forEachIndexed { index, magicItem ->
            if (magicItem.getItemViewType(datas[position], position)) {
                positionToTypeMap.put(position, index)
                return index
            }
        }
        throw RuntimeException("can't find matched item")
    }

    fun submitList(dataList: List<Any>) {
        val diffCallBack = DiffCallback(datas, dataList)
        val calculateDiff = DiffUtil.calculateDiff(diffCallBack)
        calculateDiff.dispatchUpdatesTo(this)
        datas.clear()
        datas.addAll(dataList)
    }


    private inner class DiffCallback(
        private var mOldData: List<Any>,
        private var mNewData: List<Any>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = mOldData[oldItemPosition]
            val new = mNewData[newItemPosition]

            if (old.javaClass != new.javaClass) {
                return false
            }
            val magicItem = items[positionToTypeMap.get(oldItemPosition)]

            return magicItem.areItems(old, new)

        }

        override fun getOldListSize() = mOldData.size

        override fun getNewListSize() = mNewData.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = mOldData[oldItemPosition]
            val new = mNewData[newItemPosition]

            if (old.javaClass != new.javaClass) {
                return false
            }
            val magicItem = items[positionToTypeMap.get(oldItemPosition)]

            return magicItem.areContents(old, new)
        }

    }


    companion object {
        fun repositoryAdapter(): Builder {
            return Builder()
        }
    }

}

class Builder internal constructor() {

    internal val items: MutableList<MagicItem<Any>> = ArrayList()


    fun <D : Any> addItemDsl(create: MagicDslItem<D>.() -> Unit): Builder {
        val acrobatDSL = MagicDslItem<D>()
        acrobatDSL.create()
        items.add(acrobatDSL.build() as MagicItem<Any>)
        return this
    }

    fun build(): MagicAdapter {
        return MagicAdapter(this)
    }
}
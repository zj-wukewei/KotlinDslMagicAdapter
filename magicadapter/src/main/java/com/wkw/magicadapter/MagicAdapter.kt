package com.wkw.magicadapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author GoGo on 2018-11-07.
 */
class MagicAdapter(builder: Builder) : RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>() {

    private var datas: MutableList<Any> = ArrayList()

    internal val items: MutableList<MagicItem<Any>> = builder.items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            items[viewType].layoutId(),
            parent,
            false)
        return BindingViewHolder(viewDataBinding!!)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, postion: Int) {
        val data = datas[postion]
        val magicItem = items[getItemViewType(postion)]
        val handlers = magicItem.handlers()
        for ((id, handle) in handlers) {
            holder.binding.setVariable(id, handle)
        }
        holder.binding.setVariable(BR.item, data)

        holder.binding.executePendingBindings()
    }


    override fun getItemCount() = datas.size


    override fun getItemViewType(position: Int): Int {
        return getItemConfig(position)
    }

    private fun getItemConfig(position: Int): Int {
        if (items.isEmpty()) {
            throw RuntimeException("item must config")
        }
        if (items.size == 1) {
            return 0
        }
        items.forEachIndexed { index, magicItem ->
            if (magicItem.getItemViewType(datas[position], position)) {
                return index
            }
        }
        throw RuntimeException("can't find matched item")
    }

    fun submitList(dataList: List<Any>) {
        datas.clear()
        datas.addAll(dataList)
        notifyDataSetChanged()
    }

    companion object {
        fun repositoryAdapter(): Builder {
            return Builder()
        }
    }

}

class Builder internal constructor() {

    internal val items: MutableList<MagicItem<Any>> = ArrayList()

    fun <D> addItem(create: () -> MagicItem<Any>): Builder {
        items.add(create())
        return this
    }

    fun <D> addItemDsl(create: MagicDslItem<Any>.() -> Unit): Builder {
        val acrobatDSL = MagicDslItem<Any>()
        acrobatDSL.create()
        items.add(acrobatDSL.build())
        return this
    }

    fun build(): MagicAdapter {
        return MagicAdapter(this)
    }
}
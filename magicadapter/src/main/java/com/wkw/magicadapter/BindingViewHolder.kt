package com.wkw.magicadapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * @author GoGo on 2018-11-07.
 */
class BindingViewHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)

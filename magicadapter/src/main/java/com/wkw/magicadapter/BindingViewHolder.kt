package com.wkw.magicadapter

import androidx.databinding.ViewDataBinding

/**
 * @author GoGo on 2018-11-07.
 */
class BindingViewHolder<out T : ViewDataBinding>(val binding: T) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

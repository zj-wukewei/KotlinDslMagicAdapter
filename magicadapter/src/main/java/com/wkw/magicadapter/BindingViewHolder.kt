package com.wkw.magicadapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by GoGo on 2018/11/7.
 * Email zjwkw1992@163.com
 * GitHub https://github.com/zj-wukewei
 */
class BindingViewHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
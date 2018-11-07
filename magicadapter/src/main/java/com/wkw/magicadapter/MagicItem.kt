package com.wkw.magicadapter

import android.support.annotation.LayoutRes

/**
 * Created by GoGo on 2018/11/7.
 * Email zjwkw1992@163.com
 * GitHub https://github.com/zj-wukewei
 */
abstract class MagicItem<D> {

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun getItemViewType(data: Any, position: Int): Boolean
}
  class  MagicDslItem<D> constructor(@LayoutRes var resId: Int = -1 ,
                                     var dataMeet: (d: Any, pos: Int) -> Boolean = { _: Any, _: Int -> false }) {


      internal fun build(): MagicItem<D> {
          return object: MagicItem<D> () {

              override fun getItemViewType(data: Any, position: Int) = dataMeet(data, position)

              override fun layoutId() = resId

          }
      }
  }


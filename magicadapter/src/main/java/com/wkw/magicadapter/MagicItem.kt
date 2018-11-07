package com.wkw.magicadapter

import android.support.annotation.LayoutRes
import java.util.ArrayList

/**
 * @author GoGo on 2018-11-07.
 */
abstract class MagicItem<D> {

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun getItemViewType(data: Any, position: Int): Boolean

    abstract fun handlers(): ArrayList<Pair<Int, Any?>>
}
class  MagicDslItem<D> constructor(@LayoutRes var resId: Int = -1 ,
                                   var dataMeet: (d: Any, pos: Int) -> Boolean = { _: Any, _: Int -> false },
                                   private val handlers: ArrayList<Pair<Int, Any?>> = ArrayList()) {

    fun resId(@LayoutRes resId: Int):  MagicDslItem<D>{
        this.resId = resId
        return this
    }

    fun dataMeet(dataMeet: (d: Any, pos: Int) -> Boolean):  MagicDslItem<D>{
        this.dataMeet = dataMeet
        return this
    }

    fun handler(handlerId: Int, handler: Any):  MagicDslItem<D> {
        handlers.add(handlerId to handler)
        return this
    }

    internal fun build(): MagicItem<D> {
        return object: MagicItem<D> () {
            override fun handlers() = handlers

            override fun getItemViewType(data: Any, position: Int) = dataMeet(data, position)

            override fun layoutId() = resId

        }
    }
}
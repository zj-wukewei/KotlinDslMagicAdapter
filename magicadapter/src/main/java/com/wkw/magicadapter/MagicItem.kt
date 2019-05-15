package com.wkw.magicadapter

import androidx.annotation.LayoutRes
import java.util.ArrayList

/**
 * @author GoGo on 2018-11-07.
 */
abstract class MagicItem<D> {
    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun getItemViewType(data: Any, position: Int): Boolean

    abstract fun areItems(o: D, n: D): Boolean

    abstract fun areContents(o: D, n: D): Boolean

    abstract fun handlers(): ArrayList<Pair<Int, Any?>>

    abstract fun itemIds(): ArrayList<Pair<(D) -> Int, (D) -> Any?>>
}

class MagicDslItem<D> constructor(
    @LayoutRes var resId: Int = -1,
    var dataMatch: (d: Any, pos: Int) -> Boolean = { _: Any, _: Int -> false },
    private val itemIds: ArrayList<Pair<(D) -> Int, (D) -> Any?>> = ArrayList(),
    private val handlers: ArrayList<Pair<Int, Any?>> = ArrayList()
) {

    var areItemsTheSame: ((o: D, n: D) -> Boolean)? = null
    var areContentsTheSame: ((o: D, n: D) -> Boolean)? = null

    fun handler(handlerId: Int, handler: Any): MagicDslItem<D> {
        handlers.add(handlerId to handler)
        return this
    }

    fun <R> itemId(itemId: Int, mapper: (D) -> R): MagicDslItem<D> {
        itemIds.add({ _: D -> itemId } to mapper)
        return this
    }

    internal fun build(): MagicItem<D> {
        return object : MagicItem<D>() {
            override fun areContents(o: D, n: D): Boolean {
                areContentsTheSame?.let {
                    return it.invoke(o, n)
                }
                return o.toString() == n.toString()
            }

            override fun areItems(o: D, n: D): Boolean {
                areItemsTheSame?.let {
                    return it.invoke(o, n)
                }
                return o.toString() == n.toString()
            }

            override fun itemIds() = itemIds

            override fun handlers() = handlers

            override fun getItemViewType(data: Any, position: Int) = dataMatch(data, position)

            override fun layoutId() = resId

        }
    }
}
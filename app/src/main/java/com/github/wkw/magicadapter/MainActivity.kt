package com.github.wkw.magicadapter

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.wkw.magicadapter.databinding.ActivityMainBinding
import com.wkw.magicadapter.MagicAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mAdapter = MagicAdapter.repositoryAdapter()
            .addItemDsl<String> {
                resId = R.layout.item_string
                dataMatch = { d, _ -> d is String }
                itemId(BR.user) { User(1234567, "GoGo") }
                handler(BR.presenter, View.OnClickListener {
                    Toast.makeText(this@MainActivity, R.string.item_string, Toast.LENGTH_LONG).show()
                })
                areItemsTheSame = { o, n -> o == n }
            }
            .addItemDsl<User> {
                resId = R.layout.item_user
                dataMatch = { d, _ -> d is User }
                handler(BR.presenter, View.OnClickListener {
                    Toast.makeText(this@MainActivity, R.string.item_user, Toast.LENGTH_LONG).show()
                })
                areItemsTheSame = { o, n -> o.id == n.id }
                areContentsTheSame = { o, n -> o.id == n.id && o.name == n.name}
            }
            .build()

        binding.rv.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val data = ArrayList<Any>()
        data.add("11111")
        data.add("22222")
        data.add("33333")
        data.add("44444")
        data.add("44444")
        data.add(User(111, "张三"))
        data.add(User(222, "李四"))
        data.add(User(333, "王五"))
        data.add(User(444, "桃六"))

        mAdapter.submitList(data)


        binding.rv.postDelayed({
            val newData = ArrayList<Any>()
            newData.add("11111")
            newData.add("22222")
            newData.add(User(111, "张三"))
            newData.add("33333")
            newData.add("44444")
            newData.add(User(333, "王五"))
            newData.add(User(222, "李四"))
            newData.add("44444")
            newData.add(User(444, "桃六"))


            mAdapter.submitList(newData)
        }, 5000)
    }

}

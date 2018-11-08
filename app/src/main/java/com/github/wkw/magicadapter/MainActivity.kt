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
                dataMeet = { d, _ -> d is String }
                itemId(BR.user) { User(1234567, "wwkwkwkwk") }
                handler(BR.presenter, View.OnClickListener {
                    Toast.makeText(this@MainActivity, "aaaaa", Toast.LENGTH_LONG).show()
                })
                areItemsTheSame = { o, n -> o == n }
            }
            .addItemDsl<User> {
                resId = R.layout.item_user
                dataMeet = { d, _ -> d is User }
                itemId(BR.user) { User(1234567, "wwkwkwkwk") }
                handler(BR.presenter, View.OnClickListener {
                    Toast.makeText(this@MainActivity, "bbbbbb", Toast.LENGTH_LONG).show()
                })
                areItemsTheSame = { o, n -> o.id == n.id }
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
        data.add(User(111, "aaaaa"))
        data.add(User(222, "bbbbb"))
        data.add(User(333, "ccccc"))
        data.add(User(444, "ddddd"))

        mAdapter.submitList(data)



        binding.rv.postDelayed({
            val data = ArrayList<Any>()
            data.add("11111")
            data.add("22222")
            data.add(User(111, "aabbbbbaaa"))
            data.add("33333")
            data.add("44444")
            data.add(User(333, "ccccc"))
            data.add(User(222, "bbbbb"))
            data.add("44444")
            data.add(User(444, "ddddd"))


            mAdapter.submitList(data)
        }, 5000)
    }

}

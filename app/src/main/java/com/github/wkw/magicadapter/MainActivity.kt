package com.github.wkw.magicadapter

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.wkw.magicadapter.databinding.ActivityMainBinding
import com.github.wkw.magicadapter.databinding.ItemStringBinding
import com.wkw.magicadapter.MagicAdapter

class MainActivity : AppCompatActivity() {

    lateinit var   binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val mAdapter =  MagicAdapter.repositoryAdapter()
            .addItemDsl<String> {
                resId = R.layout.item_string
                dataMeet = { d, p -> d is String}
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

        mAdapter.submitList(data)

    }

}

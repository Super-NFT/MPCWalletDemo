package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23wallet.mpcwalletdemo.adapter.AddTokenListAdapter
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddATokenBinding

class AddATokenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddATokenBinding

    private var mAdapter: AddTokenListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddATokenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@AddATokenActivity)
            if (mAdapter == null) {
                mAdapter = AddTokenListAdapter(this@AddATokenActivity)
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(null)
    }

    private fun setListener() {
        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
            }

        })
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvCustom.setOnClickListener {
            startActivity(Intent(this@AddATokenActivity, AddCustomTokenActivity::class.java))
        }
    }
}
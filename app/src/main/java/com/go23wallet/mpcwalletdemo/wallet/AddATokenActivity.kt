package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.AddTokenListAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddATokenBinding
import com.go23wallet.mpcwalletdemo.livedata.TokenListLiveData
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddATokenActivity : BaseActivity<ActivityAddATokenBinding>() {

    private var mAdapter: AddTokenListAdapter? = null

    private var selectList = mutableListOf<String>()


    override val layoutRes: Int = R.layout.activity_add_a_token

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setListener()
    }

    private fun initView() {

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@AddATokenActivity)
            if (mAdapter == null) {
                mAdapter = AddTokenListAdapter(this@AddATokenActivity, selectList)
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(null)
    }

    private fun setListener() {
        TokenListLiveData.liveData.observe(this, Observer {
            if (it != null) {
                selectList = it
            }
        })
        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdapter?.getItem(position) ?: return
                selectList.add(item)
            }
        })
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvCustom.setOnClickListener {
            startActivity(Intent(this@AddATokenActivity, AddCustomTokenActivity::class.java))
        }
    }

    override fun finish() {
        super.finish()
        UpdateDataLiveData.liveData.postValue(1)
    }
}
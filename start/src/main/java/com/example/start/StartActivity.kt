package com.example.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.go23wallet.mpcwalletdemo.wallet.WalletActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        findViewById<TextView>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java).apply {
                putExtra("uniqueId", "")
                putExtra("email", "")
                putExtra("phone", "")
            })
        }
    }
}
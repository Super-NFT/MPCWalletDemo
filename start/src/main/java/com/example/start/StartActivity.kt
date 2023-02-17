package com.example.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.go23wallet.mpcwalletdemo.data.PhoneInfo
import com.go23wallet.mpcwalletdemo.data.UserInfo
import com.go23wallet.mpcwalletdemo.wallet.WalletActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        findViewById<TextView>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java).apply {
                putExtra(
                    "user_info", UserInfo(
                        "qqqa@coins.ph",
                        "qqqa@coins.ph",
                        PhoneInfo("+86", "13671697524"),
                        "test123",
                        "https://d2vvute2v3y7pn.cloudfront.net/logo/Avalanche/0xe0bb6feD446A2dbb27F84D3C27C4ED8EA7603366.webp"
                    )
                )
            })
        }
    }
}
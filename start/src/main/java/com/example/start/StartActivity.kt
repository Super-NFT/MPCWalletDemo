package com.example.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.go23wallet.mpcwalletdemo.data.PhoneInfo
import com.go23wallet.mpcwalletdemo.data.UserInfo
import com.go23wallet.mpcwalletdemo.wallet.WalletActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val etUniqueId = findViewById<AppCompatEditText>(R.id.et_unique_id)
        val etNickname = findViewById<AppCompatEditText>(R.id.et_nickname)
        val etAvatar = findViewById<AppCompatEditText>(R.id.et_avatar)
        val etEmail = findViewById<AppCompatEditText>(R.id.et_email)
        val etDial = findViewById<AppCompatEditText>(R.id.et_dial)
        val etPhone = findViewById<AppCompatEditText>(R.id.et_phone)
        findViewById<AppCompatTextView>(R.id.tv_confirm).setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java).apply {
                putExtra(
                    "user_info", UserInfo(
                        etUniqueId.text.toString(),
                        etEmail.text.toString(),
                        PhoneInfo(etDial.text.toString(), etPhone.text.toString()),
                        etNickname.text.toString(),
                        etAvatar.text.toString(),
                    )
                )
            })
        }
    }
}
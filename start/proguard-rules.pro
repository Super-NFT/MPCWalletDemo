# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class * implements androidx.viewbinding.ViewBinding {
    *;
}

-keep class com.go23wallet.mpcwalletdemo.data.** { *; }
-keep class com.coins.app.bean.** { *; }
-keep class com.coins.app.callback.** { *; }
-keep class com.coins.app.Go23WalletManage { *; }
-keep class com.coins.app.BaseCallBack { *; }
-keep class com.coins.app.Go23WalletCallBack { *; }
-keep class com.coins.app.util.** { *; }
-keep class com.coins.app.C { *; }
-keep class com.coins.app.entity.** { *; }

# Understand the @Keep support annotation.
-keep class androidx.annotation.Keep

-keep class wallet.** {*;}

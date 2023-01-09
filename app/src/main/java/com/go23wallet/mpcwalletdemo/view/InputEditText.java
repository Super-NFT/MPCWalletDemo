package com.go23wallet.mpcwalletdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class InputEditText extends AppCompatEditText {
    public InputEditText(@NonNull Context context) {
        super(context);
    }

    public InputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InputEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), false);
    }

    class MyInputConnection extends InputConnectionWrapper implements
            InputConnection {

        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                String text = getText().toString();
                if (text.length() > 0) {//判断文本框是否有文字，如果有就去掉最后一位
                    int selectionIndex = getSelectionStart();
                    if (selectionIndex == 0) {
                        return super.sendKeyEvent(event);
                    }
                    String preText = text.substring(0, selectionIndex - 1);
                    String nextText = "";
                    if (selectionIndex != text.length()) {
                        nextText = text.substring(selectionIndex, text.length() - 1);
                    }
                    setText(String.format("%s%s", preText, nextText));

                    setSelection(selectionIndex - 1, selectionIndex - 1);//设置焦点在最后
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }
}

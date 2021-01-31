package com.laboratory.gallery

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView

class ChooseDialog(context: Context) : Dialog(context,R.style.dialog) {
    private lateinit var onDialogItemClickListener: OnDialogItemClickListener

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.choose_dialog, null, false)
        setContentView(view)

        val sharePhoto = view.findViewById<TextView>(R.id.sharePhoto)
        val downloadPhoto = view.findViewById<TextView>(R.id.downloadPhoto)
        sharePhoto.setOnClickListener {
            onDialogItemClickListener.itemClick(1)
        }
        downloadPhoto.setOnClickListener {
            onDialogItemClickListener.itemClick(2)
        }

        val dialogWindow = window
        dialogWindow!!.setGravity(Gravity.CENTER)
        val lp = dialogWindow.attributes
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置弹窗宽度
        //设置弹窗宽度
        lp.width = 800
        dialogWindow.attributes = lp
    }

    fun setDialogOnClickListener(listener: OnDialogItemClickListener) {
        onDialogItemClickListener = listener
    }

}

interface OnDialogItemClickListener {
    fun itemClick(position: Int)
}
package com.example.satellitefinder.ui.dialogs


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import com.example.satellitefinder.databinding.RattingDialogBinding
import com.example.satellitefinder.utils.*


class RattingDialog (context: Context, private val onClick: (() -> Unit)? =null): Dialog(context) {

    private lateinit var binding: RattingDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  RattingDialogBinding.inflate(LayoutInflater.from(context))

        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(context.getWindowWidth(0.90f), ViewGroup.LayoutParams.WRAP_CONTENT)

        //window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       setCancelable(true)

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

            when {

                rating >= 4.0f -> {
                    MySharePrefrencesHelper.putBoolean(context, isRatting, true)
                    context.rateUs()
                    dismiss()
                }
                else -> {
                    MySharePrefrencesHelper.putBoolean(context, isRatting, true)
                    context.sendEmail()
                    dismiss()
                }
            }
        }

        binding.btnRate.setOnClickListener {

            when {
                binding.ratingBar.rating == 0.0f -> {
                    Toast.makeText(context, "Kindly Rate Our Application", Toast.LENGTH_SHORT).show()
                }
                binding.ratingBar.rating >= 5.0f -> {
                    MySharePrefrencesHelper.putBoolean(context, isRatting, true)

                    context.rateUs()
                   dismiss()
                    onClick?.invoke()

                }
                else -> {
                    MySharePrefrencesHelper.putBoolean(context, isRatting, true)

                    context.sendEmail()
                    dismiss()
                    onClick?.invoke()
                }
            }
        }
        binding.close.setOnClickListener {
            MySharePrefrencesHelper.putBoolean(context, isRatting, true)
            dismiss()
            onClick?.invoke()
        }


    }
}
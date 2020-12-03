package com.example.filetracker.filedetail

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.filetracker.R
import com.example.filetracker.database.FileDetailWithLastMovement
import com.example.filetracker.database.MovementDetail
import java.text.SimpleDateFormat

@BindingAdapter("fileMovementTime")
fun TextView.setFileMovementTime(movement: MovementDetail?){
    movement?.let {
        text = SimpleDateFormat("EEEE dd-MMM-yyyy' Time: 'HH:mm")
                .format(it.movementTime).toString()
    }
}

@BindingAdapter("movementImage")
fun ImageView.setMovementImage(movement: MovementDetail?){
    movement?.let {
        setImageResource(when(it.movingOut){
            true -> R.drawable.ic_out_circle
            else -> R.drawable.ic_in_circle
        })
    }
}
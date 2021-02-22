package com.phaggu.filetracker.filelist

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.phaggu.filetracker.R
import com.phaggu.filetracker.database.FileDetailWithLastMovement
import java.text.SimpleDateFormat

@BindingAdapter("fileNumber")
fun TextView.setFileNumber(file: FileDetailWithLastMovement?){
    file?.let {
        text = file.fileNumber
    }
}

@BindingAdapter("fileDescription")
fun TextView.setFileDescription(file: FileDetailWithLastMovement?){
    file?.let {
        text = file.fileDescription
    }
}

@BindingAdapter("fileMovementTime")
fun TextView.setFileMovementTime(file: FileDetailWithLastMovement?){
    file?.let {
        text = SimpleDateFormat("EEEE dd-MMM-yyyy' Time: 'HH:mm")
                .format(file.movementTime).toString()
    }
}

@BindingAdapter("movementImage")
fun ImageView.setMovementImage(file: FileDetailWithLastMovement?){
    file?.let {
        setImageResource(when(file.movingOut){
            true -> R.drawable.ic_out_circle
            else -> R.drawable.ic_in_circle
        })
    }
}
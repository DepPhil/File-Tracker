package com.example.filetracker.filedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filetracker.database.FileDetailWithLastMovement
import com.example.filetracker.database.MovementDetail
import com.example.filetracker.databinding.ListItemFileBinding
import com.example.filetracker.databinding.ListItemFileDetailBinding
import com.example.filetracker.filelist.FileListClickListener

class FileDetailAdapter(
//        val clickListener: FileListClickListener
        ):
        ListAdapter<MovementDetail, ViewHolder>(FileDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movement = getItem(position)
        holder.bind(movement
//                , clickListener
        )
    }


}


class ViewHolder private constructor(val binding: ListItemFileDetailBinding):
        RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent: ViewGroup): ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemFileDetailBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }

    fun bind(item: MovementDetail
//             ,clickListener: FileListClickListener
    ){
        binding.movement = item
//        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

class FileDiffCallback: DiffUtil.ItemCallback<MovementDetail>(){
    override fun areItemsTheSame(oldItem: MovementDetail, newItem: MovementDetail): Boolean {
        return oldItem.movementId == newItem.movementId
    }

    override fun areContentsTheSame(oldItem: MovementDetail, newItem: MovementDetail): Boolean {
        return oldItem == newItem
    }

}

// A class which takes a lambda as its parameter
//class FileListClickListener(val block: (fileId: Int) -> Unit){
//    fun onClick(file: FileDetailWithLastMovement) = block(file.fileId)
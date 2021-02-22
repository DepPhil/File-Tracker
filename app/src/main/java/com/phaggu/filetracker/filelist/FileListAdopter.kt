package com.phaggu.filetracker.filelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.phaggu.filetracker.database.FileDetailWithLastMovement
import com.phaggu.filetracker.databinding.ListItemFileBinding
import timber.log.Timber

class FileListAdopter(private val clickListener: FileListClickListener):
        ListAdapter<FileDetailWithLastMovement, ViewHolder>(FileDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file, clickListener)
    }


}


class ViewHolder private constructor(val binding: ListItemFileBinding):
        RecyclerView.ViewHolder(binding.root){
            companion object{
                fun from(parent: ViewGroup): ViewHolder{
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ListItemFileBinding.inflate(layoutInflater, parent, false)
                    val view = binding.root
                    view.setOnLongClickListener {view ->
                        Timber.i("Double click!!!")
                        true
                    }
                    return ViewHolder(binding)
                }
            }

            fun bind(item: FileDetailWithLastMovement, clickListener: FileListClickListener){
                binding.file = item
                binding.clickListener = clickListener

                binding.executePendingBindings()
            }
        }



class FileDiffCallback: DiffUtil.ItemCallback<FileDetailWithLastMovement>(){
    override fun areItemsTheSame(oldItem: FileDetailWithLastMovement, newItem: FileDetailWithLastMovement): Boolean {
        return oldItem.fileId == newItem.fileId
    }

    override fun areContentsTheSame(oldItem: FileDetailWithLastMovement, newItem: FileDetailWithLastMovement): Boolean {
        return oldItem == newItem
    }

}

// A class which takes a lambda as its parameter
class FileListClickListener(val block: (fileId: Int) -> Unit){
    fun onClick(file: FileDetailWithLastMovement) = block(file.fileId)
}
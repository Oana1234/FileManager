package com.example.filemanager.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseRecyclerViewAdapter
import com.example.filemanager.base.view.BaseViewHolder
import com.example.filemanager.utils.FileType
import com.example.filemanager.utils.getFileModelsFromFiles
import com.example.filemanager.utils.getFilesFromPath
import kotlinx.android.synthetic.main.fragment_files_list.*
import kotlinx.android.synthetic.main.item_file_row.view.*

class FilesRecyclerViewAdapter : BaseRecyclerViewAdapter<FileModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FileModel> =
        FilesViewHolder(getViewHolderView(parent, R.layout.item_file_row))

    override fun onBindViewHolder(holder: BaseViewHolder<FileModel>, position: Int) {
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.fall_down
            )
        )

        super.onBindViewHolder(holder, position)
    }

    class FilesViewHolder(itemView: View) : BaseViewHolder<FileModel>(itemView) {
        override fun bind(item: FileModel) {
            itemView.apply {
                nameTextView.text = item.name
                if (item.fileType == FileType.FOLDER) {
                    folderTextView.visibility = View.VISIBLE
                    totalSizeTextView.visibility = View.GONE
                    folderTextView.text = "(${item.subFiles} files)"
                } else {
                    itemView.folderTextView.visibility = View.GONE
                    itemView.totalSizeTextView.visibility = View.VISIBLE
                    itemView.totalSizeTextView.text = "${String.format("%.2f", item.sizeInMB)} mb"
                }
            }
        }
    }


}
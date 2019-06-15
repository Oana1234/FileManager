package com.example.filemanager.ui.main.fileslist.view

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.filemanager.R
import com.example.filemanager.base.model.FileModel
import com.example.filemanager.base.view.BaseRecyclerViewAdapter
import com.example.filemanager.base.view.BaseViewHolder
import com.example.filemanager.utils.FileType
import kotlinx.android.synthetic.main.item_file_row.view.*
import android.util.SparseBooleanArray

class FilesRecyclerViewAdapter : BaseRecyclerViewAdapter<FileModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FileModel> {

        return FilesViewHolder(
            getViewHolderView(
                parent,
                R.layout.item_file_row
            ))

    }

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
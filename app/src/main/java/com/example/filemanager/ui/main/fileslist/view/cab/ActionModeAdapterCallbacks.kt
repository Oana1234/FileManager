package com.example.filemanager.ui.main.fileslist.view.cab


interface ActionModeAdapterCallbacks<T> {

    fun toggleSelection(position: Int)
    fun clearSelections()
    fun getSelectedCount(): Int
    fun getSelectedItems(): List<T>

}
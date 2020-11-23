package com.yilmazvolkan.simplenoteapp.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yilmazvolkan.simplenoteapp.R
import com.yilmazvolkan.simplenoteapp.databinding.ItemNoteBinding
import com.yilmazvolkan.simplenoteapp.util.inflate
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NotesDetailItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(selectedPosition: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    private val effectsDetailList = arrayListOf<NoteItemViewState>()

    fun setEffectsDetailList(viewStateList: MutableList<NoteItemViewState>) {
        this.effectsDetailList.clear()
        this.effectsDetailList.addAll(viewStateList)
        notifyDataSetChanged()
    }

    fun addEffectsDetail(viewState: NoteItemViewState) {
        this.effectsDetailList.add(viewState)
        notifyDataSetChanged()
    }

    fun deleteEffectsDetail(index: Int) {
        this.effectsDetailList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesDetailItemViewHolder =
        NotesDetailItemViewHolder.create(parent, onItemClickListener)

    override fun getItemCount(): Int = effectsDetailList.size

    override fun onBindViewHolder(viewHolder: NotesDetailItemViewHolder, position: Int) =
        viewHolder.bind(effectsDetailList[position])


    class NotesDetailItemViewHolder(
        private val binding: ItemNoteBinding,
        private val onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.relativeLayoutNoteItem.setOnClickListener {
                onItemClickListener?.onItemClicked(
                    adapterPosition
                )
            }
        }

        fun bind(effectsDetailList: NoteItemViewState) {
            binding.viewState = effectsDetailList
            binding.executePendingBindings()
        }

        companion object {

            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener?
            ): NotesDetailItemViewHolder {
                val binding: ItemNoteBinding = parent.inflate(R.layout.item_note, false)
                return NotesDetailItemViewHolder(binding, onItemClickListener)
            }
        }
    }
}
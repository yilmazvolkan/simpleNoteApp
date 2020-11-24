package com.yilmazvolkan.simplenoteapp.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.yilmazvolkan.simplenoteapp.R
import com.yilmazvolkan.simplenoteapp.databinding.ItemNoteBinding
import com.yilmazvolkan.simplenoteapp.util.inflate
import com.yilmazvolkan.simplenoteapp.view.NoteItemViewState
import com.bumptech.glide.request.target.Target

class NoteListAdapter(private val context: Context) : RecyclerView.Adapter<NoteListAdapter.NotesDetailItemViewHolder>() {

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

    fun notifyItemUpdated(index: Int, viewState: NoteItemViewState) {
        this.effectsDetailList[index].setTitle(viewState.getTitle())
        this.effectsDetailList[index].setDesc(viewState.getDesc())
        this.effectsDetailList[index].setImageURL(viewState.getImageURL())
        this.effectsDetailList[index].setIsEdited(viewState.getIsEdited())
        notifyItemChanged(index)
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
        viewHolder.bind(effectsDetailList[position], context)


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

        fun bind(noteItem: NoteItemViewState, context: Context) {
            if (noteItem.getImageURL().isNotEmpty()) {
                binding.imageViewPhoto.visibility = VISIBLE
                Glide.with(context)
                    .load(noteItem.getImageURL())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            binding.imageViewPhoto.visibility = GONE
                            return false
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(binding.imageViewPhoto)
            }

            binding.viewState = noteItem
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
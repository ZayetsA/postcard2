package com.example.postcard2.presets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postcard2.databinding.CardItemBinding

class PresetsAdapter(
    private val itemsList: List<PresetModel>,
    private val itemClick: (PresetModel) -> Unit
) :
    RecyclerView.Adapter<PresetsAdapter.ThemeItemHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThemeItemHolder {
        val itemBinding =
            CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ThemeItemHolder, position: Int) {
        val paymentBean: PresetModel = itemsList[position]
        holder.bind(paymentBean, itemClick)
    }

    override fun getItemCount(): Int = itemsList.size

    inner class ThemeItemHolder(private val itemBinding: CardItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PresetModel, listener: (PresetModel) -> Unit) = with(itemView) {
            setOnClickListener { listener(item) }
            itemBinding.cardItemTitle.text = item.title
            itemBinding.cardItemDescription.text = item.text
            itemBinding.cardItemImage.setImageResource(item.imageId)
        }
    }
}
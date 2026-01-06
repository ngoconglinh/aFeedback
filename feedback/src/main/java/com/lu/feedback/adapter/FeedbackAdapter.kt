package com.lu.feedback.adapter

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lu.feedback.databinding.LayoutFeedbackOptionBinding
import com.lu.feedback.model.Feedback

class FeedbackAdapter(
    private val onSelectFb: (List<Feedback>) -> Unit
) : ListAdapter<Feedback, FeedbackAdapter.FeedbackViewHolder>(FeedbackDiff) {

    var iconColorStateList: ColorStateList? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedbackViewHolder {
        val view = LayoutFeedbackOptionBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            cb.buttonTintList = iconColorStateList
        }
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FeedbackViewHolder,
        position: Int
    ) {
        holder.onBind(getItem(position), position)
    }

    inner class FeedbackViewHolder(
        private val viewBinding: LayoutFeedbackOptionBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun onBind(f: Feedback, position: Int) = with(viewBinding) {
            cb.text = f.text
            cb.isChecked = f.isSelected
            root.setOnClickListener {
                currentList[position].isSelected = !f.isSelected
                onSelectFb.invoke(
                    currentList.filter { it.isSelected && it != null }.filterNotNull()
                )
                notifyItemChanged(position)
            }
        }
    }

    object FeedbackDiff : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }
    }
}
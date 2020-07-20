package edu.rosehulman.samuelma.letsgetknotty.project


import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import edu.rosehulman.fowlerae.letsgetknotty.project.PatternAdapter
import edu.rosehulman.samuelma.letsgetknotty.R
import edu.rosehulman.samuelma.letsgetknotty.pattern.Pattern
import jp.wasabeef.picasso.transformations.CropSquareTransformation
import kotlinx.android.synthetic.main.grid_view.view.*

class PatternViewHolder(itemView: View, private val adapter: PatternAdapter): RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
    private val imageView: ImageView = itemView.findViewById(R.id.image_text_view)
    private var cardView: CardView

    init {
        itemView.setOnClickListener {
            adapter.selectPattern(adapterPosition)
        }
        cardView = itemView.row_card_view
    }

    fun bind(pattern: Pattern) {
        nameTextView.text = pattern.name
        Picasso.get().load(pattern?.imageUrl)
            .transform(CropSquareTransformation())
            .into(imageView)
        //  Log.d(Constants.TAG, "IMAGE VIEW SIZE: ${imageView.width}")
        if (pattern.showDark) {
            cardView.setCardBackgroundColor(
                ContextCompat.getColor(adapter.context, R.color.colorAccent)
            )
        } else {
            cardView.setCardBackgroundColor(Color.WHITE)
        }
    }
}
package com.mygallary.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mygallary.R
import com.mygallary.repository.datamodel.Data


class SearchImagesRecyclerAdapter(private val list : ArrayList<Data?>, private val onItemClick : (data :Data) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    private var isLoadingAdded = false

 
    inner class ViewHolderItem(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

       val imageView  = listItemView.findViewById<ImageView>(R.id.imageView)
      // val progressBar  = listItemView.findViewById<ProgressBar>(R.id.progressBar)


   }

    inner class ViewHolderLoading(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val progressBar  = listItemView.findViewById<ProgressBar>(R.id.progressBar2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        return if (viewType == VIEW_TYPE_ITEM) {
            // Inflate the custom layout
            val contactView = inflater.inflate(R.layout.image_item_layout, parent, false)
            // Return a new holder instance
            ViewHolderItem(contactView)
        }else{
            val contactView = inflater.inflate(R.layout.recycler_view_item_loading, parent, false)
            // Return a new holder instance
            ViewHolderLoading(contactView)
        }
    }

    override fun getItemCount(): Int {
      return  list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1 && isLoadingAdded) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(null)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = list.size - 1
        val result: Data? = getItem(position)
            list.removeAt(position)
            notifyItemRemoved(position)
    }

    fun add(data: Data?) {
         list.add(data)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(dataList: List<Data>) {
        for (result in dataList) {
            add(result)
        }
    }

   fun getItem(position: Int): Data? {
        return list[position]
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val data = list[position]

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {

            val itemViewHolder : ViewHolderItem = holder as ViewHolderItem

            itemViewHolder.itemView.setOnClickListener {

                data?.let { it1 -> onItemClick(it1) }

            }

            val imageUrl = data?.images?.get(0)?.link

            Glide
                .with(itemViewHolder.imageView.context)
                .load(imageUrl)
                .error(R.drawable.error_image)
                // .centerCrop()
                //  .placeholder(circularProgress)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                      //  itemViewHolder.progressBar.visibility = View.INVISIBLE

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                      //  itemViewHolder.progressBar.visibility = View.INVISIBLE
                        return false
                    }
                })
                .into(itemViewHolder.imageView)

        }else{
            val loadingViewHolder : ViewHolderLoading = holder as ViewHolderLoading

            loadingViewHolder.progressBar.visibility = View.VISIBLE

        }

    }
}
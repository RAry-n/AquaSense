package com.example.mapboxapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mapboxapp.R
import com.example.mapboxapp.ReachOut
import com.example.mapboxapp.dataclass.problems


class ReachOutAdapter (val context: Context, private  val itemList: ArrayList<problems> = ArrayList<problems>(), private val onItemClickListener: ReachOut):RecyclerView.Adapter<ReachOutAdapter.MyViewHolder>(){

    private val headings = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.issue_review_itemlayout,
            parent,false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = itemList[position]

        holder.user_name.text=currentitem.username
        holder.location.text=currentitem.locationLat.toString()+" "+currentitem.locationLat.toString()
        holder.title.text=currentitem.title
        Glide.with(context).load(currentitem.imageURL).into(holder.issueImage)
        Glide.with(context).load(currentitem.userimageURL).into(holder.userImage)


        holder.itemView.setOnClickListener{
            onItemClickListener.onItemItemClicked(position)
        }

        currentitem.title?.let { headings.add(it) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateItemList(itemList: List<problems>){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val user_name: TextView= itemView.findViewById(R.id.user_name)
        val title : TextView= itemView.findViewById(R.id.title)
        val location : TextView= itemView.findViewById(R.id.location)
        val userImage : ImageView = itemView.findViewById(R.id.userImage)
        val issueImage : ImageView = itemView.findViewById(R.id.issue)
    }

}
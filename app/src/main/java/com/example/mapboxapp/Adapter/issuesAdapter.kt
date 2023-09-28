package com.example.mapboxapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapboxapp.R
import com.example.mapboxapp.dataclass.issues
import com.squareup.picasso.Picasso


class issuesAdapter (val itemList: ArrayList<issues> = ArrayList<issues>()):
    RecyclerView.Adapter<issuesAdapter.ViewHolder>(){

//    private lateinit var mListener: onItemClickListener
//
//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.issue_card,
            parent,false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = itemList[position]

        if(currentitem.imageURL!=null){
            Picasso.get().load(currentitem.imageURL).into(holder.imageUrlIV)
        }
        holder.estimatedLossTV.text = currentitem.estimatedloss
//        holder.problemTV.text = currentitem.problem
//        holder.descriptionTV.text = currentitem.description
        holder.typeTV.text = currentitem.type
        holder.cityTV.text = currentitem.city
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

//    fun updateItemList(itemList: List<issues>){
//        this.itemList.clear()
//        this.itemList.addAll(itemList)
//        notifyDataSetChanged()
//    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageUrlIV: ImageView= view.findViewById(R.id.imageVieww)
        val estimatedLossTV : TextView = view.findViewById(R.id.probableLoss)
        val typeTV : TextView = view.findViewById(R.id.type)
        val cityTV: TextView = view.findViewById(R.id.city)
    }

}
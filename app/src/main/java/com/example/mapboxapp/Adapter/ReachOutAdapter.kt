package com.example.mapboxapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapboxapp.R
import com.example.mapboxapp.dataclass.solutions


class ReachOutAdapter (private val itemList: ArrayList<solutions> = ArrayList<solutions>()):RecyclerView.Adapter<ReachOutAdapter.MyViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.issue_review_itemlayout,
            parent,false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = itemList[position]
        holder.setIsRecyclable(false)
        holder.providedByTV.text = currentitem.providedBy
//        holder.problemTV.text = currentitem.problem
        holder.descriptionTV.text = currentitem.description
        holder.typeTV.text = currentitem.type
        holder.providedByTV.text = currentitem.providedBy
        holder.upvoteCount.text = currentitem.upvoteCount.toString()

        holder.upvoteButton.setOnClickListener {
            currentitem.upvoteCount++
        }

        holder.donateButton.setOnClickListener{
//            onItemClickListener.onItemItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

//    fun updateItemList(itemList: List<solutions>){
//        this.itemList.clear()
//        this.itemList.addAll(itemList)
//        notifyDataSetChanged()
//    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val providedByTV: TextView= itemView.findViewById(R.id.providedBy)
        //        val problemTV : TextView= itemView.findViewById(R.id.problem)
        val descriptionTV : TextView= itemView.findViewById(R.id.description)
        val typeTV : TextView = itemView.findViewById(R.id.type)
        val upvoteButton : ImageView = itemView.findViewById(R.id.upvoteButton)
        val upvoteCount : TextView = itemView.findViewById(R.id.upvoteCount)
        val donateButton: ImageView = itemView.findViewById(R.id.donateButton)
    }

}
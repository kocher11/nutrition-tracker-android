package com.example.clonefatsecret2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(private val items: List<Item>, private val context: Context) :
    RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.textView4)
        val cal: TextView = view.findViewById(R.id.textView6)
        val proteins: TextView = view.findViewById(R.id.textView9)
        val fats: TextView = view.findViewById(R.id.textView12)
        val carb: TextView = view.findViewById(R.id.textView14)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.cal.text = "Калории: ${item.cal} ккал"
        holder.proteins.text = "Белки: ${item.proteins} г"
        holder.fats.text = "Жиры: ${item.fats} г"
        holder.carb.text = "Углеводы: ${item.carb} г"


        holder.itemView.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra("selectedProduct", item)
            context.startActivity(intent)
        }
    }
}

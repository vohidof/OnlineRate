package Adapter

import Model.Soqqa
import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vohidov.volley.databinding.ItemRvBinding
import kotlin.random.Random

class RvAdapter(var list: ArrayList<Soqqa>, var itemClick: ItemClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(model: Soqqa, position: Int) {
            itemRv.txtRateName.text = model.CcyNm_UZ
            itemRv.txtRate.text = model.Ccy
            itemRv.txtRateValue.text = model.Rate
            itemRv.txtPosition.text = (position + 1).toString()

            /*Handler().postDelayed({
                val random1 = Random.nextInt(list.size)
                val random2 = Random.nextInt(list.size)

                list[random1] = list[random2]
                list[random2] = list[random1]

                Toast.makeText(itemRv.root.context, "$random1", Toast.LENGTH_SHORT).show()
                Toast.makeText(itemRv.root.context, "$random2", Toast.LENGTH_SHORT).show()
            }, 3000)*/

            itemRv.cardRv.setOnClickListener {
                itemClick.onClick1(list, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Soqqa>) {
        list = filteredList
        notifyDataSetChanged()
    }
}

interface ItemClick {
    fun onClick1(list: List<Soqqa>, position: Int)
}

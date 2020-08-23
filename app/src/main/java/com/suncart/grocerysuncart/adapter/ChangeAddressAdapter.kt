package com.suncart.grocerysuncart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.model.AddressModel
import com.suncart.grocerysuncart.util.DbUtils

class ChangeAddressAdapter(var addrList: MutableList<AddressModel>) : RecyclerView.Adapter<ChangeAddressAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.change_addr_adapter_lay, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.radioBtnAddr.text = addrList[position].userAddress.toString()
        holder.userName.text = addrList[position].userName.toString()
        if (addrList[position].inUse.equals(1)){
            holder.radioBtnAddr.isChecked = true
        }else{
            holder.radioBtnAddr.isChecked = false
        }

        holder.radioBtnAddr.setOnClickListener {
            if (holder.radioBtnAddr.isChecked){
                DbUtils.selectAddress(addrList[position].ids)
                dataSetChange()
            }
        }
    }

    override fun getItemCount(): Int {
        return addrList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioBtnAddr = itemView.findViewById<RadioButton>(R.id.radioBtnAddr)
        val userName = itemView.findViewById<TextView>(R.id.userName)
    }

    fun dataSetChange(){
        var userAddr = DbUtils.getRowInDbAddress()
        var changeAddrList = mutableListOf<AddressModel>()
        for (tList in userAddr){
            changeAddrList.add(AddressModel(tList.ids, tList.userName, tList.userAddress, tList.exactLoc, tList.isInUse))
        }
        addrList.clear()
        addrList.addAll(changeAddrList)
        notifyDataSetChanged()
    }
}
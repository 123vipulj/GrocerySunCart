package com.suncart.grocerysuncart.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbflow5.structure.exists
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.ChangeAddressAdapter
import com.suncart.grocerysuncart.database.tables.UserAddress
import com.suncart.grocerysuncart.model.AddressModel
import com.suncart.grocerysuncart.util.DbUtils
import kotlinx.android.synthetic.main.change_addr_activity.*

class ChangeAddress : AppCompatActivity() {
    private var changeAddressAdapter : ChangeAddressAdapter? = null
    private var changeAddrList : MutableList<AddressModel>? = null


    override fun onResume() {
        super.onResume()

        if (changeAddressAdapter != null){
            var userAddr = DbUtils.getRowInDbAddress()
            changeAddrList!!.clear()
            for (tList in userAddr!!){
                changeAddrList!!.add(AddressModel(tList.ids, tList.userName, tList.userAddress, tList.exactLoc, tList.isInUse))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_addr_activity)

        if (intent.getBooleanExtra("proceed_pay", false)){
            nextBtn.visibility = View.VISIBLE
        }else{
            nextBtn.visibility = View.GONE
        }

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar_address)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        val addAddressBtn = supportActionBar?.customView?.findViewById<TextView>(R.id.add_address)
        val titleBar = supportActionBar?.customView?.findViewById<TextView>(R.id.title_appbar)
        titleBar?.setText("Choose Your Address")
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        addAddressBtn?.visibility = View.VISIBLE

        addAddressBtn?.setOnClickListener {
            startActivity(Intent(this, MapPickAddress::class.java))
        }

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        var userAddr = DbUtils.getRowInDbAddress()
        changeAddrList = mutableListOf<AddressModel>()
        for (tList in userAddr){
            changeAddrList!!.add(AddressModel(tList.ids, tList.userName, tList.userAddress, tList.exactLoc, tList.isInUse))
        }
        val changeAddressRecyclerView = findViewById<RecyclerView>(R.id.changeAddrRecycler)
        changeAddressAdapter = ChangeAddressAdapter(changeAddrList!!)
        changeAddressRecyclerView.adapter = changeAddressAdapter
        changeAddressRecyclerView.layoutManager = LinearLayoutManager(this)

        nextBtn.setOnClickListener {
            if (changeAddrList!!.size == 0){
                Toast.makeText(this, "Add address first", Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(this, ProceedToPayment::class.java))
                finish()
            }
        }
    }
}
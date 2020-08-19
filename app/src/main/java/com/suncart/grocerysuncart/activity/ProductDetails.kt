package com.suncart.grocerysuncart.activity

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.SliderImage
import com.suncart.grocerysuncart.bus.ContentLoadedEvent
import com.suncart.grocerysuncart.bus.ContentLoadedMoreDetailsEvent
import com.suncart.grocerysuncart.model.BestDealModel
import com.suncart.grocerysuncart.model.SliderItem
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.util.DbUtils
import de.greenrobot.event.EventBus
import org.w3c.dom.Text

public class ProductDetails : AppCompatActivity() {

    var eventBus = EventBus.getDefault()

    lateinit var bestDealRecyclerAdapter : BestDealRecyclerAdapter
    lateinit var bestDealRecycler : RecyclerView
    lateinit var totalCart : TextView
    lateinit var contentService : ContentService

    lateinit var totalQnty : TextView
    lateinit var titleProduct : TextView
    lateinit var titleCat: TextView
    lateinit var priceTag : TextView
    lateinit var weightTag : TextView
    lateinit var quantityTag : TextView
    lateinit var descriptionContent : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_activity)

        contentService = ContentService(this)
        contentService.getAllNewsItems()
        contentService.getAllNewsItemsMoreDetails("10000")

        var bestDealList =  mutableListOf<BestDealModel>()
        bestDealList.add(BestDealModel(1, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel(2, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel(3, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        var nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        nav_icon?.visibility = View.GONE
        totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)!!

        var imageSlider = findViewById<SliderView>(R.id.imageSlider)
        var minusBtn = findViewById<ImageView>(R.id.minus_btn)
        totalQnty = findViewById<TextView>(R.id.ttl_quanity)
        var positiveBtn = findViewById<ImageView>(R.id.add_btn)
        titleProduct = findViewById<TextView>(R.id.product_title)
        titleCat = findViewById<TextView>(R.id.title_cat_p)
        priceTag = findViewById<TextView>(R.id.price_tag)
        weightTag = findViewById<TextView>(R.id.weight_tag)
        quantityTag = findViewById<TextView>(R.id.quanity_tag)
        descriptionContent = findViewById<TextView>(R.id.desc_content)

        bestDealRecycler = findViewById<RecyclerView>(R.id.best_deal_reycler)

        //cart total qty
        totalCart.text = DbUtils.getDataForTrack().toString();

        //image slider
        var sliderImageList = mutableListOf<SliderItem>()
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_one.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_4.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_2.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_3.jpg"))

        imageSlider(imageSlider, sliderImageList)
    }

    private fun imageSlider(sliderView : SliderView, sliderImgList : MutableList<SliderItem>){

        val adapter = SliderImage(this)
        sliderView.setSliderAdapter(adapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
        adapter.renewItems(sliderImgList)
        sliderView.startAutoCycle()
    }

    override fun onStart() {
        super.onStart()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (eventBus.hasSubscriberForEvent(ContentService::class.java)) {
            eventBus.unregister(this)
        }
    }

    fun onEvent(contentLoadedEvent: ContentLoadedEvent){
        if (contentLoadedEvent != null){
            val contentItems = mutableListOf<ContentItems>()
            contentItems.addAll(contentLoadedEvent.contentItemsList)

            // adapter
            bestDealRecyclerAdapter = BestDealRecyclerAdapter(this, contentItems)
            bestDealRecycler.adapter = bestDealRecyclerAdapter
            bestDealRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // set cart track of product
            bestDealRecyclerAdapter.setCartTrackListener {
                    currentQty: String? ->  totalCart.text = currentQty
            }
        }
    }

    fun onEvent(contentLoadedMoreDetailsEvent: ContentLoadedMoreDetailsEvent){
        if (contentLoadedMoreDetailsEvent != null){
            val contentLoadedMoreDetailsList = contentLoadedMoreDetailsEvent.loadedMoreDetailsEvents

            titleProduct.text = contentLoadedMoreDetailsList.get(0).productName
            weightTag.text = contentLoadedMoreDetailsList.get(0).productWeight
            priceTag.text = contentLoadedMoreDetailsList.get(0).productSp
            descriptionContent.text = contentLoadedMoreDetailsList.get(0).productDescription
        }
    }



}
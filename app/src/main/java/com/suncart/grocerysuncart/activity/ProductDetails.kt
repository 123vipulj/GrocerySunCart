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
import com.bumptech.glide.Glide
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
import kotlinx.android.synthetic.main.details_product_lay.*
import org.w3c.dom.Text

public class ProductDetails() : AppCompatActivity() {

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
    lateinit var imageSlider : SliderView
    lateinit var minusBtn : ImageView
    lateinit var positiveBtn : ImageView
    lateinit var subTitleCat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_activity)

        val idsIntentString = intent?.getStringExtra("product_ids")
        contentService = ContentService(this)
        contentService.getAllContentItems()
        contentService.getAllContentItemsMoreDetails(idsIntentString)

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

        imageSlider = findViewById<SliderView>(R.id.imageSlider)
        minusBtn = findViewById<ImageView>(R.id.minus_btn)
        totalQnty = findViewById<TextView>(R.id.ttl_quanity)
        positiveBtn = findViewById<ImageView>(R.id.add_btn)
        titleProduct = findViewById<TextView>(R.id.product_title)
        subTitleCat = findViewById<TextView>(R.id.title_sub_p)
        titleCat = findViewById<TextView>(R.id.title_cat_p)
        priceTag = findViewById<TextView>(R.id.price_tag)
        weightTag = findViewById<TextView>(R.id.weight_tag)
        quantityTag = findViewById<TextView>(R.id.quanity_tag)
        descriptionContent = findViewById<TextView>(R.id.desc_content)

        bestDealRecycler = findViewById<RecyclerView>(R.id.best_deal_reycler)

        //cart total qty
        totalCart.text = DbUtils.getDataForTrack().toString()

        minusBtn.setOnClickListener {
            if((ttl_quanity.text.toString()).toInt() != 0){
                DbUtils.insertRowDb(idsIntentString?.toLong()!!,-1)
                var ttlQty = DbUtils.getTtlQtyByIds(idsIntentString.toLong()).toString();
                quantityTag.text = ttlQty
                ttl_quanity.text = ttlQty
            }
        }

        positiveBtn.setOnClickListener {
            if((ttl_quanity.text.toString()).toInt() > 0){
                DbUtils.insertRowDb(idsIntentString?.toLong()!!,1)
                quantityTag.text = DbUtils.getTtlQtyByIds(idsIntentString.toLong()).toString()
                var ttlQty = DbUtils.getTtlQtyByIds(idsIntentString.toLong()).toString();
                quantityTag.text = ttlQty
                ttl_quanity.text = ttlQty
            }
        }
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
        sliderView.setInfiniteAdapterEnabled(false)
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
            //load images
            var sliderImageList = mutableListOf<SliderItem>()
            sliderImageList.add(SliderItem(contentLoadedMoreDetailsList[0].productPics))
            imageSlider(imageSlider, sliderImageList)

            titleProduct.text = contentLoadedMoreDetailsList[0].productName
            weightTag.text = contentLoadedMoreDetailsList[0].productWeight + " " + contentLoadedMoreDetailsList[0].productUnitType
            priceTag.text = "Rs. "+ contentLoadedMoreDetailsList[0].productSp
            descriptionContent.text = contentLoadedMoreDetailsList[0].productDescription
            quantityTag.text = DbUtils.getTtlQtyByIds(contentLoadedMoreDetailsList[0].id.toLong()).toString()
            titleCat.text = contentLoadedMoreDetailsList[0].productTitleRootCat
            subTitleCat.text = contentLoadedMoreDetailsList[0].productTitleSubCat
            ttl_quanity.text = DbUtils.getTtlQtyByIds(contentLoadedMoreDetailsList[0].id.toLong()).toString()
        }
    }



}
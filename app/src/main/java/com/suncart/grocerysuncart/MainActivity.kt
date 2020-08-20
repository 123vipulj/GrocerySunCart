package com.suncart.grocerysuncart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dbflow5.config.FlowManager
import com.google.android.material.navigation.NavigationView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.suncart.grocerysuncart.activity.MyCart
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.CategoriesAdapter
import com.suncart.grocerysuncart.adapter.SliderImage
import com.suncart.grocerysuncart.bus.ContentLoadedEvent
import com.suncart.grocerysuncart.database.AppDatabase
import com.suncart.grocerysuncart.model.BestDealModel
import com.suncart.grocerysuncart.model.CategoriesItems
import com.suncart.grocerysuncart.model.SliderItem
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.util.DbUtils
import de.greenrobot.event.EventBus
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var bestDealRecyclerAdapter: BestDealRecyclerAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration

    var eventBus = EventBus.getDefault()
    lateinit var contentService : ContentService

    lateinit var recyclerBestDeal : RecyclerView
    lateinit var recylerBestDeal_2 : RecyclerView

    lateinit var totalCart : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FlowManager.init(this)

        contentService = ContentService(this)
        contentService.getAllNewsItems()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        var bestDealList =  mutableListOf<BestDealModel>()
        bestDealList.add(BestDealModel(1, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel(2, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel(3, "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))

        var categoryItems = mutableListOf<CategoriesItems>()
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/vegetables.png","Vegetables"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/fruits.png","Fruits"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/lentils.png","Dals & Pulses"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/dry_fruits.png","Dry Fruits"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/spice.png","Spices"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/dairy_bakery.png","Dairy & Bakery"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/personal_care.png","Personal Care"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/beverage.png","Beverages"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/household.png","HouseHold"))
        categoryItems.add(CategoriesItems("http://vipultest.nbwebsolution.com/images/categories_icons/kitchen.png","Kitchen & Dining"))

        var recyclerCat = findViewById<RecyclerView>(R.id.categories_recycler);
        var categoriesAdapter = CategoriesAdapter(this, categoryItems)
        recyclerCat.adapter = categoriesAdapter
        recyclerCat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        var nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        var cartImg = supportActionBar?.customView?.findViewById<ImageView>(R.id.cart_icons);
        totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)!!

        recyclerBestDeal = findViewById<RecyclerView>(R.id.first_slide_best_deal);
        recylerBestDeal_2 = findViewById<RecyclerView>(R.id.first_slide_best_deal_2);

        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        val sliderView_2 = findViewById<SliderView>(R.id.imageSlider_2)


        cartImg?.setOnClickListener {
            val intent = Intent(this, MyCart::class.java)
            startActivity(intent)
        }

        // add no of product to track cart
        totalCart?.text = DbUtils.getDataForTrack().toString();

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        nav_icon?.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.END, true)
            }else{
                drawerLayout.openDrawer(GravityCompat.START, true);
            }
        }


        var sliderImageList = mutableListOf<SliderItem>()
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_one.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_4.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_2.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_3.jpg"))

        var sliderImageList_2 = mutableListOf<SliderItem>()
        sliderImageList_2.add(SliderItem("http://vipultest.nbwebsolution.com/images/cash.jpg"))
        sliderImageList_2.add(SliderItem("http://vipultest.nbwebsolution.com/images/miller.jpg"))

        imageSlider(sliderView, sliderImageList)
        imageSlider(sliderView_2, sliderImageList_2)
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

    private fun recyclerDeal(recyclerBestDeal: RecyclerView, bestDealRecyclerAdapter: BestDealRecyclerAdapter){
        recyclerBestDeal.adapter = bestDealRecyclerAdapter;
        recyclerBestDeal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

    }

    override fun onResume() {
        super.onResume()
        totalCart.text = DbUtils.getDataForTrack().toString();
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

            bestDealRecyclerAdapter = BestDealRecyclerAdapter(this, contentItems);
            recyclerDeal(recyclerBestDeal, bestDealRecyclerAdapter);
            recyclerDeal(recylerBestDeal_2, bestDealRecyclerAdapter)

            // set cart track of product
            bestDealRecyclerAdapter.setCartTrackListener {
                    currentQty: String? ->  totalCart?.text = currentQty
            }
        }
    }

}
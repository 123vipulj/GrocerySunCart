package com.suncart.grocerysuncart

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.suncart.grocerysuncart.adapter.BestDealRecyclerAdapter
import com.suncart.grocerysuncart.adapter.SliderImage
import com.suncart.grocerysuncart.model.BestDealModel
import com.suncart.grocerysuncart.model.SliderItem


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var recyclerBestDeal: RecyclerView
    private lateinit var bestDealRecyclerAdapter: BestDealRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bestDealList =  mutableListOf<BestDealModel>()
        bestDealList.add(BestDealModel("0", "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel("0", "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))
        bestDealList.add(BestDealModel("0", "Plastic Free grocery deliver fast", "Rs. 960",  "Rs. 1080", "102","7 kg" , "http://vipultest.nbwebsolution.com/images/fortune.jpg"))

        recyclerBestDeal = findViewById(R.id.first_slide_best_deal);
        bestDealRecyclerAdapter = BestDealRecyclerAdapter(this,
            bestDealList as List<BestDealModel>?
        );
        recyclerBestDeal.adapter = bestDealRecyclerAdapter;
        recyclerBestDeal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)


        var sliderImageList = mutableListOf<SliderItem>()
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_one.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_4.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_2.jpg"))
        sliderImageList.add(SliderItem("http://vipultest.nbwebsolution.com/images/grocery_3.jpg"))

        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        val adapter = SliderImage(this)
        sliderView.setSliderAdapter(adapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
        adapter.renewItems(sliderImageList)
        sliderView.startAutoCycle()
    }

}
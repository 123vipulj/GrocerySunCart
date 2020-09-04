package com.suncart.grocerysuncart.util

import java.util.*

class UtilApp {

    companion object{
        fun generatingRandomString(): String {
            val targetStringLength = 11
            val random = Random()
            val characters_random = arrayOf('A','B','C','D','E','F','G','H','I','J','K')
            val buffer = StringBuilder(targetStringLength)
            val charBuffer = java.lang.StringBuilder(3)

            for (i in 0 until targetStringLength) {
                buffer.append(random.nextInt(99))
            }

            for (i in 0 until 3){
                val randomCharacerInt = random.nextInt(3)
                charBuffer.append(characters_random.get(randomCharacerInt))

            }
            return charBuffer.append(buffer).toString().toUpperCase(Locale.ROOT)
        }

        fun getTotalPriceOnView() : String{
            var priceFluctuationList = DbUtils.getDataCart()
            var mrpPrice = priceFluctuationList.sumByDouble { it.productMrp.toDouble() * it.totalQty}
            var discountPrice = priceFluctuationList.sumByDouble {it.productMrp.toDouble() * it.totalQty * (it.discountProduct.toDouble()/100)}
            var totalPrice = mrpPrice - String.format("%.2f", discountPrice).toDouble()

            return mrpPrice.toString() +" "+ String.format("%.2f", discountPrice) +" "+totalPrice.toString()
        }
    }
}
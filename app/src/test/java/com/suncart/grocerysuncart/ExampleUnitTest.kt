package com.suncart.grocerysuncart

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun generatingRandomString() {
        val leftLimit = 97 // letter 'a'
        val rightLimit = 122 // letter 'z'
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
        System.out.println(charBuffer.append(buffer).toString().toUpperCase())
    }
}
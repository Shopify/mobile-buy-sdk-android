/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy3

import org.junit.Assert
import org.junit.Test
import java.time.ZoneOffset

internal fun checkForIllegalArgumentException(action: () -> Unit) {
    try {
        action()
        Assert.fail("expected IllegalArgumentException")
    } catch (e: IllegalArgumentException) {
        // ignore
    }
}

class UtilsTest {
    @Test
    fun testDateTimeParsingFormat() {
        val dateTimeStr = "2023-02-03T15:11:06"

        Assert.assertEquals(
            "Parsed date should match expected timestamp",
            "2023-02-03T15:11:06Z",
            Utils.parseDateTime(dateTimeStr).toInstant().atZone(ZoneOffset.UTC).toString()
        )
    }

    @Test
    fun test_Z_DateTimeParsingFormat() {
        val dateTimeStr = "2022-07-06T00:51:06Z"
        val dateTime = Utils.parseDateTime(dateTimeStr)
        Assert.assertEquals(
            "Parsing returns current time instead of actual date",
            dateTimeStr,
            dateTime.toInstant().atZone(ZoneOffset.UTC).toString()
        )
    }
}
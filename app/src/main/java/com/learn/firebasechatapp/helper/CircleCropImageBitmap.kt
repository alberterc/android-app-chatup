package com.learn.firebasechatapp.helper

import android.graphics.*


class CircleCropImageBitmap {
    fun getRoundedCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val widthLight = bitmap.width
        val heightLight = bitmap.height
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)
        val paintColor = Paint()
        paintColor.flags = Paint.ANTI_ALIAS_FLAG

        val rectF = RectF(Rect(0, 0, widthLight, heightLight))

        canvas.drawRoundRect(rectF, (widthLight / 2).toFloat(), (heightLight / 2).toFloat(), paintColor)

        val paintImage = Paint()
        paintImage.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        canvas.drawBitmap(bitmap, 0f, 0f, paintImage)

        return output
    }
}
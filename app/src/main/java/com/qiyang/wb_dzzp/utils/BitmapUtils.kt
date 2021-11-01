package com.qiyang.wb_dzzp.utils

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Environment
import android.view.View
import com.qiyang.wb_dzzp.MyApplication
import com.qiyang.wb_dzzp.R
import java.io.File
import java.io.FileOutputStream


/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/4/9 15:31
 * @company:
 * @email: lx802315@163.com
 */
class BitmapUtils {

    companion object {

        /**
         * @description: 保存view为bitmap
         * @date: 2019/4/19 19:30
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun createBitmap(v: View, width: Int, height: Int): Bitmap {
            //测量使得view指定大小
            val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            v.measure(measuredWidth, measuredHeight)
            //调用layout方法布局后，可以得到view的尺寸大小
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
            val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.RGB_565)
            val c = Canvas(bmp)
            c.drawColor(Color.WHITE)
            v.draw(c)
            return if (bmp != null && bmp.width != 0) {
//                zeroAndOne(bmp)!!

                bmp

            } else {
//                zeroAndOne(
//                    BitmapFactory.decodeResource(
//                        MyApplication.context.resources,
//                        R.drawable.defut,
//                        null
//                    )
//                )!!

                BitmapFactory.decodeResource(
                    MyApplication.context.resources,
                    R.drawable.ic_launcher_background,
                    null
                )

            }
        }

        /**
         * 保存图片到SD卡上
         */
        @SuppressLint("WrongConstant")
        fun saveBitmap(baseBitmap: Bitmap) {
            try {
                // 保存图片到SD卡上
                val file = File(
                    Environment.getExternalStorageDirectory(),
                    "aaa" + ".png"
                )
                var string = "/sdcard/" + file.name

                LogUtils.print("filePath : $string")

                val stream = FileOutputStream(file)
                baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                LogUtils.print("图片保存成功")
            } catch (e: Exception) {
                LogUtils.printError("图片保存失败")
                e.printStackTrace()
            }

        }

        /**
         * 保存图片到SD卡上
         */
        @SuppressLint("WrongConstant")
        fun saveBitmap(baseBitmap: Bitmap, fileName: String): Boolean {
            delectBitmap(fileName)
            try {
                // 保存图片到SD卡上
                val file = File(
                    Environment.getExternalStorageDirectory(),
                    "$fileName.png"
                )
                val string = "/sdcard/" + file.name
                LogUtils.print("filePath : $string")
                val stream = FileOutputStream(file)
                baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                LogUtils.print("图片保存成功")
                return true
            } catch (e: Exception) {
                LogUtils.printError("图片保存失败")
                e.printStackTrace()
                return false
            }

        }

        /**
         * 保存图片到SD卡上，并且返回存储地址
         */
        @SuppressLint("WrongConstant")
        fun saveBitmapToString(baseBitmap: Bitmap): String {

            try {
                // 保存图片到SD卡上
                val file = File(
                    Environment.getExternalStorageDirectory(),
                    "aaa" + ".png"
                )
                val string = "/sdcard/" + file.name

                LogUtils.print("filePath : $string")

                val stream = FileOutputStream(file)
                baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                LogUtils.print("图片保存成功")

                return string
            } catch (e: Exception) {
                LogUtils.printError("图片保存失败")
                e.printStackTrace()
                return ""
            }

        }

        /**
         * 保存图片到SD卡上
         */
        @SuppressLint("WrongConstant")
        fun saveBitmapToString(baseBitmap: Bitmap, fileName: String): String {

            try {
                // 保存图片到SD卡上
                val file = File(
                    Environment.getExternalStorageDirectory(),
                    "$fileName.png"
                )
                var string = "/sdcard/" + file.name

                LogUtils.print("filePath : $string")

                val stream = FileOutputStream(file)
                baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                LogUtils.print("save bitmap success")

                return string
            } catch (e: Exception) {
                LogUtils.print("save bitmap fail")
                e.printStackTrace()
                return ""
            }

        }

        /**
         * @description: 删除图片
         * @date: 2019/4/16 11:33
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun delectBitmap(fileName: String) {
            // 保存图片到SD卡上
            val file = File(
                Environment.getExternalStorageDirectory(),
                "$fileName.png"
            )

            if (file.exists()) {
                file.delete()
            }

        }

        /**
         *
         * @param bitmap
         * @param orientationDegree 0 - 360 范围
         * @return
         */
        fun adjustPhotoRotation(bitmap: Bitmap): Bitmap? {
            val matrix = Matrix()
            matrix.setRotate(
                180.toFloat(), bitmap.width.toFloat(),
                bitmap.height.toFloat()
            )
            val targetX: Float = bitmap.width.toFloat()
            val targetY: Float = bitmap.height.toFloat()
            val values = FloatArray(9)
            matrix.getValues(values)
            val x1 = values[Matrix.MTRANS_X]
            val y1 = values[Matrix.MTRANS_Y]
            matrix.postTranslate(targetX - x1, targetY - y1)
            val canvasBitmap = Bitmap.createBitmap(
                bitmap.width, bitmap.height,
                Bitmap.Config.ARGB_4444
            )
            val paint = Paint()
            val canvas = Canvas(canvasBitmap)
            canvas.drawBitmap(bitmap, matrix, paint)
            return canvasBitmap
        }

        private fun zeroAndOne(bm: Bitmap): Bitmap? {
            val width = bm.width //原图像宽度
            val height = bm.height //原图像高度
            var color: Int //用来存储某个像素点的颜色值
            var r: Int
            var g: Int
            var b: Int
            var a: Int //红，绿，蓝，透明度
            //创建空白图像，宽度等于原图宽度，高度等于原图高度，用ARGB_8888渲染，这个不用了解，这样写就行了
            val bmp = Bitmap.createBitmap(
                width, height
                , Bitmap.Config.RGB_565
            )
            val oldPx = IntArray(width * height) //用来存储原图每个像素点的颜色信息
            val newPx = IntArray(width * height) //用来处理处理之后的每个像素点的颜色信息
            /**
             * 第一个参数oldPix[]:用来接收（存储）bm这个图像中像素点颜色信息的数组
             * 第二个参数offset:oldPix[]数组中第一个接收颜色信息的下标值
             * 第三个参数width:在行之间跳过像素的条目数，必须大于等于图像每行的像素数
             * 第四个参数x:从图像bm中读取的第一个像素的横坐标
             * 第五个参数y:从图像bm中读取的第一个像素的纵坐标
             * 第六个参数width:每行需要读取的像素个数
             * 第七个参数height:需要读取的行总数
             */
            bm.getPixels(oldPx, 0, width, 0, 0, width, height) //获取原图中的像素信息
            for (i in 0 until width * height) { //循环处理图像中每个像素点的颜色值
                color = oldPx[i] //取得某个点的像素值
                r = Color.red(color) //取得此像素点的r(红色)分量
                g = Color.green(color) //取得此像素点的g(绿色)分量
                b = Color.blue(color) //取得此像素点的b(蓝色分量)
                a = Color.alpha(color) //取得此像素点的a通道值

                //此公式将r,g,b运算获得灰度值，经验公式不需要理解
                var gray =
                    (r.toFloat() * 0.3 + g.toFloat() * 0.59 + b.toFloat() * 0.11).toInt()
                //下面前两个if用来做溢出处理，防止灰度公式得到到灰度超出范围（0-255）
                if (gray > 255) {
                    gray = 255
                }
                if (gray < 0) {
                    gray = 0
                }
                if (gray <= 200) { //如果某像素的灰度值不是0(黑色)就将其置为0（黑色）
                    gray = 0
                }
                if (gray > 200) {
                    gray = 255
                }
                newPx[i] =
                    Color.argb(a, gray, gray, gray) //将处理后的透明度（没变），r,g,b分量重新合成颜色值并将其存储在数组中
            }
            /**
             * 第一个参数newPix[]:需要赋给新图像的颜色数组//The colors to write the bitmap
             * 第二个参数offset:newPix[]数组中第一个需要设置给图像颜色的下标值//The index of the first color to read from pixels[]
             * 第三个参数width:在行之间跳过像素的条目数//The number of colors in pixels[] to skip between rows.
             * Normally this value will be the same as the width of the bitmap,but it can be larger(or negative).
             * 第四个参数x:从图像bm中读取的第一个像素的横坐标//The x coordinate of the first pixels to write to in the bitmap.
             * 第五个参数y:从图像bm中读取的第一个像素的纵坐标//The y coordinate of the first pixels to write to in the bitmap.
             * 第六个参数width:每行需要读取的像素个数The number of colors to copy from pixels[] per row.
             * 第七个参数height:需要读取的行总数//The number of rows to write to the bitmap.
             */
            bmp.setPixels(newPx, 0, width, 0, 0, width, height) //将处理后的像素信息赋给新图
            return bmp //返回处理后的图像
        }
    }

}
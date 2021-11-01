package com.qiyang.wb_dzzp.utils

import com.qiyang.wb_dzzp.MyApplication
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/9/19 13:42
 * @company:
 * @email: lx802315@163.com
 */
class FileUtils {

    companion object {
        /**
         * @description: 存储时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun saveTime(time: String) {
            //创建文件助手对象，传入mContext程序当前的内容
            val fHelper = FileHelper(MyApplication.context)

            //获得文件名和写入内容
            val filename = "time"
            val filetext = time + ""

            try {
                //保存文件名和内容
                fHelper.savaFileToSD(filename, filetext)
            } catch (e: Exception) {
                //写入异常时
                e.printStackTrace()
            }

        }

        /**
         * @description: 获取时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun getTime(): String {
            //定论一个detail，默认为空用来存放要输出的内容
            var detail = ""
            val fHelper2 = FileHelper(MyApplication.myApplication)
            try {
                //得到输入框中文件名获得文件内容，因为可以写入多个不同名文件，所以要根据文件名来获得文件内容
                val fname = "time"
                //调用read()方法，传入上面获得的文件保，将返回的内容赋值给detail
                detail = fHelper2.readFromSD(fname)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return detail
        }

        /**
         * @description: 存储时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun saveSim(sim: String) {
            //创建文件助手对象，传入mContext程序当前的内容
            val fHelper = FileHelper(MyApplication.context)

            //获得文件名和写入内容
            val filename = "sim"
            val filetext = sim + ""

            try {
                //保存文件名和内容
                fHelper.savaFileToSD(filename, filetext)
            } catch (e: Exception) {
                //写入异常时
                e.printStackTrace()
            }

        }

        /**
         * @description: 获取时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun getSim(): String {
            //定论一个detail，默认为空用来存放要输出的内容
            var detail = ""
            val fHelper2 = FileHelper(MyApplication.myApplication)
            try {
                //得到输入框中文件名获得文件内容，因为可以写入多个不同名文件，所以要根据文件名来获得文件内容
                val fname = "sim"
                //调用read()方法，传入上面获得的文件保，将返回的内容赋值给detail
                detail = fHelper2.readFromSD(fname)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return detail
        }

        fun saveEquipId(equipId: String) {
            //创建文件助手对象，传入mContext程序当前的内容
            val fHelper = FileHelper(MyApplication.context)

            //获得文件名和写入内容
            val filename = "equipId"
            val filetext = equipId + ""

            try {
                //保存文件名和内容
                fHelper.savaFileToSD(filename, filetext)
            } catch (e: Exception) {
                //写入异常时
                e.printStackTrace()
            }

        }

        fun getEquipId(): String {
            //定论一个detail，默认为空用来存放要输出的内容
            var detail = ""
            val fHelper2 = FileHelper(MyApplication.myApplication)
            try {
                //得到输入框中文件名获得文件内容，因为可以写入多个不同名文件，所以要根据文件名来获得文件内容
                val fname = "equipId"
                //调用read()方法，传入上面获得的文件保，将返回的内容赋值给detail
                detail = fHelper2.readFromSD(fname)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return detail
        }

        /**
         * @description: 存储时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun saveDevId(DevId: String) {
            //创建文件助手对象，传入mContext程序当前的内容
            val fHelper = FileHelper(MyApplication.context)

            //获得文件名和写入内容
            val filename = "DevId"
            val filetext = DevId + ""

            try {
                //保存文件名和内容
                fHelper.savaFileToSD(filename, filetext)
            } catch (e: Exception) {
                //写入异常时
                e.printStackTrace()
            }

        }

        /**
         * @description: 获取时间
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun getDevId(): String {
            //定论一个detail，默认为空用来存放要输出的内容
            var detail = ""
            val fHelper2 = FileHelper(MyApplication.myApplication)
            try {
                //得到输入框中文件名获得文件内容，因为可以写入多个不同名文件，所以要根据文件名来获得文件内容
                val fname = "DevId"
                //调用read()方法，传入上面获得的文件保，将返回的内容赋值给detail
                detail = fHelper2.readFromSD(fname)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return detail
        }

        /**
         * @description:
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun saveString(fileName: String, content: String) {
            //创建文件助手对象，传入mContext程序当前的内容
            val fHelper = FileHelper(MyApplication.context)

            //获得文件名和写入内容
            try {
                //保存文件名和内容
                fHelper.savaFileToSD(fileName + "", content + "")
            } catch (e: Exception) {
                //写入异常时
                e.printStackTrace()
            }

        }

        /**
         * @description:
         * @date: 2019/9/16 14:45
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun getString(fileName: String): String {
            //定论一个detail，默认为空用来存放要输出的内容
            var detail = ""
            val fHelper2 = FileHelper(MyApplication.myApplication)
            try {
                //调用read()方法，传入上面获得的文件保，将返回的内容赋值给detail
                detail = fHelper2.readFromSD(fileName)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return detail
        }

        /**
         * @description: 获取文件夹内文件名称及个数
         * @date: 2020-02-24 14:22
         * @author: Meteor
         * @email: lx802315@163.com
         */
        fun getFilesAllName(path: String?): List<String>? {
            val file = File(path)
            val files: Array<File> = file.listFiles()
            if (files == null) {
                LogUtils.printError("空目录")
                return null
            }
            val s: MutableList<String> = ArrayList()
            for (i in files.indices) {
                s.add(files[i].absolutePath)
            }
            return s
        }

        /** 删除文件，可以是文件或文件夹
         * @param delFile 要删除的文件夹或文件名
         * @return 删除成功返回true，否则返回false
         */
        fun delete(delFile: String): Boolean {
            val file = File(delFile)
            return if (!file.exists()) {
                false
            } else {
                if (file.isFile) deleteSingleFile(delFile) else deleteDirectory(
                    delFile
                )
            }
        }

        /** 删除单个文件
         * @param filePathName 要删除的文件的文件名
         * @return 单个文件删除成功返回true，否则返回false
         */
        fun deleteSingleFile(`filePath$Name`: String): Boolean {
            val file = File(`filePath$Name`)
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            return if (file.exists() && file.isFile) {
                if (file.delete()) {
                    LogUtils.printError(
                        "Copy_Delete.deleteSingleFile: 删除单个文件" + `filePath$Name` + "成功！"
                    )
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

        /** 删除目录及目录下的文件
         * @param filePath 要删除的目录的文件路径
         * @return 目录删除成功返回true，否则返回false
         */
        fun deleteDirectory(filePath: String): Boolean { // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            var filePath = filePath
            if (!filePath.endsWith(File.separator)) filePath += File.separator
            val dirFile = File(filePath)
            // 如果dir对应的文件不存在，或者不是一个目录，则退出
            if (!dirFile.exists() || !dirFile.isDirectory) {

                return false
            }
            var flag = true
            // 删除文件夹中的所有文件包括子目录
            val files = dirFile.listFiles()
            for (file in files) { // 删除子文件
                if (file.isFile) {
                    flag = deleteSingleFile(file.absolutePath)
                    if (!flag) break
                } else if (file.isDirectory) {
                    flag = deleteDirectory(
                        file
                            .absolutePath
                    )
                    if (!flag) break
                }
            }
            if (!flag) {
                return false
            }
            // 删除当前目录
            return if (dirFile.delete()) {
                LogUtils.printError("Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！")
                true
            } else {
                false
            }
        }


        /**
         * 获取目录下所有文件(按时间排序)
         *
         * @param path
         * @return
         */
        fun listFileSortByModifyTime(path: String): List<File> {
            val list = getFiles(path, ArrayList())
            if (list != null && list.isNotEmpty()) {
                Collections.sort(list, FileComparator2())
            }
            return list
        }

        /**
         *
         * 获取目录下所有文件
         *
         * @param realpath
         * @param files
         * @return
         */
        fun getFiles(
            realpath: String?,
            files: ArrayList<File>
        ): List<File> {
            val realFile = File(realpath)
            if (realFile.isDirectory) {
                val subFiles: Array<File> = realFile.listFiles()
                for (file: File in subFiles) {
                    if (file.isDirectory) {
                        getFiles(file.absolutePath, files)
                    } else {
                        files.add(file)
                    }
                }
            }
            return files
        }
    }

}
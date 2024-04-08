package loli.ball.imefix

import java.io.File

class IMEFix {

    init {
        loadJarDll("/imefix.dll")
    }

    fun loadJarDll(name: String) {
        val temp = File.createTempFile(name, "")
        IMEFix::class.java.getResourceAsStream(name)!!.use { inp ->
            temp.outputStream().use { out ->
                inp.copyTo(out)
            }
        }
        System.load(temp.getAbsolutePath())
    }

}
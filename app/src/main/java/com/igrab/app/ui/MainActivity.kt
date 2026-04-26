package com.igrab.app.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.igrab.app.R
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var btnDownload: android.widget.Button
    private lateinit var statusText: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val downloadDir = "/sdcard/Download/IGDownloader"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.urlInput)
        btnDownload = findViewById(R.id.btnDownload)
        statusText = findViewById(R.id.statusText)

        File(downloadDir).mkdirs()

        btnDownload.setOnClickListener {
            val url = urlInput.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "Cole uma URL do Instagram!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startDownload(url)
        }
    }

    private fun startDownload(url: String) {
        btnDownload.isEnabled = false
        statusText.text = "⏳ Baixando..."

        Thread {
            try {
                val galleryDl = findGalleryDl()
                if (galleryDl == null) {
                    updateStatus("❌ gallery-dl não encontrado!\nInstale no Termux:\npip install gallery-dl")
                    return@Thread
                }

                val process = ProcessBuilder(galleryDl, "--directory", downloadDir, url)
                    .redirectErrorStream(true)
                    .start()

                val log = StringBuilder()
                process.inputStream.bufferedReader().forEachLine { line ->
                    log.appendLine(line)
                    handler.post { statusText.text = "⏳ $line" }
                }

                process.waitFor()

                if (process.exitValue() == 0) {
                    updateStatus("✅ Download concluído!\nSalvo em:\n$downloadDir")
                } else {
                    updateStatus("❌ Erro no download:\n$log")
                }

            } catch (e: Exception) {
                updateStatus("❌ Erro: ${e.message}")
            }
        }.start()
    }

    private fun findGalleryDl(): String? {
        val paths = listOf(
            "/data/data/com.termux/files/usr/bin/gallery-dl",
            "/data/data/com.termux/files/usr/local/bin/gallery-dl",
            "/sdcard/gallery-dl"
        )
        return paths.firstOrNull { File(it).exists() }
    }

    private fun updateStatus(msg: String) {
        handler.post {
            statusText.text = msg
            btnDownload.isEnabled = true
        }
    }
}

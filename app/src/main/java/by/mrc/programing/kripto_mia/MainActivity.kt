package by.mrc.programing.kripto_mia

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws
import kotlin.*

class MainActivity : Activity() {
    private var edtInput: EditText? = null
    private var edtOutput: EditText? = null
    private var edtKey: EditText? = null
    private var clipboardManager: ClipboardManager? = null
    private var inputMessage: String? = null
    private var outputMessage: String? = null
    private var keyMessage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtInput = findViewById(R.id.edtInput)
        edtOutput = findViewById(R.id.edtOutput)
        edtKey = findViewById(R.id.edtKey)
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun onBtnEncryptClick(view: View?) {
        keyMessage = edtKey!!.text.toString()
        inputMessage = edtInput!!.text.toString()
        if (keyMessage!!.length > 16) {
            keyMessage = keyMessage!!.substring(0, 16)
        }
        while (keyMessage!!.length < 16) {
            keyMessage = "$keyMessage "
        }
        @SuppressLint("GetInstance") val cipher = Cipher.getInstance("AES")
        val secretKeySpec = SecretKeySpec(keyMessage!!.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val bytes = cipher.doFinal(inputMessage!!.toByteArray())
        val result = StringBuilder()
        for (b in bytes) {
            result.append(b.toInt()).append(" ")
        }
        result.deleteAt(result.length - 1)
        edtOutput!!.setText(result.toString())
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, UnsupportedEncodingException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun onBtnDecryptClick(view: View?) {
        keyMessage = edtKey!!.text.toString()
        outputMessage = edtOutput!!.text.toString()
        if (keyMessage!!.length > 16) {
            keyMessage = keyMessage!!.substring(0, 16)
        }
        while (keyMessage!!.length < 16) {
            keyMessage = "$keyMessage "
        }
        val s = outputMessage!!.split(" ".toRegex()).toTypedArray()
        val sBytes = ByteArray(s.size)
        try {
            for (i in sBytes.indices) {
                sBytes[i] = s[i].toByte()
            }
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance("AES")
            val secretKeySpec = SecretKeySpec(keyMessage!!.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(sBytes)
            val result = String(bytes)
            edtInput!!.setText(result)
        } catch (error: Exception) {
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        }
    }

    fun onBtnInputPasteClick(view: View?) {
        val clipData = clipboardManager!!.primaryClip!!
        val item = clipData.getItemAt(0)
        inputMessage = item.text.toString()
        edtInput!!.setText(inputMessage)
    }

    fun onBtnInputCopyClick(view: View?) {
        val clipdata = ClipData.newPlainText("text", edtInput!!.text)
        clipboardManager!!.setPrimaryClip(clipdata)
    }

    fun onBtnInputClearClick(view: View?) {
        edtInput!!.setText("")
    }

    fun onBtnOutputPasteClick(view: View?) {
        val clipData = clipboardManager!!.primaryClip!!
        val item = clipData.getItemAt(0)
        outputMessage = item.text.toString()
        edtOutput!!.setText(outputMessage)
    }

    fun onBtnOutputCopyClick(view: View?) {
        val clipdata = ClipData.newPlainText("text", edtOutput!!.text)
        clipboardManager!!.setPrimaryClip(clipdata)
    }

    fun onBtnOutputClearClick(view: View?) {
        edtOutput!!.setText("")
    }
}
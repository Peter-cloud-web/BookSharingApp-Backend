package com.example.authentication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.toByteArray

private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hashing(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)

    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}


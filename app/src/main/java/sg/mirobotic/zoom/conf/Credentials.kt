package sg.mirobotic.zoom.conf

import android.util.Base64.*
import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class Credentials {

    companion object {

        var SDK_KEY = "az3jMfxhHIYWeKoHenNV2ULL7KkSkFRK1EWv"
        var SDK_SECRET = "8af8ShsEz07eL6FDjVeu9XmBG4edJIixVKpV"
        var SDK_DOMAIN = "zoom.us"

        @JvmStatic
        fun getJWTToken() : String {
            val time = System.currentTimeMillis()/1000

            val header = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}"
            val payload = "{\"appKey\": \"" + SDK_KEY + "\"" +
                    ""+ ", \"iat\": " + time + ", \"exp\": " + (time + 86400) + ", \"tokenExp\": " + (time + 2800) + "}"

            var token: String
            try {
                val headerBase64Str: String = encodeToString(header.toByteArray(), NO_WRAP or NO_PADDING or URL_SAFE)

                val payloadBase64Str: String =
                    encodeToString(payload.toByteArray(), NO_WRAP or NO_PADDING or URL_SAFE)
                val mac: Mac = Mac.getInstance("HMACSHA256")
                val secretKeySpec = SecretKeySpec(SDK_SECRET.toByteArray(), "HMACSHA256")
                mac.init(secretKeySpec)
                val digest: ByteArray = mac.doFinal("$headerBase64Str.$payloadBase64Str".toByteArray())
                token = "$headerBase64Str.$payloadBase64Str." + encodeToString(
                    digest,
                    NO_WRAP or NO_PADDING or URL_SAFE
                )
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                token = ""
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                token = ""
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
                token = ""
            }
            Log.e("CRED","JWT $token")
            return "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6IjVNUHV4ZTR5UVYtYldHclFYU1N1ZUEiLCJleHAiOjE2NDg3NTEzOTksImlhdCI6MTY0MTI3NjgxOH0.yyppd6thcgti7NhfMYx3jUJiU1RpJr-Dipa3hTx4914"
        }
    }

}
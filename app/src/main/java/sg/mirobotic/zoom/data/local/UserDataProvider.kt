package sg.mirobotic.zoom.data.local

import android.content.Context

class UserDataProvider(context: Context) {

    companion object {

        private var userDataProvider: UserDataProvider? = null

        fun getInstance(context: Context): UserDataProvider {
            if (userDataProvider == null) {
                userDataProvider = UserDataProvider(context)
            }
            return userDataProvider!!
        }

    }

    private val preferences = context.getSharedPreferences("mi-glass", Context.MODE_PRIVATE)

    fun saveCredentials(email: String, password: String) {
        preferences.edit().apply {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun getEmail(): String = preferences.getString("email", "")!!

    fun getPassword(): String = preferences.getString("password", "")!!

}
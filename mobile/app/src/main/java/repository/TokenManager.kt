package bf.ujkz.suiviscolaireparent.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val ELEVE_ID_KEY = intPreferencesKey("selected_eleve_id")
        private val CIVILITE_KEY = stringPreferencesKey("parent_civilite")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(ELEVE_ID_KEY)
            prefs.remove(CIVILITE_KEY)
        }
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[TOKEN_KEY] }

    suspend fun saveEleveId(id: Int) {
        context.dataStore.edit { prefs -> prefs[ELEVE_ID_KEY] = id }
    }

    val eleveIdFlow: Flow<Int?> = context.dataStore.data.map { prefs -> prefs[ELEVE_ID_KEY] }

    suspend fun saveCivilite(civilite: String?) {
        context.dataStore.edit { prefs ->
            if (civilite != null) prefs[CIVILITE_KEY] = civilite else prefs.remove(CIVILITE_KEY)
        }
    }

    val civiliteFlow: Flow<String?> = context.dataStore.data.map { prefs -> prefs[CIVILITE_KEY] }
}
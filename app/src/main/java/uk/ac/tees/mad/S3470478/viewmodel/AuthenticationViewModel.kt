package uk.ac.tees.mad.s3470478.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3470478.model.ExpenseDatabase

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Tracks the current authentication UI state
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    // Emits true if a user is currently authenticated
    private val _isLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    /**
     * Attempts to log in the user using email and password.
     * On success, updates the login state and triggers the provided success callback.
     */
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        onSuccess() // Invoke callback to sync data post-login
                        AuthUiState.Success("Login successful")
                    } else {
                        AuthUiState.Error(task.exception?.message ?: "Login failed")
                    }
                }
        }
    }

    /**
     * Registers a new user with Firebase using the provided email and password.
     * Triggers the success callback to allow post-signup logic such as data sync.
     */
    fun signup(email: String, password: String, onSuccess: () -> Unit) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        onSuccess()
                        AuthUiState.Success("Account created")
                    } else {
                        AuthUiState.Error(task.exception?.message ?: "Signup failed")
                    }
                }
        }
    }

    /**
     * Logs the user out of Firebase, resets the UI state,
     * and clears all local expense data stored in Room.
     */
    fun logout() {
        firebaseAuth.signOut()
        _isLoggedIn.value = false
        _uiState.value = AuthUiState.Idle

        // Clear locally stored expenses upon logout
        viewModelScope.launch {
            ExpenseDatabase.getDatabase(getApplication()).expenseDao().clearAllExpenses()
        }
    }

    // Returns the email of the currently logged-in user
    fun getCurrentUserEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }


    // Resets the UI state to Idle
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

package uk.ac.tees.mad.s3470478.viewmodel

import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _isLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        AuthUiState.Success("Login successful")//unresolved"Success"
                    } else {
                        AuthUiState.Error(task.exception?.message ?: "Login failed")
                    }
                }
        }
    }

    fun signup(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _uiState.value = if (task.isSuccessful) {
                        _isLoggedIn.value = true
                        AuthUiState.Success("Account created")
                    } else {
                        AuthUiState.Error(task.exception?.message ?: "Signup failed")
                    }
                }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _isLoggedIn.value = false
        _uiState.value = AuthUiState.Idle
    }
    fun getCurrentUserEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

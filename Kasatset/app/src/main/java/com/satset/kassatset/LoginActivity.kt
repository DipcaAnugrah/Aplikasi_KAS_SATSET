package com.satset.kassatset

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.satset.kassatset.ManajemenWarga.MenuManajemen
import com.satset.kassatset.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        binding.button4.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Periksa apakah pengguna sudah masuk (non-null) dan perbarui UI sesuai.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        // Update UI sesuai dengan pengguna yang masuk
        val intent = Intent(this, MenuManajemen::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in berhasil, perbarui UI dengan informasi pengguna yang masuk
                    Log.d("LoginActivity", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // Jika sign in gagal, tampilkan pesan kepada pengguna.
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    val errorMsg = task.exception?.message ?: "Kesalahan tidak diketahui"
                    Toast.makeText(this, "Login gagal: $errorMsg", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MenuManajemen::class.java)
            startActivity(intent)
            finish()
        } else {
            // Tetap di layar login
            Toast.makeText(this, "Login gagal, silakan coba lagi.", Toast.LENGTH_SHORT).show()
        }
    }
}

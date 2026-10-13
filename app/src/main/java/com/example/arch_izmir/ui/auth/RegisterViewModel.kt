package com.example.arch_izmir.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arch_izmir.data.repository.AuthResult
import com.example.arch_izmir.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository): ViewModel(){
    fun register(
        fullName: String,
        password:String,
        passwordRepeat:String,
        email:String,
        onResult: (AuthResult) -> Unit
    ) {
        // Boş Alan Kontrolü
        if(fullName.isBlank() || email.isBlank() || password.isBlank()){
            onResult(AuthResult.Error("Lütfen tüm alanları doldurunuz."))
        }
        // Eposta formatı
        if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            onResult(AuthResult.Error("Geçerli bir e-posta giriniz."))
        }
        //Şifre Uzunluğu
        if(password.length<5){
            onResult(AuthResult.Error("Şifre en az 5 karakter olmalı."))
            return
        }
        // Şifreler aynı mı
        if(passwordRepeat != password){
            onResult(AuthResult.Error("Şifreler eşleşmiyor."))
        }
        // Hepsi doğruysa kaydet
        viewModelScope.launch {
            onResult(repository.register(fullName,email,password))
        }
    }

}
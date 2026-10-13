package com.example.arch_izmir.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.arch_izmir.data.local.User
import com.example.arch_izmir.data.local.UserDao

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
class UserRepository(private val userDao: UserDao) {
    suspend fun register(fullName:String,email:String,password: String):AuthResult{

        val cleanEmail = email.trim().lowercase()

        if(userDao.findByEmail(cleanEmail) !=null){
            return AuthResult.Error("Bu Eposta zaten kayıtlı.")
        }
        val hash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

        val user = User(
            fullName = fullName.trim(),
            passwordHash = hash,
            email = cleanEmail
        )
        val newId = userDao.insert(user)
        return AuthResult.Success(user.copy(id=newId))
    }

    suspend fun login(email: String, password: String): AuthResult {
        val cleanEmail=email.trim().lowercase()
        val user = userDao.findByEmail(cleanEmail)
            ?: return AuthResult.Error("E-posta veya şifre hatalı")

        val ok = BCrypt.verifyer()
            .verify(password.toCharArray(), user.passwordHash)
            .verified

        return if (ok) AuthResult.Success(user)
        else AuthResult.Error("E-posta veya şifre hatalı")
    }

}
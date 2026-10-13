package com.example.arch_izmir.ui.auth

import com.example.arch_izmir.data.repository.UserRepository
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.arch_izmir.R
import com.example.arch_izmir.databinding.FragmentRegisterBinding
import com.example.arch_izmir.data.local.AppDatabase
import com.example.arch_izmir.data.repository.AuthResult
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private  var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Veri tabanı ve repository

        val db = AppDatabase.getInstance(requireContext())
        val repository = UserRepository(db.userDao())

        // Kayıt ol butonu
        binding.btnRegister.setOnClickListener {

            val nameSurname = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordAgain = binding.etConfirmPassword.text.toString()

            if (nameSurname.isBlank()) {
                Toast.makeText(requireContext(), "Ad soyad boş", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                Toast.makeText(requireContext(), "E-posta geçersiz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 4) {
                Toast.makeText(requireContext(), "Şifre 4 karakterden kısa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != passwordAgain) {
                Toast.makeText(requireContext(), "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewLifecycleOwner.lifecycleScope.launch {
                val sonuc = repository.register(nameSurname, email, password)

                when (sonuc) {
                    is AuthResult.Success -> {
                        Toast.makeText(requireContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.loginFragment)
                    }
                    is AuthResult.Error -> {
                        Toast.makeText(requireContext(), sonuc.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // giriş sayfasına dön
        binding.tvLoginRedirect.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

    }


}
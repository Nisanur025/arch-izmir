package com.example.arch_izmir.ui.auth

import com.example.arch_izmir.data.repository.UserRepository
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.arch_izmir.R
import com.example.arch_izmir.databinding.FragmentLoginBinding
import com.example.arch_izmir.data.local.AppDatabase
import com.example.arch_izmir.data.local.SessionAdministrator
import com.example.arch_izmir.data.repository.AuthResult
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private  var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = AppDatabase.getInstance(requireContext())
        val repository = UserRepository(db.userDao())

        // Giris Hatırla
        val session = SessionAdministrator(requireContext())

        // Daha önce beni hatırla seçiliyse atla
        if (session.isRememberChecked()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }

       binding.tvRegister.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
       }

        // Login Butonu
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            // Boş kontrolleri
            if (email.isBlank()) {
                binding.etEmail.error = "E-posta boş olamaz"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                binding.etPassword.error = "Şifre boş olamaz"
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                when (val sonuc = repository.login(email, password)) {
                    is AuthResult.Success -> {
                        session.saveSession(binding.cbRememberMe.isChecked, email)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is AuthResult.Error -> {
                        Toast.makeText(requireContext(), sonuc.message, Toast.LENGTH_LONG).show()
                    }
                }
            }


        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
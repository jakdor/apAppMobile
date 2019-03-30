package com.jakdor.apapp.ui.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

class RegistrationFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: RegistrationViewModel? = null

    var isPasswordCorrect: Boolean = false
    var isRePasswordCorrect: Boolean = false
    var isEmailCorrect: Boolean = false
    var isLoginCorrect: Boolean = false
    var isNameNotEmpty: Boolean = false
    var isSurnameNotEmpty: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        }

        val surname: String = surname_editText.text.toString()
        surname_editText.error = viewModel?.isEmptyValidation(surname)

        val name: String = name_editText.text.toString()
        name_editText.error = viewModel?.isEmptyValidation(name)

        val email: String = email_editText.text.toString()
        email_editText.error = viewModel?.isEmptyValidation(email)

        val rePassword: String = rePassword_editText.text.toString()
        rePassword_editText.error = viewModel?.isEmptyValidation(rePassword)

        val password: String = password_editText.text.toString()
        password_editText.error = viewModel?.isEmptyValidation(password)

        val login: String = login_editText.text.toString()
        login_editText.error = viewModel?.isEmptyValidation(login)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.isEnabled = false

        password_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val password: String = password_editText.text.toString()
                val rePassword: String = rePassword_editText.text.toString()

                when {
                    viewModel?.validatePassword(password, rePassword) != null -> {
                        password_editText.error = viewModel?.validatePassword(password, rePassword)
                        isPasswordCorrect = false
                    }
                    viewModel?.isEmptyValidation(password)!=null -> {
                        password_editText.error = viewModel?.isEmptyValidation(password)
                        isPasswordCorrect = false
                    }
                    else -> {
                        password_editText.error=null
                        isPasswordCorrect = true
                    }
                }

                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val password: String = password_editText.text.toString()

                when {
                    viewModel?.validatePassword(password, password) != null -> {
                        password_editText.error = viewModel?.validatePassword(password, password)
                        isPasswordCorrect = false
                    }
                    viewModel?.isEmptyValidation(password)!=null -> {
                        password_editText.error = viewModel?.isEmptyValidation(password)
                        isPasswordCorrect = false
                    }
                    else -> {
                        password_editText.error=null
                        isPasswordCorrect = true
                    }
                }

            }
        })
        rePassword_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val password: String = password_editText.text.toString()
                val rePassword: String = rePassword_editText.text.toString()

                when {
                    viewModel?.validatePassword(rePassword, password) != null -> {
                        rePassword_editText.error = viewModel?.validatePassword(rePassword, password)
                        isRePasswordCorrect = false
                    }
                    viewModel?.isEmptyValidation(rePassword)!=null -> {
                        rePassword_editText.error = viewModel?.isEmptyValidation(rePassword)
                        isRePasswordCorrect = false
                    }
                    else -> {
                        rePassword_editText.error=null
                        isRePasswordCorrect = true
                    }
                }

                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val rePassword: String = rePassword_editText.text.toString()
                val password: String = password_editText.text.toString()

                when {
                    viewModel?.validatePassword(rePassword, password) != null -> {

                        rePassword_editText.error = viewModel?.validatePassword(rePassword, password)
                        isRePasswordCorrect = false
                    }
                    viewModel?.isEmptyValidation(rePassword)!=null -> {
                        rePassword_editText.error = viewModel?.isEmptyValidation(rePassword)
                        isRePasswordCorrect = false
                    }
                    else -> {
                        rePassword_editText.error=null
                        isRePasswordCorrect = true
                    }
                }
            }
        })
        email_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email: String = email_editText.text.toString()

                when {
                    viewModel?.validateEmail(email) != null -> {
                        email_editText.error = viewModel?.validateEmail(email)
                        isEmailCorrect = false
                    }
                    viewModel?.isEmptyValidation(email)!=null -> {
                        email_editText.error = viewModel?.isEmptyValidation(email)
                        isEmailCorrect = false
                    }
                    else -> {
                        email_editText.error = null
                        isEmailCorrect = true
                    }
                }
            }
        })
        name_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name: String = name_editText.text.toString()

                if(viewModel?.isEmptyValidation(name) != null){
                    name_editText.error = viewModel?.isEmptyValidation(name)
                    isNameNotEmpty = false
                }else{
                    name_editText.error = null
                    isNameNotEmpty = true
                }
            }
        })
        surname_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val surname: String = surname_editText.text.toString()

                if(viewModel?.isEmptyValidation(surname) != null){
                    surname_editText.error = viewModel?.isEmptyValidation(surname)
                    isSurnameNotEmpty = false
                }else{
                    surname_editText.error = null
                    isSurnameNotEmpty = true
                }
            }
        })
        login_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                register_button.isEnabled = (isPasswordCorrect && isRePasswordCorrect && isEmailCorrect
                        && isNameNotEmpty && isSurnameNotEmpty && isLoginCorrect)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val login: String = login_editText.text.toString()

                when {
                    viewModel?.validateLogin(login) != null -> {
                        login_editText.error = viewModel?.validateLogin(login)
                        isLoginCorrect = false
                    }
                    viewModel?.isEmptyValidation(login)!=null -> {
                        login_editText.error = viewModel?.isEmptyValidation(login)
                        isLoginCorrect = false
                    }
                    else -> {
                        login_editText.error = null
                        isLoginCorrect = true
                    }
                }
            }
        })
    }



    companion object {
        const val CLASS_TAG = "RegistrationFragment"

        fun getInstance(): RegistrationFragment {
            val bundle = Bundle()
            val fragment = RegistrationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}

package com.jakdor.apapp.ui.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject
import kotlin.math.log

class RegistrationFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: RegistrationViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        }

        val registerObserver = Observer<Boolean> { newStatus ->
            register_button.isEnabled = newStatus
        }

        viewModel?.registerPossibility?.observe(this, registerObserver)

        observePasswordStatus()

        observeRePasswordStatus()

        observeEmailStatus()

        observeLoginStatus()

        observeNameStatus()

        observeSurnameStatus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.setOnClickListener{
            val login: String = login_editText.text.toString()

            viewModel?.validateLogin(login)
        }

        password_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password: String = password_editText.text.toString()

                viewModel?.validatePassword(password,true)
            }
        })
        rePassword_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val rePassword: String = rePassword_editText.text.toString()

                viewModel?.validatePassword(rePassword, false)
            }
        })
        email_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email: String = email_editText.text.toString()

                viewModel?.validateEmail(email)
            }
        })
        name_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name: String = name_editText.text.toString()

                viewModel?.isEmptyValidation(name, true)
            }
        })
        surname_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val surname: String = surname_editText.text.toString()

                viewModel?.isEmptyValidation(surname, false)
            }
        })
        login_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val login: String = login_editText.text.toString()

                viewModel?.validateLogin(login)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun observeEmailStatus(){
        val emailObserver = Observer<RegistrationViewModel.EmailStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.EmailStatus.OK-> {
                    email_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.EmailStatus.NOAT -> {
                    email_wrapper.error = getString(R.string.emailNoAt)
                }
                RegistrationViewModel.EmailStatus.NODOT ->{
                    email_wrapper.error = getString(R.string.emailNoDot)
                }
                RegistrationViewModel.EmailStatus.WRONGEMAIL ->{
                    email_wrapper.error = getString(R.string.emailWrong)
                }
            }
        }

        viewModel?.emailStatus?.observe(this, emailObserver)
    }

    private fun observePasswordStatus() {
        val passwordObserver = Observer<RegistrationViewModel.PasswordStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.PasswordStatus.OK -> {
                    password_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.PasswordStatus.LENGTH ->{
                    password_wrapper.error = getString(R.string.passwordLength)
                }
                RegistrationViewModel.PasswordStatus.SPECIALCASE ->{
                    password_wrapper.error = getString(R.string.passwordSpecialCase)
                }
                RegistrationViewModel.PasswordStatus.DIGITCASE ->{
                    password_wrapper.error = getString(R.string.passwordDigitCase)
                }
                RegistrationViewModel.PasswordStatus.UPPERCASE ->{
                    password_wrapper.error = getString(R.string.passwordUpperCase)
                }
            }
        }

        viewModel?.passwordStatus?.observe(this, passwordObserver)
    }

    private fun observeRePasswordStatus() {
        val rePasswordObserver = Observer<RegistrationViewModel.PasswordStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.PasswordStatus.OK -> {
                    rePassword_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.PasswordStatus.LENGTH ->{
                    rePassword_wrapper.error = getString(R.string.passwordLength)
                }
                RegistrationViewModel.PasswordStatus.SPECIALCASE ->{
                    rePassword_wrapper.error = getString(R.string.passwordSpecialCase)
                }
                RegistrationViewModel.PasswordStatus.DIGITCASE ->{
                    rePassword_wrapper.error = getString(R.string.passwordDigitCase)
                }
                RegistrationViewModel.PasswordStatus.UPPERCASE ->{
                    rePassword_wrapper.error = getString(R.string.passwordUpperCase)
                }
            }
        }

        viewModel?.rePasswordStatus?.observe(this, rePasswordObserver)
    }

    private fun observeSurnameStatus() {
        val surnameObserver = Observer<RegistrationViewModel.FullNameStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.FullNameStatus.OK -> {
                    surname_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.FullNameStatus.EMPTY ->{
                    surname_wrapper.error = getString(R.string.noEmptyField)
                }
            }
        }

        viewModel?.surnameStatus?.observe(this, surnameObserver)
    }

    private fun observeNameStatus() {
        val nameObserver = Observer<RegistrationViewModel.FullNameStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.FullNameStatus.OK -> {
                    name_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.FullNameStatus.EMPTY ->{
                    name_wrapper.error = getString(R.string.noEmptyField)
                }
            }
        }

        viewModel?.nameStatus?.observe(this, nameObserver)
    }

    private fun observeLoginStatus() {
        val loginObserver = Observer<RegistrationViewModel.LoginStatus> { newStatus ->
            when(newStatus){
                RegistrationViewModel.LoginStatus.OK -> {
                    login_wrapper.isErrorEnabled = false
                }
                RegistrationViewModel.LoginStatus.EMPTY ->{
                    login_wrapper.error = getString(R.string.noEmptyField)
                }
            }
        }

        viewModel?.loginStatus?.observe(this, loginObserver)
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

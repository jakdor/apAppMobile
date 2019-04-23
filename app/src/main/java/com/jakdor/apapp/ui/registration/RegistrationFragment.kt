package com.jakdor.apapp.ui.registration

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import kotlinx.android.synthetic.main.registration.*
import javax.inject.Inject


class RegistrationFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: RegistrationViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.hide()
        }
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

        observeRegisterRequestStatus()
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
            val email: String = email_editText.text.toString()
            val password: String = password_editText.text.toString()
            val rePassword: String = rePassword_editText.text.toString()
            val name: String = name_editText.text.toString()
            val surname: String = surname_editText.text.toString()

            if(viewModel?.checkPasswords(password, rePassword) == true){
                hideKeyboard(activity as MainActivity)
                viewModel?.registerRequest(login, email, password, name, surname)
            }
        }

        password_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password: String = password_editText.text.toString()
                viewModel?.validatePassword(password,true)
            }
        })

        rePassword_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val rePassword: String = rePassword_editText.text.toString()
                viewModel?.validatePassword(rePassword, false)
            }
        })

        email_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email: String = email_editText.text.toString()
                viewModel?.validateEmail(email)
            }
        })

        name_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name: String = name_editText.text.toString()
                viewModel?.isEmptyValidation(name, true)
            }
        })

        surname_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
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

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeRegisterRequestStatus() {
        viewModel?.registerRequest?.observe(this, Observer {
            handleNewRegisterStatus(it)
        })
    }

    private fun observeEmailStatus(){
        viewModel?.emailStatus?.observe(this, Observer {
            handleNewEmailStatus(it)
        })
    }

    private fun observePasswordStatus() {
        viewModel?.passwordStatus?.observe(this, Observer {
            handleNewPasswordStatus(it, password_wrapper)
        })
    }

    private fun observeRePasswordStatus() {
        viewModel?.rePasswordStatus?.observe(this, Observer {
            handleNewPasswordStatus(it, rePassword_wrapper)
        })
    }

    private fun observeSurnameStatus() {
        viewModel?.surnameStatus?.observe(this, Observer {
            handleNewNameStatus(it, surname_wrapper)
        })
    }

    private fun observeNameStatus() {
        viewModel?.nameStatus?.observe(this, Observer {
            handleNewNameStatus(it, name_wrapper)
        })
    }

    private fun observeLoginStatus() {
        viewModel?.loginStatus?.observe(this, Observer {
            handleNewLoginStatus(it)
        })
    }

    fun handleNewRegisterStatus(status: RegistrationViewModel.RegisterRequestStatus){
        when(status){
            RegistrationViewModel.RegisterRequestStatus.ERROR -> {
                Toast.makeText(context, getString(R.string.registerError), Toast.LENGTH_SHORT).show()
            }
            RegistrationViewModel.RegisterRequestStatus.OK -> {
                Toast.makeText(context, getString(R.string.registerSuccess), Toast.LENGTH_SHORT).show()
                (activity as MainActivity).switchToLoginFragment()
            }
            RegistrationViewModel.RegisterRequestStatus.EDIT -> {}
        }
    }

    fun handleNewEmailStatus(status: RegistrationViewModel.EmailStatus){
        when(status){
            RegistrationViewModel.EmailStatus.OK-> email_wrapper.isErrorEnabled = false
            RegistrationViewModel.EmailStatus.NOAT -> email_wrapper.error = getString(R.string.emailNoAt)
            RegistrationViewModel.EmailStatus.NODOT -> email_wrapper.error = getString(R.string.emailNoDot)
            RegistrationViewModel.EmailStatus.WRONGEMAIL -> email_wrapper.error = getString(R.string.emailWrong)
            RegistrationViewModel.EmailStatus.TAKEN -> email_wrapper.error = getString(R.string.emailTaken)
        }
    }

    fun handleNewPasswordStatus(status: RegistrationViewModel.PasswordStatus, input: TextInputLayout){
        when(status){
            RegistrationViewModel.PasswordStatus.OK -> input.isErrorEnabled = false
            RegistrationViewModel.PasswordStatus.LENGTH -> input.error = getString(R.string.passwordLength)
            RegistrationViewModel.PasswordStatus.DIGITCASE -> input.error = getString(R.string.passwordDigitCase)
            RegistrationViewModel.PasswordStatus.UPPERCASE -> input.error = getString(R.string.passwordUpperCase)
            RegistrationViewModel.PasswordStatus.CORRECT -> input.error = getString(R.string.passwordsNoMatch)
        }
    }

    fun handleNewNameStatus(status: RegistrationViewModel.FullNameStatus, input: TextInputLayout){
        when(status){
            RegistrationViewModel.FullNameStatus.OK -> input.isErrorEnabled = false
            RegistrationViewModel.FullNameStatus.EMPTY -> input.error = getString(R.string.noEmptyField)
        }
    }

    fun handleNewLoginStatus(status: RegistrationViewModel.LoginStatus){
        when(status){
            RegistrationViewModel.LoginStatus.OK -> login_wrapper.isErrorEnabled = false
            RegistrationViewModel.LoginStatus.EMPTY -> login_wrapper.error = getString(R.string.noEmptyField)
            RegistrationViewModel.LoginStatus.TAKEN -> login_wrapper.error = getString(R.string.loginTaken)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, HIDE_NOT_ALWAYS)
        }
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

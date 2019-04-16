package com.jakdor.apapp.ui.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.appcompat.app.AppCompatActivity

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
            val login: String = apartment_number_editText.text.toString()
            val email: String = apartment_street_editText.text.toString()
            val password: String = password_editText.text.toString()
            val rePassword: String = rePassword_editText.text.toString()
            val name: String = apartment_name_editText.text.toString()
            val surname: String = apartment_city_editText.text.toString()

            if(viewModel?.checkPasswords(password, rePassword) == true){
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

        apartment_street_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email: String = apartment_street_editText.text.toString()
                viewModel?.validateEmail(email)
            }
        })

        apartment_name_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name: String = apartment_name_editText.text.toString()
                viewModel?.isEmptyValidation(name, true)
            }
        })

        apartment_city_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val surname: String = apartment_city_editText.text.toString()
                viewModel?.isEmptyValidation(surname, false)
            }
        })

        apartment_number_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val login: String = apartment_number_editText.text.toString()
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
            handleNewNameStatus(it, apartment_city_wrapper)
        })
    }

    private fun observeNameStatus() {
        viewModel?.nameStatus?.observe(this, Observer {
            handleNewNameStatus(it, apartment_name_wrapper)
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
            RegistrationViewModel.EmailStatus.OK-> apartment_street_wrapper.isErrorEnabled = false
            RegistrationViewModel.EmailStatus.NOAT -> apartment_street_wrapper.error = getString(R.string.emailNoAt)
            RegistrationViewModel.EmailStatus.NODOT -> apartment_street_wrapper.error = getString(R.string.emailNoDot)
            RegistrationViewModel.EmailStatus.WRONGEMAIL -> apartment_street_wrapper.error = getString(R.string.emailWrong)
            RegistrationViewModel.EmailStatus.TAKEN -> apartment_street_wrapper.error = getString(R.string.emailTaken)
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
            RegistrationViewModel.LoginStatus.OK -> apartment_number_wrapper.isErrorEnabled = false
            RegistrationViewModel.LoginStatus.EMPTY -> apartment_number_wrapper.error = getString(R.string.noEmptyField)
            RegistrationViewModel.LoginStatus.TAKEN -> apartment_number_wrapper.error = getString(R.string.loginTaken)
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

package com.jakdor.apapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject

class LoginFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: LoginViewModel? = null

    private var initSubs = false
    private var isLoginUnlocked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.hide()
        }
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel?.checkLoginFilled(loginEditText.text.toString())
            }
        })

        editPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel?.checkPasswordFilled(editPassword.text.toString())
            }
        })

        loginButton.setOnClickListener {
            if(isLoginUnlocked) viewModel?.login(loginEditText.text.toString(), editPassword.text.toString())
        }

        registerButton.setOnClickListener {
            (activity as MainActivity).switchToRegistrationFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        if(!initSubs) {
            viewModel?.loginPossibility?.observe(this, Observer {
                handleNewLoginPossibility(it)
            })

            viewModel?.loginRequestStatus?.observe(this, Observer {
                handleNewLoginRequestStatus(it)
            })

            initSubs = true
        }
    }

    fun handleNewLoginPossibility(status: Boolean){
        isLoginUnlocked = status
        loginButton.isEnabled = status
    }

    fun handleNewLoginRequestStatus(status: LoginViewModel.LoginRequestStatus) {
        when (status) {
            LoginViewModel.LoginRequestStatus.Idle -> {
            }
            LoginViewModel.LoginRequestStatus.Pending -> {
                loginButton.visibility = View.GONE
                registerButton.visibility = View.GONE
                login_progress.visibility = View.VISIBLE
                login_progress.progress = 0
            }
            LoginViewModel.LoginRequestStatus.Success -> {
                Toast.makeText(activity, getString(R.string.singed_in), Toast.LENGTH_LONG).show()
                (activity as MainActivity).switchToApartmentListFragment()
            }
            LoginViewModel.LoginRequestStatus.BadCardinals -> {
                Toast.makeText(activity, getString(R.string.invalid_login_toast), Toast.LENGTH_LONG).show()
                loginButton.visibility = View.VISIBLE
                registerButton.visibility = View.VISIBLE
                login_progress.visibility = View.GONE
            }
            LoginViewModel.LoginRequestStatus.Error -> {
                Toast.makeText(activity, getString(R.string.login_error_toast), Toast.LENGTH_LONG).show()
                loginButton.visibility = View.VISIBLE
                registerButton.visibility = View.VISIBLE
                login_progress.visibility = View.GONE
            }
        }
    }

    companion object {
        const val CLASS_TAG = "LoginFragment"

        fun getInstance(): LoginFragment {
            val bundle = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
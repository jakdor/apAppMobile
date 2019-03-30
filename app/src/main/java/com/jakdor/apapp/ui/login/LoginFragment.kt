package com.jakdor.apapp.ui.login


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
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
import android.widget.Toast


class LoginFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: LoginViewModel? = null

    var isLoginFilled: Boolean = false
    var isPasswordFilled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isLoginFilled = !viewModel!!.isEditTextEmpty(loginEditText)

                loginButton.isEnabled = isLoginFilled && isPasswordFilled
            }
        })

        editPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isPasswordFilled = !viewModel!!.isEditTextEmpty(editPassword)

                loginButton.isEnabled = isLoginFilled && isPasswordFilled
            }
        })

        loginButton.setOnClickListener { Toast.makeText(activity, "dupa123", Toast.LENGTH_LONG).show() }

        registerButton.setOnClickListener { Toast.makeText(activity, "dupa123", Toast.LENGTH_LONG).show() }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)


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
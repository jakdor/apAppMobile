package com.jakdor.apapp.ui.registration

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

class RegistrationFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: RegistrationViewModel? = null
    var isPasswordCorrect: Boolean = false
    var isRePasswordCorrect: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.isEnabled = false

        password_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(isPasswordCorrect && isRePasswordCorrect){
                    register_button.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val password: String = password_editText.text.toString()

                if(viewModel!!.validatePassword(password, password) != null){

                    password_editText.error = viewModel!!.validatePassword(password, password)
                }else{
                    password_editText.error=null
                    isPasswordCorrect = true;
                }

            }
        })
        rePassword_editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(isPasswordCorrect && isRePasswordCorrect){
                    register_button.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val rePassword: String = rePassword_editText.text.toString()
                val password: String = password_editText.text.toString()

                if(viewModel!!.validatePassword(rePassword, password) != null){

                    rePassword_editText.error = viewModel!!.validatePassword(rePassword, password)
                }else{
                    rePassword_editText.error=null
                    if(password_editText.text.toString() == rePassword_editText.text.toString()){
                        isRePasswordCorrect = true
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

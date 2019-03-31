package com.jakdor.apapp.ui.registration

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.R
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import java.util.regex.Pattern
import javax.inject.Inject

class RegistrationViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
    BaseViewModel(application, rxSchedulersFacade){

    val registerPossibility = MutableLiveData<Boolean>().apply { value = false }

    private var isPasswordCorrect: Boolean = false
    private var isRePasswordCorrect: Boolean = false
    private var isEmailCorrect: Boolean = false
    private var isLoginCorrect: Boolean = true
    private var isNameNotEmpty: Boolean = false
    private var isSurnameNotEmpty: Boolean = false

    enum class PasswordStatus{
        OK, UPPERCASE, DIGITCASE, LENGTH, CORRECT
    }

    val passwordStatus = MutableLiveData<PasswordStatus>().apply { value = PasswordStatus.OK }
    val rePasswordStatus = MutableLiveData<PasswordStatus>().apply { value = PasswordStatus.OK }

    enum class EmailStatus{
        OK, NODOT, NOAT, WRONGEMAIL
    }

    val emailStatus = MutableLiveData<EmailStatus>().apply { value = EmailStatus.OK }

    enum class LoginStatus{
        OK, EMPTY
    }

    val loginStatus = MutableLiveData<LoginStatus>().apply { value = LoginStatus.OK }

    enum class FullNameStatus{
        OK, EMPTY
    }

    val nameStatus = MutableLiveData<FullNameStatus>().apply { value = FullNameStatus.OK }
    val surnameStatus = MutableLiveData<FullNameStatus>().apply { value = FullNameStatus.OK }

    fun validatePassword(password: String, isPassword: Boolean) {

        val upperCase: Pattern = Pattern.compile("[A-Z]")
        val digitCase: Pattern = Pattern.compile("[0-9]")

        if(isPassword){
            isPasswordCorrect= false
        }else {
            isRePasswordCorrect = false
        }

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)

        if(!upperCase.matcher(password).find()){
            if(isPassword){
                passwordStatus.postValue(PasswordStatus.UPPERCASE)
            } else{
                rePasswordStatus.postValue(PasswordStatus.UPPERCASE)
            }
            return
        }
        if(!digitCase.matcher(password).find()){
            if(isPassword){
                passwordStatus.postValue(PasswordStatus.DIGITCASE)
            } else{
                rePasswordStatus.postValue(PasswordStatus.DIGITCASE)
            }
            return
        }
        if(password.length < 8){
            if(isPassword){
                passwordStatus.postValue(PasswordStatus.LENGTH)
            } else{
                rePasswordStatus.postValue(PasswordStatus.LENGTH)
            }
            return
        }

        if(isPassword){
            isPasswordCorrect= true
            passwordStatus.postValue(PasswordStatus.OK)
        }else {
            isRePasswordCorrect = true
            rePasswordStatus.postValue(PasswordStatus.OK)
        }

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)
    }

    fun validateEmail(email: String) {

        val emailPattern: Pattern = Pattern.compile("(?:[^@]+)(?:\\@)(?:[^@]+)")
        val emailPattern2: Pattern = Pattern.compile("(?:.+)(?:\\.)(?:[^\\.\\@]+)")

        val findAt: Pattern = Pattern.compile("(?:\\@)")
        val findDot: Pattern = Pattern.compile("(?:\\.)")

        isEmailCorrect = false

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)

        if(!emailPattern.matcher(email).find()){
            if(!findAt.matcher(email).find()) {
                emailStatus.postValue(EmailStatus.NOAT)
            }else{
                emailStatus.postValue(EmailStatus.WRONGEMAIL)
            }
            return
        }

        if(!emailPattern2.matcher(email).find()){
            if(!findDot.matcher(email).find()) {
                emailStatus.postValue(EmailStatus.NODOT)
            }else{
                emailStatus.postValue(EmailStatus.WRONGEMAIL)
            }
            return
        }

        isEmailCorrect = true
        emailStatus.postValue(EmailStatus.OK)

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)
    }

    fun isEmptyValidation(textToValidate: String, isName: Boolean) {

        if(isName){
            isNameNotEmpty= false
        }else {
            isSurnameNotEmpty = false
        }

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)

        if(textToValidate.trim().isEmpty()){
            if(isName){
                nameStatus.postValue(FullNameStatus.EMPTY)
            }else {
                surnameStatus.postValue(FullNameStatus.EMPTY)
            }
            return
        }

        if(isName){
            isNameNotEmpty= true
            nameStatus.postValue(FullNameStatus.OK)
        }else {
            isSurnameNotEmpty = true
            surnameStatus.postValue(FullNameStatus.OK)
        }

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)
    }

    fun validateLogin(login: String) {

        //TODO: sprawdzenie czy w bazie istnieje juz login

        isLoginCorrect = false

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)

        if(login.trim().isEmpty()){
            loginStatus.postValue(LoginStatus.EMPTY)
            return
        }

        isLoginCorrect = true
        loginStatus.postValue(LoginStatus.OK)

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)
    }

    fun checkPasswords(password: String, rePassword: String){

        if(password != rePassword){
            isRePasswordCorrect = false
            rePasswordStatus.postValue(PasswordStatus.CORRECT)
        }else{
            isRePasswordCorrect = true
            rePasswordStatus.postValue(PasswordStatus.OK)
        }

        registerPossibility.postValue(isEmailCorrect && isPasswordCorrect && isRePasswordCorrect
                && isLoginCorrect && isNameNotEmpty && isSurnameNotEmpty)
    }
}

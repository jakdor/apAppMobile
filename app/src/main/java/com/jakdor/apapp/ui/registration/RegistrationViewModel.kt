package com.jakdor.apapp.ui.registration

import android.app.Application
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import java.util.regex.Pattern
import javax.inject.Inject

class RegistrationViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
    BaseViewModel(application, rxSchedulersFacade){

    fun validatePassword(password: String, rePassword: String): String? {

        val upperCase: Pattern = Pattern.compile("[A-Z]")
        val digitCase: Pattern = Pattern.compile("[0-9]")
        val specialCase: Pattern = Pattern.compile("[\$&+,:;=?@#|'<>.^*()%!-]")

        if(!upperCase.matcher(password).find()){
            return "Hasło musi zawierać conajmniej jedną dużą literę"
        }
        if(!digitCase.matcher(password).find()){
            return "Hasło musi zawierać conajmniej jedną cyfrę"
        }
        if(!specialCase.matcher(password).find()){
            return "Hasło musi zawierać conajmniej jeden znak specjalny"
        }
        if(password.length < 8){
            return "Hasło musi zawierać conajmniej 8 znaków"
        }
        if(password != rePassword){
            return "Podane hasła się nie zgadzają"
        }
        return null
    }

    fun validateEmail(email: String): String? {

        val emailPattern: Pattern = Pattern.compile("(?:[^@]+)(?:\\@)(?:[^@]+)")
        val emailPattern2: Pattern = Pattern.compile("(?:.+)(?:\\.)(?:[^\\.\\@]+)")

        val findAt: Pattern = Pattern.compile("(?:\\@)")
        val findDot: Pattern = Pattern.compile("(?:\\.)")

        if(!emailPattern.matcher(email).find()){
            if(!findAt.matcher(email).find()) {
                return "Brak @ w adresie email"
            }else{
                return "Błędny adres email"
            }
        }

        if(!emailPattern2.matcher(email).find()){
            if(!findDot.matcher(email).find()) {
                return "Brak . w adresie email"
            }else{
                return "Błędny adres email"
            }
        }

        return null
    }

    fun isEmptyValidation(textToValidate: String): String? {

        if(textToValidate.isEmpty()){
            return "To pole nie może być puste"
        }

        return null
    }

    fun validateLogin(login: String): String? {

        //TODO: sprawdzenie czy w bazie istnieje juz login

        return null
    }
}

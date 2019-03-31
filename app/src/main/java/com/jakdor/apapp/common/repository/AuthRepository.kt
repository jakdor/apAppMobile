package com.jakdor.apapp.common.repository

import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.RetrofitFactory
import javax.inject.Inject

class AuthRepository
@Inject constructor(private val retrofitFactory: RetrofitFactory){

    private val apiService: BackendService =
        retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private var bearerToken : String = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkU1MjM1QTUxQjg1MDIxMkMzN0VDQjMyODc3QkY5MzhDODc5Q0JBQUQiLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiJjZjgzMDMxNS02YWFlLTQwYjAtOWM3OC0zNTFiODdjYTM0MGMiLCJuYW1lIjoiNTZ5QHoueiIsInRva2VuX3VzYWdlIjoiYWNjZXNzX3Rva2VuIiwianRpIjoiNTQ0MGZhNTAtNGVlYy00Y2E3LTgwNjUtNjkyNDljZjg1YjNlIiwic2NvcGUiOiJvZmZsaW5lX2FjY2VzcyIsImF1ZCI6IlJlc291cmNlU2VydmVyIiwibmJmIjoxNTU0MDcyOTk4LCJleHAiOjE1NTQwNzY1OTgsImlhdCI6MTU1NDA3Mjk5OCwiaXNzIjoiaHR0cDovL2F1dGhzZXJ2ZXIvIn0.unG7z63Muzv-yEUnADkmaKyHvddPh21ucXjbcxlyg9M_-i0Hnr4SOIf7oWutAf53-7dFMo_Gkzc5H0d47VuzVyUDZZLxKQ3I7z6DBg-VSPGEVk6nk9sA8-xGc1ioSZimLITzQUy0byJ8v5UCgXxY-nj0WEtfuuEt3R2fwv1fsI2QMsefukk6iwwdezVgoKgA_DGCvUB_j5DyGPegQFbkZ2jbsPznKdZCY73HIv2qZxWrH-FSuBlBniwldyOvpaPTM2hCBpl7ovipMqDGAda3JkQ7uUysEyOPQQ7ALi9Hvm3Bp2adyxgH_Zn1_Voz40MvVzgWHJyRY1g43t5kao9duQ"

    fun getBearerToken(): String{
        return bearerToken
    }
}
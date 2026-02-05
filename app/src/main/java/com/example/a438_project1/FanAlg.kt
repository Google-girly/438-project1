package com.example.a438_project1

import android.util.Log


sealed class FanQuizResponse{
    data class Success(val result: String): FanQuizResponse()
    data class Error(val message: String): FanQuizResponse()

}
object FanDetermine{
    private const val TAG = "FanDetermine"

    fun getFanResult(score: Int): FanQuizResponse{
        if (score !in 0..10) {
            Log.e(TAG, "Invalid score: $score")
            return FanQuizResponse.Error("Invalid score: $score")
        }
       val result = when (score){
            10 -> "Perfect Score! You are a Fan!"
            in 8..9 -> "You have their songs in your playlist!"
            7 -> "Alright ball knowledge"
            else -> "Not a Fan! Keep listening to their music Buddy"

        }

        return FanQuizResponse.Success(result)

    }

}
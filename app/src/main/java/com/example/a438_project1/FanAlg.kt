package com.example.a438_project1

object FanDetermine{

    fun fanAlg(score: Int): String{
        return when (score){
            10 -> "Perfect Score! You are a Fan!"
            8,9 -> "You have their songs in your playlist!"
            7 -> "Alright ball knowledge"
            else -> "Not a Fan! Keep listening to their music Buddy"


        }
    }

}
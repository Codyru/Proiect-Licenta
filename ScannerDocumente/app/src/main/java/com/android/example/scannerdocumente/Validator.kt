package com.android.example.scannerdocumente

class Validator {

    fun validateCNP(CNP:String):Boolean{
        if (CNP.length != 13 || !CNP.matches("[0-9]+".toRegex())) {
            return false
        }

        val controlDigit = CNP[12].toString().toIntOrNull()
        if (controlDigit == null) {
            return false
        }

        val coefficients = arrayOf(2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9)
        var sum = 0
        for (i in 0 until 12) {
            val digit = CNP[i].toString().toIntOrNull()
            if (digit == null) {
                return false
            }
            sum += digit * coefficients[i]
        }

        val remainder = sum % 11
        val controlDigitComputed = if (remainder == 10) 1 else remainder

        return controlDigit == controlDigitComputed
    }

    fun checkNationalitate(input: String): Boolean {
        for (enumValue in Nationalitate.values()) {
            if (enumValue.name == input) {
                return true
            }
        }
        return false
    }

}
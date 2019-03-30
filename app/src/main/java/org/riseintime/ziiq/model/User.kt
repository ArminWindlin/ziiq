package org.riseintime.ziiq.model

data class User(val name: String,
                val bio: String,
                val reports: Int){
    constructor(): this("","", 0)
}
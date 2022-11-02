package com.man.story.domain.request

/**
 *
 * Created by Lukmanul Hakim on  29/09/22
 * devs.lukman@gmail.com
 */
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
package com.man.story.data.response.login

import com.google.gson.annotations.SerializedName
import com.man.story.core.base.BaseResponse
import com.man.story.domain.model.login.LoginModel

data class LoginResponseDTO(
    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null
) : BaseResponse() {

    companion object {
        fun LoginResponseDTO.toDomain() = LoginModel(
            userId = loginResult?.userId.orEmpty(),
            name = loginResult?.name.orEmpty(),
            token = loginResult?.token.orEmpty()
        )
    }

}

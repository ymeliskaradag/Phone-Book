package com.meliskaradag.telefonrehberiuygulamasi.data.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

//Tüm cevaplarda gelen ortak
data class ApiResponse<T>(
    val success: Boolean,
    val messages: List<String>?,
    val data: T?,
    val status: Int
)

//Create/Update istek gövdesi
data class UserCreateUpdateBody(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null
)

//Tek kullanıcı cevabı için
data class UserDto(
    val id: String,
    val createdAt: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String?
)

//Liste cevabı: data.users
data class GetAllResponse(
    val users: List<UserDto>
)

//UploadImage cevabı: data.imageUrl
data class UploadImageResponse(
    val imageUrl: String?
)

interface ApiService {

    //CREATE  -> POST /api/User
    @POST("api/User")
    suspend fun createUser(
        @Body body: UserCreateUpdateBody
    ): Response<ApiResponse<UserDto>>

    //READ-ALL -> GET /api/User/GetAll  (response: data.users)
    @GET("api/User/GetAll")
    suspend fun getAllUsers(): Response<ApiResponse<GetAllResponse>>

    //READ-ONE -> GET /api/User/{id}
    @GET("api/User/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<ApiResponse<UserDto>>

    //UPDATE -> PUT /api/User/{id}
    @PUT("api/User/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body body: UserCreateUpdateBody
    ): Response<ApiResponse<UserDto>>

    //DELETE -> DELETE /api/User/{id}
    //Swagger data: {} döndürüyor, o yüzden JsonObject kullanıyoruz.
    @DELETE("api/User/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): Response<ApiResponse<JsonObject>>

    //UPLOAD IMAGE -> POST /api/User/UploadImage  (multipart, field adı: image)
    @Multipart
    @POST("api/User/UploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<UploadImageResponse>>
}

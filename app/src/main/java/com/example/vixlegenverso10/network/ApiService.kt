package com.example.vixlegenverso10.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// ============================================================================
// MODELOS DE DADOS PARA A API MYSQL
// ============================================================================

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("cpf") val cpf: String
)

data class LoginResponse(
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("primeiroNome") val primeiroNome: String,
    @SerializedName("ultimoNome") val ultimoNome: String,
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String?
)

data class ProcessoJuridico(
    @SerializedName("numeroProcesso") val numeroProcesso: String,
    @SerializedName("vara") val vara: String?,
    @SerializedName("comarca") val comarca: String?,
    @SerializedName("tribunal") val tribunal: String?,
    @SerializedName("instancia") val instancia: String?,
    @SerializedName("segredoJustica") val segredoJustica: Boolean?,
    @SerializedName("clienteNome") val clienteNome: String?,
    @SerializedName("status") val status: String?
)

data class MovimentacaoProcessual(
    @SerializedName("idMovimentacao") val idMovimentacao: Int,
    @SerializedName("data") val data: String,
    @SerializedName("descricao") val descricao: String
)

// ============================================================================
// INTERFACE DE ROTAS RETROFIT
// ============================================================================

interface ApiService {

    @POST("usuarios/autenticar")
    suspend fun autenticarUsuario(@Body loginRequest: LoginRequest): LoginResponse

    @GET("processos")
    suspend fun getProcessos(): List<ProcessoJuridico>

    @GET("processos/buscar")
    suspend fun buscarProcessos(@Query("query") query: String): List<ProcessoJuridico>

    @GET("processos/{numeroProcesso}/movimentacoes")
    suspend fun getMovimentacoes(@Path("numeroProcesso") numeroProcesso: String): List<MovimentacaoProcessual>
}

// ============================================================================
// CLIENTE DE CONEXÃO
// ============================================================================

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
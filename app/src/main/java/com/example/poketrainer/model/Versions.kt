package com.example.poketrainer.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class Versions(
    val generationI: GenerationI,
    val generationIi: GenerationIi,
    val generationIii: GenerationIii,
    val generationIv: GenerationIv,
    @SerializedName("generation-v")
    val generationV: GenerationV,
    val generationVi: GenerationVi,
    val generationVii: GenerationVii,
    val generationViii: GenerationViii
)
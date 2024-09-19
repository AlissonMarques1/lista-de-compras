package com.example.teladecadastro.models

data class CompraItem(
    var nome: String,
    var quantidade: Int,
    val id: String = "" // ID será atribuído pelo Firestore
)

package com.example.teladecadastro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seuapp.ListaComprasFragment
import com.example.teladecadastro.databinding.ActivityTelaPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btListaCompras.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ListaComprasFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val voltarTelaLogin = Intent(this, TelaLogin::class.java)
            startActivity(voltarTelaLogin)
            finish()

        }

        binding.btGravarDadosDB.setOnClickListener {
            val usuarioAtual = FirebaseAuth.getInstance().currentUser
            val nome = binding.editNome.text.toString()
            val sobrenome = binding.editSobrenome.text.toString()
            val idade = binding.editIdade.text.toString()
            val db = FirebaseFirestore.getInstance()

            // Verificar se os campos não estão vazios
            if (nome.isNotEmpty() && sobrenome.isNotEmpty() && idade.isNotEmpty()) {
                val dadosUsuario = hashMapOf(
                    "nome" to nome,
                    "sobrenome" to sobrenome,
                    "idade" to idade
                )

                db.collection("usuarios")
                    .document(usuarioAtual?.uid ?: "")
                    .set(dadosUsuario)
                    .addOnSuccessListener {
                        // Limpar os campos após o sucesso
                        binding.editNome.setText("")
                        binding.editSobrenome.setText("")
                        binding.editIdade.setText("")
                        Toast.makeText(this, "Dados gravados com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao gravar os dados: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Função para ler dados do usuário
        fun lerDadosUsuario() {
            val usuarioAtual = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()

            db.collection("usuarios").document(usuarioAtual?.uid ?: "")
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome")
                        val sobrenome = document.getString("sobrenome")
                        val idade = document.getString("idade")
                        // Exibir os dados lidos no TextView
                        binding.tvDadosLidos.text = "Nome: $nome\nSobrenome: $sobrenome\nIdade: $idade"
                    } else {
                        binding.tvDadosLidos.text = "Documento não encontrado"
                    }
                }
                .addOnFailureListener { e ->
                    binding.tvDadosLidos.text = "Erro ao recuperar documento: ${e.message}"
                }
        }

        // Função para excluir um documento
        fun excluirUsuario() {
            val usuarioAtual = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()

            db.collection("usuarios").document(usuarioAtual?.uid ?: "")
                .delete()
                .addOnSuccessListener {
                    Log.d("Firestore", "Documento excluído com sucesso!")
                    Toast.makeText(this, "Usuário excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.tvDadosLidos.text = "Nenhum dado lido" // Limpa a exibição
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Erro ao excluir documento", e)
                    Toast.makeText(this, "Erro ao excluir usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

// Exemplo de como chamar essas funções
        binding.btLerDados.setOnClickListener {
            lerDadosUsuario()
        }

        binding.btExcluirUsuario.setOnClickListener {
            excluirUsuario()
        }
    }
}
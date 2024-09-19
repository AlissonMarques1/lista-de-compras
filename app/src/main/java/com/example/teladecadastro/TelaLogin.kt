package com.example.teladecadastro

// Importa as bibliotecas necessárias para o funcionamento da Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teladecadastro.databinding.ActivityTelaLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// Define a classe 'TelaLogin' que herda de 'AppCompatActivity'
class TelaLogin : AppCompatActivity() {

    // Declara uma variável para o binding da Activity
    private lateinit var binding: ActivityTelaLoginBinding
    // Instancia o FirebaseAuth para autenticação
    private val auth = FirebaseAuth.getInstance()

    // Método que é chamado quando a Activity é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o binding usando o layout da Activity
        binding = ActivityTelaLoginBinding.inflate(layoutInflater)
        // Define o conteúdo da tela para o layout vinculado
        setContentView(binding.root)

        // Define uma ação ao clicar no botão de login
        binding.btEntrar.setOnClickListener { view ->

            // Obtém o texto dos campos de email e senha
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            // Verifica se os campos de email e senha estão vazios
            if (email.isEmpty() || senha.isEmpty()) {
                // Exibe uma mensagem Snackbar informando que todos os campos devem ser preenchidos
                val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
//                snackbar.setBackgroundTint(color.red) // Define a cor do fundo do Snackbar (comentado)
                snackbar.show() // Exibe o Snackbar
            } else {
                // Tenta fazer login com email e senha usando Firebase Authentication
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        // Se o login for bem-sucedido
                        if (autenticacao.isSuccessful) {
                            // Navega para a tela principal
                            navegarTelaPrincipal()
                        }
                    }.addOnFailureListener {
                        // Se o login falhar, exibe uma mensagem de erro
                        val snackbar = Snackbar.make(view, "Erro ao fazer o login do usuário!", Snackbar.LENGTH_SHORT)
//                        snackbar.setBackgroundTint(color.red) // Define a cor do fundo do Snackbar (comentado)
                        snackbar.show() // Exibe o Snackbar
                    }
            }
        }

        // Define uma ação ao clicar no texto de cadastro, levando o usuário à tela de cadastro
        binding.txtCadastrar.setOnClickListener {
            val intent = Intent(this, TelaCadastro::class.java) // Cria uma intenção para abrir a TelaCadastro
            startActivity(intent) // Inicia a Activity de cadastro
        }
    }

    // Função para navegar para a tela principal
    private fun navegarTelaPrincipal() {
        val intent = Intent(this, TelaPrincipal::class.java) // Cria uma intenção para abrir a TelaPrincipal
        startActivity(intent) // Inicia a Activity de tela principal
        finish() // Finaliza a tela de login para que o usuário não possa voltar a ela
    }

    // Método chamado quando a Activity é iniciada
    override fun onStart() {
        super.onStart()
        // Verifica se já existe um usuário autenticado
        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        // Se o usuário estiver logado, navega automaticamente para a tela principal
        if (usuarioAtual != null) {
            navegarTelaPrincipal()
        }
    }
}

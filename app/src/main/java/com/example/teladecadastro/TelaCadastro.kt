package com.example.teladecadastro

// Importa as bibliotecas necessárias para o funcionamento da Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teladecadastro.databinding.ActivityTelaCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

// Define a classe 'TelaCadastro' que herda de 'AppCompatActivity'
class TelaCadastro : AppCompatActivity() {

    // Declara uma variável para o binding da Activity
    private lateinit var binding: ActivityTelaCadastroBinding
    // Instancia o FirebaseAuth para autenticação
    private val auth = FirebaseAuth.getInstance()

    // Método que é chamado quando a Activity é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o binding usando o layout da Activity
        binding = ActivityTelaCadastroBinding.inflate(layoutInflater)
        // Define o conteúdo da tela para o layout vinculado
        setContentView(binding.root)

        // Define uma ação ao clicar no botão de cadastro
        binding.btCadastrar.setOnClickListener { view ->

            // Obtém o texto dos campos de email e senha
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            // Verifica se os campos de email e senha estão vazios
            if (email.isEmpty() || senha.isEmpty()) {
                // Cria uma mensagem Snackbar informando que todos os campos devem ser preenchidos
                val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
//                snackbar.setBackgroundTint(color.red) // Define a cor do fundo do Snackbar (comentado)
                snackbar.show() // Exibe o Snackbar
            } else {
                // Tenta criar um usuário com email e senha usando Firebase Authentication
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
                    // Se o cadastro for bem-sucedido
                    if (cadastro.isSuccessful) {
                        // Exibe uma mensagem de sucesso ao cadastrar o usuário
                        val snackbar = Snackbar.make(view, "Sucesso ao cadastrar usuário!", Snackbar.LENGTH_SHORT)
//                        snackbar.setBackgroundTint(color.green) // Define a cor do fundo do Snackbar (comentado)
                        snackbar.show() // Exibe o Snackbar
                        // Limpa os campos de email e senha
                        binding.editEmail.setText("")
                        binding.editSenha.setText("")
                    }
                }.addOnFailureListener { exception ->
                    // Se ocorrer um erro, trata as exceções comuns do FirebaseAuth
                    val mensagemErro = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um Email válido!"
                        is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao cadastrar usuário!"
                    }
                    // Exibe uma mensagem Snackbar com o erro
                    val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
//                    snackbar.setBackgroundTint(color.red) // Define a cor do fundo do Snackbar (comentado)
                    snackbar.show() // Exibe o Snackbar
                }
            }
        }

    }
}

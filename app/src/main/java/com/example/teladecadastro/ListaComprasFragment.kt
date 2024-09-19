package com.example.seuapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teladecadastro.databinding.FragmentListaComprasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListaComprasFragment : Fragment() {

    private var _binding: FragmentListaComprasBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val comprasAdapter = ComprasAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaComprasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.recyclerViewCompras.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCompras.adapter = comprasAdapter

        // Botão para adicionar novo item
        binding.btnAdicionarItem.setOnClickListener {
            val nomeItem = binding.editNomeItem.text.toString()
            val quantidadeItem = binding.editQuantidadeItem.text.toString()

            if (nomeItem.isNotEmpty() && quantidadeItem.isNotEmpty()) {
                val itemCompra = hashMapOf(
                    "nome" to nomeItem,
                    "quantidade" to quantidadeItem
                )

                // Salvar no Firestore
                db.collection("usuarios").document(auth.currentUser?.uid ?: "")
                    .collection("compras").add(itemCompra)
                    .addOnSuccessListener {
                        comprasAdapter.adicionarItem(CompraItem(nomeItem, quantidadeItem))
                        binding.editNomeItem.text.clear()
                        binding.editQuantidadeItem.text.clear()
                        Toast.makeText(context, "Item adicionado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Erro ao adicionar item", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Carregar os itens já existentes no Firestore
        carregarItensCompras()
    }

    private fun carregarItensCompras() {
        db.collection("usuarios").document(auth.currentUser?.uid ?: "")
            .collection("compras").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nome = document.getString("nome") ?: ""
                    val quantidade = document.getString("quantidade") ?: ""
                    comprasAdapter.adicionarItem(CompraItem(nome, quantidade))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.seuapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teladecadastro.databinding.ItemCompraBinding

data class CompraItem(val nome: String, val quantidade: String)

class ComprasAdapter(private val listaCompras: MutableList<CompraItem>) :
    RecyclerView.Adapter<ComprasAdapter.ComprasViewHolder>() {

    inner class ComprasViewHolder(val binding: ItemCompraBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(compraItem: CompraItem) {
            binding.tvNomeItem.text = compraItem.nome
            binding.tvQuantidadeItem.text = compraItem.quantidade
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprasViewHolder {
        val binding = ItemCompraBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ComprasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComprasViewHolder, position: Int) {
        holder.bind(listaCompras[position])
    }

    override fun getItemCount(): Int = listaCompras.size

    fun adicionarItem(compraItem: CompraItem) {
        listaCompras.add(compraItem)
        notifyDataSetChanged()
    }
}

package com.example.productsfirebase

import androidx.recyclerview.widget.RecyclerView
import com.example.productsfirebase.databinding.ProductItemBinding

class ProductViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.productName.text = product.name
        binding.productPrice.text = "${product.price} $"
        binding.productDescription.text = product.description
    }
}

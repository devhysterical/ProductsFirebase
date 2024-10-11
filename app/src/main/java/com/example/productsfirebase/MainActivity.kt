package com.example.productsfirebase

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productsfirebase.Product
import com.example.productsfirebase.ProductDetailActivity
import com.example.productsfirebase.ProductViewHolder
import com.example.productsfirebase.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.example.productsfirebase.databinding.ActivityMainBinding
import com.example.productsfirebase.databinding.ProductItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Add Product button
        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java)
            startActivity(intent)
        }

        displayProducts()
    }

    private fun displayProducts() {
        val query = FirebaseDatabase.getInstance().reference.child("products")
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val binding = ProductItemBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding)
            }

            override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Product) {
                holder.bind(model)
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                    intent.putExtra("product", model)
                    startActivity(intent)
                }
            }
        }

        binding.recyclerView.adapter = adapter

        // Kiểm tra xem có sản phẩm nào không
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result!!.childrenCount == 0L) {
                Toast.makeText(this, "Chưa có sản phẩm nào trong danh sách.", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.startListening()
    }
}

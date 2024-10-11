package com.example.productsfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productsfirebase.R
import com.example.productsfirebase.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var nameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("products")

        // Bind views
        nameTextView = findViewById(R.id.text_view_product_name)
        priceTextView = findViewById(R.id.text_view_product_price)
        descriptionTextView = findViewById(R.id.text_view_product_description)
        editButton = findViewById(R.id.button_edit_product)
        deleteButton = findViewById(R.id.button_delete_product)

        // Get product ID from intent
        productId = intent.getStringExtra("product_id")

        // Load product details from Firebase
        loadProductDetails()

        // Set the onClickListener for the Edit button
        editButton.setOnClickListener {
            val intent = Intent(this, EditProductActivity::class.java)
            intent.putExtra("product_id", productId)
            startActivity(intent)
        }

        // Set the onClickListener for the Delete button
        deleteButton.setOnClickListener {
            deleteProduct()
        }
    }

    private fun loadProductDetails() {
        productId?.let { id ->
            database.child(id).get().addOnSuccessListener { dataSnapshot ->
                val product = dataSnapshot.getValue(Product::class.java)
                product?.let {
                    nameTextView.text = it.name
                    priceTextView.text = it.price.toString()
                    descriptionTextView.text = it.description
                } ?: run {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load product details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteProduct() {
        productId?.let { id ->
            database.child(id).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                    finish()  // Close activity after deletion
                } else {
                    Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
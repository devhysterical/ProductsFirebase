package com.example.productsfirebase.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productsfirebase.FirebaseUtils
import com.example.productsfirebase.R
import com.example.productsfirebase.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddProductActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Initialize views
        nameEditText = findViewById(R.id.edit_text_product_name)
        priceEditText = findViewById(R.id.edit_text_product_price)
        descriptionEditText = findViewById(R.id.edit_text_product_description)
        addButton = findViewById(R.id.button_add_product)

        // Initialize Firebase Database reference
        database = FirebaseUtils.getDatabaseReference()

        // Set click listener for add button
        addButton.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        val name = nameEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new product object
        val productId = database.child("products").push().key // Tạo ID ngẫu nhiên cho sản phẩm
        val product = Product(productId ?: "", name, price.toDouble(), description)

        // Save product to Firebase
        database.child("products").child(productId!!).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish() // Đóng activity sau khi thêm sản phẩm
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
    }
}
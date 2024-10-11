package com.example.productsfirebase.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productsfirebase.R
import com.example.productsfirebase.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProductActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button

    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("products")

        // Bind views
        nameEditText = findViewById(R.id.edit_text_product_name)
        priceEditText = findViewById(R.id.edit_text_product_price)
        descriptionEditText = findViewById(R.id.edit_text_product_description)
        saveButton = findViewById(R.id.button_save_product)

        // Get product ID from intent
        productId = intent.getStringExtra("product_id")

        // Load product details for editing
        loadProductDetails()

        // Set the onClickListener for the Save button
        saveButton.setOnClickListener {
            updateProduct()
        }
    }

    private fun loadProductDetails() {
        productId?.let { id ->
            database.child(id).get().addOnSuccessListener { dataSnapshot ->
                val product = dataSnapshot.getValue(Product::class.java)
                product?.let {
                    nameEditText.setText(it.name)
                    priceEditText.setText(it.price.toString())
                    descriptionEditText.setText(it.description)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load product details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProduct() {
        val name = nameEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim().toDoubleOrNull()
        val description = descriptionEditText.text.toString().trim()

        if (name.isEmpty() || price == null || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create product object
        val updatedProduct = Product(productId!!, name, price, description)

        // Update product in Firebase
        database.child(productId!!).setValue(updatedProduct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after updating
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
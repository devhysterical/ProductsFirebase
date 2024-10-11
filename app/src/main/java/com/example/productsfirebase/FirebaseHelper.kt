package com.example.productsfirebase

import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance().getReference("products")

    // Add product
    fun addProduct(product: Product, onComplete: () -> Unit) {
        val productId = database.push().key ?: return
        product.id = productId
        database.child(productId).setValue(product).addOnCompleteListener {
            if (it.isSuccessful) onComplete()
        }
    }

    // Read all products
    fun getAllProducts(onSuccess: (List<Product>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (data in snapshot.children) {
                    val product = data.getValue(Product::class.java)
                    if (product != null) {
                        products.add(product)
                    }
                }
                onSuccess(products)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Update product
    fun updateProduct(product: Product, onComplete: () -> Unit) {
        database.child(product.id!!).setValue(product).addOnCompleteListener {
            if (it.isSuccessful) onComplete()
        }
    }

    // Delete product
    fun deleteProduct(productId: String, onComplete: () -> Unit) {
        database.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) onComplete()
        }
    }
}
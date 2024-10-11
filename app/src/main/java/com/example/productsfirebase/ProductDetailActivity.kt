package com.example.productsfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productsfirebase.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var firebaseHelper: FirebaseHelper
    private var isEditMode = false
    private var currentProduct: Product? = null
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()

        currentProduct = intent.getParcelableExtra("product")
        if (currentProduct != null) {
            isEditMode = true
            populateProductDetails(currentProduct!!)
        }

        // Nút Save
        binding.btnSave.setOnClickListener {
            val name = binding.etProductName.text.toString()
            val price = binding.etProductPrice.text.toString().toInt()
            val description = binding.etProductDescription.text.toString()

            val product = Product(currentProduct?.id, name, price, description)

            if (isEditMode) {
                firebaseHelper.updateProduct(product) {
                    Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra("product", product)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            } else {
                firebaseHelper.addProduct(product) {
                    Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    resultIntent.putExtra("product", product)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }

        // Nút Delete
        binding.btnDelete.setOnClickListener {
            if (isEditMode) {
                firebaseHelper.deleteProduct(currentProduct!!.id!!) {
                    Toast.makeText(this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_CANCELED) // Không cần gửi sản phẩm đã xóa về MainActivity
                    finish()
                }
            }
        }
    }

    private fun populateProductDetails(product: Product) {
        binding.etProductName.setText(product.name)
        binding.etProductPrice.setText(product.price.toString())
        binding.etProductDescription.setText(product.description)
    }
}

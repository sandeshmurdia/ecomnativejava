package com.example.ecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.model.Product
import com.zipy.zipyandroid.Zipy

class ProductAdapter(
    private var products: List<Product>,
    private val onWishlistClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvName.text = product.name
                tvDescription.text = product.description
                tvPrice.text = "$${product.price}"

                Glide.with(itemView.context)
                    .load(product.imageUrl)
                    .centerCrop()
                    .into(ivProduct)

                btnWishlist.setImageResource(
                    if (product.isInWishlist) android.R.drawable.star_big_on
                    else android.R.drawable.star_big_off
                )

                btnWishlist.setOnClickListener {
                    onWishlistClick(product)
                    notifyItemChanged(adapterPosition)
                    // Log wishlist interaction with detailed product info
                    val wishlistDetails = mapOf(
                        "product_id" to product.id,
                        "product_name" to product.name,
                        "product_price" to product.price,
                        "action" to if (product.isInWishlist) "add" else "remove",
                        "timestamp" to System.currentTimeMillis(),
                        "screen" to "product_list",
                        "position" to adapterPosition
                    )
                    Zipy.logMessage(
                        "Product wishlist interaction",
                        wishlistDetails
                    )
                }

                btnAddToCart.setOnClickListener {
                    onAddToCartClick(product)
                    notifyItemChanged(adapterPosition)
                    try {
                        // Log add to cart with detailed product and cart info
                        val cartDetails = mapOf(
                            "product_id" to product.id,
                            "product_name" to product.name,
                            "product_price" to product.price,
                            "quantity" to product.quantity,
                            "total_price" to (product.price * product.quantity),
                            "timestamp" to System.currentTimeMillis(),
                            "screen" to "product_list",
                            "position" to adapterPosition,
                            "is_in_wishlist" to product.isInWishlist,
                            "action" to "add_to_cart"
                        )
                        Zipy.logMessage("Product added to cart", cartDetails)
                    } catch (e: Exception) {
                        val errorDetails = mapOf(
                            "product_id" to product.id,
                            "product_name" to product.name,
                            "error_type" to "cart_operation_failed",
                            "timestamp" to System.currentTimeMillis(),
                            "error_message" to e.message
                        )
                        Zipy.logError("Failed to add product to cart", errorDetails)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
} 
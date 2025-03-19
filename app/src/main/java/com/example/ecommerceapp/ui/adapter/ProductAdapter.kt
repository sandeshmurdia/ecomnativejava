package com.example.ecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.ItemProductBinding
import com.example.ecommerceapp.model.Product

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
                }

                btnAddToCart.setOnClickListener {
                    onAddToCartClick(product)
                    notifyItemChanged(adapterPosition)
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
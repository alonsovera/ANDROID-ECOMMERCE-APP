package com.example.ecopetsapp;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productoNom, productoDescripcion, productoPrecio, productoCantidad, productopid;
    public ImageView productoImagen;
    public ItemClickListener listener;

    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);
        productoNom = (TextView) itemView.findViewById(R.id.producto_nombre);
        productoDescripcion = (TextView) itemView.findViewById(R.id.producto_descripcion);
        productoPrecio = (TextView) itemView.findViewById(R.id.producto_precio);
        productoCantidad = (TextView)  itemView.findViewById(R.id.producto_cantidad);
        productoImagen = (ImageView) itemView.findViewById(R.id.producto_imagen);

        // Aquí se asigna el listener a cada item de la lista
        itemView.setOnClickListener(this);
    }

    // Método para asignar el listener
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    // Método llamado cuando se hace clic
    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}

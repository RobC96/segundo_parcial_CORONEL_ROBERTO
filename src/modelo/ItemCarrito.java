
package modelo;

import java.io.Serializable;
import java.util.Objects;

public class ItemCarrito implements Serializable {
    private static final long serialVersionUID = 1L;

    private Producto producto;
    private Integer cantidad;

    public ItemCarrito(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getNombreProducto(){
        return this.producto.getNombre();
    }
    
    public Double getSubtotal(){
        return this.producto.getPrecio() * this.cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.producto);
        hash = 59 * hash + Objects.hashCode(this.cantidad);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemCarrito other = (ItemCarrito) obj;
        return Objects.equals(this.producto, other.producto);
    }
}
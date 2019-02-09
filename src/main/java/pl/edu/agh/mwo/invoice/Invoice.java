package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Collection<Product> products = new ArrayList<>();

	
	public Collection<Product> getProducts() {
		return products;
	}


	public void addProduct(Product product) {
		this.products.add(product);
	}


	public void addProduct(Product product, Integer quantity) {
//		for (int i = 0; i <quantity; i++) {
//			addProduct(product);
//		}
		if (quantity <= 0) {
			throw new IllegalArgumentException("Illegal arg exep");
		}
//		while (quantity < 0) {
//			addProduct(product);
//			quantity--;
//		}
		for (int i = 0; i <quantity; i++) {
			addProduct(product);
		}
	}

	public BigDecimal getSubtotal() {
		BigDecimal sub = BigDecimal.ZERO;
		for (Product p : this.products) {
			sub = sub.add(p.getPrice());
		}
		return sub;
	}

	public BigDecimal getTax() {
		BigDecimal tax = BigDecimal.ZERO;
		for (Product p : this.products) {
			tax = tax.add(p.getPrice().multiply(p.getTaxPercent()));
		}
		return tax;
	}

	public BigDecimal getTotal() {
		return getSubtotal().add(getTax());
	}
}

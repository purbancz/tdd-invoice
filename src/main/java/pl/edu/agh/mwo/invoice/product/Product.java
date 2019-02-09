package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) {
		if (name == "" || name == null || price == null || price.compareTo(new BigDecimal("0")) < 0)  {
		    throw new IllegalArgumentException("Illegal arg exep");
		}
		this.name = name;
		this.price = price;
		this.taxPercent = tax;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getTaxPercent() {
		return taxPercent;
	}
	
	public BigDecimal getPriceWithTax() {
		return this.getPrice().add(this.getPrice().multiply(this.getTaxPercent()));
	}
	
	


}

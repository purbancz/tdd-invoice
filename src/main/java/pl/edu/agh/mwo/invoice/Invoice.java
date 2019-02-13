package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Map<Product, Integer> products = new LinkedHashMap<Product, Integer>();
	private int invoiceNum;
	private static Integer nextNumber = 1;

	public int getInvoiceNum() {
		return invoiceNum;
	}

	public Invoice() {
		this.invoiceNum = nextNumber++;
	}

	public void addProduct(Product product) {
		addProduct(product, 1);
	}

	public void addProduct(Product product, Integer quantity) {
		if (product == null || quantity <= 0) {
			throw new IllegalArgumentException();
		}
		if (this.products.containsKey(product)) {
			Integer newQantitiy = this.products.get(product) + quantity;
			this.products.put(product, newQantitiy);
		}
		else {
			products.put(product, quantity);
		}
	}

	public BigDecimal getNetTotal() {
		BigDecimal totalNet = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalNet = totalNet.add(product.getPrice().multiply(quantity));
		}
		return totalNet;
	}

	public BigDecimal getTaxTotal() {
		return getGrossTotal().subtract(getNetTotal());
	}

	public BigDecimal getGrossTotal() {
		BigDecimal totalGross = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
		}
		return totalGross;
	}

	public String getAsText(Invoice invoice) {
		StringBuilder sb = new StringBuilder();
		sb.append("nr " + this.invoiceNum);
		for (Product p : invoice.products.keySet()) {
			sb.append("\n");
			sb.append(p.getName());
			sb.append(" ");
			sb.append(products.get(p).toString());
			sb.append(" ");
			sb.append(p.getPrice());
			
		}
		sb.append("\nLiczba pozycji: ");
		sb.append(this.products.size());
		
		return sb.toString();
	}
}

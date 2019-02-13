package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private Invoice invoice;

	@Before
	public void createEmptyInvoiceForTheTest() {
		invoice = new Invoice();
	}

	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getSubtotal()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTax()));
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTotal()));
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
		invoice.addProduct(taxFreeProduct);
		Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProducts() {
		invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
		invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
		invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		// tax: 0
		invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
		// tax: 8
		invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
		// tax: 2.30
		invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		// price with tax: 200
		invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
		// price with tax: 108
		invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
		// price with tax: 12.30
		invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
		Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test
	public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
		// 2x kubek - price: 10
		invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
		// 3x kozi serek - price: 30
		invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
		// 1000x pinezka - price: 10
		invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
		Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		// 2x chleb - price with tax: 10
		invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
		// 3x chedar - price with tax: 32.40
		invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
		// 1000x pinezka - price with tax: 12.30
		invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
		Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
	}

	// tutaj

	// @Test
	// public void testGetInvoiceNumber() {
	// invoice.setInvoiceNum(12);
	// Assert.assertEquals(invoice.getInvoiceNum(), 12);
	// }

	// @Test(expected = IllegalArgumentException.class)
	// public void testInvoiceNumLessThanOne() {
	// invoice.setInvoiceNum(0);
	// }

	@Test
	public void testInvoiceHasNumber() {
		Integer number = invoice.getInvoiceNum();
		Assert.assertNotNull(number);
	}

	@Test
	public void testInvoicesHasDifferentNumber() {
		Integer number = invoice.getInvoiceNum();
		Integer number1 = new Invoice().getInvoiceNum();
		Assert.assertNotEquals(number, number1);
	}

	@Test
	public void testInvoiceHasSameNumber() {
		Integer number = invoice.getInvoiceNum();
		Integer number1 = invoice.getInvoiceNum();
		Assert.assertEquals(number, number1);
	}

	@Test
	public void testInvoicesHasConseqNumber() {
		for (int i = 0; i < 100; i++) {
			Integer number = invoice.getInvoiceNum();
			Integer number1 = new Invoice().getInvoiceNum();
			Assert.assertNotEquals(number, Matchers.lessThan(number1));
		}
	}

	@Test
	public void testPrintInvoiceContainsNumber() {
		String printedIvoice = invoice.getAsText(invoice);
		String number = String.valueOf(invoice.getInvoiceNum());
		Assert.assertThat(printedIvoice, Matchers.containsString("nr " + number));
	}
	
	@Test
	public void testPrintInvoiceContainsProductDetails() {
		invoice.addProduct(new TaxFreeProduct("Kot", new BigDecimal("5")), 2);
		String printedIvoice = invoice.getAsText(invoice);
		Assert.assertThat(printedIvoice, Matchers.containsString("Kot 2 5"));
	}
	
	@Test
	public void testPrintInvoiceContainsManyProductDetails() {
		invoice.addProduct(new TaxFreeProduct("Kot", new BigDecimal("5")), 2);
		invoice.addProduct(new TaxFreeProduct("Pies", new BigDecimal("7")), 3);
		String printedIvoice = invoice.getAsText(invoice);
		Assert.assertThat(printedIvoice, Matchers.containsString("Kot 2 5\nPies 3 7"));
	}
	
	@Test
	public void testPrintInvoiceContainsProductsQuantity() {
		invoice.addProduct(new TaxFreeProduct("Kot", new BigDecimal("5")), 2);
		invoice.addProduct(new TaxFreeProduct("Pies", new BigDecimal("7")), 3);
		String printedIvoice = invoice.getAsText(invoice);
		Assert.assertThat(printedIvoice, Matchers.containsString("Liczba pozycji: 2"));
	}
	
	@Test
	public void testPrintInvoiceContainsSameProductTwice() {
		invoice.addProduct(new TaxFreeProduct("Kot", new BigDecimal("5")));
		invoice.addProduct(new TaxFreeProduct("Kot", new BigDecimal("5")));
		String printedIvoice = invoice.getAsText(invoice);
		Assert.assertThat(printedIvoice, Matchers.containsString("Kot 2 5"));	
	}
	
	


}

/**
 * 
 */
package com.acme.mytrader.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.BuyPriceListener;

/**
 * @author pratik_aagal
 *
 */
public class BuyPriceListenerTest {

	@Test
	public void testInitializeStateForBuyPriceListener() {
		ExecutionService executionService = Mockito.mock(ExecutionService.class);

		BuyPriceListener buyPriceListener = new BuyPriceListener("ADANI", 50.00, 100, executionService, false);

		assertEquals(buyPriceListener.getSecurity(), "ADANI");
		//assertEquals(buyPriceListener.getTriggerLevel(), 50.00);
		assertEquals(buyPriceListener.getQuantityToPurchase(), 100);
		assertFalse(buyPriceListener.isTradeExecuted());
	}

	@Test
	public void testBuy_whenThresholdIsMet() {
		ExecutionService executionService = Mockito.mock(ExecutionService.class);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

		BuyPriceListener buyPriceListener = new BuyPriceListener("ADANI", 50.00, 100, executionService, false);
		buyPriceListener.priceUpdate("ADANI", 25.00);

		verify(executionService, times(1)).buy(acString.capture(), acDouble.capture(), acInteger.capture());
		assertEquals(acString.getValue(), "ADANI");
		assertEquals(acDouble.getValue(), Double.valueOf(25.00));
		assertTrue(buyPriceListener.isTradeExecuted());
	}

	@Test
	public void testShouldNotBuy_whenThresholdIsNotMet() {
		ExecutionService executionService = Mockito.mock(ExecutionService.class);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

		BuyPriceListener buyPriceListener = new BuyPriceListener("ADANI", 50.00, 100, executionService, false);
		buyPriceListener.priceUpdate("ADANI", 55.00);

		verify(executionService, times(0)).buy(acString.capture(), acDouble.capture(), acInteger.capture());
		assertFalse(buyPriceListener.isTradeExecuted());
	}

	@Test
	public void testShouldNotBuy_whenSecurityIsDifferent() {
		ExecutionService executionService = Mockito.mock(ExecutionService.class);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

		BuyPriceListener buyPriceListener = new BuyPriceListener("APPL", 50.00, 100, executionService, false);
		buyPriceListener.priceUpdate("ADANI", 55.00);

		verify(executionService, times(0)).buy(acString.capture(), acDouble.capture(), acInteger.capture());
		assertFalse(buyPriceListener.isTradeExecuted());
	}

	@Test
	public void testGivenSeveralPriceUpdates_whenTradeIsAlreadyExecucted_shouldBuyOnlyOnce() {
		ExecutionService executionService = Mockito.mock(ExecutionService.class);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

		BuyPriceListener buyPriceListener = new BuyPriceListener("ADANI", 50.00, 100, executionService, false);
		buyPriceListener.priceUpdate("ADANI", 25.00);
		buyPriceListener.priceUpdate("ADANI", 10.00);
		buyPriceListener.priceUpdate("ADANI", 35.00);

		verify(executionService, times(1)).buy(acString.capture(), acDouble.capture(), acInteger.capture());
		assertEquals(acString.getValue(), "ADANI");
		assertEquals(acDouble.getValue(), Double.valueOf(25.00));
		assertTrue(buyPriceListener.isTradeExecuted());
	}
}

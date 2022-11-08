package com.acme.mytrader.strategy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceSource;


public class TradingStrategyTest {
	
	ExecutionService tradeExecutionService;
	TradingStrategy tradingStrategy;
	PriceSource priceSource;
	
    @Test
    public void testNothing() {
    	
    }
    
    @Before
	public void setup(){
    	tradeExecutionService = Mockito.mock(ExecutionService.class);
    	tradingStrategy = Mockito.mock(TradingStrategy.class);
    	priceSource = Mockito.mock(PriceSource.class);
    }
    
    @Test
    public void testAutoBuyForSuccessfulBuy() throws Exception { 
      ArgumentCaptor<String> securityCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<Double> priceCaptor = ArgumentCaptor.forClass(Double.class);
      ArgumentCaptor<Integer> volumeCaptor = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<Boolean> tradeExecutedCaptor = ArgumentCaptor.forClass(Boolean.class);
      tradingStrategy = new TradingStrategy(tradeExecutionService, priceSource);
      List<TradingStrategy> input = Arrays.asList(new TradingStrategy(tradeExecutionService, 
    		  "ADANI", 50.00, 10, false, 1200.00));
      tradingStrategy.autoBuy(input);
      tradingStrategy = new TradingStrategy(tradeExecutionService, 
    		  "ADANI", 30.00, 10, false, 1200.00);
      tradingStrategy.setPriceSource(priceSource);
      tradingStrategy.setTradeExecutionService(tradeExecutionService);
      tradingStrategy.priceUpdate("ADANI", 30.00);
      verify(tradingStrategy, times(1)).priceUpdate(securityCaptor.capture(), priceCaptor.capture());
      assertEquals(tradeExecutedCaptor.getValue(), true);
      verify(tradeExecutionService, times(1)).buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());
      assertEquals(securityCaptor.getValue(), "ADANI");
    }
    
    @Test
    public void testAutoBuyForNotSuccessfulBuy() throws Exception {
      List<TradingStrategy> input = Arrays.asList(new TradingStrategy(tradeExecutionService, 
    		  "APPL", 50.00, 10, false, 1200.00));
      tradingStrategy.autoBuy(input);
      verifyZeroInteractions(tradeExecutionService);
    }
}

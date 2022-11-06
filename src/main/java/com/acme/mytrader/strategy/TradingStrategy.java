package com.acme.mytrader.strategy;

import java.util.Arrays;
import java.util.List;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.execution.TradeExecutionService;
import com.acme.mytrader.price.BuyPriceListener;
import com.acme.mytrader.price.PriceSourceImpl;
import com.acme.mytrader.price.PriceSourceRunnable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy {
	
	private final ExecutionService tradeExecutionService;
	private final PriceSourceRunnable priceSource;
	
	public TradingStrategy(ExecutionService tradeExecutionService, PriceSourceRunnable priceSource) {
		this.tradeExecutionService = tradeExecutionService;
		this.priceSource = priceSource;
	}

	public void autoBuy(List<SecurityDTO> request) throws InterruptedException {

	    request.stream().map(
	        r -> new BuyPriceListener(r.getSecurity(), r.getPriceThreshold(), r.getVolume(),
	            tradeExecutionService, false)).forEach(priceSource::addPriceListener);
	    Thread thread = new Thread(priceSource);
	    thread.start();
	    thread.join();
	    request.stream().map(
	        r -> new BuyPriceListener(r.getSecurity(), r.getPriceThreshold(), r.getVolume(),
	            tradeExecutionService, false)).forEach(priceSource::removePriceListener);
	  }

	  //This is a demo test
	  public static void main(String[] args) throws InterruptedException {
	    TradingStrategy tradingStrategy = new TradingStrategy(new TradeExecutionService(1),
	        new PriceSourceImpl());
	    
	    final SecurityDTO adani = SecurityDTO.builder().security("ADANI").priceThreshold(2940.00).volume(12)
	        .build();
	    final SecurityDTO reliance = SecurityDTO.builder().security("Reliance").priceThreshold(1203.00)
	        .volume(22)
	        .build();
	    tradingStrategy.autoBuy(Arrays.asList(adani, reliance));
	  }

}

@AllArgsConstructor
@Builder
@Data
class SecurityDTO {

	private final String security;
	private final Double priceThreshold;
	private final Integer volume;
}
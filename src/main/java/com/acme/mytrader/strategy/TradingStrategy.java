package com.acme.mytrader.strategy;

import java.util.List;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.execution.TradeExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;

import lombok.Data;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
@Data
public class TradingStrategy implements PriceListener {
	
	private ExecutionService tradeExecutionService;
	private PriceSource priceSource;
	private String security;
	private double triggerLevel;
	private int quantityToPurchase;
	private boolean tradeExecuted;
	private double priceThreshold;
	private int volume;
	
	

	/**
	 * @param tradeExecutionService
	 * @param priceSource
	 * @param security
	 * @param triggerLevel
	 * @param quantityToPurchase
	 * @param tradeExecuted
	 * @param priceThreshold
	 * @param volume
	 */
	public TradingStrategy(ExecutionService tradeExecutionService, String security,
			double triggerLevel, int quantityToPurchase, boolean tradeExecuted, double priceThreshold) {
		this.tradeExecutionService = tradeExecutionService;
		this.security = security;
		this.triggerLevel = triggerLevel;
		this.quantityToPurchase = quantityToPurchase;
		this.tradeExecuted = tradeExecuted;
		this.priceThreshold = priceThreshold;
	}
	
	public void autoBuy(List<TradingStrategy> request) throws InterruptedException {
		request.stream().map(r -> new TradingStrategy(tradeExecutionService, r.getSecurity(), 
				r.getTriggerLevel(), r.getQuantityToPurchase(), r.isTradeExecuted(), r.getPriceThreshold()))
		.forEach(priceSource::addPriceListener);
	}

	@Override
	public void priceUpdate(String security, double price) {
		if (canBuy(security, price)) {
			tradeExecutionService = new TradeExecutionService();
			tradeExecutionService.buy(security, price, quantityToPurchase);
			tradeExecuted = true;
		}
	}
	
	private boolean canBuy(String security, double price) {
		return (!tradeExecuted) && this.security.equals(security) && (price < this.triggerLevel);
	}



	/**
	 * @param tradeExecutionService
	 */
	public TradingStrategy(ExecutionService tradeExecutionService, PriceSource priceSource) {
		this.tradeExecutionService = tradeExecutionService;
		this.priceSource = priceSource;
	}

}
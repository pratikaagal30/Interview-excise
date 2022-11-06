/**
 * 
 */
package com.acme.mytrader.price;

import java.util.Objects;

import com.acme.mytrader.execution.ExecutionService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author pratik_aagal
 *
 */

@AllArgsConstructor
@Builder
@Data
public class BuyPriceListener implements PriceListener {

	private final String security;
	private final double triggerLevel;
	private final int quantityToPurchase;
	private final ExecutionService executionService;
	private boolean tradeExecuted;

	@Override
	public void priceUpdate(String security, double price) {
		if (canBuy(security, price)) {
			executionService.buy(security, price, quantityToPurchase);
			tradeExecuted = true;
		}
	}

	private boolean canBuy(String security, double price) {
		return (!tradeExecuted) && this.security.equals(security) && (price < this.triggerLevel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executionService, quantityToPurchase, security, tradeExecuted, triggerLevel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BuyPriceListener)) {
			return false;
		}
		BuyPriceListener other = (BuyPriceListener) obj;
		return Objects.equals(executionService, other.executionService)
				&& quantityToPurchase == other.quantityToPurchase && Objects.equals(security, other.security)
				&& tradeExecuted == other.tradeExecuted
				&& Double.doubleToLongBits(triggerLevel) == Double.doubleToLongBits(other.triggerLevel);
	}

	@Override
	public String toString() {
		return "BuyPriceListener [security=" + security + ", triggerLevel=" + triggerLevel + ", quantityToPurchase="
				+ quantityToPurchase + ", executionService=" + executionService + ", tradeExecuted=" + tradeExecuted
				+ ", hashCode()=" + hashCode() + ", getSecurity()=" + getSecurity() + ", getTriggerLevel()="
				+ getTriggerLevel() + ", getQuantityToPurchase()=" + getQuantityToPurchase()
				+ ", getExecutionService()=" + getExecutionService() + ", isTradeExecuted()=" + isTradeExecuted()
				+ ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}
}
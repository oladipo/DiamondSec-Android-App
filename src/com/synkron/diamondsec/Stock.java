package com.synkron.diamondsec;

public class Stock {
	private String StockCode;
	private String Name;
	//change the data type later to a numeric or floating point type
	private String TotalUnits;
	private String AverageUnitPrice;
	private String Cost;
	private String Fees;
	private String MarketValue;
	private String MarketQuote;
	private String Sector;
	private String Price;
	private String LClose;
	private String Change;
	private String Volume;
	
	public String getStockCode() {
		return StockCode;
	}
	public void setStockCode(String stockCode) {
		StockCode = stockCode;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the totalUnits
	 */
	public String getTotalUnits() {
		return TotalUnits;
	}
	/**
	 * @param totalUnits the totalUnits to set
	 */
	public void setTotalUnits(String totalUnits) {
		TotalUnits = totalUnits;
	}
	/**
	 * @return the averageUnitPrice
	 */
	public String getAverageUnitPrice() {
		return AverageUnitPrice;
	}
	/**
	 * @param averageUnitPrice the averageUnitPrice to set
	 */
	public void setAverageUnitPrice(String averageUnitPrice) {
		AverageUnitPrice = averageUnitPrice;
	}
	/**
	 * @return the cost
	 */
	public String getCost() {
		return Cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(String cost) {
		Cost = cost;
	}
	/**
	 * @return the fees
	 */
	public String getFees() {
		return Fees;
	}
	/**
	 * @param fees the fees to set
	 */
	public void setFees(String fees) {
		Fees = fees;
	}
	/**
	 * @return the marketValue
	 */
	public String getMarketValue() {
		return MarketValue;
	}
	/**
	 * @param marketValue the marketValue to set
	 */
	public void setMarketValue(String marketValue) {
		MarketValue = marketValue;
	}
	/**
	 * @return the marketQuote
	 */
	public String getMarketQuote() {
		return MarketQuote;
	}
	/**
	 * @param marketQuote the marketQuote to set
	 */
	public void setMarketQuote(String marketQuote) {
		MarketQuote = marketQuote;
	}
	/**
	 * @return the sector
	 */
	public String getSector() {
		return Sector;
	}
	/**
	 * @param sector the sector to set
	 */
	public void setSector(String sector) {
		Sector = sector;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return Price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		Price = price;
	}
	/**
	 * @return the volume
	 */
	public String getVolume() {
		return Volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(String volume) {
		Volume = volume;
	}
	/**
	 * @return the change
	 */
	public String getChange() {
		return Change;
	}
	/**
	 * @param change the change to set
	 */
	public void setChange(String change) {
		Change = change;
	}
	/**
	 * @return the lClose
	 */
	public String getLClose() {
		return LClose;
	}
	/**
	 * @param lClose the lClose to set
	 */
	public void setLClose(String lClose) {
		LClose = lClose;
	}
}

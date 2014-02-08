package scaper

class Investment {

	static transients = [ 'previousPrice']
	
    static constraints = {
		ticker unique: true
		price scale:2,nullable:true
		priceLastUpdated nullable:true
		percentageChange nullable:true
		valueChange nullable:true
		ytdReturn(scale:2,nullable:true)
    }
		
	String name
	String ticker
	BigDecimal price
	Date priceLastUpdated
	BigDecimal valueChange
	BigDecimal percentageChange
	BigDecimal ytdReturn
	
	String toString() {
		return "$name ($ticker)"
	}
	
	BigDecimal getPreviousPrice() {
		price - valueChange
	}
	
}

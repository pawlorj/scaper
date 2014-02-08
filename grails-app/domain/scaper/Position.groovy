package scaper

class Position {
	static belongsTo = Portfolio
	
	static transients = [ 'value', 'previousValue']
	
    static constraints = {
    }
	
	Investment investment
	int quantity
	
	String toString() {
		"$quantity shares of $investment"
	}
	
	BigDecimal getValue() {
		quantity * investment.price
	}
	
	BigDecimal getPreviousValue() {
		quantity * investment.previousPrice
	}

}

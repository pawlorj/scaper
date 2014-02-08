package scaper

class Portfolio {

	static belongsTo = Person
	
	static transients = [ 'value', 'previousValue', 'percentageChange', 'change']
	
    static constraints = {
		name nullable:true
    }
	
	static hasMany = [positions : Position]
	String name
	
	String toString() {
		name
	}
	
	BigDecimal getValue() {
		positions.sum { p -> p.value }
	}
	
	BigDecimal getPreviousValue() {
		positions.sum { p -> p.previousValue }
	}

	BigDecimal getChange() {
		value - previousValue
	}

	BigDecimal getPercentageChange() {
		(change / previousValue * 100.0).round(3)
	}


}

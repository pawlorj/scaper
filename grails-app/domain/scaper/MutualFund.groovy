package scaper

class MutualFund extends Investment {

	static m = [one:1, two:2, three:3, four:4, five:5, nostar:0]
	
    static constraints = {
		threeMonthReturn(scale:2,nullable:true)
		threeYearReturn(scale:2,nullable:true)
		fiveYearReturn(scale:2,nullable:true)		
    }
	
	FundCategory category
	FundFamily fundFamily
	int rating
	BigDecimal threeMonthReturn
	BigDecimal threeYearReturn
	BigDecimal fiveYearReturn
	
	void assignRating (String sRating) {
		//rating four-star
		sRating = sRating - 'rating ' - '-star'
		rating = m[sRating]
	}
	
	String toString() {
		name
	}
}

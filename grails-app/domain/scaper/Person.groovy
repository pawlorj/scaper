package scaper

class Person {

    static constraints = {
		firstName nullable:false
		lastName nullable:false
    }
	
	String firstName
	String lastName
	
	static hasMany = [portfolios : Portfolio]

}

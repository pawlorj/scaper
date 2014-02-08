package scaper

import grails.transaction.Transactional

@Transactional
class SetupService {

	def nasdaqPortfolioService

	
	def investorMap = [
			[firstName:'Bill', lastName:'Ackman', portfolio:'PERSHING SQUARE CAPITAL MANAGEMENT, L.P.', url:'pershing-square-capital-management,-l.p.-683405'],	
			[firstName:'Paul', lastName:'Singer', portfolio:'ELLIOTT ASSOCIATES, L.P.', url:'elliott-associates,-l.p.-30166'],	
			[firstName:'Carl', lastName:'Icahn', portfolio:'ICAHN ASSOCIATES HOLDING LLC', url:'icahn-carl-c-18190']			     
	]
	
	
	
	
	
    def setup() {
		
		investorMap.each { i ->
			def p = new Person(firstName:i.firstName, lastName: i.lastName)
			
			def portfolio = new Portfolio(name:i.portfolio)
			p.addToPortfolios portfolio
					
			nasdaqPortfolioService.loadPortfolio(i.url).each { position ->
				portfolio.addToPositions position
			}
			
			p.save()
						
		}
		
    }
}

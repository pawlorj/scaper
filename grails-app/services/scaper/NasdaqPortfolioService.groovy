package scaper

import grails.transaction.Transactional

@Transactional
class NasdaqPortfolioService {
	
	String baseUrl = "http://www.nasdaq.com/quotes/institutional-portfolio"	
	
	def yahooCsvStockService
	
    def loadPortfolio(String contextUrl) {
		def url = "$baseUrl/$contextUrl"
		log.debug "Getting portfolio for $url"
		def tagsoupParser = org.ccil.cowan.tagsoup.jaxp.SAXParserImpl.newInstance(null)
		def slurper = new XmlSlurper(tagsoupParser)
		def positions = []
		def symbolMap = [:]
					
		def document = slurper.parse(url)
		   // Extracting information
		def htmlParser = slurper.parse(url)
		htmlParser.'**'.find { it.@id == 'total-positions-table' }.table.tbody.tr.each { r ->
			if (r.td[0].text().trim()) {
				def symbol = (r.td[0].a.@href.text() - 'http://www.nasdaq.com/symbol/' - '/institutional-holdings').toUpperCase()	
				int quantity = java.text.NumberFormat.instance.parse (r.td[5].text())
				if (quantity > 0) {
					log.debug "Adding $quantity shares of $symbol to portfolio map"
					symbolMap[symbol] =  quantity
				}
			}	
		}
		
		def m = yahooCsvStockService.quotes(symbolMap.keySet())
		
		m.each { k,v ->
			def p = new Position()
			p.quantity = symbolMap[k]
			p.investment = v
			positions+=p
			log.debug "Adding $p to Portfolio"
		}
				
		return positions
    }
	
}

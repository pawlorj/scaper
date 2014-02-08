package scaper

import grails.transaction.Transactional

@Transactional
class YahooCsvStockService {

    def quotes(def symbols) {
		def m = [:]
		String ticker = symbols.join('+')
		log.debug "Getting quotes for $ticker"
		def url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=${ticker}&f=snl1t1c1p2&e=.csv")
		log.debug url
				
		url.text.eachCsvLine { s ->
		  	def stock = Stock.findByTicker(s[0])
			if (!stock) {
				stock = new Stock()
			}
			
			stock.ticker = s[0]
			stock.name = s[1]
			stock.price = s[2] as Double
			stock.priceLastUpdated = s[3].toDate()
			stock.valueChange = s[4] as Double
			stock.percentageChange = (s[5] - '%' - '"' - '"') as Double
			
			try {
				if (!stock.save()) {
					stock.errors.each {
						println it
					}
				} else {
					m[stock.ticker] = stock
				}
			} catch (Exception e) {
				println "Error saving $stock"
			}
		}
		
		log.debug "Got quotes $m"
		return m
    }
	
	
	def tagMap = [ "ask": 'a',
		"averagedailyvolume": 'a2',
		"asksize": 'a5',
		"bid": 'b',
		"ask(realtime)": 'b2',
		"bid(realtime)": 'b3',
		"bookvalue": 'b4',
		"bidsize": 'b6',
		"change&percentchange": 'c',
		"change": 'c1',
		"commission": 'c3',
		"change(realtime)": 'c6',
		"afterhourschange(realtime)": 'c8',
		"dividend/share": 'd',
		"lasttradedate": 'd1',
		"tradedate": 'd2',
		"earnings/share": 'e',
		"errorindication": 'e1',
		"epsestimatecurrentyear": 'e7',
		"epsestimatenextyear": 'e8',
		"epsestimatenextquarter": 'e9',
		"floatshares": 'f6',
		"dayslow": 'g',
		"dayshigh": 'h',
		"52weeklow": 'j',
		"52weekhigh": 'k',
		"holdingsgainpercent": 'g1',
		"annualizedgain": 'g3',
		"holdingsgain": 'g4',
		"Holdingsgainpercent(realtime)": 'g5',
		"Holdingsgain(realtime)": 'g6',
		"moreinfo": 'i',
		"orderbook(realtime)": 'i5',
		"marketcapitalization": 'j1',
		"marketcapitalization(realtime)": 'j3',
		"ebitda": 'j4',
		"changefrom52weeklow": 'j5',
		"percentchangefrom52weeklow": 'j6',
		"lasttrade(realtime)withtime": 'k1',
		"changepercent(realtime)": 'k2',
		"lasttradesize": 'k3',
		"changefrom52weekhigh": 'k4',
		"percentchangefrom52weekhigh": 'k5',
		"lasttrade(withtime)": 'l',
		"lasttrade": 'l1',
		"highlimit": 'l2',
		"lowlimit": 'l3',
		"daysrange": 'm',
		"daysrange(realtime)": 'm2',
		"50daymovingaverage": 'm3',
		"200daymovingaverage": 'm4',
		"changefrom200daymovingaverage": 'm5',
		"percentchangefrom200daymovingaverage": 'm6',
		"changefrom50daymovingaverage": 'm7',
		"percentchangefrom50daymovingaverage": 'm8',
		"name": 'n',
		"notes": 'n4',
		"open": 'o',
		"previousclose": 'p',
		"pricepaid": 'p1',
		"changeinpercent": 'p2',
		"price/sales": 'p5',
		"price/book": 'p6',
		"exdividenddate": 'q',
		"peratio": 'r',
		"dividendpaydate": 'r1',
		"peratio(realtime)": 'r2',
		"pegratio": 'r5',
		"price/epsestimatecurrentyear": 'r6',
		"price/epsestimatenextyear": 'r7',
		"symbol": 's',
		"sharesowned": 's1',
		"shortratio": 's7',
		"lasttradetime": 't1',
		"tradelinks": 't6',
		"tickertrend": 't7',
		"1yrtargetprice": 't8',
		"volume": 'v',
		"holdingsvalue": 'v1',
		"holdingsvalue(realtime)": 'w7',
		"52weekrange": 'w',
		"daysvaluechange": 'w1',
		"daysvaluechange(realtime)": 'w4',
		"stockexchange": 'x',
		"dividendyield": 'y']

}

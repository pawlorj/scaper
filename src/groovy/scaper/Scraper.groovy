package scaper

class Scraper {
	
	static def testHtml = '''<div class=\"yfi-table-container \">\n<table><colgroup><col><col><col><col><col><col><col><col><col></colgroup><thead><tr><th id=\"table-0-0-0\" class=\"fname\" scope=\"col\"><span>Fund Name</span></th>\n<th id=\"table-0-0-1\" class=\"tkr\" scope=\"col\"><span>Ticker</span></th>\n<th id=\"table-0-0-2\" class=\"cat\" scope=\"col\"><span>Category</span></th>\n<th id=\"table-0-0-3\" class=\"ffly\" scope=\"col\"><span>Fund Family</span></th>\n<th id=\"table-0-0-4\" class=\"nav3m  selected\" scope=\"col\"><a href='?mod_id=mediaquotesmutualfunds&scol=nav3m&stype=asc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colhead'>3-Mo Return</a><a href='?mod_id=mediaquotesmutualfunds&scol=nav3m&stype=asc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colsort' class='sort desc'></a></th>\n<th id=\"table-0-0-5\" class=\"navytd\" scope=\"col\"><a href='?mod_id=mediaquotesmutualfunds&scol=navytd&stype=desc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colhead'>YTD Return</a></th>\n<th id=\"table-0-0-6\" class=\"nav3yr\" scope=\"col\"><a href='?mod_id=mediaquotesmutualfunds&scol=nav3yr&stype=desc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colhead'>3-YR Return</a></th>\n<th id=\"table-0-0-7\" class=\"nav5yr\" scope=\"col\"><a href='?mod_id=mediaquotesmutualfunds&scol=nav5yr&stype=desc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colhead'>5-YR Return</a></th>\n<th id=\"table-0-0-8\" class=\"mstar\" scope=\"col\"><a href='?mod_id=mediaquotesmutualfunds&scol=mstar&stype=desc&rcnt=100&tab=tab1&cat=%24FOCA%24MB%24%24' id='colhead'>Morningstar Rating</a></th></tr></thead><tbody><tr><td class=\"fname\" headers=\"table-0-0-0 \">Fidelity Advisor\u00ae Leveraged Co Stk Z</td>\n<td class=\"tkr\" headers=\"table-0-0-1 \"><a href=\"/q?s=FZAKX\" data-ylk=\"lt:s;\">FZAKX</a></td>\n<td class=\"cat\" headers=\"table-0-0-2 \"><a href=\"http://finance.yahoo.com/funds/lists/?mod_id=mediaquotesmutualfunds&cat=$FOCA$MB$$\" data-ylk=\"lt:s;\">Mid-Cap Blend</a></td>\n<td class=\"ffly\" headers=\"table-0-0-3 \"><a href=\"http://finance.yahoo.com/funds/lists/?mod_id=mediaquotesmutualfunds&ff=0C00001YR0\" data-ylk=\"lt:s;\">Fidelity Investments</a></td>\n<td class=\"nav3m  selected align ticker_up\" headers=\"table-0-0-4 \">+10.58%</td>\n<td class=\"navytd  align ticker_up\" headers=\"table-0-0-5 \">+32.71%</td>\n<td class=\"nav3yr  align ticker_up\" headers=\"table-0-0-6 \">+18.9%</td>\n<td class=\"nav5yr  align ticker_up\" headers=\"table-0-0-7 \">+26.05%</td>\n<td class=\"mstar\" headers=\"table-0-0-8 \"><div class='rating four-star'></div></td></tr>'''


	def testNfl() {
		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagsoupParser)
		def url = "http://www.nfl.com/stats/categorystats?archive=false&conference=null&statisticCategory=PASSING&season=2010&seasonType=REG&amp;experience=null&tabSeq=0&qualified=true&Submit=Go"
		def htmlParser = slurper.parse(url)

		tabledata = htmlParser.'**'.find { it.@class == 'data-table1' }.tbody.tr.collect {
			[
				Rk: it.td[0].text().trim(),
				Player: it.td[1].text().trim(),
				Team: it.td[2].text().trim(),
				Pos: it.td[3].text().trim(),
				Comp: it.td[4].text().trim(),
				Att: it.td[5].text().trim(),
				Pct: it.td[6].text().trim(),
				AttG: it.td[7].text().trim(),
				Yds: it.td[8].text().trim(),
				Avg: it.td[9].text().trim(),
				YdsG: it.td[10].text().trim(),
				TD: it.td[11].text().trim(),
				Int: it.td[12].text().trim(),
				First: it.td[13].text().trim(),
				FirstPercent: it.td[14].text().trim(),
				Lng: it.td[15].text().trim(),
				TwentyPlus: it.td[16].text().trim(),
				FortyPlus: it.td[17].text().trim(),
				Sck: it.td[18].text().trim(),
				Rate: it.td[19].text().trim()
			]
		}

		tabledata.each { it -> println it }
	}
	
	FundCategory findOrCreateFundCategory(def category) {
		def c = FundCategory.findByName(category)
		if (!c) {
			c = new FundCategory()
			c.name =  category
			if (!c.save()) {
				c.errors.each {
					println it
				}
			}
		}

		return c
	}

	FundFamily findOrCreateFundFamily(def family) {
		def c = FundFamily.findByName(family)
		if (!c) {
			c = new FundFamily()
			c.name =  family
			if (!c.save()) {
				c.errors.each {
					println it
				}
			}
		}
		return c
	}
	
	MutualFund findOrCreateFund(def m, def category, def family,boolean save = true) {
		def c = MutualFund.findByTicker(m.ticker)
		if (!c) {
			c = new MutualFund()
			c.ticker =  m.ticker
			c.name = m.name
			c.fundFamily = family
			c.category = category
			c.assignRating(m.rating)
			c.threeMonthReturn = m.threeMonthReturn == 'N/A' ? null : m.threeMonthReturn.toBigDecimal()
			c.ytdReturn = m.ytdReturn == 'N/A' ? null : m.ytdReturn.toBigDecimal()
			c.threeYearReturn = m.threeYearReturn == null ? 0.0 : m.threeYearReturn.toBigDecimal()
			c.fiveYearReturn = m.fiveYearReturn == null ? 0.0 : m.fiveYearReturn.toBigDecimal()

			
			try {
				if (save && !c.save()) {
					c.errors.each {
						println it
					}
				}
			} catch (Exception e) {
				log.info "Error saving $c"
			}
		}
		
		return c

	}
	
	def test() {
		def f = new FundFamily()
		f.name = 'Fidelity Advisor\u00ae Leveraged Co Stk Z'
					if (!f.save()) {
				f.errors.each {
					println it
				}
			}

	}
	
	
	def dumpTable(def url) {
		println "**************************************************************$url*******************"
		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagsoupParser)
		def htmlParser = slurper.parse(url)
		def tabledata = htmlParser.'**'.find { it.@class == 'yfi-table-container' }.table.tbody.tr.each {
				println it.td[0].text().trim()
				println it
		}

	   def n = htmlParser.'**'.find { it.@class=='yom-button next' }
	   
	   if (!n) {
		   n = htmlParser.'**'.find { it.@class=='yom-button last' }
	   }
  
	   return n ? n.@href : null
	}
	
	def dumpRaw(def url) {
		println "**************************************************************$url*******************"
		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagsoupParser)
		def htmlParser = slurper.parse(url)
		def tabledata = htmlParser.'**'.find { it.@class == 'yfi-table-container' }.table.tbody
		return htmlParser	
	}
	
	
	def parseTable(def url) {
//		println "**************************************************************$url*******************"
		def n
		def funds = []
//		def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
		def tagsoupParser = org.ccil.cowan.tagsoup.jaxp.SAXParserImpl.newInstance(null)
		def slurper = new XmlSlurper(tagsoupParser)
		
			
		def document = slurper.parse(url)
		   // Extracting information
		def htmlParser = slurper.parse(url)
//			def htmlParser = slurper.parseText(testHtml)
		def tabledata = htmlParser.'**'.find { it.@class == 'yfi-table-container' }.table.tbody.tr.collect {
			[
				name: it.td[0].text().trim().replaceAll('\u00AC\u00C6','\u00AE'),  //This is some kind of bug or anomaly???
				ticker: it.td[1].text().trim(),
				category: it.td[2].text().trim(),
				family: it.td[3].text().trim(),
				threeMonthReturn: it.td[4].text().trim().replaceAll('%',''),
				ytdReturn: it.td[5].text().trim().replaceAll('%',''),
				threeYearReturn: it.td[6].text().trim().replaceAll('%',''),
				fiveYearReturn: it.td[7].text().trim().replaceAll('%',''),
				rating: it.td[8].div.@class.text().trim()

			]
		}

		tabledata.each { 
//			it -> println it 
			def c = this.findOrCreateFundCategory(it.category)
			def f = this.findOrCreateFundFamily(it.family)
			
			def m = this.findOrCreateFund(it, c, f)
			funds+=m
		}


		n = htmlParser.'**'.find { it.@class=='yom-button next' }
	   
	   if (!n) {
		   n = htmlParser.'**'.find { it.@class=='yom-button last' }
	   }
  
	   return ['funds':funds,'next':n ? n.@href : null]
		
	
	}
	
	def parseYahoo(String category, int page = 1) {
//		def category = '$FOCA$LG$$'  // Large Growth
//		category = '$FOCA$MB$$' // Mid Blend
		def baseUrl = 'http://finance.yahoo.com/funds/lists/'
//		def baseUrl = 'http://www.webcleats.com/scraper-test.html'
		def url = baseUrl + "?mod_id=mediaquotesmutualfunds&scol=nav3m&stype=desc&rcnt=100&tab=tab1&cat=\$FOCA\$${category}\$\$&page=$page"
		def funds = []
		
		while (url) {
          def r = parseTable(url)
          if (r.next) url = baseUrl + r.next
          else url = null
		  funds+=r.funds
		}

//		return dumpRaw(url)			
		return funds.flatten()
	}

}

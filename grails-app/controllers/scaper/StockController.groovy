package scaper

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON
import static javax.servlet.http.HttpServletResponse.*

class StockController {

    static final int SC_UNPROCESSABLE_ENTITY = 422

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() { }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		response.setIntHeader('X-Pagination-Total', Stock.count())
		render Stock.list(params) as JSON
    }

    def save() {
        def stockInstance = new Stock(request.JSON)
        def responseJson = [:]
        if (stockInstance.save(flush: true)) {
            response.status = SC_CREATED
            responseJson.id = stockInstance.id
            responseJson.message = message(code: 'default.created.message', args: [message(code: 'stock.label', default: 'Stock'), stockInstance.id])
        } else {
            response.status = SC_UNPROCESSABLE_ENTITY
            responseJson.errors = stockInstance.errors.fieldErrors.collectEntries {
                [(it.field): message(error: it)]
            }
        }
        render responseJson as JSON
    }

    def get() {
        def stockInstance = Stock.get(params.id)
        if (stockInstance) {
			render stockInstance as JSON
        } else {
			notFound params.id
		}
    }

    def update() {
        def stockInstance = Stock.get(params.id)
        if (!stockInstance) {
            notFound params.id
            return
        }

        def responseJson = [:]

        if (request.JSON.version != null) {
            if (stockInstance.version > request.JSON.version) {
				response.status = SC_CONFLICT
				responseJson.message = message(code: 'default.optimistic.locking.failure',
						args: [message(code: 'stock.label', default: 'Stock')],
						default: 'Another user has updated this Stock while you were editing')
				cache false
				render responseJson as JSON
				return
            }
        }

        stockInstance.properties = request.JSON

        if (stockInstance.save(flush: true)) {
            response.status = SC_OK
            responseJson.id = stockInstance.id
            responseJson.message = message(code: 'default.updated.message', args: [message(code: 'stock.label', default: 'Stock'), stockInstance.id])
        } else {
            response.status = SC_UNPROCESSABLE_ENTITY
            responseJson.errors = stockInstance.errors.fieldErrors.collectEntries {
                [(it.field): message(error: it)]
            }
        }

        render responseJson as JSON
    }

    def delete() {
        def stockInstance = Stock.get(params.id)
        if (!stockInstance) {
            notFound params.id
            return
        }

        def responseJson = [:]
        try {
            stockInstance.delete(flush: true)
            responseJson.message = message(code: 'default.deleted.message', args: [message(code: 'stock.label', default: 'Stock'), params.id])
        } catch (DataIntegrityViolationException e) {
            response.status = SC_CONFLICT
            responseJson.message = message(code: 'default.not.deleted.message', args: [message(code: 'stock.label', default: 'Stock'), params.id])
        }
        render responseJson as JSON
    }

    private void notFound(id) {
        response.status = SC_NOT_FOUND
        def responseJson = [message: message(code: 'default.not.found.message', args: [message(code: 'stock.label', default: 'Stock'), params.id])]
        render responseJson as JSON
    }
}

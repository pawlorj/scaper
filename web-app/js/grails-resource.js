/**
 * This module defines the resource mappings required by Angular JS to map to a
 * Grails CRUD URL scheme that uses `"/$controller/$id?"(resource: controller)`.
 */
angular.module('grailsService', ['ngResource']).factory('Grails', function($resource) {
    var $body = $('body');

    return $resource('/scaper/stock/:id.json', {id: '@id'}, {
        update: {method: 'PUT'},
    });
});

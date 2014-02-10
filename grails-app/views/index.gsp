<%@ page import="scaper.Stock" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="ng-app">
        <g:set var="entityName" value="${message(code: 'stock.label', default: 'Stock')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'scaffolding.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'bootstrap/css', file: 'bootstrap.min.css')}" type="text/css">
             
  		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> 
     </head>
    <body data-ng-app="scaffolding" data-base-url="${createLink(uri: '/stock/')}">
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><a class="list" href="#stock"><g:message code="default.list.label" args="[entityName]" /></a></li>
                <li><a class="create" href="#stock/create"><g:message code="default.new.label" args="[entityName]" /></a></li>
            </ul>
        </div>
        <div class="content" role="main" data-ng-view>
        </div>
 		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular.js" type="text/javascript"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-resource.min.js"></script>
  		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.12/angular-route.js"></script>
  		<script src="js/scaffolding.js"></script>
  		<script src="js/grails-resource.js"></script>
  		<script src="bootstrap/js/bootstrap.js"></script>
  		
    </body>
</html>

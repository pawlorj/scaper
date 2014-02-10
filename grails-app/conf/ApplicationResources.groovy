modules = {
	'angular-scaffolding' {
		dependsOn 'jquery'
		resource id: 'js', url: [plugin: 'angular-scaffolding', dir: 'js', file: 'scaffolding.js']
		resource id: 'css', url: [plugin: 'angular-scaffolding', dir: 'css', file: 'scaffolding.css']
	}

	'angular-grails-resource' {
		dependsOn 'angular-scaffolding'
		resource id: 'js', url: [plugin: 'angular-scaffolding', dir: 'js', file: 'grails-resource.js']
	}
	
	application {
		resource url:'js/application.js'
	}
}

class UrlMappings {

	static mappings = {
		"/stock"(resources: "stock")
		
        "/"(view:"/index")
        "500"(view:'/error')
	}
}

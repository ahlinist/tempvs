package tempvs

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'user')
        "500"(view:'/error')
        "404"(view:'/notFound')

        "/library**"(view: "/index")
        "/messaging**"(view: "/messaging")
        "/stash**"(view: "/index")

        "/api/${service}/${uri}**"(controller: 'api', action: 'call')
    }
}

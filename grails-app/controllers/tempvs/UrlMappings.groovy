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

        "/library"(view: "/library/index")
        "/library/admin"(view: "/library/admin")
        "/library/period/*"(view: "/library/period")
        "/library/source/*"(view: "/library/source")

        "/message**"(view: "/message")

        "/stash**"(view: "/stash")

        "/api/${service}/${uri}**"(controller: 'api', action: 'call')
    }
}

package tempvs

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "500"(view:'/error')
        "404"(view:'/notFound')

        "/"(redirect: "/profile")
        "/library**"(view: "/index")
        "/messaging**"(view: "/index")
        "/stash**"(view: "/index")
        "/profile**"(view: "/index")

        "/api/${service}/${uri}**"(controller: 'api', action: 'call')
    }
}

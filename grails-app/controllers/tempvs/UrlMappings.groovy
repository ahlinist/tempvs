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

        "/message/api/${uri}**"(controller: 'message', action: 'api')
        "/library/api/${uri}**"(controller: 'library', action: 'api')
    }
}

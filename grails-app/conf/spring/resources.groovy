import club.tempvs.rest.RestCaller

beans = {
    restCaller(RestCaller) {
        restTemplate = ref "restTemplate"
    }
}

package club.tempvs.json

import groovy.json.JsonSlurper

class JsonConverter {

    public <T> T convert(Class<T> clazz, String json) {
        def jsonMap = new JsonSlurper().parseText(json)
        clazz.newInstance(jsonMap)
    }
}

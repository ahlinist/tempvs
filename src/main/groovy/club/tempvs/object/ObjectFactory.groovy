package club.tempvs.object

import groovy.transform.CompileStatic

@CompileStatic
class ObjectFactory {
    public <T> T getInstance(Class<T> clazz) {
        clazz.newInstance()
    }
}
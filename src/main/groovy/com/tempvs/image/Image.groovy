package com.tempvs.image

import groovy.transform.CompileStatic

@CompileStatic
interface Image {
    void save()
    void setMetaData(Map metaData)
    byte[] getBytes()
}
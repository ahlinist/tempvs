package com.tempvs.image

import groovy.transform.CompileStatic

@CompileStatic
interface ImageDAO {
    Image get(String collection, Map query)
    Image create(InputStream inputStream, String collection, String fileName)
    Boolean save(Image image, Map metaData)
}
package com.tempvs.image

interface Image {
    Boolean save()
    void setMetaData(Map metaData)
    List<Byte> getBytes()
}
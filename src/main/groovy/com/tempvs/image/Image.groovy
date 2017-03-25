package com.tempvs.image

interface Image {
    void save()
    void setMetaData(Map metaData)
    List<Byte> getBytes()
}
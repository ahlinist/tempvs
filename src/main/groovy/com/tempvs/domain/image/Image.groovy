package com.tempvs.domain.image

import com.tempvs.domain.BasePersistent

abstract class Image extends BasePersistent{
    String pathToFile

    static constraints = {
        pathToFile nullable: true, blank: true
    }
}

package com.tempvs.mongodb

import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.Image

class MongoImageFactory {
    Image getImage(GridFSFile fridFSFile) {
        new MongoImage(gridFSFile: fridFSFile)
    }
}

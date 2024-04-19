package com.t.module_t.database.entity;

import java.io.File;

public class FileT {
    public File file;
    public String type;
    public FileT(File file, String type){
        this.file = file;
        this.type = type;
    }
}

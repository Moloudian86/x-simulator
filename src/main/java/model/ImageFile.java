package model;

import java.io.InputStream;


public class ImageFile extends File{
    private ImageFormat format;

    public ImageFile(String path, ImageFormat format){
        super(path);
        this.format = format;
    }


    public ImageFormat getFormat() {
        return format;
    }

    public void setFormat(ImageFormat format) {
        this.format = format;
    }
}

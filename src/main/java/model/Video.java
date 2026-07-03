package model;

public class Video extends File{
    private VideoFormat format;
    private VideoQuality quality;
    private int duration;

    public Video(String path, VideoFormat format, VideoQuality quality, int duration){
        super(path);
        this.format = format;
        this.duration = duration;
    }
    public VideoFormat getFormat() {
        return format;
    }

    public VideoQuality getQuality() {
        return quality;
    }

    public int getDuration() {
        return duration;
    }

    public void setFormat(VideoFormat format) {
        this.format = format;
    }

    public void setQuality(VideoQuality quality) {
        this.quality = quality;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

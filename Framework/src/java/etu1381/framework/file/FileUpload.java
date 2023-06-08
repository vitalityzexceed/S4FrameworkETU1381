package etu1381.framework.file;

public class FileUpload 
{
    private String name;
    private String path;
    private byte[] bytearray;

    public FileUpload(String name, String path, byte[] bytearray) {
        this.setName(name);
        this.setPath(path);
        this.setBytearray(bytearray);
    }

    public FileUpload() {
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBytearray(byte[] bytearray) {
        this.bytearray = bytearray;
    }

}

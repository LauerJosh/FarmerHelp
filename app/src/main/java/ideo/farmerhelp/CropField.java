package ideo.farmerhelp;

public class CropField
{
    private long id;
    private String cropField;

    public long getID()
    {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public String getCropfield()
    {
        return cropField;
    }

    public void setCropfield(String cropField)
    {
        this.cropField = cropField;
    }

    @Override
    public String toString()
    {
        return cropField;
    }
}


package DragItAndDropIt;

import java.io.Serializable;

public class Fruit implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String name = "";
        private String img = "";
	public Fruit(String name,String img)
	{
            
            this.name = name;
            this.img = img;
                
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
        public String getImg()
	{
		return img;
	}

	public void setimg(String img)
	{
		this.img = img;
	}
	@Override
	public String toString()
	{
		return name;
	}
}

package ColladaObjects;

public class Input
{
	public int Semantic;
	public String Source;
	public int Offset;
	
	public Input(String semantic, String source, int offset)
	{
		if(semantic.equals("VERTEX"))
			Semantic = 0;
		else if(semantic.equals("NORMAL"))
			Semantic = 1;
		else if(semantic.equals("TEXCOORD"))
			Semantic = 2;
		
		Source = source;
		Offset = offset;
	}
	
	public int getSemantic()
	{
		return Semantic;
	}
}

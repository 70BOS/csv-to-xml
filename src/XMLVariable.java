import java.util.Arrays;
import java.util.List;

public class XMLVariable {
	private String name;
	private List<String> value;
	
	public XMLVariable(String n){
		name = n;
	}

	public void setDomain(String[] data,int n){
		value = Arrays.asList(data).subList(2,n);
	}
	
	public String getName(){
		return name;
	}
	public List<String> getDomain(){
		return value;
	}
	public int getDomainSize(){
		return value.size();
	}

	//returns a String in xml format that describes this XMLVariable
	public String toXML(){
		
		String result = "<VARIABLE TYPE=\"String\">\n\t<NAME>"+name+"</NAME>\n";
		for(int i=0;i<value.size();i++){
			result = result+"\t<VALUE>"+value.get(i)+"</VALUE>\n";
		}
		result=result+"</VARIABLE>";
		return result;
		
	}

	
}

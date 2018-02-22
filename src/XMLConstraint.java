
import java.util.ArrayList;
import java.util.List;

public class XMLConstraint {
	
	public String type = "Custom";
	public String customName = "Custom";
	public List<String> given = new ArrayList<String>();
	public List<String> truthTable = new ArrayList<String>();
	
	/*constructor takes two strings
	 *used for Alldiff constraints*/
	public XMLConstraint(String s1,String s2){
		customName = "Alldiff";
		given.add(s1);
		given.add(s2);
	}
	
	//constructor, initialize a literal in CNF
	public XMLConstraint(String s1, List<XMLVariable> list) throws IllegalArgumentException{
		String[] sentence = s1.split("/");
		if(s1.contains("=")){
			sentence = s1.split("=");
		}
		given.add(sentence[0]);//variable name
		
		//get the XMLVariable has name s1
		XMLVariable v = null;
		for(int j=0;j<list.size();j++){
			if(list.get(j).getName().equals(given.get(0))){
				v = list.get(j);
			}
		}
		if(v!=null){
			List<String> domain = v.getDomain();
			
			//sets truth table
			if(s1.contains("/")){
				for(int i=0;i<domain.size();i++){
					if(sentence[1].equals(domain.get(i))){
						truthTable.add("F");
					}
					else{
						truthTable.add("T");
					}
				}
			}
			else if(s1.contains("=")){
				for(int i=0;i<domain.size();i++){
					if(sentence[1].equals(domain.get(i))){
						truthTable.add("T");
					}
					else{
						truthTable.add("F");
					}
				}
			}
			else{//if a literal is not in "/" or "=" format
				throw new IllegalArgumentException("literal is not in \"/\" or \"=\" format");
			}
		}
		else{
			throw new IllegalArgumentException("variable dne");
		}
		
	}
	
	public void setName(String s){
		customName = s;
	}

	public List<String> getTruthTable(){
		return truthTable;
	}
	
	//sets truth table of this Alldiff constraint
	public void setAlldiffTruthTable(List<XMLVariable> list)throws IllegalArgumentException{
		
		/*checks if variables in given are valid*/
		XMLVariable v1=null;
		for(int j=0;j<list.size();j++){
			if(list.get(j).getName().equals(given.get(0))){
				v1 = list.get(j);
			}
		}
		XMLVariable v2=null;
		for(int j=0;j<list.size();j++){
			if(list.get(j).getName().equals(given.get(1))){
				v2 = list.get(j);
			}
		}
		
		//if variables are valid, set truth table
		if(v1!=null&&v2!=null){
			List<String> domain1 = v1.getDomain();
			List<String> domain2 = v2.getDomain();
			for (int i=0;i<v1.getDomainSize();i++){
				for(int j=0;j<v2.getDomainSize();j++){
					//if the assignments of the variables are same, F
					if(domain1.get(i).equals(domain2.get(j))){
						truthTable.add("F");
					}
					//otherwise, T
					else{
						truthTable.add("T");
					}
				}
			}
		}
		
		else{//variables are not valid, throw an exception
			throw new IllegalArgumentException();
		}
	}
	
	/*performs logical or operation on this and another XMLConstraint c
	 *modifies this truth table and returns this 
	*/
	public XMLConstraint or(XMLConstraint c) {
		this.given.addAll(c.given);
		List<String> newTB = new ArrayList<String>();
		List<String> tb1 = this.truthTable;
		List<String> tb2 = c.getTruthTable();
		for(int i=0; i<tb1.size();i++){
			for(int j=0; j<tb2.size();j++){
				if(tb1.get(i)=="F"&&tb2.get(j)=="F"){
					newTB.add("F");
				}
				else{
					newTB.add("T");
				}
			}
		}
		truthTable = newTB;
		return this;		
	}
	
	//returns a String in xml format that describes this XMLConstraint
	public String toXML(){
		String result = "<CONSTRAINT TYPE=\"Custom\">\n\t<CUSTOMNAME>"+customName+"</CUSTOMNAME>\n";
		for(int i=0;i<given.size();i++){
			result = result+"\t<GIVEN>"+given.get(i)+"</GIVEN>\n";
		}
		result=result+"\t<TABLE>\n\t\t";
		for(int i=0;i<truthTable.size();i++){
			result = result+truthTable.get(i)+ " ";
		}
		result=result+"\n\t</TABLE>\n</CONSTRAINT>";
		return result;
	}
}
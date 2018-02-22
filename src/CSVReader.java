import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/* A CSVReader is used for reading lines from a csv file
 * This class also contains methods for creating XMLVariable and XMLConstraints
 **/
public class CSVReader {
	
	public BufferedReader br;
	public String line;
	
	public CSVReader(String csvFile)throws IOException{
		br = new BufferedReader(new FileReader(csvFile));
		line = null;
	}
	
	/*	This method is only used when reading lines 
	 * 	number of variables,,,,,, and number of constraints,,,,,,,
	 * 	return the number of variables or number of constraints as an int
	 *  if the number is less than 0, throw NumberFormatException.
	 **/
	public int getNumOfArguments() {
		try{
			if(line==null){
				return -1;
			}
			else{
				String data[] = line.split(",");
				int result = Integer.parseInt(data[0]);
				if(result<0)
					throw new NumberFormatException();
				return result;

			}
		}
		catch(NumberFormatException e){
			System.out.println("TAT");
		}
		return -1;
	}
	
	//reads a line of text
	public void readLine() throws IOException{
		line = br.readLine();
	}
	
	//tells whether a line that describes a constraint is valid
	public boolean validLine(String[]d, int num){
			return d.length == num+2;
	}
	
	/*creates an XMLVariable from a line read from csv file
	 *if the line is not valid, return null, 
	 *otherwise return the XMLVariable object
	 **/
	public XMLVariable createVariable(){
		String domain[] = line.split(",");
		if(domain.length<2||!validLine(domain, Integer.parseInt(domain[1]))){
			return null;
		}
		else{
			XMLVariable result = new XMLVariable(domain[0]);
			result.setDomain(domain,domain.length);
			return result;
		}
			
	}
	
	/*creates an Alldiff Constraint and returns it as an XMLConstraint object
	 *this method takes a list of XMLVariables for this problem,
	 *and two Strings, which are the names of the variables that share this constraint
	 **/
	public XMLConstraint createAlldiffConstraint(String given1, String given2,List<XMLVariable> list) throws IOException{
		XMLConstraint result = new XMLConstraint(given1, given2);
		result.setAlldiffTruthTable(list);
		return result;
	}
	
	/*creates and returns an XMLConstraint object for each CNF in knowledge base
	 *this method takes an array of Strings, which are the names of the variables
	 *and a list of XMLVariables for this problem 
	 **/
	public XMLConstraint createCNF(String[] variables,List<XMLVariable> list){
		XMLConstraint result = new XMLConstraint(variables[0],list);
		for(int i=1;i<variables.length;i++){
			XMLConstraint c = new XMLConstraint(variables[i],list);
			result = result.or(c);
		}
		return result;
	}
	
	//close bufferedReader
	public void close() throws IOException{
		br.close();
	}
	
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVToXML {
	
	/* test whether the name of XMLVariable is duplicate
	 * return true if names are duplicate
	 * */
	public static boolean DuplicateVar(List<XMLVariable> list, XMLVariable v){
		for(int i=0;i<list.size();i++){
			if(list.get(i).getName().equals(v.getName()))
				return true;
		}
		return false;
	}
	public static void main(String[] args) throws Exception{
		
		String csvFile = args[0];            //the csv file to be converted		
		CSVReader r = new CSVReader(csvFile);//initialize CSVReader
		
		
		//initialize two lists to store variables and constraints
		List<XMLVariable> variableList = new ArrayList<XMLVariable>();
		List<XMLConstraint> constraintList = new ArrayList<XMLConstraint>();

		
		/*read first line and get the number of variables*/
		r.readLine();
		int numOfArg =  r.getNumOfArguments();
		
		
		/* create an XMLVariable object for each of the variables in csv file,
		 * and add it to variable list,
		 * throw exception if there exist duplicate variables.
		 */
		try{
			for(int i=0;i<numOfArg;i++){
				r.readLine();
				XMLVariable v = r.createVariable();
				if(v!=null&&!CSVToXML.DuplicateVar(variableList, v)){
					variableList.add(v);
				}
				else{
					throw new IllegalArgumentException("duplicate variable");
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
		/* create an XMLConstraint object for each of the alldiff constraints in csv file,
		 * and add it to constraint list*/
		r.readLine();
		int numberOfAlldiff = r.getNumOfArguments();
		for(int i=0;i<numberOfAlldiff;i++){
			//store the variables share the same alldiff constraint to the array data
			r.readLine();
			String[] data = r.line.split(",");
			//convert it to binary constraint
			for(int j=0;j<data.length;j++){
				for(int k=j+1;k<data.length;k++){
					XMLConstraint c = r.createAlldiffConstraint(data[j], data[k],variableList);
					constraintList.add(c);
				}
			}
		}
		
		
		/* create an XMLConstraint object for each CNF in knowledge base
		 * and add it to constraint list*/
		r.readLine();
		int numberOfCNF = r.getNumOfArguments();
		for(int i=0;i<numberOfCNF;i++){
			r.readLine();
			String[] data = r.line.split(",");
			XMLConstraint c = r.createCNF(data, variableList);
			constraintList.add(c);
		}
		
		
		//Convert to XML
		//create xml file
		String dir = System.getProperty("user.dir");
		String path = dir+"\\output";
		File file = new File(path+".xml");
	      if (file.createNewFile()){
	    	path = path+".xml";
	      }else{
	    	int i=1;
	    	file = new File(path+i+".xml");
	    	while(!file.createNewFile()){
	    		i++;
	    		file = new File(path+i+".xml");
	    	}
	    	path = path+i+".xml";
	      }
	    System.out.println("Created xml file at: " + path);
	    
	    
	    //write to xml file just created
		FileWriter fw = new FileWriter(path);
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write("<CSPIF VERSION=\"0.01\">\n");
	    bw.write("<CSP>\n");
	    bw.write("<NAME>"+"= ="+"</NAME>\n");
	    bw.write("<DESCRIPTION>\n\t<SHORT></SHORT>\n\t<DETAILED></DETAILED>\n</DESCRIPTION>\n");
	    	//write variables
	    for(int i=0;i<variableList.size();i++){
	    	bw.write(variableList.get(i).toXML()+"\n");
	    }
	    	//write constraints
	    for(int i=0;i<constraintList.size();i++){
	    	bw.write(constraintList.get(i).toXML()+"\n");
	    }
	    	//closing tags
	    bw.write("</CSP>\n");
	    bw.write("</CSPIF>\n");
	    
	    
	    //close bufferedReader and bufferedWriter
	    bw.close();
		r.close();
	}
}


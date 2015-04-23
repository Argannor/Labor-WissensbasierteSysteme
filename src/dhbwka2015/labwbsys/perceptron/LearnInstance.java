package dhbwka2015.labwbsys.perceptron;

public class LearnInstance {

	public boolean c;
	public double data[];
	
	public LearnInstance(int size){
		data = new double[size];
	}
	
	public LearnInstance(boolean newClass, double newData[]){
		c = newClass;
		data = new double[newData.length];
		for (int i=0; i<data.length; ++i){
			data[i] = newData[i];
		}
	}
	
	public boolean setData(double[] newdata){
		if (data.length != newdata.length){
			return false;
		}
		
		for (int i=0; i<data.length; ++i){
			data[i] = newdata[i];
		}
		
		return true;
	}
	
	public String toString(){
		StringBuilder res = new StringBuilder();
		
		for (int i=0; i<data.length; ++i){
			res.append(" ");
			res.append(data[i]);
		}
		res.append("  ->  ");
		res.append(c);
		
		return res.toString();
	}
}

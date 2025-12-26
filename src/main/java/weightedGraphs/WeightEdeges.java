package weightedGraphs;

import grafos.Edges;

public class WeightEdeges extends Edges implements Comparable<WeightEdeges> {
	
	protected double weight;
	
	
	public WeightEdeges(int u, int v,double weight) {
		super(u, v);
		this.weight=weight;
		
		
	}
	
	public double getWeight() {
		return weight;
	}

	@Override
	public int compareTo(WeightEdeges arg0) {
		
		if(weight>(arg0.weight)) {
			return 1;
		}
		else if(weight==(arg0.weight))
			return 0;
		else {
			return -1;
		}
		
	}

}

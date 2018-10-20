package model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("planCostShare")
public class planCostShare implements Serializable{
	private int deductible;
	private String _org;
	
	public int getDeductible() {
		return deductible;
	}
	public void setDeductible(int deductible) {
		this.deductible = deductible;
	}
	public String get_org() {
		return _org;
	}
	public void set_org(String _org) {
		this._org = _org;
	}
	
	
}

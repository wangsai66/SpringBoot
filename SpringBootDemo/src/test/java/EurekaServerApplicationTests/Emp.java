package EurekaServerApplicationTests;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Emp implements Serializable{
	
	private String name;
	private int age;
	
	public Emp(String name, int age) {
		this.name=name;
		this.age=age;
	}

	

	
	
	
}
